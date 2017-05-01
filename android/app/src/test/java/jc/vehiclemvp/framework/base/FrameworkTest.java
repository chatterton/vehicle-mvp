package jc.vehiclemvp.framework.base;

import jc.vehiclemvp.framework.android.Router;
import jc.vehiclemvp.framework.android.Schedulers;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class FrameworkTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock protected Router mockRouter;
    @Mock protected Schedulers mockSchedulers;
    @Mock protected StateManager mockStateManager;

    protected TestScheduler testScheduler = new TestScheduler();
    protected PublishSubject<Boolean> testInitState;

    public void before() {
        initMockSchedulers(mockSchedulers, testScheduler);
        testInitState = PublishSubject.create();
        when(mockStateManager.initStateObservable(any(Scheduler.class), any(Scheduler.class))).thenReturn(testInitState);
    }

    public static Schedulers initMockSchedulers(Schedulers mockSchedulers, Scheduler testScheduler) {
        when(mockSchedulers.computationThread()).thenReturn(testScheduler);
        when(mockSchedulers.ioThread()).thenReturn(testScheduler);
        when(mockSchedulers.newThread()).thenReturn(testScheduler);
        when(mockSchedulers.uiThread()).thenReturn(testScheduler);
        return mockSchedulers;
    }

}
