package jc.vehiclemvp;

import jc.vehiclemvp.framework.android.BaseApplication;
import jc.vehiclemvp.framework.base.BaseState;
import jc.vehiclemvp.framework.base.StateManager;
import jc.vehiclemvp.injection.ActivityComponent;
import jc.vehiclemvp.injection.DaggerActivityComponent;
import jc.vehiclemvp.injection.NetworkModule;
import jc.vehiclemvp.state.VehicleState;

import java.util.Arrays;

import javax.inject.Inject;

public class VehicleApplication extends BaseApplication {

    private ActivityComponent activityComponent;

    @Inject VehicleState vehicleState;
    @Inject StateManager stateManager;

    @Override
    public void onCreate() {
        super.onCreate();

        activityComponent = DaggerActivityComponent.builder()
                .applicationModule(applicationModule)
                .networkModule(new NetworkModule())
                .build();
        activityComponent.inject(this);

        stateManager.setStates(Arrays.asList(new BaseState[]{
                vehicleState
        }));
        stateManager.refreshAll();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

}
