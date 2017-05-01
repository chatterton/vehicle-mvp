package jc.vehiclemvp.state;

import jc.vehiclemvp.framework.android.Schedulers;
import jc.vehiclemvp.framework.base.BaseState;
import jc.vehiclemvp.framework.base.DefaultObserver;
import jc.vehiclemvp.network.VehicleServices;
import jc.vehiclemvp.network.data.GetVehicles;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.ResponseBody;

@Singleton
public class VehicleState extends BaseState {

    private final VehicleServices vehicleServices;
    private final Schedulers schedulers;

    private final BehaviorSubject<State> stateSubject = BehaviorSubject.create();

    private GetVehicles cachedServerResponse;
    private final List<State.Vehicle> addedVehicles = new ArrayList<>();

    @Inject
    public VehicleState(VehicleServices services, Schedulers schedulers) {
        this.vehicleServices = services;
        this.schedulers = schedulers;
        stateSubject.subscribe(new DefaultObserver<State>());
    }

    ///// BaseState

    @Override
    public Observable<Boolean> refreshCallback() {
        return vehicleServices.getVehicles()
                .flatMap(new Function<GetVehicles, ObservableSource<Boolean>>() {
                    @Override
                    public Observable<Boolean> apply(GetVehicles getVehicles) {
                        cachedServerResponse = getVehicles;
                        publishStateObject();
                        return Observable.just(true);
                    }
                });
    }

    private void publishStateObject() {
        StateImpl state = new StateImpl();
        if (null != cachedServerResponse) {
            for (GetVehicles.Vehicle v : cachedServerResponse.getVehicles()) {
                StateImpl.VehicleImpl vehicle = new StateImpl.VehicleImpl();
                vehicle.model = v.getModel();
                state.vehicles.add(vehicle);
            }
        }
        for (State.Vehicle v : addedVehicles) {
            state.vehicles.add(v);
        }
        stateSubject.onNext(state);
    }

    @Override
    public Observable<State> getStateObservable() {
        return stateSubject;
    }

    ///// Business logic

    public void addVehicle(String modelName) {
        StateImpl.VehicleImpl vehicle = new StateImpl.VehicleImpl();
        vehicle.model = modelName;
        addedVehicles.add(vehicle);
        saveVehicle(vehicle);
        publishStateObject();
    }

    private void saveVehicle(State.Vehicle vehicle) {
        if (null != delegate) {
            delegate.baseStateDidBeginUpdate(this);
        }
        vehicleServices.addVehicle(vehicle.getModel())
                .subscribeOn(schedulers.ioThread())
                .subscribe(new DefaultObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody discard) {
                        if (null != delegate) {
                            delegate.baseStateDidFinishUpdating(VehicleState.this, true);
                        }
                    }
                });
    }

    public interface State extends BaseState.State {
        List<Vehicle> getVehicles();
        interface Vehicle {
            String getModel();
        }
    }

    private static class StateImpl implements State {
        final List<State.Vehicle> vehicles = new ArrayList<>();

        @Override
        public List<State.Vehicle> getVehicles() {
            return vehicles;
        }

        private static class VehicleImpl implements State.Vehicle {
            String model;

            @Override
            public String getModel() {
                return model;
            }
        }
    }
}
