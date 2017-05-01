package jc.vehiclemvp.screens;

import jc.vehiclemvp.VehicleApplication;
import jc.vehiclemvp.framework.android.NavigableActivity;
import jc.vehiclemvp.presenters.HomeScreenPresenter;

import java.io.Serializable;

import javax.inject.Inject;

public class HomeScreenActivity extends NavigableActivity implements HomeScreen {

    @Inject
    HomeScreenPresenter presenter;

    @Override
    public void onCreateScreen(Serializable state) {
        super.onCreateScreen(state);
        ((VehicleApplication) getApplication()).getActivityComponent().inject(this);
        presenter.bindView(this);
    }

    @Override
    protected Integer getLayoutResourceId() {
        return jc.vehiclemvp.R.layout.activity_home_screen;
    }

}
