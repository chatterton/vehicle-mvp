package jc.vehiclemvp.framework.base;

import jc.vehiclemvp.framework.android.Route;

import java.util.List;

public interface NavigableScreen extends BaseScreen {

    void setNavigationItems(List<Item> items);

    void setSideNavigationWidthPixels(int width);

    int getHeaderHeight();

    class Item {
        public String title;
        public Route route;
    }

}
