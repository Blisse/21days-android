package ai.victorl.toda.screens.entry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import ai.victorl.toda.R;
import ai.victorl.toda.data.entry.CalendarDate;
import ai.victorl.toda.utils.di.Injector;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditEntryActivity extends AppCompatActivity {

    public static final String KEY_ENTRY_DATE = "ai.victorl.toda.entry.ENTRY_DATE";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.entry_view) AddEditEntryView addEditEntryView;

    @Inject AddEditEntryContract.Presenter entryPresenter;
    @Inject
    AddEditEntryAdapter addEditEntryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        CalendarDate date = new CalendarDate.Builder().forToday();

        ButterKnife.bind(this);
        Injector.buildEntryComponent(this, addEditEntryView, date.toString()).inject(this);

        setSupportActionBar(toolbar);

        addEditEntryView.setPresenter(entryPresenter);
        addEditEntryView.setAddEditEntryAdapter(addEditEntryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        entryPresenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        entryPresenter.stop();
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
}
