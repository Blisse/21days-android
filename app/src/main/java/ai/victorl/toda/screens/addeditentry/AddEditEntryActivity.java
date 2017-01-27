package ai.victorl.toda.screens.addeditentry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import ai.victorl.toda.screens.addeditentry.views.AddEditEntryAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditEntryActivity extends AppCompatActivity implements AddEditEntryContract.View {

    public static final String KEY_ENTRY_DATE = "ai.victorl.toda.addeditentry.ENTRY_DATE";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.entry_recyclerview) RecyclerView entryRecyclerView;

    @OnClick(R.id.fab)
    void onFabClick() {
        entryPresenter.save();
    }

    @Inject AddEditEntryContract.Presenter entryPresenter;

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
        entryPresenter.start();
        entryPresenter.load(getIntent().getStringExtra(KEY_ENTRY_DATE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        entryPresenter.stop();
        entryPresenter.save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry, menu);
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
    public void showEntry(Entry entry) {
        entryAdapter.setEntry(entry);
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public static void start(Context context, @NonNull CalendarDay day) {
        context.startActivity(new Intent(context, AddEditEntryActivity.class)
                .putExtra(KEY_ENTRY_DATE, EntryDateFormatter.format(day)));
    }
}
