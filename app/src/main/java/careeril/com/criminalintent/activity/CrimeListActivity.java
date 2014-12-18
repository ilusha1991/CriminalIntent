package careeril.com.criminalintent.activity;

import android.support.v4.app.Fragment;

import careeril.com.criminalintent.fragments.CrimeListFragment;

/**
 * Created by Lordsnow1991 on 11/23/14.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Fragment fragment = new CrimeListFragment();

        return fragment;

    }

}
