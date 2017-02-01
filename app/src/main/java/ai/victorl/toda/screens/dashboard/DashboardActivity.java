package ai.victorl.toda.screens.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import javax.inject.Inject;

import ai.victorl.toda.R;
import ai.victorl.toda.app.TodaApp;
import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.data.entry.EntryDateFormatter;
import ai.victorl.toda.screens.addeditentry.AddEditEntryActivity;
import ai.victorl.toda.screens.dashboard.views.DashboardAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity implements DashboardContract.View {

    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerview) RecyclerView dashboardRecyclerView;

    @Inject DashboardContract.Presenter dashboardPresenter;

    private DashboardAdapter dashboardAdapter;

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
        dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dashboardAdapter = new DashboardAdapter(day -> dashboardPresenter.selectDay(day));
        dashboardRecyclerView.setAdapter(dashboardAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dashboardPresenter.subscribe();
        dashboardPresenter.load();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dashboardPresenter.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_today:
                dashboardAdapter.refreshCalendar();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void showChangesSaved(CalendarDay day) {
        StringBuilder sb = new StringBuilder();
        if (day != null) {
            sb.append(EntryDateFormatter.shortFormat(day)).append(" ");
        }
        sb.append("Saved");
        Snackbar.make(coordinatorLayout, sb.toString(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showChangesCancelled(CalendarDay day) {
        StringBuilder sb = new StringBuilder();
        if (day != null) {
            sb.append(EntryDateFormatter.shortFormat(day)).append(" ");
        }
        sb.append("Cancelled");
        Snackbar.make(coordinatorLayout, sb.toString(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEntryDeleted(CalendarDay day) {
        StringBuilder sb = new StringBuilder();
        if (day != null) {
            sb.append(EntryDateFormatter.shortFormat(day)).append(" ");
        }
        sb.append("Deleted");
        Snackbar.make(coordinatorLayout, sb.toString(), Snackbar.LENGTH_SHORT).show();
    }
}
