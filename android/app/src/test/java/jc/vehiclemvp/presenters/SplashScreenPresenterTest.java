package jc.vehiclemvp.presenters;

import jc.vehiclemvp.framework.android.Route;
import jc.vehiclemvp.framework.base.FrameworkTest;
import jc.vehiclemvp.screens.SplashScreen;
import jc.vehiclemvp.storage.PlatformLiteralStore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SplashScreenPresenterTest extends FrameworkTest {

    @Mock PlatformLiteralStore mockPlatformLiteralStore;
    @Mock
    SplashScreen mockView;

    SplashScreenPresenter presenter;

    @Before
    public void before() {
        super.before();
        when(mockPlatformLiteralStore.getInteger(any(PlatformLiteralStore.Key.class))).thenReturn(4);
        reset(mockView);
        presenter = new SplashScreenPresenter(mockSchedulers, mockRouter, mockStateManager, mockPlatformLiteralStore);
        presenter.bindView(mockView);
    }

    @Test
    public void onScreenResume_getsDurationValue() {
        presenter.screenResumedAction();
        verify(mockPlatformLiteralStore).getInteger(any(PlatformLiteralStore.Key.class));
    }

    @Test
    public void onScreenResume_showsLoadingMessage() {
        presenter.screenResumedAction();
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES);
        verify(mockView).showLoadingText();
    }

    @Test
    public void onScreenResume_routesToHomeScreen() {
        presenter.screenResumedAction();
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES);
        verify(mockRouter).openScreen(Route.HOME_SCREEN, true, null);
    }

}
