package de.mobicom.notebookplusplus.adapter;

import android.graphics.PorterDuff;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import static android.graphics.Color.parseColor;

public class DataBindingAdapter {

    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter("colorFilter")
    public static void setColorFilter(ImageView imageView, int resource) {
        imageView.setColorFilter(resource, PorterDuff.Mode.MULTIPLY);
    }

}
