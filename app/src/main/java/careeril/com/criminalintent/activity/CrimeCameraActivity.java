package careeril.com.criminalintent.activity;

import android.support.v4.app.Fragment;

import careeril.com.criminalintent.fragments.CrimeCameraFragment;

/**
 * Created by Lordsnow1991 on 12/11/14.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();

    }
}
