package jc.vehiclemvp.framework.base;

import jc.vehiclemvp.framework.android.Route;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.Serializable;
import java.util.List;

import static org.mockito.Mockito.*;

public class NavigationPresenterTest extends FrameworkTest {

    private static final int TOOLBAR_HEIGHT = 99;
    private static final int SCREEN_WIDTH = 300;

    @Mock NavigableScreen mockView;
    @Mock PlatformHelper mockPlatformHelper;

    NavigationPresenter presenter;

    @Before
    public void before() {
        super.before();
        reset(mockView);
        presenter = new NavigationPresenter(mockRouter, mockPlatformHelper);
    }

    @Test
    public void onBindView_setsNavigationItems() {
        presenter.bindView(mockView);
        verify(mockView).setNavigationItems(anyList());
    }

    @Test
    public void onBindView_navigationContainsHomeScreen() {
        presenter.bindView(mockView);
        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        verify(mockView).setNavigationItems(argument.capture());
        List<NavigableScreen.Item> list = argument.getValue();
        boolean found = false;
        for (NavigableScreen.Item item : list) {
            if (item.route.equals(Route.HOME_SCREEN)) {
                found = true;
            }
        }
        Assert.assertTrue("Navigation must contain route to home screen", found);
    }

    @Test
    public void onBindView_setsSideNavigationWidth() {
        when(mockPlatformHelper.getWindowWidth()).thenReturn(SCREEN_WIDTH);
        when(mockView.getHeaderHeight()).thenReturn(TOOLBAR_HEIGHT);
        presenter.bindView(mockView);
        ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
        verify(mockView).setSideNavigationWidthPixels(argument.capture());
        Assert.assertEquals(SCREEN_WIDTH - TOOLBAR_HEIGHT, (long) argument.getValue());
    }

    @Test
    public void onNavigationItemSelected_routesToScreen() {
        NavigableScreen.Item item = new NavigableScreen.Item();
        item.route = Route.HOME_SCREEN;
        presenter.navigationItemSelectedAction(item);
        ArgumentCaptor<Route> argument = ArgumentCaptor.forClass(Route.class);
        verify(mockRouter).openScreen(argument.capture(), anyBoolean(), any(Serializable.class));
        Assert.assertEquals("Route must match argument", Route.HOME_SCREEN, argument.getValue());
    }

}
