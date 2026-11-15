package io.partsaver.prototype

import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.ByteArrayInputStream
import java.io.IOException

/**
 * Offline WebView host for the real working HTML/JavaScript demonstration.
 * Vehicle data is SIMULATED. Pleos SDK is NOT linked. Not a native Kotlin UI.
 */
class MainActivity : Activity() {
    private lateinit var web: WebView

    companion object {
        private const val ASSET_HOST = "appassets.androidplatform.net"
        private const val HOME_URL = "https://appassets.androidplatform.net/index.html"
        private val WEB_ASSETS = setOf("index.html", "style.css", "engine.js", "provider.js", "app.js")
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Landscape full screen to resemble the intended infotainment prototype.
        if (Build.VERSION.SDK_INT >= 30) {
            //window.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }

        web = WebView(this)
        web.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = false
            allowContentAccess = false
            allowFileAccessFromFileURLs = false
            allowUniversalAccessFromFileURLs = false
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            javaScriptCanOpenWindowsAutomatically = false
            setSupportMultipleWindows(false)
        }
        web.isHorizontalScrollBarEnabled = false
        web.isVerticalScrollBarEnabled = false
        web.webViewClient = object : WebViewClient() {
            // Never navigate outside this packaged app. No vehicle/network APIs.
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                return !(uri.scheme == "https" && uri.host == ASSET_HOST && uri.path == "/index.html")
            }

            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse {
                val uri = request.url
                val file = uri.path?.removePrefix("/") ?: ""
                if (uri.scheme != "https" || uri.host != ASSET_HOST || file !in WEB_ASSETS) {
                    return rejected()
                }
                val mime = when (file.substringAfterLast('.')) {
                    "html" -> "text/html"
                    "js" -> "application/javascript"
                    "css" -> "text/css"
                    else -> "text/plain"
                }
                return try {
                    WebResourceResponse(mime, "UTF-8", assets.open(file))
                } catch (_: IOException) {
                    rejected()
                }
            }
        }
        web.webChromeClient = object : WebChromeClient() {
            override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage(message)
                    .setPositiveButton("확인") { _, _ -> result.confirm() }
                    .setNegativeButton("취소") { _, _ -> result.cancel() }
                    .setOnCancelListener { result.cancel() }
                    .show()
                return true
            }
        }
        setContentView(web)
        web.loadUrl(HOME_URL)
    }

    private fun rejected(): WebResourceResponse = WebResourceResponse(
        "text/plain", "UTF-8", 404, "Not Found", emptyMap(),
        ByteArrayInputStream(ByteArray(0))
    )

    override fun onDestroy() {
        web.destroy()
        super.onDestroy()
    }
}
