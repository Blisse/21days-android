package ai.victorl.toda.screens.dashboard;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import ai.victorl.toda.R;
import ai.victorl.toda.app.TodaApp;
import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.data.entry.EntryDateFormatter;
import ai.victorl.toda.screens.addeditentry.AddEditEntryActivity;
import ai.victorl.toda.screens.dashboard.views.DashboardAdapter;
import ai.victorl.toda.screens.dashboard.views.DashboardHeader;
import ai.victorl.toda.screens.login.LoginActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity implements DashboardContract.View {

    @BindView(R.id.drawer) DrawerLayout drawerLayout;
    @BindView(R.id.navigation) NavigationView navigationView;
    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerview) RecyclerView dashboardRecyclerView;

    @BindString(R.string.popup_entry_deleted) String popupEntryDeletedString;
    @BindString(R.string.popup_entry_cancelled) String popupEntryChangesCancelledString;
    @BindString(R.string.popup_entry_saved) String popupEntryChangesSavedString;

    @Inject DashboardContract.Presenter dashboardPresenter;

    private DashboardHeader dashboardHeader;
    private DashboardAdapter dashboardAdapter;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);
        DaggerDashboardComponent.builder()
                .dataComponent(TodaApp.from(this).getDataComponent())
                .dashboardModule(new DashboardModule(this))
                .build()
                .inject(this);

        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_sign_out:
                    dashboardPresenter.signOut();
                    return true;
            }
            return false;
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        dashboardHeader = new DashboardHeader(navigationView.getHeaderView(0));

        dashboardAdapter = new DashboardAdapter(day -> dashboardPresenter.selectDay(day));

        dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dashboardRecyclerView.setAdapter(dashboardAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dashboardPresenter.subscribe();
        dashboardPresenter.load();
        drawerToggle.syncState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dashboardPresenter.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dashboardPresenter.result(requestCode, resultCode);
    }

    @Override
    public void showEntries(List<Entry> entries) {
        dashboardAdapter.setEntries(entries);
    }

    @Override
    public void goToAddEdit(CalendarDay day) {
        AddEditEntryActivity.startActivityForResult(this, AddEditEntryActivity.REQUEST_ADD_EDIT, day);
    }

    @Override
    public void goToLogin() {
        LoginActivity.startActivity(this);
    }

    @Override
    public void showFirebaseUser(String name, String email, Uri photo) {
        dashboardHeader.setName(name);
        dashboardHeader.setEmail(email);
        Picasso.with(this)
                .load(photo)
                .into(dashboardHeader);
    }

    @Override
    public void showChangesSaved(CalendarDay day) {
        StringBuilder sb = new StringBuilder();
        if (day != null) {
            sb.append(EntryDateFormatter.shortFormat(day)).append(" ");
        }
        sb.append(popupEntryChangesSavedString);
        Snackbar.make(coordinatorLayout, sb.toString(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showChangesCancelled(CalendarDay day) {
        StringBuilder sb = new StringBuilder();
        if (day != null) {
            sb.append(EntryDateFormatter.shortFormat(day)).append(" ");
        }
        sb.append(popupEntryChangesCancelledString);
        Snackbar.make(coordinatorLayout, sb.toString(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEntryDeleted(CalendarDay day) {
        StringBuilder sb = new StringBuilder();
        if (day != null) {
            sb.append(EntryDateFormatter.shortFormat(day)).append(" ");
        }
        sb.append(popupEntryDeletedString);
        Snackbar.make(coordinatorLayout, sb.toString(), Snackbar.LENGTH_SHORT).show();
    }

    public static void startActivity(AppCompatActivity activity) {
        Intent dashboardIntent = new Intent(activity, DashboardActivity.class);
        dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(dashboardIntent);
    }
}
