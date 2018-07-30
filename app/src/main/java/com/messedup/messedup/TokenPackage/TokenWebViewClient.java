package com.messedup.messedup.TokenPackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.messedup.messedup.PaymentGatewayActivity;

/**
 * Created by saurabh on 1/5/18.
 */

public class TokenWebViewClient extends WebViewClient {


    WebView webView;
    ProgressDialog progressDialog;
    ImageView errimg;
    TextView errtext;

    public TokenWebViewClient(WebView webView, ImageView errimg, TextView errtext) {
        this.webView = webView;
        this.errimg = errimg;
        this.errtext = errtext;
        progressDialog = new ProgressDialog(webView.getContext());
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
        //Your code to do
        webView.setVisibility(View.INVISIBLE);
        errimg.setVisibility(View.VISIBLE);
        errtext.setVisibility(View.VISIBLE);
        Toast.makeText(view.getContext(), "Oops, Please check your Internet Connection!", Toast.LENGTH_LONG).show();
    }

    @Override
//Implement shouldOverrideUrlLoading//
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        try {
//Check whether the URL contains a whitelisted domain. In this example, we’re checking
//whether the URL contains the “example.com” string//
            if (!Uri.parse(url).getHost().contains("facebook.com")) {

//                Toast.makeText(view.getContext(), "URL: " + url, Toast.LENGTH_SHORT).show();

//            Toast.makeText(view.getContext(), "Hi Mark", Toast.LENGTH_SHORT).show();
//If the URL does contain the “example.com” string, then the shouldOverrideUrlLoading method
//will return ‘false” and the URL will be loaded inside your WebView//
                return false;
            }

//If the URL doesn’t contain this string, then it’ll return “true.” At this point, we’ll
//launch the user’s preferred browser, by firing off an Intent//

            if (Uri.parse(url).getHost().contains("tokenJson.php")) {

//                Toast.makeText(view.getContext(), "Final " + url, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(view.getContext(), PaymentGatewayActivity.class);

                view.getContext().startActivity(intent);
                return true;
            }
            return true;

        }
     catch (Exception e) {
        Log.d("ExceptionCaught", "*****");
         return true;
    }

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        Log.d("WebView", "your current url when webpage loading.." + url);
        if (url.contains("tokenJson.php")) {


            view.setVisibility(View.INVISIBLE);
            errimg.setVisibility(View.INVISIBLE);
            errtext.setVisibility(View.INVISIBLE);

            progressDialog.setMessage("Loading your order...");
            progressDialog.setCancelable(false);
            progressDialog.show();


//            Toast.makeText(view.getContext(), "Final " + url, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(view.getContext(), PaymentGatewayActivity.class);

//            view.getContext().startActivity(intent);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        if(url.contains("tokenJson.php")) {
            webView.loadUrl("javascript:HtmlViewer.showHTML" +
                    "(document.getElementsByTagName('body')[0].innerHTML);");

            if (webView.canGoBack()) {
                progressDialog.dismiss();
                webView.goBack();
            }
//            Intent intent = new Intent(view.getContext(), PaymentGatewayActivity.class);

//            view.getContext().startActivity(intent);
        }
    }

}
