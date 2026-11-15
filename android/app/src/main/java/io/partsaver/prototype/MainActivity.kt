package io.partsaver.prototype

import android.app.Activity
import android.os.Bundle
import android.webkit.*
import android.app.AlertDialog
import java.io.ByteArrayInputStream

/** Unbuilt Kotlin host for the tested WEB prototype. Not a native Pleos UI. */
class MainActivity : Activity() {
    private lateinit var web: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        web = WebView(this)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.allowFileAccess = false
        web.settings.allowContentAccess = false
        web.settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean =
                request.url.host != "appassets.androidplatform.net"
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse {
                val uri = request.url
                val path = uri.path?.removePrefix("/") ?: "index.html"
                val mime = when(path.substringAfterLast('.')) {
                    "html" -> "text/html"; "js" -> "application/javascript"; "css" -> "text/css"
                    else -> "application/octet-stream"
                }
                if(uri.scheme == "https" && uri.host == "appassets.androidplatform.net" && path in setOf("index.html","style.css","engine.js","provider.js","app.js")) {
                    return WebResourceResponse(mime,"UTF-8",assets.open(path))
                }
                return WebResourceResponse("text/plain","UTF-8",404,"Not Found",emptyMap(),ByteArrayInputStream(ByteArray(0)))
            }
        }
        web.webChromeClient = object : WebChromeClient() {
            override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
                AlertDialog.Builder(this@MainActivity).setMessage(message)
                    .setPositiveButton("확인") { _, _ -> result.confirm() }
                    .setNegativeButton("취소") { _, _ -> result.cancel() }
                    .setOnCancelListener { result.cancel() }.show()
                return true
            }
        }
        setContentView(web)
        web.loadUrl("https://appassets.androidplatform.net/index.html")
    }
    override fun onDestroy() { web.destroy(); super.onDestroy() }
}
