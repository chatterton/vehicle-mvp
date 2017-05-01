package jc.vehiclemvp.framework.android;

public enum Route {

    HOME_SCREEN("jc.vehiclemvp.screens.HomeScreenActivity"),
    SPLASH_SCREEN("jc.vehiclemvp.screens.SplashScreenActivity"),
    TEST_SCREEN("jc.vehiclemvp.screens.VehicleScreenActivity");

    private final String androidClass;

    Route(String androidClass) {
        this.androidClass = androidClass;
    }

    String getAndroidClassName() {
        return this.androidClass;
    }

}
