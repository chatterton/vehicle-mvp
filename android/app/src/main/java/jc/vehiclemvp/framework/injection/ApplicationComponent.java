package jc.vehiclemvp.framework.injection;

import jc.vehiclemvp.framework.android.BaseApplication;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { ApplicationModule.class })
public interface ApplicationComponent {

    void inject(BaseApplication application);

}
