package de.mobicom.notebookplusplus.adapter;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import static android.graphics.Color.parseColor;

public class DataBindingAdapter {

    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter("colorFilter")
    public static void setColorFilter(ImageView imageView, String resource) {
        imageView.setColorFilter(parseColor(resource), PorterDuff.Mode.MULTIPLY);
    }

    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
