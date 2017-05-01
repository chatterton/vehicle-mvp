package jc.vehiclemvp.framework.android;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import jc.vehiclemvp.framework.injection.ApplicationComponent;
import jc.vehiclemvp.framework.injection.ApplicationModule;
import jc.vehiclemvp.framework.injection.DaggerApplicationComponent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BaseApplication extends Application {

    protected ApplicationModule applicationModule;

    private Activity currentActivity = null;

    @Inject
    public BaseApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationModule = new ApplicationModule(this);

        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(applicationModule)
                .build();
        applicationComponent.inject(this);

        registerActivityLifecycleCallbacks(createLifecycleCallback());
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    private Application.ActivityLifecycleCallbacks createLifecycleCallback() {
        return new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                currentActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) { }

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) { }

            @Override
            public void onActivityStopped(Activity activity) { }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }

            @Override
            public void onActivityDestroyed(Activity activity) { }
        };
    }

    @Override
    public void startActivity(Intent intent) {
        Bundle startActivityOptions = ActivityOptions.makeCustomAnimation(this, jc.vehiclemvp.R.anim.fast_fade_in, jc.vehiclemvp.R.anim.fast_fade_out).toBundle();
        if (null != currentActivity) {
            currentActivity.startActivity(intent, startActivityOptions);
        } else {
            startActivity(intent, startActivityOptions);
        }
    }

}
