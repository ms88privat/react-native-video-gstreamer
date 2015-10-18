package com.nick.reactnativemediaplayer;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MediaPlayerPackage implements ReactPackage {
    private List<NativeModule> modules = null;
    private List<ViewManager> viewManager = null;
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        modules = new ArrayList<>();
        modules.add(new MediaPlayerModule(reactContext, this));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        viewManager = new ArrayList<>();
        viewManager.add(new MediaPlayerManager());
        return viewManager;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Arrays.asList();
    }
    
    public List<ViewManager> getViewManager() {
        return viewManager;
    }
}