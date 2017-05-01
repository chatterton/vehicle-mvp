package jc.vehiclemvp.framework.android;

import android.content.Intent;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Router {

    @Inject
    BaseApplication application;

    @Inject
    Router() { }

    public void openScreen(Route route, boolean clearStack, Serializable state) {
        Class nextView = classForName(route.getAndroidClassName());
        Intent intent = new Intent(application, nextView);
        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        if (null != state) {
            intent.putExtra(BaseActivity.SAVED_STATE_KEY, state);
        }
        application.startActivity(intent);
    }

    private Class classForName(String name) {
        Class c;
        try {
            c = Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found: "+name);
        }
        return c;
    }

}
