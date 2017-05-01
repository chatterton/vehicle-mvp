package jc.vehiclemvp.framework.android;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import butterknife.BindView;
import jc.vehiclemvp.VehicleApplication;
import jc.vehiclemvp.framework.base.NavigableScreen;
import jc.vehiclemvp.framework.base.NavigationPresenter;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

public abstract class NavigableActivity extends BaseActivity implements NavigableScreen {

    @Inject
    NavigationPresenter navigationPresenter;

    @BindView(jc.vehiclemvp.R.id.navigable_drawer_layout) DrawerLayout drawerLayout;
    @BindView(jc.vehiclemvp.R.id.navigable_content_toolbar) Toolbar toolbar;
    @BindView(jc.vehiclemvp.R.id.navigable_left_drawer) View leftDrawer;
    @BindView(jc.vehiclemvp.R.id.navigable_left_drawer_layout) ViewGroup leftDrawerLayout;

    @Override
    public void onCreateScreen(Serializable state) {
        super.onCreateScreen(state);
        setActionBar(toolbar);
        ((VehicleApplication) getApplication()).getActivityComponent().inject(this);

        toolbar.setNavigationIcon(jc.vehiclemvp.R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        navigationPresenter.bindView(this);
    }

    @Override
    public void setNavigationItems(final List<Item> items) {
        leftDrawerLayout.removeAllViews();
        for (final Item item : items) {
            View widget = getLayoutInflater().inflate(jc.vehiclemvp.R.layout.widget_left_hand_nav_cell, leftDrawerLayout, false);
            TextView title = (TextView) widget.findViewById(jc.vehiclemvp.R.id.left_nav_title);
            title.setText(item.title);
            widget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationItemSelected(item);
                }
            });
            leftDrawerLayout.addView(widget);
        }
    }

    private void navigationItemSelected(Item item) {
        navigationPresenter.navigationItemSelectedAction(item);
    }

    @Override
    public void setSideNavigationWidthPixels(int width) {
        ViewGroup.LayoutParams params = leftDrawer.getLayoutParams();
        params.width = width;
        leftDrawer.setLayoutParams(params);
    }

    @Override
    public int getHeaderHeight() {
        return toolbar.getHeight();
    }

}
