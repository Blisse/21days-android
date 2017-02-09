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

import com.github.clans.fab.FloatingActionButton;
import com.jakewharton.rxbinding.view.RxView;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import javax.inject.Inject;

import ai.victorl.toda.R;
import ai.victorl.toda.app.TodaApp;
import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.data.entry.EntryDateFormatter;
import ai.victorl.toda.data.settings.TodaSettings;
import ai.victorl.toda.screens.addeditentry.views.AddEditEntryAdapter;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditEntryActivity extends AppCompatActivity implements AddEditEntryContract.View {

    public static final int REQUEST_ADD_EDIT = 1;
    public static final int RESULT_ADD_EDIT_SUCCESS = 1;
    public static final int RESULT_ADD_EDIT_CANCELLED = 2;
    public static final int RESULT_ADD_EDIT_DELETED = 3;
    private static final String KEY_ENTRY_DATE = "ai.victorl.toda.addeditentry.ENTRY_DATE";

    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerview) RecyclerView entryRecyclerView;
    @BindView(R.id.fab_delete) FloatingActionButton deleteFab;
    @BindView(R.id.fab_cancel) FloatingActionButton cancelFab;
    @BindView(R.id.fab_save) FloatingActionButton saveFab;

    @BindColor(R.color.green) int greenColour;
    @BindColor(R.color.orange) int orangeColour;
    @BindColor(R.color.darkgray) int darkGrayColour;

    @BindString(R.string.popup_entry_saved) String popupEntryChangesSavedString;

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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RxView.clicks(saveFab)
                .subscribe(x -> {
                    entryPresenter.save(true);
                });

        RxView.clicks(cancelFab)
                .subscribe(x -> {
                    entryPresenter.cancel();;
                });

        RxView.clicks(deleteFab)
                .subscribe(x -> {
                    entryPresenter.delete();
                });

        entryAdapter = new AddEditEntryAdapter(new AddEditEntryAdapter.AddEditEntryListener() {
            @Override
            public void onChangeJournal(String journal) {
                entryPresenter.setEntryJournal(journal);
            }

            @Override
            public void onChangeGratitudes(List<String> gratitudes) {
                entryPresenter.setEntryGratitudes(gratitudes);
            }

            @Override
            public void onChangeExercise(String exercise) {
                entryPresenter.setEntryExercise(exercise);
            }

            @Override
            public void onChangeMeditation(String meditation) {
                entryPresenter.setEntryMeditation(meditation);
            }

            @Override
            public void onChangeKindness(String kindness) {
                entryPresenter.setEntryKindness(kindness);
            }
        });

        entryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    public void onBackPressed() {
        if (todaSettings.shouldSaveOnBack()) {
            entryPresenter.save(false);
            returnToDashboardAsSaved();
        } else {
            returnToDashboardAsCancelled();
        }
    }

    @Override
    public void showEntry(Entry entry) {
        entryAdapter.load(entry);
        entryRecyclerView.setAdapter(entryAdapter);
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void updateStatus(int status) {
        if (status < 33) {
            toolbar.setBackgroundColor(darkGrayColour);
        } else if (status < 100) {
            toolbar.setBackgroundColor(orangeColour);
        } else {
            toolbar.setBackgroundColor(greenColour);
        }
    }

    @Override
    public void showChangesSaved() {
        Snackbar.make(coordinatorLayout, popupEntryChangesSavedString, Snackbar.LENGTH_SHORT).show();
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
                .putExtra(KEY_ENTRY_DATE, EntryDateFormatter.shortFormat(day)), requestCode);
    }
}
