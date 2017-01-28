package ai.victorl.toda.screens.addeditentry.views;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ai.victorl.toda.R;
import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.screens.addeditentry.AddEditEntryContract;
import ai.victorl.toda.ui.EntryChangeViewDecorator;
import ai.victorl.toda.ui.RecyclerViewHolder;
import ai.victorl.toda.ui.RecyclerViewHolderLayout;
import ai.victorl.toda.ui.TextWatcherAdapter;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditEntryAdapter<T extends AddEditEntryAdapter.EntryViewHolder> extends RecyclerView.Adapter<T> {

    private final List<RecyclerViewHolderLayout<T>> addEditEntryViewHolderLayouts = Arrays.asList(
            new RecyclerViewHolderLayout<T>(R.layout.item_entry_journal, (Class<T>) EntryJournalViewHolder.class),
            new RecyclerViewHolderLayout<T>(R.layout.item_entry_gratitudes, (Class<T>) EntryGratitudesViewHolder.class),
            new RecyclerViewHolderLayout<T>(R.layout.item_entry_exercise, (Class<T>) EntryExerciseViewHolder.class),
            new RecyclerViewHolderLayout<T>(R.layout.item_entry_meditation, (Class<T>) EntryMeditationViewHolder.class),
            new RecyclerViewHolderLayout<T>(R.layout.item_entry_kindness, (Class<T>) EntryKindnessViewHolder.class)
    );

    private final AddEditEntryContract.Presenter entryPresenter;

    private Entry entry;

    public AddEditEntryAdapter(AddEditEntryContract.Presenter presenter) {
        entryPresenter = presenter;
    }

    public void setEntry(Entry oEntry) {
        if (entry == null || oEntry == null) {
            entry = oEntry;
            notifyDataSetChanged();
        } else {
            if (!entry.date.equals(oEntry.date)) {
                notifyDataSetChanged();
            } else {
                List<Integer> changedPositions = new ArrayList<>();
                if (!TextUtils.equals(entry.journal, oEntry.journal)) {
                    changedPositions.add(0);
                }
                if (!entry.gratitudes.equals(oEntry.gratitudes)) {
                    changedPositions.add(1);
                }
                if (TextUtils.equals(entry.exercise, oEntry.exercise)) {
                    changedPositions.add(2);
                }
                if (TextUtils.equals(entry.meditation, oEntry.meditation)) {
                    changedPositions.add(3);
                }
                if (TextUtils.equals(entry.kindness, oEntry.kindness)) {
                    changedPositions.add(4);
                }

                entry = oEntry;
                for (Integer position : changedPositions) {
                    notifyItemChanged(position);
                }
            }
        }
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return RecyclerViewHolder.create(addEditEntryViewHolderLayouts.get(viewType), parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        holder.onBind(entry);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return entry != null ? addEditEntryViewHolderLayouts.size() : 0;
    }

    static class EntryJournalViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText journalEditText;
        @BindView(R.id.header_linearlayout) LinearLayoutCompat header;

        public EntryJournalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(final Entry entry) {
            final EntryChangeViewDecorator viewDecorator = new EntryChangeViewDecorator(header, darkGrayColour, orangeColour, greenColour);
            journalEditText.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    entry.journal = s.toString();
                    viewDecorator.onEntryChanged(entry.getJournalComplete());
                }
            });
            journalEditText.setText(entry.journal);
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
        public void onBind(final Entry entry) {
            final EntryChangeViewDecorator viewDecorator = new EntryChangeViewDecorator(header, darkGrayColour, orangeColour, greenColour);
            gratitudesAdapter = new EntryGratitudesAdapter(gratitudes -> {
                entry.gratitudes = gratitudes;
                completionTextView.setText(String.format(Locale.getDefault(), "%d/3", entry.gratitudes.size()));
                viewDecorator.onEntryChanged(entry.getGratitudesComplete());
            });
            gratitudesAdapter.setGratitudes(entry.gratitudes);

            gratitudesRecyclerView.setLayoutManager(new LinearLayoutManager(gratitudesRecyclerView.getContext()));
            gratitudesRecyclerView.setAdapter(gratitudesAdapter);

            addGratitudeImageView.setOnClickListener(v -> {
                gratitudesAdapter.addGratitude();
            });
            removeGratitudeImageView.setOnClickListener(v -> {
                int size = gratitudesAdapter.getItemCount();
                if ((size > 0) && TextUtils.isEmpty(entry.gratitudes.get(size - 1))) {
                    gratitudesAdapter.removeGratitude(size - 1);
                }
            });
        }
    }

    static class EntryExerciseViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText exerciseEditText;
        @BindView(R.id.header_linearlayout) LinearLayoutCompat header;

        public EntryExerciseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(final Entry entry) {
            final EntryChangeViewDecorator viewDecorator = new EntryChangeViewDecorator(header, darkGrayColour, orangeColour, greenColour);
            exerciseEditText.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    entry.exercise = s.toString();
                    viewDecorator.onEntryChanged(entry.getExerciseComplete());
                }
            });
            exerciseEditText.setText(entry.exercise);
        }
    }

    static class EntryMeditationViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText meditationEditText;
        @BindView(R.id.header_linearlayout) LinearLayoutCompat header;

        public EntryMeditationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(final Entry entry) {
            final EntryChangeViewDecorator viewDecorator = new EntryChangeViewDecorator(header, darkGrayColour, orangeColour, greenColour);
            meditationEditText.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    entry.meditation = s.toString();
                    viewDecorator.onEntryChanged(entry.getMeditationComplete());
                }
            });
            meditationEditText.setText(entry.meditation);
        }
    }

    static class EntryKindnessViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText kindnessEditText;
        @BindView(R.id.header_linearlayout) LinearLayoutCompat header;

        public EntryKindnessViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(final Entry entry) {
            final EntryChangeViewDecorator viewDecorator = new EntryChangeViewDecorator(header, darkGrayColour, orangeColour, greenColour);
            kindnessEditText.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    entry.kindness = s.toString();
                    viewDecorator.onEntryChanged(entry.getKindnessComplete());
                }
            });
            kindnessEditText.setText(entry.kindness);
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

        public abstract void onBind(Entry entry);
    }

}
