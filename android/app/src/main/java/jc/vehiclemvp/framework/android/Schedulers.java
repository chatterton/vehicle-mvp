package jc.vehiclemvp.framework.android;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;

@Singleton
public class Schedulers {

    @Inject
    Schedulers() { }

    public io.reactivex.Scheduler uiThread() {
        return AndroidSchedulers.mainThread();
    }

    public io.reactivex.Scheduler ioThread() {
        return io.reactivex.schedulers.Schedulers.io();
    }

    public io.reactivex.Scheduler computationThread() {
        return io.reactivex.schedulers.Schedulers.computation();
    }

    public io.reactivex.Scheduler newThread() {
        return io.reactivex.schedulers.Schedulers.newThread();
    }

}
