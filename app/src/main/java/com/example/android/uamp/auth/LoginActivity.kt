/*
 * Copyright 2015 The AppAuth for Android Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
package com.example.android.uamp.auth

import android.annotation.TargetApi
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.AnyThread
import androidx.annotation.ColorRes
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.RegistrationResponse
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.browser.AnyBrowserMatcher
import net.openid.appauth.browser.BrowserMatcher
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 * Demonstrates the usage of the AppAuth to authorize a user with an OAuth2 / OpenID Connect
 * provider. Based on the configuration provided in `res/raw/auth_config.json`, the code
 * contained here will:
 *
 * - Retrieve an OpenID Connect discovery document for the provider, or use a local static
 * configuration.
 * - Utilize dynamic client registration, if no static client id is specified.
 * - Initiate the authorization request using the built-in heuristics or a user-selected browser.
 *
 * _NOTE_: From a clean checkout of this project, the authorization service is not configured.
 * Edit `res/raw/auth_config.json` to provide the required configuration properties. See the
 * README.md in the app/ directory for configuration instructions, and the adjacent IDP-specific
 * instructions.
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var mAuthService: AuthorizationService
    private lateinit var mAuthStateManager: AuthStateManager
    private lateinit var mConfiguration: Configuration
    private val mClientId = AtomicReference<String?>()
    private val mAuthRequest: AtomicReference<AuthorizationRequest?> =
        AtomicReference()
    private val mAuthIntent: AtomicReference<CustomTabsIntent?> =
        AtomicReference()
    private var mAuthIntentLatch = CountDownLatch(1)
    private lateinit var mExecutor: ExecutorService
    private var mUsePendingIntents = false
    private var mBrowserMatcher: BrowserMatcher = AnyBrowserMatcher.INSTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mExecutor = Executors.newSingleThreadExecutor()
        mAuthStateManager = AuthStateManager.getInstance(this)
        mConfiguration = Configuration.getInstance(this)
        if (mAuthStateManager.current.isAuthorized
            && !mConfiguration.hasConfigurationChanged()
        ) {
            Log.i(TAG, "User is already authenticated, proceeding to token activity")
            startActivity(Intent(this, TokenActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_login)
        findViewById<View>(R.id.retry).setOnClickListener {
            mExecutor.submit { initializeAppAuth() }
        }
        findViewById<View>(R.id.start_auth).setOnClickListener { startAuth() }
        if (!mConfiguration.isValid) {
            displayError(mConfiguration.configurationError, false)
            return
        }
        if (mConfiguration.hasConfigurationChanged()) {
            // discard any existing authorization state due to the change of configuration
            Log.i(TAG, "Configuration change detected, discarding old state")
            mAuthStateManager.replace(AuthState())
            mConfiguration.acceptConfiguration()
        }
        if (intent.getBooleanExtra(EXTRA_FAILED, false)) {
            displayAuthCancelled()
        }
        displayLoading("Initializing")
        mExecutor.submit { initializeAppAuth() }
    }

    override fun onStart() {
        super.onStart()
        if (mExecutor.isShutdown) {
            mExecutor = Executors.newSingleThreadExecutor()
        }
    }

    override fun onStop() {
        super.onStop()
        mExecutor.shutdownNow()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAuthService.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        displayAuthOptions()
        if (resultCode == Activity.RESULT_CANCELED) {
            displayAuthCancelled()
        } else {
            val intent = Intent(this, TokenActivity::class.java)
            data?.extras?.let { intent.putExtras(it) }
            startActivity(intent)
        }
    }

    @MainThread
    fun startAuth() {
        displayLoading("Making authorization request")
        mUsePendingIntents =
            (findViewById<View>(R.id.pending_intents_checkbox) as CheckBox).isChecked()

        // WrongThread inference is incorrect for lambdas
        // noinspection WrongThread
        mExecutor.submit { doAuth() }
    }

    /**
     * Initializes the authorization service configuration if necessary, either from the local
     * static values or by retrieving an OpenID discovery document.
     */
    @WorkerThread
    private fun initializeAppAuth() {
        Log.i(TAG, "Initializing AppAuth")
        recreateAuthorizationService()
        if (mAuthStateManager.current.authorizationServiceConfiguration != null) {
            // configuration is already created, skip to client initialization
            Log.i(TAG, "auth config already established")
            initializeClient()
            return
        }

        // WrongThread inference is incorrect for lambdas
        // noinspection WrongThread
        runOnUiThread { displayLoading("Retrieving discovery document") }
        Log.i(TAG, "Retrieving OpenID discovery doc")
        AuthorizationServiceConfiguration.fetchFromUrl(
            mConfiguration.discoveryUri,
            AuthorizationServiceConfiguration.RetrieveConfigurationCallback { config: AuthorizationServiceConfiguration?, ex: AuthorizationException? ->
                handleConfigurationRetrievalResult(
                    config,
                    ex
                )
            },
            mConfiguration.connectionBuilder
        )
    }

    @MainThread
    private fun handleConfigurationRetrievalResult(
        config: AuthorizationServiceConfiguration?,
        ex: AuthorizationException?
    ) {
        if (config == null) {
            Log.i(TAG, "Failed to retrieve discovery document", ex)
            displayError("Failed to retrieve discovery document: " + ex?.message, true)
            return
        }
        Log.i(TAG, "Discovery document retrieved")
        mAuthStateManager.replace(AuthState(config))
        mExecutor.submit { initializeClient() }
    }

    /**
     * Initiates a dynamic registration request if a client ID is not provided by the static
     * configuration.
     */
    @WorkerThread
    private fun initializeClient() {
        mClientId.set(mConfiguration.clientId)
        runOnUiThread { initializeAuthRequest() }
        return
    }

    /**
     * Performs the authorization request, using the browser selected in the spinner,
     * and a user-provided `login_hint` if available.
     */
    @WorkerThread
    private fun doAuth() {
        try {
            mAuthIntentLatch.await()
        } catch (ex: InterruptedException) {
            Log.w(TAG, "Interrupted while waiting for auth intent")
        }
        if (mUsePendingIntents) {
            val completionIntent = Intent(this, TokenActivity::class.java)
            val cancelIntent = Intent(this, LoginActivity::class.java)
            cancelIntent.putExtra(EXTRA_FAILED, true)
            cancelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            var flags = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                flags = flags or PendingIntent.FLAG_MUTABLE
            }
            mAuthService.performAuthorizationRequest(
                mAuthRequest.get()!!,
                PendingIntent.getActivity(this, 0, completionIntent, flags),
                PendingIntent.getActivity(this, 0, cancelIntent, flags),
                mAuthIntent.get()!!
            )
        } else {
            val intent: Intent = mAuthService.getAuthorizationRequestIntent(
                mAuthRequest.get()!!,
                mAuthIntent.get()!!
            )
            startActivityForResult(intent, RC_AUTH)
        }
    }

    private fun recreateAuthorizationService() {
        if (mAuthService != null) {
            Log.i(TAG, "Discarding existing AuthService instance")
            mAuthService.dispose()
        }
        mAuthService = createAuthorizationService()
        mAuthRequest.set(null)
        mAuthIntent.set(null)
    }

    private fun createAuthorizationService(): AuthorizationService {
        Log.i(TAG, "Creating authorization service")
        val builder: AppAuthConfiguration.Builder = AppAuthConfiguration.Builder()
        builder.setBrowserMatcher(mBrowserMatcher)
        builder.setConnectionBuilder(mConfiguration.connectionBuilder)
        return AuthorizationService(this, builder.build())
    }

    @MainThread
    private fun displayLoading(loadingMessage: String) {
        findViewById<View>(R.id.loading_container).visibility = View.VISIBLE
        findViewById<View>(R.id.auth_container).visibility = View.GONE
        findViewById<View>(R.id.error_container).visibility = View.GONE
        (findViewById<View>(R.id.loading_description) as TextView).setText(loadingMessage)
    }

    @MainThread
    private fun displayError(error: String?, recoverable: Boolean) {
        findViewById<View>(R.id.error_container).visibility = View.VISIBLE
        findViewById<View>(R.id.loading_container).visibility = View.GONE
        findViewById<View>(R.id.auth_container).visibility = View.GONE
        (findViewById<View>(R.id.error_description) as TextView).setText(error)
        findViewById<View>(R.id.retry).visibility = if (recoverable) View.VISIBLE else View.GONE
    }

    // WrongThread inference is incorrect in this case
    @AnyThread
    private fun displayErrorLater(error: String, recoverable: Boolean) {
        runOnUiThread { displayError(error, recoverable) }
    }

    @MainThread
    private fun initializeAuthRequest() {
        createAuthRequest()
        warmUpBrowser()
        displayAuthOptions()
    }

    @MainThread
    private fun displayAuthOptions() {
        findViewById<View>(R.id.auth_container).visibility = View.VISIBLE
        findViewById<View>(R.id.loading_container).visibility = View.GONE
        findViewById<View>(R.id.error_container).visibility = View.GONE
        val state: AuthState = mAuthStateManager.current
        val config: AuthorizationServiceConfiguration = state.authorizationServiceConfiguration!!
        var authEndpointStr: String = if (config.discoveryDoc != null) {
            "Discovered auth endpoint: \n"
        } else {
            "Static auth endpoint: \n"
        }
        authEndpointStr += config.authorizationEndpoint
        (findViewById<View>(R.id.auth_endpoint) as TextView).text = authEndpointStr
        var clientIdStr: String = if (state.lastRegistrationResponse != null) {
            "Dynamic client ID: \n"
        } else {
            "Static client ID: \n"
        }
        clientIdStr += mClientId
        (findViewById<View>(R.id.client_id) as TextView).text = clientIdStr
    }

    private fun displayAuthCancelled() {
        Snackbar.make(
            findViewById(R.id.coordinator),
            "Authorization canceled",
            Snackbar.LENGTH_SHORT
        )
            .show()
    }

    private fun warmUpBrowser() {
        mAuthIntentLatch = CountDownLatch(1)
        mExecutor.execute {
            Log.i(TAG, "Warming up browser instance for auth request")
            val intentBuilder: CustomTabsIntent.Builder =
                mAuthService.createCustomTabsIntentBuilder(mAuthRequest.get()!!.toUri())
            intentBuilder.setToolbarColor(getColorCompat(R.color.colorPrimary))
            mAuthIntent.set(intentBuilder.build())
            mAuthIntentLatch.countDown()
        }
    }

    private fun createAuthRequest() {
        Log.i(TAG, "Creating auth request")
        val authRequestBuilder: AuthorizationRequest.Builder = AuthorizationRequest.Builder(
            mAuthStateManager.current.authorizationServiceConfiguration!!,
            mClientId.get()!!,
            ResponseTypeValues.CODE,
            mConfiguration.redirectUri
        )
            .setScope(mConfiguration.scope)
        mAuthRequest.set(authRequestBuilder.build())
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun getColorCompat(@ColorRes color: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getColor(color)
        } else {
            resources.getColor(color)
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
        private const val EXTRA_FAILED = "failed"
        private const val RC_AUTH = 100
    }
}
 */
