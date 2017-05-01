package jc.vehiclemvp.framework.base;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseScreenPresenterTest extends FrameworkTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock BaseScreen mockView;

    BaseScreenPresenter presenter;

    @Before
    public void before() {
        super.before();
        presenter = new BaseScreenPresenter(mockSchedulers, mockRouter, mockStateManager) {
            @Override
            protected void bindView(BaseScreen view) {
                super.bindView(view);
            }
        };
        reset(mockView);
    }

    @Test
    public void onBindView_subscribesToInitState() {
        Assert.assertFalse(testInitState.hasObservers());
        presenter.bindView(mockView);
        verify(mockStateManager).initStateObservable(any(Scheduler.class), any(Scheduler.class));
        Assert.assertTrue(testInitState.hasObservers());
    }

    @Test
    public void onUnbindView_unsubscribesFromInitState() {
        presenter.bindView(mockView);
        presenter.unbindView();
        Assert.assertFalse(testInitState.hasObservers());
    }
    @Test
    public void whenInitStateFalse_showsProgressSpinner() {
        presenter.bindView(mockView);
        testInitState.onNext(false);
        testScheduler.triggerActions();
        verify(mockView).showProgressSpinner();
        verify(mockView, never()).hideProgressSpinner();
    }

    @Test
    public void whenInitStateTrue_hidesProgressSpinner() {
        presenter.bindView(mockView);
        testInitState.onNext(false);
        testScheduler.triggerActions();
        testInitState.onNext(true);
        testScheduler.triggerActions();
        verify(mockView).showProgressSpinner();
        verify(mockView).hideProgressSpinner();
    }

    @Test
    public void initStateSubscription_usesIOAndUIThreads() {
        TestScheduler ioScheduler = new TestScheduler();
        when(mockSchedulers.ioThread()).thenReturn(ioScheduler);
        TestScheduler uiScheduler = new TestScheduler();
        when(mockSchedulers.uiThread()).thenReturn(uiScheduler);
        presenter.bindView(mockView);
        verify(mockStateManager).initStateObservable(ioScheduler, uiScheduler);
    }

}
