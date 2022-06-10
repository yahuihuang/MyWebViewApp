package com.myyhhuang.mywebviewapp.ui;

import android.net.Uri;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsProcessor {
    //    private JsProcessor(){}

    private JsResponse  mJsRes;

    public  JsResponse getJsResponse(){
        return mJsRes;
    }

    public static boolean isProtocol(String url) {
        return !TextUtils.isEmpty(url) && url.startsWith("jsbridge://");
    }

    //    jsbridge://android-app/method123?a=123&b=345#jsMethod1(p1,p2)"
    public boolean parseJsBridge(String url, WebAppInterface webNative){
        if(!isProtocol(url)) return false;
        int i = -1;
        Uri uri = Uri.parse(url);
        String methodName = uri.getPath();//method1
        methodName = methodName.replace("/", "");

        String params = uri.getQuery();//a=123&b=345
        String callback = uri.getFragment();//#jsMethod1(p1,p2)

        i = callback.lastIndexOf('(');
        String jsMethod = callback.substring(0,i);
        String jsParams = callback.substring(i+1,callback.length()-1);

        mJsRes = new JsResponse(methodName,jsMethod);
        mJsRes.parseJsCallbackParams(jsParams);
        mJsRes.parseNativeParams(params);

        String[] args = mJsRes.getMethodArgs();
        if(null==args){
            jsCallNoParamMethod(webNative,methodName);
        }else{
            int count = args.length;
            Class[] javaParamsType = new Class[count];
            for(int t=0;t<count;t++){
                javaParamsType[t] = String.class;
            }
            jsCallParamMethod(webNative,methodName,javaParamsType,mJsRes.getMethodArgs());
        }
        return true;
    }

    public boolean jsCallNoParamMethod(Object obj,String mName){
        boolean success = false;
        try {
            Method method=obj.getClass().getMethod(mName);
            method.invoke(obj);
            success = true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return success;
    }

    //getMethod("sayHello", String.class,int.class);
    public boolean jsCallParamMethod(Object obj,String mName,Class<?>[] parameterTypes,Object[] args){
        if(parameterTypes.length!=args.length) return false;
        boolean success = false;
        try {
            Method method = obj.getClass().getMethod(mName,parameterTypes);
            method.invoke(obj, args);
            success = true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return success;
    }
}
