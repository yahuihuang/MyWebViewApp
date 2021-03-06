package com.myyhhuang.mywebviewapp.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.myyhhuang.mywebviewapp.MainActivity;
import com.myyhhuang.mywebviewapp.R;
import com.myyhhuang.mywebviewapp.databinding.FragmentNotificationsBinding;

import java.util.Objects;

public class NotificationsFragment extends Fragment implements View.OnClickListener {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private BridgeWebView mWebView;
//    private static final String URL = "http://testepf.test.com.tw:4200/";
//    private static final String URL = "http://10.0.2.2:5500/a.html";
    private static final String URL = "http://10.0.2.2:4200/";
//    private static final String URL = "file:///android_asset/demo.html";
//    private static final String URL = "http://10.0.2.2:5500/demo.html";
    long exitTime = 0;
    private TextView mTvUser;
    private EditText mEditName;
    private EditText mEditCookie;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        // Soft Keyboard overlaps Edit text field.
        Objects.requireNonNull(getActivity()).getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initWebView();
        registerHandlers();
        initViews();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.reload();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * ????????? WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mWebView = binding.mainWv;

        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        // ?????? localStorage
        mWebView.getSettings().setDomStorageEnabled(true);
        // ????????????javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // ????????????
        mWebView.getSettings().setBuiltInZoomControls(true);
        // ??????UserAgent
        mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString() + "android");
        // ?????????????????????????????????,?????????????????????WebView
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));

        mWebView.loadUrl(URL);
    }

    /**
     * ????????? H5 ?????????????????????
     */
    private void registerHandlers() {
        // ????????????????????????
        mWebView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i("webview (Android???)", data);
//                Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
                function.onCallBack("??????????????? JS ???????????????");
            }
        });

//        // ????????????????????? reloadUrl ??????
//        mWebView.registerHandler("reloadUrl", new BridgeHandler() {
//
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                mWebView.reload();
//                Toast.makeText(getActivity(), "????????????~", Toast.LENGTH_SHORT).show();
//                function.onCallBack("");
//            }
//        });
//
//        // ???????????? User ????????? changeUser ??????
//        mWebView.registerHandler("changeUser", new BridgeHandler() {
//
//            @Override
//            public void handler(String user, CallBackFunction function) {
//                mTvUser.setText(user);
//                function.onCallBack("");
//            }
//        });
//
//        mWebView.registerHandler("submitFromWeb", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Log.i("registerHandler", "handler = submitFromWeb, data from web = " + data);
//                function.onCallBack("submitFromWeb exe, response data from Java");
//            }
//        });
    }

    /**
     * ??????????????? View ??????
     */
    private void initViews() {
        binding.btnName.setOnClickListener(this);
        binding.btnInit.setOnClickListener(this);
        binding.btnCookie.setOnClickListener(this);
        mTvUser = binding.tvUser;
        mEditCookie = binding.editCookie;
        mEditName = binding.editName;
    };

    @Override
    public void onClick(View v) {
        Log.i("btn", "in onClick");
        switch (v.getId()) {
            case R.id.btn_init:
                // ?????? H5 ???????????????????????????
                Log.i("btn", "in btn_init");
                mWebView.send("??????????????? JS ?????????", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Toast.makeText(getContext(), data, Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.btn_name:
                Log.i("btn", "in btn_name");
//                // ?????? H5 ????????? changeName ????????????
//                mWebView.callHandler("changeName", mEditName.getText().toString(), new CallBackFunction() {
//                    @Override
//                    public void onCallBack(String data) {
//                        Log.i("btn", data);
//                        Toast.makeText(getContext(), "name ????????????", Toast.LENGTH_SHORT).show();
//                        mEditName.setText("");
//                    }
//                });
                mWebView.callHandler("functionInJs", mEditName.getText().toString(), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.i("webview", "??????callback data: " + data);
                    }
                });
                break;
            case R.id.btn_cookie:
                Log.i("btn", "in btn_cookie");
                syncCookie(getContext(), URL, "token=" + mEditCookie.getText().toString());
                // ?????? H5 ????????? syncCookie ????????????
                mWebView.callHandler("syncCookie", "", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Toast.makeText(getContext(), "Cookie ????????????", Toast.LENGTH_SHORT).show();
                        mEditCookie.setText("");
                    }
                });
                break;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
//            // ?????????????????????
//            mWebView.goBack();
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exit();
//            return false;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * ????????????
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getContext(), "????????????????????????",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
//            finish();
            System.exit(0);
        }
    }

    /**
     * ??????????????? Cookie ???????????????
     * @param context   ???????????????
     * @param url       url ??????
     * @param cookie    ??????????????? cookie ???????????????"token=azhd57hkslz"
     */
    @SuppressWarnings("deprecation")
    private static void syncCookie(Context context, String url, String cookie){
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// ??????
        cookieManager.removeAllCookie();
        cookieManager.setCookie(url, cookie);
        String newCookie = cookieManager.getCookie(url);
        Log.i("tag ",  "newCookie == " + newCookie);
        CookieSyncManager.getInstance().sync();
    }
}