package jc.vehiclemvp.screens;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import jc.vehiclemvp.VehicleApplication;
import jc.vehiclemvp.framework.android.BaseActivity;
import jc.vehiclemvp.presenters.SplashScreenPresenter;

import java.io.Serializable;

import javax.inject.Inject;

public class SplashScreenActivity extends BaseActivity implements SplashScreen {

    @BindView(jc.vehiclemvp.R.id.splash_loading) TextView loadingText;

    @Inject SplashScreenPresenter presenter;

    @Override
    public void onCreateScreen(Serializable state) {
        super.onCreateScreen(state);
        ((VehicleApplication) getApplication()).getActivityComponent().inject(this);
        presenter.bindView(this);
    }

    @Override
    protected Integer getLayoutResourceId() {
        return jc.vehiclemvp.R.layout.activity_splash_screen;
    }

    @Override
    public void onResumeScreen() {
        super.onResumeScreen();
        presenter.screenResumedAction();
    }

    @Override
    public void onFinishScreen() {
        presenter.unbindView();
    }

    @Override
    public void showLoadingText() {
        loadingText.setVisibility(View.VISIBLE);
    }

}
