package jc.vehiclemvp.framework.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ViewStub;

import jc.vehiclemvp.framework.base.BaseScreen;
import jc.vehiclemvp.framework.base.NavigableScreen;

import java.io.Serializable;

import butterknife.ButterKnife;

public abstract class BaseActivity extends Activity implements BaseScreen {

    public static final String SAVED_STATE_KEY = "SAVED_STATE_KEY";

    protected ProgressDialog progressDialog;

    ///// Activity lifecycle

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView();
        ButterKnife.bind(this);
        Serializable state = null;
        if (null != savedInstanceState) {
            state = savedInstanceState.getSerializable(SAVED_STATE_KEY);
        }
        onCreateScreen(state);
    }

    private void setupContentView() {
        if (this instanceof NavigableScreen) {
            setContentView(jc.vehiclemvp.R.layout.activity_navigable_screen);
            ViewStub stub = (ViewStub) findViewById(jc.vehiclemvp.R.id.navigable_content_viewstub);
            stub.setLayoutResource(getLayoutResourceId());
            stub.inflate();
        } else {
            setContentView(getLayoutResourceId());
        }
    }

    @Override
    protected final void onResume() {
        super.onResume();
        onResumeScreen();
    }

    @Override
    public final void finish() {
        super.finish();
    }

    ///// BaseScreen Lifecycle

    @Override
    public void onCreateScreen(Serializable state) {
        // stub
    }

    @Override
    public void onResumeScreen() {
        // stub
    }

    @Override
    public void finishScreen() {
        finish();
    }

    protected abstract Integer getLayoutResourceId();

    ///// Spinners

    @Override
    public void showProgressSpinner() {
        if (null == progressDialog) {
            progressDialog = ProgressDialog.show(this, null, getString(jc.vehiclemvp.R.string.loading), true, false);
        }
    }

    @Override
    public void hideProgressSpinner() {
        if (null != progressDialog) {
            try {
                progressDialog.dismiss();
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                progressDialog = null;
            }
        }
    }
}
