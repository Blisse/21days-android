package ai.victorl.toda.screens.entry;

import android.support.annotation.LayoutRes;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import ai.victorl.toda.R;
import ai.victorl.toda.ui.TextWatcherAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditEntryAdapter extends RecyclerView.Adapter<AddEditEntryAdapter.EntryViewHolder> {

    private final AddEditEntryContract.Presenter entryPresenter;

    public AddEditEntryAdapter(AddEditEntryContract.Presenter presenter) {
        entryPresenter = presenter;
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return EntryViewHolder.create(viewType, LayoutInflater.from(parent.getContext()), parent);
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
        return entryPresenter != null ? 5 : 0;
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

        public abstract void onBind(AddEditEntryContract.Presenter entryPresenter);
    }

    static class EntryJournalViewHolder extends EntryViewHolder {

        @BindView(R.id.entry_journal_edittext) EditText journalEditText;

        public EntryJournalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(AddEditEntryContract.Presenter entryPresenter) {
            journalEditText.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });
        }
    }

    static class EntryGratitudesViewHolder extends EntryViewHolder {

        public EntryGratitudesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(AddEditEntryContract.Presenter entryPresenter) {

        }
    }

    static class EntryExerciseViewHolder extends EntryViewHolder {

        public EntryExerciseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(AddEditEntryContract.Presenter entryPresenter) {

        }
    }

    static class EntryMeditationViewHolder extends EntryViewHolder {

        public EntryMeditationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(AddEditEntryContract.Presenter entryPresenter) {

        }
    }

    static class EntryKindnessViewHolder extends EntryViewHolder {

        public EntryKindnessViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(AddEditEntryContract.Presenter entryPresenter) {

        }
    }
}
