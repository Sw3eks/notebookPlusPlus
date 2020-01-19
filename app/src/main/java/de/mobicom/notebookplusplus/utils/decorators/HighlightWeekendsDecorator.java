package de.mobicom.notebookplusplus.utils.decorators;

import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.DayOfWeek;

import androidx.fragment.app.Fragment;
import de.mobicom.notebookplusplus.R;

public class HighlightWeekendsDecorator implements DayViewDecorator {

    private final int highlightColor;

    public HighlightWeekendsDecorator(Fragment context) {
        highlightColor = context.getResources().getColor(R.color.colorPeterRiver, null);
    }

    @Override
    public boolean shouldDecorate(final CalendarDay day) {
        final DayOfWeek weekDay = day.getDate().getDayOfWeek();
        return weekDay == DayOfWeek.SATURDAY || weekDay == DayOfWeek.SUNDAY;
    }

    @Override
    public void decorate(final DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(highlightColor));
    }
}
