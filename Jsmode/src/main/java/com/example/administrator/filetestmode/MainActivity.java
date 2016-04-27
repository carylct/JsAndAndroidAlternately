package com.example.administrator.filetestmode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;





public class MainActivity extends Activity {
    private WebView webView;
    private Button button;
    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) this.findViewById(R.id.webView);
        button = (Button) this.findViewById(R.id.button);
        WebSettings setting = webView.getSettings();
        setting.setLightTouchEnabled(true);
        //设置支持javascript
        setting.setJavaScriptEnabled(true);
        /**
         * 增加接口方法,让html页面调用
         */
        webView.addJavascriptInterface(new JavaScriptObject(this),"demo");
        /**
         * 加载本地html
         */
        webView.loadUrl("file:///android_asset/js.html");
        webView.setWebChromeClient(new MyWebChromeClient());
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webView.loadUrl("javascript:show('activity传过来的数据')"); //调用javascript函数
                  /*
                  * 通过webView.loadUrl("javascript:xxx")方式就可以调用当前网页中的名称
                  * 为xxx的javascript方法
                  */
            }
        });
    }
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            super.onProgressChanged(view, newProgress);
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {

            super.onReceivedTitle(view, title);
        }
    }
    final Handler myHandler = new Handler();
    /**
     * 供js调用的方法
     *  targetSdkVersion>=17如果不加 @JavascriptInterface调用不成功
     */
    public class JavaScriptObject {
        Context mContxt;

        public JavaScriptObject(Context mContxt) {
            this.mContxt = mContxt;
        }
        @JavascriptInterface
        public void startName(String name) {
            Toast.makeText(mContxt, "调用name" + name, Toast.LENGTH_LONG).show();
        }
        @JavascriptInterface
        public void startPhone(String name) {
            Toast.makeText(mContxt, "调用phone:" + name, Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void showToast(final String webMessage) {
            final String msgeToast = webMessage;
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    /**
                     * 调用js内的方法
                     */
                    webView.loadUrl("javascript:show('"+msgeToast+"')");
                }
            });
            /**
             * 原生做弹框
             */
            Toast.makeText(mContxt, webMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
