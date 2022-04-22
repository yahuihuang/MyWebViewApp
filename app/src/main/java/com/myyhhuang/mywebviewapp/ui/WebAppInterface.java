package com.myyhhuang.mywebviewapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import com.hjq.toast.ToastUtils;

public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void showDialog(String dialogMsg){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        // Setting Dialog Title
        alertDialog.setTitle("JS triggered Dialog");
        // Setting Dialog Message
        alertDialog.setMessage(dialogMsg);
        alertDialog.show();
    }

    public void showHello(String title,String msg){
        ToastUtils.show(title + msg);
    }
}
