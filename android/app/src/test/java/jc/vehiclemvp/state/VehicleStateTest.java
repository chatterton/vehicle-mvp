package jc.vehiclemvp.state;

import jc.vehiclemvp.framework.android.Schedulers;
import jc.vehiclemvp.framework.base.BaseState;
import jc.vehiclemvp.framework.base.DefaultObserver;
import jc.vehiclemvp.framework.base.FrameworkTest;
import jc.vehiclemvp.network.VehicleServices;
import jc.vehiclemvp.network.data.GetVehicles;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.*;

public class VehicleStateTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    VehicleServices mockVehicleServices;

    @Mock
    Schedulers mockSchedulers;

    TestScheduler testScheduler = new TestScheduler();

    PublishSubject<GetVehicles> testSubject = PublishSubject.create();

    VehicleState vehicleState;

    @Before
    public void before() {
        vehicleState = new VehicleState(mockVehicleServices, FrameworkTest.initMockSchedulers(mockSchedulers, testScheduler));
        when(mockVehicleServices.getVehicles()).thenReturn(testSubject);
    }

    @Test
    public void refreshCallback_subscribesToService() {
        vehicleState.refreshCallback().subscribe(new DefaultObserver<Boolean>());
        verify(mockVehicleServices).getVehicles();
    }

    @Test
    public void stateObservable_beforeServiceReturn_doesNotPublishState() {
        TestObserver<VehicleState.State> testObserver = TestObserver.create();
        vehicleState.getStateObservable().subscribe(testObserver);
        vehicleState.refreshCallback().subscribe(new DefaultObserver<Boolean>());
        testScheduler.triggerActions();
        testObserver.assertNotComplete();
        testObserver.assertNoValues();
    }

    @Test
    public void stateObservable_afterServiceReturn_returnsState() {
        TestObserver<VehicleState.State> testObserver = TestObserver.create();
        vehicleState.getStateObservable().subscribe(testObserver);
        vehicleState.refreshCallback().subscribe(new DefaultObserver<Boolean>());
        testSubject.onNext(mock(GetVehicles.class));
        testScheduler.triggerActions();
        testObserver.assertNotComplete();
        testObserver.assertValueCount(1);
    }

    @Test
    public void stateObservable_afterReturningState_cachesValue() {
        TestObserver<VehicleState.State> testObserver = TestObserver.create();
        vehicleState.getStateObservable().subscribe(testObserver);
        vehicleState.refreshCallback().subscribe(new DefaultObserver<Boolean>());
        testSubject.onNext(mock(GetVehicles.class));
        testScheduler.triggerActions();
        testObserver.assertNotComplete();
        testObserver.assertValueCount(1);
        VehicleState.State firstState = (VehicleState.State) testObserver.getEvents().get(0).get(0);
        TestObserver<VehicleState.State> testObserver2 = TestObserver.create();
        vehicleState.getStateObservable().subscribe(testObserver2);
        testObserver.assertNotComplete();
        testObserver2.assertValueCount(1);
        Assert.assertTrue(testObserver2.getEvents().get(0).get(0).equals(firstState));
    }

    @Test
    public void stateObservable_withNoSubscribers_stillCachesValue() {
        vehicleState.refreshCallback().subscribe(new DefaultObserver<Boolean>());
        testSubject.onNext(mock(GetVehicles.class));
        testScheduler.triggerActions();
        TestObserver<VehicleState.State> testObserver2 = TestObserver.create();
        vehicleState.getStateObservable().subscribe(testObserver2);
        testObserver2.assertNotComplete();
        testObserver2.assertValueCount(1);
    }

    private VehicleState.State processAndReturnTestResponse() {
        vehicleState.refreshCallback().subscribe(new DefaultObserver<Boolean>());
        GetVehicles getVehicles = mock(GetVehicles.class);
        GetVehicles.Vehicle vehicle1 = mock(GetVehicles.Vehicle.class);
        when(vehicle1.getModel()).thenReturn("model1");
        GetVehicles.Vehicle vehicle2 = mock(GetVehicles.Vehicle.class);
        when(vehicle2.getModel()).thenReturn("model2");
        when(getVehicles.getVehicles()).thenReturn(Arrays.asList(vehicle1, vehicle2));

        TestObserver<VehicleState.State> testObserver = TestObserver.create();
        vehicleState.getStateObservable().subscribe(testObserver);
        testSubject.onNext(getVehicles);
        testScheduler.triggerActions();

        VehicleState.State state =  (VehicleState.State) testObserver.getEvents().get(0).get(0);
        return state;
    }

    @Test
    public void stateObject_hasCorrectVehicleCount() {
        VehicleState.State state = processAndReturnTestResponse();
        Assert.assertEquals(2, state.getVehicles().size());
    }

    @Test
    public void stateObjectVehicles_haveCorrectModelsAndOrder() {
        VehicleState.State state = processAndReturnTestResponse();
        Assert.assertEquals("model1", state.getVehicles().get(0).getModel());
        Assert.assertEquals("model2", state.getVehicles().get(1).getModel());
    }

    private TestObserver<VehicleState.State> addVehicle(String name) {
        Observable<ResponseBody> observable = Observable.just(mock(ResponseBody.class));
        when(mockVehicleServices.addVehicle(anyString())).thenReturn(observable);
        TestObserver<VehicleState.State> testObserver = TestObserver.create();
        vehicleState.getStateObservable().subscribe(testObserver);
        vehicleState.refreshCallback().subscribe(new DefaultObserver<Boolean>());
        testSubject.onNext(mock(GetVehicles.class));
        testScheduler.triggerActions();
        vehicleState.addVehicle(name);
        testScheduler.triggerActions();
        return testObserver;
    }

    @Test
    public void addingVehicle_publishesNewState() {
        TestObserver<VehicleState.State> testObserver = addVehicle("vehicle1");
        testObserver.assertNotComplete();
        testObserver.assertValueCount(2);
        VehicleState.State state = (VehicleState.State) testObserver.getEvents().get(0).get(1);
        Assert.assertEquals("vehicle1", state.getVehicles().get(0).getModel());
    }

    @Test
    public void addingVehicle_postsToServer() {
        addVehicle("vehicleX");
        verify(mockVehicleServices).addVehicle("vehicleX");
    }

    @Test
    public void addingVehicle_requestsRefreshIfSet() {
        BaseState.Delegate mockDelegate = mock(BaseState.Delegate.class);
        vehicleState.setDelegate(mockDelegate);
        addVehicle("foo");
        verify(mockDelegate).baseStateDidBeginUpdate(vehicleState);
        verify(mockDelegate).baseStateDidFinishUpdating(vehicleState, true);
    }

}
