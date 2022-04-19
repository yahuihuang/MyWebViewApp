package com.myyhhuang.mywebviewapp.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.myyhhuang.mywebviewapp.R;
import com.myyhhuang.mywebviewapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // WebView
        final WebView webView = binding.webview;
        webView.setBackgroundColor(Color.YELLOW);
//        String customHtml = "<html><body><h1>Welcome to Tutlane</h1>" +
//                "<h2>Welcome to Tutlane</h2><h3>Welcome to Tutlane</h3>" +
//                "<p>It's a Static Web HTML Content.</p>" +
//                "</body></html>";
//        webView.loadData(customHtml, "text/html", "UTF-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://flow.tssco.com.tw:8080/");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}