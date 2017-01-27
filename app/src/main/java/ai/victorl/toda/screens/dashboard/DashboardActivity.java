package ai.victorl.toda.screens.dashboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import javax.inject.Inject;

import ai.victorl.toda.R;
import ai.victorl.toda.app.TodaApp;
import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.screens.dashboard.views.DashboardAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity implements DashboardContract.View {

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

        dashboardAdapter = new DashboardAdapter(this);
        dashboardRecyclerView.setAdapter(dashboardAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dashboardPresenter.start();
        dashboardPresenter.load();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dashboardPresenter.stop();
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showEntries(List<Entry> entries) {
        dashboardAdapter.setEntries(entries);
    }
}
