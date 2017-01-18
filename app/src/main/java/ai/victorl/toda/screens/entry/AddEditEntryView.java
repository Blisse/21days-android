package ai.victorl.toda.screens.entry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import ai.victorl.toda.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditEntryView extends LinearLayoutCompat implements AddEditEntryContract.View {

    @BindView(R.id.entry_refreshlayout) SwipeRefreshLayout entrySwipeRefreshLayout;
    @BindView(R.id.entry_recyclerview) RecyclerView entryRecyclerView;

    private AddEditEntryContract.Presenter entryPresenter;

    public AddEditEntryView(Context context) {
        super(context);
        init(context);
    }

    public AddEditEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View root = inflate(context, R.layout.view_content_entry, this);

        ButterKnife.bind(this, root);

        entryRecyclerView.setLayoutManager(new LinearLayoutManager(context));

    }

    @Override
    public void setAddEditEntryAdapter(AddEditEntryAdapter addEditEntryAdapter) {
        entryRecyclerView.setAdapter(addEditEntryAdapter);
    }

    @Override
    public void setPresenter(@NonNull AddEditEntryContract.Presenter presenter) {
        entryPresenter = presenter;
    }
}
