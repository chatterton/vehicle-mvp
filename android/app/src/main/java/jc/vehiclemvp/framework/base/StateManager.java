package jc.vehiclemvp.framework.base;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import jc.vehiclemvp.framework.android.Schedulers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class StateManager implements BaseState.Delegate {

    private final Schedulers schedulers;

    private final BehaviorSubject<Boolean> initializationSubject = BehaviorSubject.createDefault(true);

    private List<BaseState> states;

    @Inject
    public StateManager(Schedulers s) {
        this.schedulers = s;
        initializationSubject.subscribe(new DefaultObserver<Boolean>());
        initializationSubject.onNext(false);
    }

    public void setStates(List<BaseState> states) {
        this.states = states;
        for (BaseState state : states) {
            state.setDelegate(this);
        }
    }

    public void refreshAll() {
        List<Observable<Boolean>> callbacks = new ArrayList<>();
        for (BaseState state : states) {
            callbacks.add(state.refreshCallback());
        }
        Observable.merge(callbacks)
                .last(true)
                .subscribeOn(schedulers.ioThread())
                .subscribeWith(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        initializationSubject.onNext(true);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}

                });
    }

    public Observable<Boolean> initStateObservable(Scheduler subscribeOn, Scheduler observeOn) {
        return initializationSubject.subscribeOn(subscribeOn).observeOn(observeOn);
    }

    ///// BaseState.Delegate

    @Override
    public void baseStateDidBeginUpdate(BaseState requester) {
        initializationSubject.onNext(false);
    }

    @Override
    public void baseStateDidFinishUpdating(BaseState requester, boolean requestRefresh) {
        initializationSubject.onNext(true);
    }
}
