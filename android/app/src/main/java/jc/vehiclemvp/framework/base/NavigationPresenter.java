package jc.vehiclemvp.framework.base;

import jc.vehiclemvp.framework.android.Route;
import jc.vehiclemvp.framework.android.Router;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NavigationPresenter extends BasePresenter<NavigableScreen> {

    private final PlatformHelper platformHelper;
    private final Router router;

    private List<NavigableScreen.Item> sideNavigation;

    @Inject
    public NavigationPresenter(Router r, PlatformHelper p) {
        super();
        this.platformHelper = p;
        this.router = r;
    }

    @Override
    public void bindView(NavigableScreen view) {
        super.bindView(view);
        view.setNavigationItems(getSideNavigation());
        view.setSideNavigationWidthPixels(platformHelper.getWindowWidth() - view.getHeaderHeight());
    }

    private List<NavigableScreen.Item> getSideNavigation() {
        if (null == sideNavigation) {
            sideNavigation = new ArrayList<>();
            NavigableScreen.Item item = new NavigableScreen.Item();
            item.route = Route.HOME_SCREEN;
            item.title = "Home";
            sideNavigation.add(item);
            item = new NavigableScreen.Item();
            item.route = Route.TEST_SCREEN;
            item.title = "Vehicles";
            sideNavigation.add(item);
        }
        return sideNavigation;
    }

    public void navigationItemSelectedAction(NavigableScreen.Item item) {
        router.openScreen(item.route, false, null);
    }

}
