package com.dtag.osta.utility;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private OnTimePicked onTime;
    private Context context;
    private boolean isForReservation;

    public TimePickerFragment(OnTimePicked onPicked, Context context, boolean isForReservation) {
        this.onTime = onPicked;
        this.context = context;
        this.isForReservation = isForReservation;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog pickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                onTime.onTimeSetDone(String.format(Locale.US, "%02d:%02d:00", hourOfDay, minute));
            }
        }, currentHour, currentMinute, true);
        return pickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        onTime.onTimeSetDone(String.format(Locale.US, "%d:%d:00", hourOfDay, minute));
    }

    public interface OnTimePicked {
        void onTimeSetDone(String timeText);
    }
}