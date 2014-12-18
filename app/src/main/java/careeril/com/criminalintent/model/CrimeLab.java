package careeril.com.criminalintent.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lordsnow1991 on 11/23/14.
 */
public class CrimeLab {


    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";
    private static CrimeLab sCrimeLab;
    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSerializer;
    private Context mAppContext;


    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
        try {
            mCrimes = mSerializer.loadCrimes();
        } catch (Exception e) {
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes: ", e);
        }

    }

    public static CrimeLab get(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public boolean saveCrimes() {
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving crimes: ", e);
            return false;
        }
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }


    public void addCrime(Crime c) {
        mCrimes.add(c);
    }

    public void deleteCrime(Crime c) {
        mCrimes.remove(c);
        saveCrimes();
    }

}