package jc.vehiclemvp.screens;

import android.widget.EditText;
import android.widget.TextView;

import jc.vehiclemvp.VehicleApplication;
import jc.vehiclemvp.framework.android.NavigableActivity;
import jc.vehiclemvp.presenters.VehicleScreenPresenter;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class VehicleScreenActivity extends NavigableActivity implements VehicleScreen {

    @Bind(jc.vehiclemvp.R.id.vehicle_list) TextView vehicleListText;
    @Bind(jc.vehiclemvp.R.id.vehicle_new_text) EditText vehicleNewText;

    @Inject VehicleScreenPresenter presenter;

    @Override
    public void onCreateScreen(Serializable state) {
        super.onCreateScreen(state);
        ((VehicleApplication) getApplication()).getActivityComponent().inject(this);
        presenter.bindView(this);
    }

    @Override
    protected Integer getLayoutResourceId() {
        return jc.vehiclemvp.R.layout.activity_vehicle_screen;
    }

    @Override
    public void showVehicleList(List<String> vehicles) {
        String list = "";
        for (String v : vehicles) {
            list += v + "\n";
        }
        vehicleListText.setText(list);
    }

    @Override
    public String getNewVehicleText() {
        return vehicleNewText.getText().toString();
    }

    @OnClick(jc.vehiclemvp.R.id.vehicle_new_button)
    public void addVehicleClick() {
        presenter.addVehicleAction();
    }

}
