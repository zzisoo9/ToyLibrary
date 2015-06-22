package com.zzisoo.toylibrary;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class PreferencesUtility {
	public static void set(Context context, String name, String key, boolean value)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static void set(Context context, String name, String key, int value)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static void set(Context context, String name, String key, String value)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void set(Context context, String name, String key, Set<String> value)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putStringSet(key, value);
		editor.commit();
	}
	
	public static boolean getBoolean(Context context, String name, String key)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return pref.getBoolean(key, false);
	}
	
	public static int getInt(Context context, String name, String key)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return pref.getInt(key, -1);
	}
	
	public static String getString(Context context, String name, String key)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return pref.getString(key, "");
	}
	
	public static Set<String> getStringSet (Context context, String name, String key)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return pref.getStringSet(key, null);
	}
	
	public static boolean getBoolean(Context context, String name, String key, boolean bDefault)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return pref.getBoolean(key, bDefault);
	}
	
	public static int getInt(Context context, String name, String key, int nDefault)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return pref.getInt(key, nDefault);
	}
	
	public static String getString(Context context, String name, String key, String strDefault )
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return pref.getString(key, strDefault);
	}
	
	public static Set<String> getStringSet (Context context, String name, String key, Set<String> strsetDefault)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return pref.getStringSet(key, strsetDefault);
	}
	
	public static boolean hasValue(Context context, String name, String key)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return pref.contains(key);
	}
	
	public static void removeKey(Context context, String name, String key)
	{
		SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();
	}
}
