package ai.victorl.toda.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public RecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public static <T extends RecyclerViewHolder> T create(RecyclerViewHolderLayout<T> viewHolderLayout, Context context, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return create(viewHolderLayout, inflater, parent);
    }

    private static <T extends RecyclerViewHolder> T create(RecyclerViewHolderLayout<T> viewHolderLayout, LayoutInflater inflater, ViewGroup parent) {
        return create(viewHolderLayout.viewHolderClass, viewHolderLayout.layoutId, inflater, parent);
    }

    public static <T extends RecyclerViewHolder> T create(Class<T> clazz, @LayoutRes int layoutId, LayoutInflater inflater, ViewGroup parent) {
        try {
            View root = inflater.inflate(layoutId, parent, false);
            Constructor<T> ctor = clazz.getConstructor(View.class);
            return ctor.newInstance(root);
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
