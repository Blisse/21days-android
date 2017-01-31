package ai.victorl.toda.screens.addeditentry.views;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.Arrays;
import java.util.List;

import ai.victorl.toda.R;
import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.screens.addeditentry.AddEditEntryContract;
import ai.victorl.toda.ui.RecyclerViewHolder;
import ai.victorl.toda.ui.RecyclerViewHolderLayout;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.observers.Observers;

public class AddEditEntryAdapter<T extends AddEditEntryAdapter.EntryViewHolder> extends RecyclerView.Adapter<T> {

    private final List<RecyclerViewHolderLayout<T>> addEditEntryViewHolderLayouts = Arrays.asList(
            new RecyclerViewHolderLayout<T>(R.layout.item_entry_journal, (Class<T>) EntryJournalViewHolder.class),
            new RecyclerViewHolderLayout<T>(R.layout.item_entry_gratitudes, (Class<T>) EntryGratitudesViewHolder.class),
            new RecyclerViewHolderLayout<T>(R.layout.item_entry_exercise, (Class<T>) EntryExerciseViewHolder.class),
            new RecyclerViewHolderLayout<T>(R.layout.item_entry_meditation, (Class<T>) EntryMeditationViewHolder.class),
            new RecyclerViewHolderLayout<T>(R.layout.item_entry_kindness, (Class<T>) EntryKindnessViewHolder.class)
    );

    private final AddEditEntryContract.Presenter entryPresenter;

    public AddEditEntryAdapter(AddEditEntryContract.Presenter presenter) {
        this.entryPresenter = presenter;
    }

    public void load() {
        notifyDataSetChanged();
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return RecyclerViewHolder.create(addEditEntryViewHolderLayouts.get(viewType), parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        holder.onBind(entryPresenter);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return addEditEntryViewHolderLayouts.size();
    }

    private static void recolourView(int status, View view, int baseColour, int oneColour, int twoColour) {
        if (status < 20) {
            view.setBackgroundColor(baseColour);
        } else if (status < 100) {
            view.setBackgroundColor(oneColour);
        } else {
            view.setBackgroundColor(twoColour);
        }
    }

    static class EntryJournalViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText editText;
        @BindView(R.id.header_linearlayout) LinearLayoutCompat header;

        public EntryJournalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(AddEditEntryContract.Presenter presenter) {
            RxTextView.textChangeEvents(editText)
                    .skip(1)
                    .map(e -> e.text().toString())
                    .subscribe(s -> {
                        presenter.setEntryJournal(s);
                        recolourView(Entry.journalComplete(s), header, darkGrayColour, orangeColour, greenColour);
                    });

            presenter.getEntry()
                    .map(entry -> entry.journal)
                    .subscribe(RxTextView.text(editText)::call);
        }
    }

    static class EntryGratitudesViewHolder extends EntryViewHolder {

        @BindView(R.id.add_imageview) ImageView addGratitudeImageView;
        @BindView(R.id.remove_imageview) ImageView removeGratitudeImageView;
        @BindView(R.id.completion_textview) TextView completionTextView;
        @BindView(R.id.recyclerview) RecyclerView gratitudesRecyclerView;
        @BindView(R.id.header_linearlayout) LinearLayoutCompat header;

        private EntryGratitudesAdapter gratitudesAdapter;

        public EntryGratitudesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(AddEditEntryContract.Presenter presenter) {
            gratitudesAdapter = new EntryGratitudesAdapter(presenter, completionTextView, addGratitudeImageView, removeGratitudeImageView,
                    Observers.create(gratitudes -> recolourView(Entry.gratitudesComplete(gratitudes), header, darkGrayColour, orangeColour, greenColour)));

            gratitudesRecyclerView.setLayoutManager(new LinearLayoutManager(gratitudesRecyclerView.getContext()));
            gratitudesRecyclerView.setAdapter(gratitudesAdapter);

            presenter.getEntry()
                    .map(entry -> entry.gratitudes)
                    .subscribe(gratitudes -> {
                        gratitudesAdapter.load(gratitudes.size());
                    });
        }
    }

    static class EntryExerciseViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText editText;
        @BindView(R.id.header_linearlayout) LinearLayoutCompat header;

        public EntryExerciseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(AddEditEntryContract.Presenter presenter) {
            RxTextView.textChangeEvents(editText)
                    .skip(1)
                    .map(e -> e.text().toString())
                    .subscribe(s -> {
                        presenter.setEntryExercise(s);
                        recolourView(Entry.exerciseComplete(s), header, darkGrayColour, orangeColour, greenColour);
                    });

            presenter.getEntry()
                    .map(entry -> entry.exercise)
                    .subscribe(RxTextView.text(editText)::call);
        }
    }

    static class EntryMeditationViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText editText;
        @BindView(R.id.header_linearlayout) LinearLayoutCompat header;

        public EntryMeditationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(AddEditEntryContract.Presenter presenter) {
            RxTextView.textChangeEvents(editText)
                    .skip(1)
                    .map(e -> e.text().toString())
                    .subscribe(s -> {
                        presenter.setEntryMeditation(s);
                        recolourView(Entry.meditationComplete(s), header, darkGrayColour, orangeColour, greenColour);
                    });

            presenter.getEntry()
                    .map(entry -> entry.meditation)
                    .subscribe(RxTextView.text(editText)::call);
        }
    }

    static class EntryKindnessViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText editText;
        @BindView(R.id.header_linearlayout) LinearLayoutCompat header;

        public EntryKindnessViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(AddEditEntryContract.Presenter presenter) {
            RxTextView.textChangeEvents(editText)
                    .skip(1)
                    .map(e -> e.text().toString())
                    .subscribe(s -> {
                        presenter.setEntryKindness(s);
                        recolourView(Entry.kindnessComplete(s), header, darkGrayColour, orangeColour, greenColour);
                    });

            presenter.getEntry()
                    .map(entry -> entry.kindness)
                    .subscribe(RxTextView.text(editText)::call);
        }
    }

    static abstract class EntryViewHolder extends RecyclerViewHolder {

        @BindColor(R.color.green) int greenColour;
        @BindColor(R.color.orange) int orangeColour;
        @BindColor(R.color.darkgray) int darkGrayColour;

        EntryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public abstract void onBind(AddEditEntryContract.Presenter presenter);
    }

}
