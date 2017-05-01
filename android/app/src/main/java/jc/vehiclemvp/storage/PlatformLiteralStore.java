package jc.vehiclemvp.storage;

import jc.vehiclemvp.framework.android.BaseApplication;

import javax.inject.Inject;

public class PlatformLiteralStore {

    @Inject
    BaseApplication application;

    @Inject
    PlatformLiteralStore() { }

    public enum Key {
        SPLASH_SCREEN_DURATION_SECONDS(jc.vehiclemvp.R.integer.animation_splash_screen_s);

        private final int platformKey;

        Key(int platformKey) {
            this.platformKey = platformKey;
        }
    }

    public Integer getInteger(Key key) {
        return application.getResources().getInteger(key.platformKey);
    }

}
