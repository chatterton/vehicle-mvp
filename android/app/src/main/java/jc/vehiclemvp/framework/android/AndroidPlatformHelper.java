package jc.vehiclemvp.framework.android;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

import jc.vehiclemvp.framework.base.PlatformHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AndroidPlatformHelper implements PlatformHelper {

    final BaseApplication application;

    @Inject
    public AndroidPlatformHelper(BaseApplication a) {
        this.application = a;
    }

    @Override
    public int getWindowWidth() {
        Point size = getSize();
        return size.x;
    }

    private Point getSize() {
        Activity a = getActivity();
        Display display = a.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private Activity getActivity() {
        return application.getCurrentActivity();
    }

}
