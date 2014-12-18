package careeril.com.criminalintent.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import careeril.com.criminalintent.R;

/**
 * Created by Lordsnow1991 on 12/1/14.
 */
public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_TIME =
            "careeril.com.criminalintent.time";
    private Time mTime;
    private Calendar mCal;


    public static TimePickerFragment newInstance(Calendar cal) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME, cal);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //mTime = (Time)getArguments().getSerializable(EXTRA_TIME);
        Calendar calendar = (Calendar) getArguments().getSerializable(EXTRA_TIME);
        mCal = calendar;

        int h = calendar.get(calendar.HOUR_OF_DAY);
        int m = calendar.get(calendar.MINUTE);

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_time, null);

        TimePicker timepicker = (TimePicker) v.findViewById(R.id.dialog_time_timePicker);
        timepicker.setCurrentHour(h);
        timepicker.setCurrentMinute(m);
        timepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int h, int m) {

                //mTime  = (Time)new GregorianCalendar(0, 0, 0, h, m).getTime();
                mCal = new GregorianCalendar(0, 0, 0, h, m);
                getArguments().putSerializable(EXTRA_TIME, mCal);


            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();

    }


    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(EXTRA_TIME, mCal);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }


}
