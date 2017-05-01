package jc.vehiclemvp.injection;

import jc.vehiclemvp.VehicleApplication;
import jc.vehiclemvp.framework.android.NavigableActivity;
import jc.vehiclemvp.framework.injection.ApplicationModule;
import jc.vehiclemvp.screens.HomeScreenActivity;
import jc.vehiclemvp.screens.SplashScreenActivity;
import jc.vehiclemvp.screens.VehicleScreenActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { ApplicationModule.class, NetworkModule.class })
public interface ActivityComponent {

    void inject(VehicleApplication application);
    void inject(NavigableActivity activity);

    void inject(HomeScreenActivity activity);
    void inject(SplashScreenActivity activity);
    void inject(VehicleScreenActivity activity);

}
