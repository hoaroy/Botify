package com.example.Sachpee.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Sachpee.R;

public class PaymentWebViewActivity extends AppCompatActivity {
    private WebView webView;
    private static final String TAG = "PaymentWebView";

    // URL mà bạn muốn mở để thanh toán
    private String orderUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_webview); // Tạo layout phù hợp

        webView = findViewById(R.id.webview_payment);
        webView.getSettings().setJavaScriptEnabled(true); // Bật JavaScript nếu cần

        // Nhận URL từ Intent
        Intent intent = getIntent();
        orderUrl = intent.getStringExtra("ORDER_URL");

        if (orderUrl != null) {
            webView.setWebViewClient(new PaymentWebViewClient());
            webView.loadUrl(orderUrl);
        } else {
            Toast.makeText(this, "Không tìm thấy URL thanh toán", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private class PaymentWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Navigating to URL: " + url);

            // Kiểm tra URL để xác định xem thanh toán đã hoàn tất hoặc hủy hay chưa
            if (url.startsWith("yourapp://paymentresult")) { // Thay bằng custom scheme mà bạn đã định nghĩa
                Uri uri = Uri.parse(url);
                String status = uri.getQueryParameter("status");
                String appTransId = uri.getQueryParameter("apptransid");

                if ("1".equals(status)) {
                    // Thanh toán thành công
                    Toast.makeText(PaymentWebViewActivity.this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
                } else {
                    // Thanh toán thất bại hoặc hủy
                    Toast.makeText(PaymentWebViewActivity.this, "Thanh toán thất bại hoặc đã hủy.", Toast.LENGTH_LONG).show();
                }

                // Quay lại Activity trước đó (hoặc xử lý theo ý bạn)
                finish();
                return true; // Không tải URL này trong WebView
            }

            // Cho phép WebView tải các URL khác
            return false;
        }
    }
}
