package com.zzisoo.toylibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SharedPref {

    public static final String PREF_PRE = "com.zzisoo.toylibrary.";
    public static final String PREF_TOYS_LIST = "com.zzisoo.toylibrary.toys.list";

    public static final int NODATA_INT = -1;
    public static final long NODATA_LONG = -1l;
    public static final String NODATA_STRING = "";

    private SharedPreferences m_Pref = null;
    private Context m_Context = null;
    private SharedPreferences.Editor m_Editor = null;

    public SharedPref(Context a_Context) {
        m_Context = a_Context;

        if (Build.VERSION.SDK_INT <= 10) {
            m_Pref = PreferenceManager.getDefaultSharedPreferences(m_Context.getApplicationContext());
        } else {
            m_Pref = m_Context.getSharedPreferences(getDefaultSharedPreferencesName(m_Context), Context.MODE_MULTI_PROCESS);
        }

    }

    String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    public void setUpdateMode() {
        if (m_Editor != null) {
            m_Editor.clear();
            m_Editor = null;
        }

        m_Editor = m_Pref.edit();
    }

    public void updateFinish() {
        m_Editor.apply();
    }

    public void deletePreferences() {
        setUpdateMode();
        m_Editor.clear();
        updateFinish();
    }

    public String makeName(String strName) {
        return PREF_PRE + strName;
    }

    // Boolean
    public void setBooleanPref(String strName, boolean bValue) {
        m_Editor.putBoolean(strName, bValue);
    }

    public boolean getBooleanPref(String strName, boolean bDefault) {
        return m_Pref.getBoolean(strName, bDefault);
    }

    // String
    public void setStringPref(String strName, String strValue) {
        m_Editor.putString(strName, strValue);
    }

    public String getStringPref(String strName) {
        return m_Pref.getString(strName, NODATA_STRING);
    }

    public String getStringPref(String strName, String strValue) {
        return m_Pref.getString(strName, strValue);
    }

    // int
    public void setIntPref(String strName, int nValue) {
        m_Editor.putInt(strName, nValue);
    }

    public int getIntPref(String strName) {
        return m_Pref.getInt(strName, NODATA_INT);
    }

    public int getIntPref(String strName, int nDefault) {
        return m_Pref.getInt(strName, nDefault);
    }

    // long
    public void setLongPref(String strName, long nValue) {
        m_Editor.putLong(strName, nValue);
    }

    public long getLongPref(String strName) {
        return m_Pref.getLong(strName, NODATA_LONG);
    }

    public long getLongPref(String strName, long nDefault) {
        return m_Pref.getLong(strName, nDefault);
    }

    public ArrayList<String> getStringArrayList(String strPrefName) {

        JSONArray joinJsonArray;
        String strJoinPkgList = getStringPref(strPrefName, new JSONArray().toString());

        ArrayList<String> joinList = new ArrayList<String>();
        try {
            joinJsonArray = new JSONArray(strJoinPkgList);
            int num = joinJsonArray.length();
            for (int index = 0; index < num; index++) {
                joinList.add(joinJsonArray.get(index).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("SharedPref", "====================================================================");
        Log.e("SharedPref", strPrefName + "===" + "now : " + joinList.size());
        for (int index = 0; index < joinList.size(); index++) {
            Log.e("Test", index + " : " + joinList.get(index));
        }
        return joinList;
    }

    public ArrayList<String> addStringArrayListItem(String strPrefName, String addItem) {

        ArrayList<String> orgArrayList = getStringArrayList(strPrefName);

        Log.e("SharedPref", "====================================================================");
        Log.e("SharedPref", strPrefName + "<<< before : " + orgArrayList.size());
        for (int index = 0; index < orgArrayList.size(); index++) {
            Log.e("Test", index + " : " + orgArrayList.get(index));
        }

        if (orgArrayList.contains(addItem)) {
            Log.e(SharedPref.class.getSimpleName(), "Already Added Item :" + addItem);

            Log.e("SharedPref", "--------------------------------------------------------------------");
            Log.e("SharedPref", strPrefName + ">>> after : " + orgArrayList.size());
            for (int index = 0; index < orgArrayList.size(); index++) {
                Log.e("Test", index + " : " + orgArrayList.get(index));
            }
            Log.e("SharedPref", "====================================================================");

            return orgArrayList;
        } else {
            orgArrayList.add(addItem);

            JSONArray result = new JSONArray();
            for (int i = 0; i < orgArrayList.size(); i++) {
                result.put(orgArrayList.get(i));
            }
            setUpdateMode();
            m_Editor.putString(strPrefName, result.toString());
            Log.e(SharedPref.class.getSimpleName(), "Added Item :" + addItem);
            updateFinish();

            Log.e("SharedPref", "--------------------------------------------------------------------");
            Log.e("SharedPref", strPrefName + ">>> after : " + orgArrayList.size());
            for (int index = 0; index < orgArrayList.size(); index++) {
                Log.e("Test", index + " : " + orgArrayList.get(index));
            }
            Log.e("SharedPref", "====================================================================");

            return orgArrayList;
        }

    }

    public ArrayList<String> removeStringArrayListItem(String strPrefName, String delItem) {

        ArrayList<String> orgArrayList = getStringArrayList(strPrefName);

        Log.e("SharedPref", "=======input array=============================================================");
        Log.e("SharedPref", strPrefName + "<<< before : " + orgArrayList.size());
        for (int index = 0; index < orgArrayList.size(); index++) {
            Log.e("Test", index + " : " + orgArrayList.get(index));
        }

        if (!orgArrayList.contains(delItem)) {
            Log.e(SharedPref.class.getSimpleName(), "NOT Found Item :" + delItem);

            Log.e("SharedPref", "--------------------------------------------------------------------");
            Log.e("SharedPref", strPrefName + ">>> after : " + orgArrayList.size());
            for (int index = 0; index < orgArrayList.size(); index++) {
                Log.e("Test", index + " : " + orgArrayList.get(index));
            }
            Log.e("SharedPref", "====================================================================");

            return orgArrayList;
        } else {
            Log.e(SharedPref.class.getSimpleName(), "Found Item :" + delItem);
            orgArrayList.add(delItem);

            JSONArray result = new JSONArray();
            for (int i = 0; i < orgArrayList.size(); i++) {
                if (orgArrayList.get(i).equals(delItem)) {
                    Log.e("Test", i + " : >>>delete(skip add) " + orgArrayList.get(i));
                    orgArrayList.remove(i);
                    i = i - 1;
                } else {
                    Log.e("Test", i + " : " + orgArrayList.get(i));
                    result.put(orgArrayList.get(i));
                }
            }
            setUpdateMode();
            m_Editor.putString(strPrefName, result.toString());
            Log.e(SharedPref.class.getSimpleName(), "Delete Item :" + delItem);
            updateFinish();

            Log.e("SharedPref", "---output array-----------------------------------------------------------------");
            Log.e("SharedPref", strPrefName + ">>> after : " + orgArrayList.size());
            for (int index = 0; index < orgArrayList.size(); index++) {
                Log.e("Test", index + " : " + orgArrayList.get(index));
            }
            Log.e("SharedPref", "====================================================================");

            return orgArrayList;
        }

    }

}
