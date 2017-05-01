package jc.vehiclemvp.presenters;

import jc.vehiclemvp.framework.android.Router;
import jc.vehiclemvp.framework.android.Schedulers;
import jc.vehiclemvp.framework.base.BaseScreenPresenter;
import jc.vehiclemvp.framework.base.DefaultObserver;
import jc.vehiclemvp.framework.base.StateManager;
import jc.vehiclemvp.screens.VehicleScreen;
import jc.vehiclemvp.state.VehicleState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VehicleScreenPresenter extends BaseScreenPresenter<VehicleScreen> {

    private final VehicleState vehicleState;
    private DefaultObserver<VehicleState.State> vehicleStateObserver;

    @Inject
    VehicleScreenPresenter(Schedulers s, Router r, StateManager sm, VehicleState vs) {
        super(s, r, sm);
        vehicleState = vs;
    }

    @Override
    public void bindView(VehicleScreen view) {
        super.bindView(view);
        vehicleStateObserver = createVehicleStateSubscriber();
        vehicleState.getStateObservable()
                .subscribeOn(schedulers.ioThread())
                .observeOn(schedulers.uiThread())
                .subscribeWith(vehicleStateObserver);
    }

    @Override
    public void unbindView() {
        super.unbindView();
        vehicleStateObserver.dispose();
    }

    public void addVehicleAction() {
        String model = view.getNewVehicleText();
        if (!model.isEmpty()) {
            vehicleState.addVehicle(model);
        }
    }

    private DefaultObserver<VehicleState.State> createVehicleStateSubscriber() {
        return new DefaultObserver<VehicleState.State>() {

            @Override
            public void onNext(VehicleState.State vehicleState) {
                if (null != vehicleState) {
                    List<String> vehicles = new ArrayList<>();
                    for(VehicleState.State.Vehicle v : vehicleState.getVehicles()) {
                        vehicles.add(v.getModel());
                    }
                    view.showVehicleList(vehicles);
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

}
