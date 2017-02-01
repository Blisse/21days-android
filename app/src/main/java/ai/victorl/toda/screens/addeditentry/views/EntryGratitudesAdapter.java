package ai.victorl.toda.screens.addeditentry.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import ai.victorl.toda.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryGratitudesAdapter extends RecyclerView.Adapter<EntryGratitudesAdapter.EntryGratitudeViewHolder> {

    private final GratitudesChangeListener listener;
    private final List<String> gratitudes = new ArrayList<>();

    public EntryGratitudesAdapter(GratitudesChangeListener listener) {
        this.listener = listener;
    }

    public void setGratitudes(List<String> gratitudes) {
        this.gratitudes.clear();
        this.gratitudes.addAll(gratitudes);
        notifyDataSetChanged();
        listener.onChangeGratitudes(gratitudes);
    }

    public void addGratitude(String gratitude) {
        int last = gratitudes.size();
        gratitudes.add(last, gratitude);
        notifyItemInserted(last);
        listener.onChangeGratitudes(gratitudes);
    }

    public void popGratitude() {
        if (gratitudes.size() > 0) {
            int last = gratitudes.size() - 1;
            gratitudes.remove(last);
            notifyItemRemoved(last);
            listener.onChangeGratitudes(gratitudes);
        }
    }

    @Override
    public EntryGratitudeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_entry_gratitude, parent, false);
        return new EntryGratitudeViewHolder(root);
    }

    @Override
    public void onBindViewHolder(EntryGratitudeViewHolder holder, int position) {
        holder.onBind(listener, gratitudes, position);
    }

    @Override
    public int getItemCount() {
        return gratitudes.size();
    }

    static class EntryGratitudeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.edittext) EditText editText;

        EntryGratitudeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(GratitudesChangeListener listener, List<String> gratitudes, final int position) {
            if (0 <= position && position < gratitudes.size()) {
                RxTextView.text(editText)
                        .call(gratitudes.get(position));

                RxTextView.textChanges(editText)
                        .map(CharSequence::toString)
                        .subscribe(s -> {
                            gratitudes.set(position, s);
                            listener.onChangeGratitudes(gratitudes);
                        });
            }
        }
    }

    public interface GratitudesChangeListener {
        void onChangeGratitudes(List<String> gratitudes);
    };
}
