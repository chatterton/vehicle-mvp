package jc.vehiclemvp.presenters;

import jc.vehiclemvp.framework.base.BaseScreenPresenter;
import jc.vehiclemvp.framework.android.Route;
import jc.vehiclemvp.framework.android.Router;
import jc.vehiclemvp.framework.android.Schedulers;
import jc.vehiclemvp.framework.base.StateManager;
import jc.vehiclemvp.screens.SplashScreen;
import jc.vehiclemvp.storage.PlatformLiteralStore;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Scheduler;

public class SplashScreenPresenter extends BaseScreenPresenter<SplashScreen> {

    private PlatformLiteralStore platformLiteralStore;

    @Inject
    SplashScreenPresenter(Schedulers s, Router r, StateManager sm, PlatformLiteralStore p) {
        super(s, r, sm);
        this.platformLiteralStore = p;
    }

    @Override
    public void bindView(SplashScreen view) {
        super.bindView(view);
    }

    @Override
    public void unbindView() {
        super.unbindView();
    }

    public void screenResumedAction() {
        int duration = platformLiteralStore.getInteger(PlatformLiteralStore.Key.SPLASH_SCREEN_DURATION_SECONDS);

        Scheduler.Worker worker = schedulers.uiThread().createWorker();
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                SplashScreenPresenter.this.view.showLoadingText();
            }
        }, duration / 2, TimeUnit.SECONDS);
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                router.openScreen(Route.HOME_SCREEN, true, null);
            }
        }, 2, TimeUnit.SECONDS);

    }

}
