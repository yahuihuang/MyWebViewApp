package com.myyhhuang.mywebviewapp.ui.dashboard;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.myyhhuang.mywebviewapp.R;
import com.myyhhuang.mywebviewapp.databinding.FragmentDashboardBinding;
import com.myyhhuang.mywebviewapp.ui.WebAppInterface;

public class DashboardFragment extends Fragment {
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private boolean loadingFinished = true;
    private boolean redirect = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        // 輸入網址列
        final EditText urlText2 = (EditText) binding.urlText2;
        urlText2.setTextColor(Color.BLUE);
        urlText2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                gotoLoadUrl();
                return true;
            }
        });

        // 查詢Button
        final Button qryUrlBtn = (Button) binding.button;
        qryUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("網址", "qryUrlBtn.setOnClickListener");
                gotoLoadUrl();
            }
        });

        return root;
    }

    void gotoLoadUrl() {
        final WebView webView2 = binding.webview2;

        WebSettings webSettings = webView2.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setDefaultTextEncodingName("UTF-8");
        //支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

//        webView2.setWebViewClient(new WebViewClient());

        webView2.addJavascriptInterface(new WebAppInterface(this.getContext()), "Android");
        webView2.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String urlNewString) {
//                showMessage("webview", "shouldOverrideUrlLoading");
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                webView.loadUrl(urlNewString);
                return true;
            }

//            @Override
//            public void onPageStarted(WebView view, String url) {
//                showMessage("webview", "onPageStarted: " + url);
//                loadingFinished = false;
//                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
//            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                showMessage("webview", "onPageFInished: " + url);
                if (!redirect) {
                    loadingFinished = true;
                    //HIDE LOADING IT HAS FINISHED
                } else {
                    redirect = false;
                }


                view.loadUrl("javascript:(function () {" +
                        "    var objs = document.getElementsByTagName(\"img\");" +
                        "    for (var i = 0; i < objs.length; i++) {" +
                        "        objs[i].onclick = function () {" +
                        "            window.imgClickListener.openImage(this.src);" +
                        "        }" +
                        "    }" +
                        "})()");
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                String summary = "<html><body><strong>" + errorCode + ": " + description + "</body></html>";
                showMessage("webview", summary);
            }
        });

        final EditText urlText2 = (EditText) binding.urlText2;
        System.out.println("urlText2: " + urlText2.getText().toString());
        webView2.loadUrl(urlText2.getText().toString());
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title).setMessage(message).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}