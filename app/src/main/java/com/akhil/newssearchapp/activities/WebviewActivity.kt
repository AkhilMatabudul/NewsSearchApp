package com.akhil.newssearchapp.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import com.akhil.newssearchapp.R
import com.akhil.newssearchapp.databinding.ActivityWebviewBinding

class WebviewActivity : AppCompatActivity() {
    lateinit var binding:ActivityWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.light_blue_600)
        }
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("url")
        val title = intent.getStringExtra("title")
        binding.title.text = title
        val webSettings: WebSettings = binding.webview.settings
        webSettings.javaScriptEnabled = true

        // Set a WebViewClient to handle events inside the WebView
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                // Handle URL loading within the WebView
                return false
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                // Page loading is complete, hide the progress bar
                binding.progressHorizontal.visibility = View.GONE
                binding.webview.visibility = View.VISIBLE
            }
        }

        // Set a WebChromeClient to handle progress updates, alerts, etc.
        binding.webview.webChromeClient = object : WebChromeClient() {
            // Override methods as needed
        }
        binding.backbtn.setOnClickListener { finish() }
        binding.webview.loadUrl(url!!)

    }
    // Override onBackPressed to handle WebView navigation
    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            super.onBackPressed()
        }
    }
}