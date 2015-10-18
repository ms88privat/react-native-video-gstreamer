package com.nick.devicecontroller;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import android.app.Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class DeviceControllerPackage implements ReactPackage {
    private List<NativeModule> modules = null;
    private Activity activity = null;
    
    public DeviceControllerPackage(Activity activity) {
        super();
        this.activity = activity;   
    }
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        modules = new ArrayList<>();
        modules.add(new DeviceControllerModule(reactContext, activity));
        return modules;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}