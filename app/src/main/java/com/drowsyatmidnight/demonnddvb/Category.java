package com.drowsyatmidnight.demonnddvb;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.drowsyatmidnight.demonnddvb.drawer.DrawerAdapter;
import com.drowsyatmidnight.demonnddvb.drawer.DrawerItem;
import com.drowsyatmidnight.demonnddvb.drawer.FmArticle;
import com.drowsyatmidnight.demonnddvb.drawer.Item_Menu;
import com.drowsyatmidnight.demonnddvb.drawer.SpaceItem;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Category extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_HOME = 0;
    private static final int POS_NEWS = 1;
    private static final int POS_SPORT = 2;
    private static final int POS_TECHNICAL = 3;
    private static final int POS_ECONOMY = 4;
    private static final int POS_CAR = 5;
    private static final int POS_EXIT = 7;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    public static SlidingRootNavBuilder slidingRootNavBuilder;
    private DrawerAdapter adapter;
    public static boolean isNetworkAccess = true;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getActiveNetworkInfo()!=null){
                isNetworkAccess = true;
                Toast.makeText(Category.this, "Network is Connected", Toast.LENGTH_SHORT).show();
            }else {
                isNetworkAccess = false;
                Toast.makeText(Category.this, "No network is Connected", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (broadcastReceiver != null) {
                this.unregisterReceiver(broadcastReceiver);
            }
        } catch (IllegalArgumentException e) {
            broadcastReceiver = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
        slidingRootNavBuilder = new SlidingRootNavBuilder(this);
        slidingRootNavBuilder.withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu)
                .inject();
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_HOME).setChecked(true),
                createItemFor(POS_NEWS),
                createItemFor(POS_SPORT),
                createItemFor(POS_TECHNICAL),
                createItemFor(POS_ECONOMY),
                createItemFor(POS_CAR),
                new SpaceItem(28),
                createItemFor(POS_EXIT)));
        adapter.setListener(this);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_HOME);
    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_EXIT) {
            finish();
        }
        txtTitle.setText(screenTitles[position]);
        Fragment selectedScreen = FmArticle.newInstance(getResources().getStringArray(R.array.link_RSS)[position], position);
        showFragment(selectedScreen);
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private DrawerItem createItemFor(int position) {
        return new Item_Menu(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}
