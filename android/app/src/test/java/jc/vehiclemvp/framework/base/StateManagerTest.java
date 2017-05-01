package jc.vehiclemvp.framework.base;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.Mockito.*;

public class StateManagerTest extends FrameworkTest {

    @Mock BaseState mockBaseState;

    StateManager stateManager;

    @Before
    public void before() {
        super.before();
        reset(mockBaseState);
        stateManager = new StateManager(mockSchedulers);
    }

    @Test
    public void addOneStateAndRefresh_callsStateCallback() {
        stateManager.setStates(Arrays.asList(mockBaseState));
        stateManager.refreshAll();
        verify(mockBaseState).refreshCallback();
    }

    @Test
    public void addTwoStatesAndRefresh_observesBothCallbacks() {
        PublishSubject<Boolean> testSubject = PublishSubject.create();
        when(mockBaseState.refreshCallback()).thenReturn(testSubject);
        Assert.assertFalse(testSubject.hasObservers());
        BaseState mockBaseState2 = mock(BaseState.class);
        PublishSubject<Boolean> testSubject2 = PublishSubject.create();
        when(mockBaseState2.refreshCallback()).thenReturn(testSubject2);
        Assert.assertFalse(testSubject2.hasObservers());

        stateManager.setStates(Arrays.asList(mockBaseState, mockBaseState2));
        stateManager.refreshAll();
        testScheduler.triggerActions();

        Assert.assertTrue(testSubject.hasObservers());
        Assert.assertTrue(testSubject2.hasObservers());
    }

    @Test
    public void initState_initiallyReturnsFalse() {
        final ArrayList<Boolean> calls = new ArrayList<>();
        Consumer<Boolean> testObserver = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                calls.add(aBoolean);
            }
        };
        stateManager.initStateObservable(testScheduler, testScheduler)
                .subscribe(testObserver);
        testScheduler.triggerActions();

        Assert.assertEquals(1, calls.size());
        Assert.assertEquals(false, calls.get(0));
    }

    @Test
    public void initState_returnsTrueAfterInit() {
        when(mockSchedulers.ioThread()).thenReturn(Schedulers.trampoline());
        PublishSubject<Boolean> testSubject =  PublishSubject.create();
        when(mockBaseState.refreshCallback()).thenReturn(testSubject);

        final ArrayList<Boolean> calls = new ArrayList<>();
        Consumer<Boolean> testObserver = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                calls.add(aBoolean);
            }
        };
        stateManager.initStateObservable(Schedulers.trampoline(), Schedulers.trampoline())
                .subscribe(testObserver);

        stateManager.setStates(Arrays.asList(mockBaseState));
        stateManager.refreshAll();
        testSubject.onNext(true);
        testSubject.onComplete();

        Assert.assertEquals(2, calls.size());
        Assert.assertEquals(true, calls.get(1));
    }

    @Test
    public void refresh_onlyDoesWorkOnIOThread() {
        PublishSubject<Boolean> testSubject = PublishSubject.create();
        when(mockBaseState.refreshCallback()).thenReturn(testSubject);
        stateManager.setStates(Arrays.asList(mockBaseState));
        stateManager.refreshAll();
        testScheduler.triggerActions();

        verify(mockSchedulers, never()).computationThread();
        verify(mockSchedulers, never()).uiThread();
        verify(mockSchedulers, never()).newThread();
        verify(mockSchedulers, atLeastOnce()).ioThread();
    }

    @Test
    public void initStateObservable_subscribesAndObservesOnCorrectThreads() {
        TestScheduler ioScheduler = new TestScheduler();
        TestScheduler uiScheduler = new TestScheduler();

        PublishSubject<Boolean> testSubject = PublishSubject.create();
        when(mockBaseState.refreshCallback()).thenReturn(testSubject);
        stateManager.setStates(Arrays.asList(mockBaseState));

        TestObserver<Boolean> testObserver = TestObserver.create();
        stateManager
                .initStateObservable(ioScheduler, uiScheduler)
                .subscribe(testObserver);

        stateManager.refreshAll();
        testSubject.onNext(true);
        ioScheduler.triggerActions();
        testObserver.assertNoErrors();
        testObserver.assertValueCount(0);
        uiScheduler.triggerActions();
        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
    }

    @Test
    public void setStates_setsDelegateOnAll() {
        BaseState state1 = mock(BaseState.class);
        BaseState state2 = mock(BaseState.class);
        stateManager.setStates(Arrays.asList(state1, state2));
        verify(state1).setDelegate(any(BaseState.Delegate.class));
        verify(state2).setDelegate(any(BaseState.Delegate.class));
    }

    @Test
    public void beginUpdate_publishesFalseToInitializationState() {
        BaseState state1 = mock(BaseState.class);
        stateManager.setStates(Arrays.asList(state1));
        final ArrayList<Boolean> calls = new ArrayList<>();
        Consumer<Boolean> testObserver = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                calls.add(aBoolean);
            }
        };
        stateManager.initStateObservable(Schedulers.trampoline(), Schedulers.trampoline())
                .subscribe(testObserver);

        stateManager.baseStateDidBeginUpdate(state1);
        Assert.assertEquals(2, calls.size());
        Assert.assertEquals(false, calls.get(1));
    }

    @Test
    public void finishUpdate_withoutRefresh_publishesTrueToInitializationState() {
        BaseState state1 = mock(BaseState.class);
        stateManager.setStates(Arrays.asList(state1));
        final ArrayList<Boolean> calls = new ArrayList<>();
        Consumer<Boolean> testObserver = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                calls.add(aBoolean);
            }
        };
        stateManager.initStateObservable(Schedulers.trampoline(), Schedulers.trampoline())
                .subscribe(testObserver);

        stateManager.baseStateDidBeginUpdate(state1);
        stateManager.baseStateDidFinishUpdating(state1, false);
        Assert.assertEquals(3, calls.size());
        Assert.assertEquals(true, calls.get(2));
    }
}