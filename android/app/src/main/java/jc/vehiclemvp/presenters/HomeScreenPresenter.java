package jc.vehiclemvp.presenters;

import jc.vehiclemvp.framework.android.Router;
import jc.vehiclemvp.framework.android.Schedulers;
import jc.vehiclemvp.framework.base.BaseScreenPresenter;
import jc.vehiclemvp.framework.base.StateManager;
import jc.vehiclemvp.screens.HomeScreen;

import javax.inject.Inject;

public class HomeScreenPresenter extends BaseScreenPresenter<HomeScreen> {

    @Inject
    HomeScreenPresenter(Schedulers s, Router r, StateManager sm) {
        super(s, r, sm);
    }

    @Override
    public void bindView(HomeScreen view) {
        super.bindView(view);
    }

    @Override
    public void unbindView() {
        super.unbindView();
    }

}
