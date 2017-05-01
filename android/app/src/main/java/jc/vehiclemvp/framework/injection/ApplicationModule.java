package jc.vehiclemvp.framework.injection;

import jc.vehiclemvp.framework.base.LocalProperties;
import jc.vehiclemvp.framework.base.PlatformHelper;
import jc.vehiclemvp.framework.android.AndroidPlatformHelper;
import jc.vehiclemvp.framework.android.BaseApplication;
import jc.vehiclemvp.framework.android.AndroidLocalProperties;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final BaseApplication application;

    public ApplicationModule(BaseApplication a) {
        this.application = a;
    }

    @Provides
    @Singleton
    BaseApplication provideBaseApplication() {
        return application;
    }

    @Provides
    PlatformHelper providePlatformHelper() {
        return new AndroidPlatformHelper(application);
    }

    @Provides
    @Singleton
    LocalProperties provideLocalProperties() {
        AndroidLocalProperties localProperties = new AndroidLocalProperties(application);
        localProperties.init();
        return localProperties;
    }

}
