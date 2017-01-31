package ai.victorl.toda.screens.addeditentry.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;
import java.util.Locale;

import ai.victorl.toda.R;
import ai.victorl.toda.screens.addeditentry.AddEditEntryContract;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class EntryGratitudesAdapter extends RecyclerView.Adapter<EntryGratitudesAdapter.EntryGratitudeViewHolder> {

    private final AddEditEntryContract.Presenter presenter;
    private final TextView statusTextView;
    private final Observer<List<String>> observer;

    private int gratitudes = 0;

    public EntryGratitudesAdapter(AddEditEntryContract.Presenter presenter, TextView statusTextView, View addView, View removeView, Observer<List<String>> observer) {
        this.presenter = presenter;
        this.statusTextView = statusTextView;
        this.observer = observer;

        RxView.clicks(addView)
                .subscribe((x) -> {
                    gratitudes += 1;
                    presenter.addEntryGratitude("");
                    notifyItemInserted(gratitudes-1);
                    updateStatus();
                });

        RxView.clicks(removeView)
                .subscribe((x) -> {
                    if (gratitudes > 0) {
                        gratitudes -= 1;
                        presenter.removeEntryGratitudes(gratitudes);
                        notifyItemRemoved(gratitudes);
                        updateStatus();
                    }
                });
    }

    private void updateStatus() {
        statusTextView.setText(String.format(Locale.getDefault(), "%d/3", gratitudes));
    }

    public void load(int gratitudes) {
        this.gratitudes = gratitudes;
        updateStatus();
        notifyDataSetChanged();
    }

    @Override
    public EntryGratitudeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_entry_gratitude, parent, false);
        return new EntryGratitudeViewHolder(root);
    }

    @Override
    public void onBindViewHolder(EntryGratitudeViewHolder holder, int position) {
        holder.onBind(presenter, position, observer);
    }

    @Override
    public int getItemCount() {
        return gratitudes;
    }

    static class EntryGratitudeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.edittext) EditText editText;

        EntryGratitudeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(AddEditEntryContract.Presenter presenter, final int position, Observer<List<String>> observer) {
            presenter.getEntry()
                    .map(entry -> entry.gratitudes.get(position))
                    .subscribe(RxTextView.text(editText)::call);

            RxTextView.textChangeEvents(editText)
                    .map(e -> e.text().toString())
                    .subscribe(s -> {
                        presenter.setEntryGratitude(position, s);
                        presenter.getEntry()
                                .map(entry -> entry.gratitudes)
                                .subscribe(observer::onNext);
                    });
        }
    }
}
