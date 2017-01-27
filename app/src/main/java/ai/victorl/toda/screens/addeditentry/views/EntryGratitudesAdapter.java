package ai.victorl.toda.screens.addeditentry.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ai.victorl.toda.R;
import ai.victorl.toda.ui.TextWatcherAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryGratitudesAdapter extends RecyclerView.Adapter<EntryGratitudesAdapter.EntryGratitudeViewHolder> {

    private final List<String> gratitudes = new ArrayList<>();

    private final GratitudesChangedListener listener;

    public EntryGratitudesAdapter(GratitudesChangedListener listener) {
        this.listener = listener;
    }

    public void setGratitudes(Collection<String> gratitudes) {
        this.gratitudes.clear();
        this.gratitudes.addAll(gratitudes);
        notifyDataSetChanged();
        listener.onGratitudesChanged(this.gratitudes);
    }

    public void addGratitude() {
        int position = gratitudes.size();
        this.gratitudes.add(position, "");
        notifyItemInserted(position);
        listener.onGratitudesChanged(this.gratitudes);
    }

    public List<String> getGratitudes() {
        return gratitudes;
    }

    public void removeGratitude(int position) {
        this.gratitudes.remove(position);
        notifyItemRemoved(position);
        listener.onGratitudesChanged(this.gratitudes);
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

        @BindView(R.id.entry_gratitude_edittext) EditText gratitudeEditText;

        EntryGratitudeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(final GratitudesChangedListener listener, final List<String> gratitudes, final int position) {
            gratitudeEditText.setText(gratitudes.get(position));
            gratitudeEditText.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (position < gratitudes.size()) {
                        // TODO: Investigate issue where position >= gratitudes.size()
                        gratitudes.set(position, s.toString());
                        listener.onGratitudesChanged(gratitudes);
                    } else {
                        Log.d("Toda", "gratitudeEditText::onTextChanged: position >= gratitudes.size()");
                    }
                }
            });
        }
    }

    interface GratitudesChangedListener {
        void onGratitudesChanged(List<String> gratitudes);
    }
}
