package jc.vehiclemvp.presenters;

import jc.vehiclemvp.framework.base.FrameworkTest;
import jc.vehiclemvp.screens.VehicleScreen;
import jc.vehiclemvp.state.VehicleState;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

import static org.mockito.Mockito.*;

public class VehicleScreenPresenterTest extends FrameworkTest {

    @Mock
    VehicleScreen mockView;
    @Mock
    VehicleState mockVehicleState;

    PublishSubject<VehicleState.State> testSubject = PublishSubject.create();

    VehicleScreenPresenter presenter;

    @Before
    public void before() {
        super.before();
        reset(mockView);
        when(mockVehicleState.getStateObservable()).thenReturn(testSubject);
        presenter = new VehicleScreenPresenter(mockSchedulers, mockRouter, mockStateManager, mockVehicleState);
    }

    @Test
    public void onBindView_subscribesToVehicleState() {
        Assert.assertFalse(testSubject.hasObservers());
        presenter.bindView(mockView);
        testScheduler.triggerActions();
        Assert.assertTrue(testSubject.hasObservers());
    }

    @Test
    public void bindAndUnbind_preserveViewCorrectly() {
        class WrappedVehicleScreenPresenter extends VehicleScreenPresenter {

            WrappedVehicleScreenPresenter(VehicleState vs) {
                super(mockSchedulers, mockRouter, mockStateManager, vs);
            }

            public VehicleScreen getView() {
                return this.view;
            }
        }
        WrappedVehicleScreenPresenter check = new WrappedVehicleScreenPresenter(mockVehicleState);

        Assert.assertNull(check.getView());
        check.bindView(mockView);
        Assert.assertNotNull(check.getView());
        check.unbindView();
        Assert.assertNull(check.getView());
    }

    @Test
    public void onUnbindView_vehicleStateUnsubscribes() {
        presenter.bindView(mockView);
        testScheduler.triggerActions();
        presenter.unbindView();
        Assert.assertFalse(testSubject.hasObservers());
    }

    @Test
    public void onPublishState_addsVehiclesToView() {
        presenter.bindView(mockView);
        testScheduler.triggerActions();
        VehicleState.State state = mock(VehicleState.State.class);
        VehicleState.State.Vehicle vehicle1 = mock(VehicleState.State.Vehicle.class);
        VehicleState.State.Vehicle vehicle2 = mock(VehicleState.State.Vehicle.class);
        List<VehicleState.State.Vehicle> list = Arrays.asList(vehicle1, vehicle2);
        when(state.getVehicles()).thenReturn(list);
        when(vehicle1.getModel()).thenReturn("foo");
        when(vehicle2.getModel()).thenReturn("fredd");
        testSubject.onNext(state);
        testScheduler.triggerActions();
        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        verify(mockView).showVehicleList(argument.capture());
        List<String> check = argument.getValue();
        Assert.assertTrue(check.contains("foo"));
        Assert.assertTrue(check.contains("fredd"));
    }

    @Test
    public void addVehicleAction_forwardsContentToState() {
        when(mockView.getNewVehicleText()).thenReturn("vehicle1");
        presenter.bindView(mockView);
        presenter.addVehicleAction();
        verify(mockVehicleState).addVehicle("vehicle1");
    }

    @Test
    public void addVehicleAction_withEmptyContent_doesNothing() {
        when(mockView.getNewVehicleText()).thenReturn("");
        presenter.bindView(mockView);
        presenter.addVehicleAction();
        verify(mockVehicleState, never()).addVehicle(anyString());
    }

}
