package careeril.com.criminalintent.fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import careeril.com.criminalintent.R;
import careeril.com.criminalintent.activity.CrimeCameraActivity;
import careeril.com.criminalintent.model.Crime;
import careeril.com.criminalintent.model.CrimeLab;

/**
 * Created by Lordsnow1991 on 11/18/14.
 */
public class CrimeFragment extends Fragment {
    public static final String EXTRA_CRIME_ID =
            "careeril.com.criminalintent.crime_id";
    private static final String DIALOG_DATE = "date";

    private static final String TAG = "CrimeFragment";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;

    //private boolean mConfig=false;
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mSaveButton;

    private ImageButton mPhotoButton;

    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;

    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        mConfig=true;
//        super.onConfigurationChanged(newConfig);
//    }

    private void updateDate() {
        //mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setText(Crime.dateString(mCrime.getCal()));
    }

    @Override
    public void onPause() {
        super.onPause();


        if (CrimeLab.get(getActivity()).getCrime(mCrime.getId()).isSaveFlag())
            CrimeLab.get(getActivity()).saveCrimes();
        else {
            CrimeLab.get(getActivity()).deleteCrime(mCrime);
            CrimeLab.get(getActivity()).saveCrimes();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Calendar date = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setCal(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                ImageView v = (ImageView) getActivity().findViewById(R.id.crime_fragment_previewIMG);
                String path = getActivity()
                        .getFileStreamPath(filename).getAbsolutePath();
                File imgFile = new File(filename);
                Bitmap bm = BitmapFactory.decodeFile(path);

                v.setImageBitmap(bm);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("title", mCrime.getTitle());
        savedInstanceState.putLong("date", mCrime.getCal().getTime().getTime());
        savedInstanceState.putBoolean("solved", mCrime.isSolved());


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);

        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        if (mCrime == null) {
            Crime c = new Crime();


            if (savedInstanceState != null) {


                Date date = new Date();
                date.setTime(savedInstanceState.getLong("date", 0));
                Calendar call = Calendar.getInstance();
                call.setTime(date);
                c.setCal(call);
                c.setSolved(savedInstanceState.getBoolean("solved", false));
                c.setTitle(savedInstanceState.getString("title", ""));
            }
            CrimeLab.get(getActivity()).addCrime(c);
            mCrime = c;
        }


        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_save_crime:

                CrimeLab.get(getActivity()).getCrime(mCrime.getId()).setSaveFlag(true);
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }

        }
        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(
                    CharSequence c, int start, int before, int count) {
                mCrime.setTitle(c.toString());
            }

            public void beforeTextChanged(
                    CharSequence c, int start, int count, int after) {
                // This space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // This one too
            }
        });


        mSaveButton = (Button) v.findViewById(R.id.saveButton);
        mSaveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                CrimeLab.get(getActivity()).getCrime(mCrime.getId()).setSaveFlag(true);
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }

            }

        });


        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();


                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getCal());

                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);


                dialog.show(fm, DIALOG_DATE);
            }
        });
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the crime's solved property
                mCrime.setSolved(isChecked);
            }
        });


        return v;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }


}
