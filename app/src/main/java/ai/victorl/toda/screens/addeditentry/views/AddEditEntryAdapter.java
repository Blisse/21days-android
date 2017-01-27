package ai.victorl.toda.screens.addeditentry.views;

import android.support.annotation.LayoutRes;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.victorl.toda.R;
import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.screens.addeditentry.AddEditEntryContract;
import ai.victorl.toda.ui.TextWatcherAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditEntryAdapter extends RecyclerView.Adapter<AddEditEntryAdapter.EntryViewHolder> {

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
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return EntryViewHolder.create(viewType, LayoutInflater.from(parent.getContext()), parent);
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
        return entry != null ? 5 : 0;
    }

    static class EntryJournalViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText journalEditText;

        public EntryJournalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(final Entry entry) {
            journalEditText.setText(entry.journal);
            journalEditText.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    entry.journal = s.toString();
                }
            });
        }
    }

    static class EntryGratitudesViewHolder extends EntryViewHolder {

        @BindView(R.id.add_imageview) ImageView addGratitudeImageView;
        @BindView(R.id.remove_imageview) ImageView removeGratitudeImageView;
        @BindView(R.id.recyclerview) RecyclerView gratitudesRecyclerView;

        private EntryGratitudesAdapter gratitudesAdapter;

        public EntryGratitudesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(final Entry entry) {
            gratitudesAdapter = new EntryGratitudesAdapter(gratitudes -> entry.gratitudes = gratitudes);
            gratitudesAdapter.setGratitudes(entry.gratitudes);

            gratitudesRecyclerView.setLayoutManager(new LinearLayoutManager(gratitudesRecyclerView.getContext()));
            gratitudesRecyclerView.setAdapter(gratitudesAdapter);

            addGratitudeImageView.setOnClickListener(v -> {
                gratitudesAdapter.addGratitude();
            });
            removeGratitudeImageView.setOnClickListener(v -> {
                int size = gratitudesAdapter.getItemCount();
                if ((size > 3) && TextUtils.isEmpty(entry.gratitudes.get(size - 1))) {
                    gratitudesAdapter.removeGratitude(size - 1);
                }
            });
        }
    }

    static class EntryExerciseViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText exerciseEditText;

        public EntryExerciseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(final Entry entry) {
            exerciseEditText.setText(entry.exercise);
            exerciseEditText.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    entry.exercise = s.toString();
                }
            });
        }
    }

    static class EntryMeditationViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText meditationEditText;

        public EntryMeditationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(final Entry entry) {
            meditationEditText.setText(entry.meditation);
            meditationEditText.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    entry.meditation = s.toString();
                }
            });
        }
    }

    static class EntryKindnessViewHolder extends EntryViewHolder {

        @BindView(R.id.edittext) EditText kindnessEditText;

        public EntryKindnessViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(final Entry entry) {
            kindnessEditText.setText(entry.kindness);
            kindnessEditText.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    entry.kindness = s.toString();
                }
            });
        }
    }

    static abstract class EntryViewHolder extends RecyclerView.ViewHolder {

        EntryViewHolder(View itemView) {
            super(itemView);
        }

        private static final Map<Integer, Pair<Class, Integer>> layoutMap = new HashMap<>();

        static {
            layoutMap.put(0, Pair.<Class, Integer>create(EntryJournalViewHolder.class, R.layout.item_entry_journal));
            layoutMap.put(1, Pair.<Class, Integer>create(EntryGratitudesViewHolder.class, R.layout.item_entry_gratitudes));
            layoutMap.put(2, Pair.<Class, Integer>create(EntryExerciseViewHolder.class, R.layout.item_entry_exercise));
            layoutMap.put(3, Pair.<Class, Integer>create(EntryMeditationViewHolder.class, R.layout.item_entry_meditation));
            layoutMap.put(4, Pair.<Class, Integer>create(EntryKindnessViewHolder.class, R.layout.item_entry_kindness));
        }

        public static <T extends EntryViewHolder> EntryViewHolder create(int viewType, LayoutInflater inflater, ViewGroup parent) {
            Pair<Class, Integer> layout = layoutMap.get(viewType);
            return create(layout.first, layout.second, inflater, parent);
        }

        public static <T extends EntryViewHolder> EntryViewHolder create(Class<T> clazz, @LayoutRes int layoutId, LayoutInflater inflater, ViewGroup parent) {
            try {
                View root = inflater.inflate(layoutId, parent, false);
                Constructor<T> ctor = clazz.getConstructor(View.class);
                return ctor.newInstance(root);
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        public abstract void onBind(Entry entry);
    }

}
