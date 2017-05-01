package jc.vehiclemvp.screens;

import jc.vehiclemvp.framework.base.NavigableScreen;

import java.util.List;

public interface VehicleScreen extends NavigableScreen {

    void showVehicleList(List<String> vehicles);

    String getNewVehicleText();

}
