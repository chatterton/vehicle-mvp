package jc.vehiclemvp.framework.base;

import jc.vehiclemvp.framework.android.Router;
import jc.vehiclemvp.framework.android.Schedulers;

public abstract class BaseScreenPresenter<T extends BaseScreen> extends BasePresenter {

    protected Schedulers schedulers;
    protected Router router;

    private DefaultObserver<Boolean> initializationSubscriber;
    private final StateManager stateManager;

    protected T view;

    public BaseScreenPresenter(Schedulers s, Router r, StateManager sm) {
        this.schedulers = s;
        this.router = r;

        initializationSubscriber = new DefaultObserver<Boolean>() {
            @Override
            public void onNext(Boolean value) {
                if (value) {
                    view.hideProgressSpinner();
                }
            }
        };
        stateManager = sm;
    }

    protected void bindView(T view) {
        this.view = view;
        view.showProgressSpinner();
        stateManager.initStateObservable(schedulers.ioThread(), schedulers.uiThread())
                .subscribe(initializationSubscriber);
    }

    public void unbindView() {
        initializationSubscriber.dispose();
        view.hideProgressSpinner();
        this.view = null;
    }

}
