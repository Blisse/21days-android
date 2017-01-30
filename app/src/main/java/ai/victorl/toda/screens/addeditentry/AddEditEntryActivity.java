package ai.victorl.toda.screens.addeditentry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import javax.inject.Inject;

import ai.victorl.toda.R;
import ai.victorl.toda.app.TodaApp;
import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.data.entry.EntryDateFormatter;
import ai.victorl.toda.data.settings.TodaSettings;
import ai.victorl.toda.screens.addeditentry.views.AddEditEntryAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditEntryActivity extends AppCompatActivity implements AddEditEntryContract.View {

    public static final int REQUEST_ADD_EDIT = 1;
    public static final int RESULT_ADD_EDIT_SUCCESS = 1;
    public static final int RESULT_ADD_EDIT_CANCELLED = 2;
    public static final int RESULT_ADD_EDIT_DELETED = 3;
    private static final String KEY_ENTRY_DATE = "ai.victorl.toda.addeditentry.ENTRY_DATE";

    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerview) RecyclerView entryRecyclerView;

    @OnClick(R.id.fab)
    void onFabClick() {
        entryPresenter.save();
    }

    @Inject AddEditEntryContract.Presenter entryPresenter;
    @Inject TodaSettings todaSettings;

    private AddEditEntryAdapter entryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        ButterKnife.bind(this);
        DaggerAddEditEntryComponent.builder()
                .dataComponent(TodaApp.from(this).getDataComponent())
                .addEditEntryModule(new AddEditEntryModule(this))
                .build()
                .inject(this);

        setSupportActionBar(toolbar);
        entryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        entryAdapter = new AddEditEntryAdapter(entryPresenter);
        entryRecyclerView.setAdapter(entryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        entryPresenter.subscribe();
        entryPresenter.load(getIntent().getStringExtra(KEY_ENTRY_DATE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        entryPresenter.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                entryPresenter.cancel();
                return true;
            case R.id.action_delete:
                entryPresenter.delete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (todaSettings.shouldSaveOnBack()) {
            entryPresenter.save();
            returnToDashboardAsSaved();
        } else {
            returnToDashboardAsCancelled();
        }
    }

    @Override
    public void showEntry(Entry entry) {
        entryAdapter.setEntry(entry);
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void showChangesSaved() {
        Snackbar.make(coordinatorLayout, "Changes Saved", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void returnToDashboardAsCancelled() {
        setResult(RESULT_ADD_EDIT_CANCELLED);
        finish();
    }

    public void returnToDashboardAsSaved() {
        setResult(RESULT_ADD_EDIT_SUCCESS);
        finish();
    }

    @Override
    public void returnToDashboardAsDeleted() {
        setResult(RESULT_ADD_EDIT_DELETED);
        finish();
    }

    public static void startActivityForResult(AppCompatActivity activity, int requestCode, @NonNull CalendarDay day) {
        activity.startActivityForResult(new Intent(activity, AddEditEntryActivity.class)
                .putExtra(KEY_ENTRY_DATE, EntryDateFormatter.format(day)), requestCode);
    }
}
