package de.mobicom.notebookplusplus.adapter;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.databinding.BindingAdapter;
import de.mobicom.notebookplusplus.R;

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

    @BindingAdapter("bookmarkIcon")
    public static void setBookmarked(ImageView imageView, boolean bookmark) {
        if (bookmark) {
            imageView.setImageResource(R.drawable.ic_note_bookmark_enabled);
            imageView.setColorFilter(R.color.colorAlizarin, PorterDuff.Mode.DST);
        } else {
            imageView.setImageResource(R.drawable.ic_note_bookmark_disabled);
        }
    }

    @BindingAdapter("bindDatePicker")
    public static void setDate(DatePicker datepicker, LocalDate date) {
        if (date != null) {
            datepicker.updateDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        }
    }
}
