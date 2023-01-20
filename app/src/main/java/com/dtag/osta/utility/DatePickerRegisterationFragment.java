package com.dtag.osta.utility;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerRegisterationFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private OnDatePicked onDatePicked;
    private Context context;
    private boolean isForReservation;

    public DatePickerRegisterationFragment(OnDatePicked onDatePicked, Context context, boolean isForReservation) {
        this.onDatePicked = onDatePicked;
        this.context = context;
        this.isForReservation = isForReservation;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(context, this, year, month, day);
        if (isForReservation) {
        }

        // Create a new instance of DatePickerDialog and return it
        return pickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        onDatePicked.onDateSetDone(String.format(Locale.US, "%d-%d-%d", year, (month + 1), dayOfMonth));

    }

    public interface OnDatePicked {
        void onDateSetDone(String dateText);
    }
}