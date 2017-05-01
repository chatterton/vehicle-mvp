package jc.vehiclemvp.framework.base;

import io.reactivex.Observable;

public abstract class BaseState<T extends BaseState.State> {

    protected Delegate delegate;

    public abstract Observable<Boolean> refreshCallback();

    public abstract Observable<T> getStateObservable();

    public void setDelegate(Delegate d) {
        this.delegate = d;
    }

    public interface State { }

    public interface Delegate {
        void baseStateDidBeginUpdate(BaseState requester);
        void baseStateDidFinishUpdating(BaseState requester, boolean requestUpdate);
    }

}
