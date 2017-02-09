package ai.victorl.toda.screens.addeditentry.views;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ai.victorl.toda.R;
import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.ui.RecyclerViewHolder;
import ai.victorl.toda.ui.RecyclerViewHolderLayout;
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

    private final AddEditEntryListener listener;
    private Entry displayEntry;

    public AddEditEntryAdapter(AddEditEntryListener listener) {
        this.listener = listener;
    }

    public void load(Entry entry) {
        displayEntry = entry;
        notifyDataSetChanged();
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return RecyclerViewHolder.create(addEditEntryViewHolderLayouts.get(viewType), parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        holder.onBind(listener, displayEntry);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return addEditEntryViewHolderLayouts.size();
    }

    static class EntryJournalViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText editText;
        @BindView(R.id.header_linearlayout) LinearLayoutCompat header;

        public EntryJournalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(AddEditEntryListener listener, final Entry entry) {
            RxTextView.text(editText)
                    .call(entry.journal);

            RxTextView.textChanges(editText)
                    .map(CharSequence::toString)
                    .subscribe(s -> {
                        listener.onChangeJournal(s);
                        recolourView(Entry.journalComplete(s), header);
                    });
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
        public void onBind(AddEditEntryListener listener, final Entry entry) {
            gratitudesAdapter = new EntryGratitudesAdapter(gratitudes -> {
                listener.onChangeGratitudes(gratitudes);
                RxTextView.text(completionTextView).call(String.format(Locale.getDefault(), "%d/3", gratitudes.size()));
                recolourView(Entry.gratitudesComplete(gratitudes), header);
            });
            gratitudesAdapter.setGratitudes(entry.gratitudes);

            RxView.clicks(addGratitudeImageView)
                    .subscribe(x -> {
                        gratitudesAdapter.addGratitude("");
                    });
            RxView.clicks(removeGratitudeImageView)
                    .subscribe(x -> {
                        gratitudesAdapter.popGratitude();
                    });

            gratitudesRecyclerView.setLayoutManager(new LinearLayoutManager(gratitudesRecyclerView.getContext()));
            gratitudesRecyclerView.setAdapter(gratitudesAdapter);
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
        public void onBind(AddEditEntryListener listener, final Entry entry) {
            RxTextView.text(editText)
                    .call(entry.exercise);

            RxTextView.textChanges(editText)
                    .map(CharSequence::toString)
                    .subscribe(s -> {
                        listener.onChangeExercise(s);
                        recolourView(Entry.exerciseComplete(s), header);
                    });
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
        public void onBind(AddEditEntryListener listener, final Entry entry) {
            RxTextView.text(editText)
                    .call(entry.meditation);

            RxTextView.textChanges(editText)
                    .map(CharSequence::toString)
                    .subscribe(s -> {
                        listener.onChangeMeditation(s);
                        recolourView(Entry.meditationComplete(s), header);
                    });
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
        public void onBind(AddEditEntryListener listener, final Entry entry) {
            RxTextView.text(editText)
                    .call(entry.kindness);

            RxTextView.textChanges(editText)
                    .map(CharSequence::toString)
                    .subscribe(s -> {
                        listener.onChangeKindness(s);
                        recolourView(Entry.kindnessComplete(s), header);
                    });
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

        protected void recolourView(int status, View view) {
            recolourView(status, view, darkGrayColour, orangeColour, greenColour);
        }

        protected void recolourView(int status, View view, int baseColour, int oneColour, int twoColour) {
            if (status < 20) {
                view.setBackgroundColor(baseColour);
            } else if (status < 100) {
                view.setBackgroundColor(oneColour);
            } else {
                view.setBackgroundColor(twoColour);
            }
        }

        public abstract void onBind(AddEditEntryListener listener, Entry entry);
    }

    public interface AddEditEntryListener {
        void onChangeJournal(String journal);
        void onChangeGratitudes(List<String> gratitudes);
        void onChangeExercise(String exercise);
        void onChangeMeditation(String meditation);
        void onChangeKindness(String kindness);
    };
}
