package ai.victorl.toda.screens.dashboard.views;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import ai.victorl.toda.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardHeader implements Target {
    @BindView(R.id.navigation_header_imageview) ImageView headerImageView;
    @BindView(R.id.navigation_header_name_textview) TextView headerNameTextView;
    @BindView(R.id.navigation_header_email_textview) TextView headerEmailTextView;

    public DashboardHeader(View view) {
        ButterKnife.bind(this, view);
    }

    public void setName(String name) {
        headerNameTextView.setText(name);
    }

    public void setEmail(String email) {
        headerEmailTextView.setText(email);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        headerImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        headerImageView.setImageDrawable(errorDrawable);
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        headerImageView.setImageDrawable(placeHolderDrawable);
    }
}
