package careeril.com.criminalintent.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by Lordsnow1991 on 11/18/14.
 */
public class Crime {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_SAVE = "save";
    private UUID mId;
    private String mTitle;
    // private Date mDate;
    private boolean mSolved;
    private boolean mSaveFlag = false;
    private Calendar mCal;
    public Crime() {
        // Generate unique identifier
        mId = UUID.randomUUID();
        // mDate = new Date();
        mCal = Calendar.getInstance();
    }
    public Crime(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE)) {
            mTitle = json.getString(JSON_TITLE);
        }
        mSaveFlag = json.getBoolean(JSON_SAVE);
        mSolved = json.getBoolean(JSON_SOLVED);
        Date date = new Date(json.getLong(JSON_DATE));
        mCal = new GregorianCalendar();
        mCal.setTime(date);


    }

    public static String dateString(Calendar d) {

        if (d != null) {
            int year = d.get(Calendar.YEAR);
            int month = d.get(Calendar.MONTH);
            int day = d.get(Calendar.DAY_OF_MONTH);

            int h = d.get(Calendar.HOUR);
            int m = d.get(Calendar.MINUTE);
            int aorp = d.get(Calendar.AM_PM);

            String[] f = {"Jan", "Feb", "Mar", "Apt", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            String[] ap = {"AM", "PM"};


            String out = "" + day + " " + f[month] + " " + year + "  -  " + h + ":" + m + " " + ap[aorp] + "";


            return out;
        }
        return "null";
    }

    public boolean isSaveFlag() {
        return mSaveFlag;
    }

    public void setSaveFlag(boolean saveFlag) {
        mSaveFlag = saveFlag;
    }

    public Calendar getCal() {
        return mCal;
    }

    public void setCal(Calendar cal) {
        mCal = cal;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_SAVE, mSaveFlag);

        json.put(JSON_DATE, mCal.getTime().getTime());

        return json;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
