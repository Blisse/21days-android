package ai.victorl.toda.ui;

import android.support.annotation.LayoutRes;

public class RecyclerViewHolderLayout<T extends RecyclerViewHolder> {
    @LayoutRes
    public int layoutId;
    public Class<T> viewHolderClass;

    public RecyclerViewHolderLayout(@LayoutRes int layoutId, Class<T> viewHolderClass) {
        this.layoutId = layoutId;
        this.viewHolderClass = viewHolderClass;
    }
}
