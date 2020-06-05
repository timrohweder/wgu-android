package android.example.rohwedercoursescheduler;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.widget.DatePicker;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private String tag;


    public DatePickerFragment() {
        // Required empty public constructor
    }

    public DatePickerFragment(String tag) {
        // Required empty public constructor
        this.tag = tag;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(
                getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker,
                          int year, int month, int day) {
        switch (getActivity().getLocalClassName()) {
            case "AddEditTermActivity":
                ((AddEditTermActivity) getActivity()).processDatePickerResult(year, month, day, this.tag);
                break;
            case "AddEditCourseActivity":
                ((AddEditCourseActivity) getActivity()).processDatePickerResult(year, month, day, this.tag);
                break;
            case "AddEditAssessmentActivity":
                ((AddEditAssessmentActivity) getActivity()).processDatePickerResult(year, month, day, this.tag);
                break;
        }
    }
}
