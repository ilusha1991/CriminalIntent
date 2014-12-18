package careeril.com.criminalintent.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import careeril.com.criminalintent.R;

/**
 * Created by Lordsnow1991 on 11/26/14.
 */


public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE =
            "careeril.com.criminalintent.date";
    private static final int REQUEST_TIME = 3;
    private static final String DIALOG_TIME = "time";
    private Date mDate;
    private Calendar mCal;

    public static DatePickerFragment newInstance(Calendar cal) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, cal);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mCal);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_TIME) {

            int m = mCal.get(Calendar.MONTH);
            int d = mCal.get(Calendar.DAY_OF_MONTH);
            int y = mCal.get(Calendar.YEAR);
            Calendar time = (Calendar) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

            int h = time.get(Calendar.HOUR_OF_DAY);
            int mi = time.get(Calendar.MINUTE);
            mCal = new GregorianCalendar(y, m, d, h, mi);
            sendResult(Activity.RESULT_OK);


        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
        // Create a Calendar to get the year, month, and day
        Calendar calendar = (Calendar) getArguments().getSerializable(EXTRA_DATE);
        //calendar.setTime(mDate);
        mCal = calendar;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_date, null);

        DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                // Translate year, month, day into a Date object using a calendar
                //mDate = new GregorianCalendar(year, month, day).getTime();
                mCal = new GregorianCalendar(year, month, day);

                // Update argument to preserve selected value on rotation
                getArguments().putSerializable(EXTRA_DATE, mCal);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentManager fm = getActivity()
                                        .getSupportFragmentManager();


                                TimePickerFragment tdialog = TimePickerFragment
                                        .newInstance(Calendar.getInstance());

                                tdialog.setTargetFragment(DatePickerFragment.this, REQUEST_TIME);


                                tdialog.show(fm, DIALOG_TIME);
                                /////////////                                    sendResult(Activity.RESULT_OK);

                            }
                        })
                .create();

    }
}
