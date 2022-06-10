package com.myyhhuang.mywebviewapp.ui;

import java.util.HashMap;
import java.util.Map;

public class JsResponse {
    //call native
    public String methodName;
    private Map<String,String> nativeParams = new HashMap<>();
    //callback js method
    public String callJsMethod;
    //    public Map<String,String> jsParams = new HashMap<>();
    private int jsParamCount;

    public JsResponse(String javaMethod,String jsMethod){
        this.methodName = javaMethod;
        this.callJsMethod = jsMethod;
    }

    public String[] getMethodArgs(){
        if(0==nativeParams.size()) return null;
        String[] params = new String[nativeParams.size()];
        int n = 0;
        for(Map.Entry<String,String> entry:nativeParams.entrySet()){
            params[n] = entry.getValue();
            n++;
        }
        return params;
    }

    public void parseNativeParams(String params){
//        int i = 0,len = params.length();
        if(!nativeParams.isEmpty())
            nativeParams.clear();

        String[] eles = params.split("&");
        for(String ele:eles){
            int eq = ele.indexOf('=');
            String value = ele.substring(eq + 1);
            String name = ele.substring(0,eq);
            nativeParams.put(name,value);
        }

    }

    public void parseJsCallbackParams(String jsparams){
        String[] eles = jsparams.split(",");
        jsParamCount = eles.length;
    }
}
