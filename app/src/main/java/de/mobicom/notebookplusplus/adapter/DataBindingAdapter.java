package de.mobicom.notebookplusplus.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class DataBindingAdapter {

    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

}
