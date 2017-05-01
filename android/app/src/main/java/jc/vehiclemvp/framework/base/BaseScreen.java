package jc.vehiclemvp.framework.base;

import java.io.Serializable;

public interface BaseScreen extends BaseView {

    void onCreateScreen(Serializable state);

    void onResumeScreen();

    void onFinishScreen();

    void showProgressSpinner();

    void hideProgressSpinner();

}
