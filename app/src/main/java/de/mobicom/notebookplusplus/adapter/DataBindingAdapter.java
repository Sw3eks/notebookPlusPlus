package de.mobicom.notebookplusplus.adapter;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @BindingAdapter("bindDate")
    public static void formatDate(TextView view, LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMM - HH:mm");
        String text = formatter.format(dateTime);
        view.setText(text);
    }
}
