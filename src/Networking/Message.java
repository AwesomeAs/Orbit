package Networking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Message extends JSONObject {
	
	public Message(String method) {
		this.put("method", method);
	}
	
	public Message(String method, Object value) {
		this(method);
		this.put("value", value);
	}
	
	public JSONObject put(String key, Object value) {
		try {
			super.put(key, value);
		} catch (JSONException e) {}
		return this;
	}
	
	public JSONObject put(String key, boolean value) {
		try {
			super.put(key, value);
		} catch (JSONException e) {}
		return this;
	}
	
	public JSONObject put(String key, int value) {
		try {
			super.put(key, value);
		} catch (JSONException e) {}
		return this;
	}
	
	public JSONObject put(String key, float value) {
		try {
			super.put(key, value);
		} catch (JSONException e) {}
		return this;
	}
	
	public JSONObject put(String key, double value) {
		try {
			super.put(key, value);
		} catch (JSONException e) {}
		return this;
	}
	
	public String getString(String key) {
		try {
			return super.getString(key);
		} catch (JSONException e) {}
		return null;
	}
	
	public Object get(String key) {
		try {
			return super.get(key);
		} catch (JSONException e) {}
		return null;
	}
	
	public boolean getBoolean(String key) {
		try {
			return super.getBoolean(key);
		} catch (JSONException e) {}
		return false;
	}
	
	public int getInt(String key) {
		try {
			return super.getInt(key);
		} catch (JSONException e) {}
		return 0;
	}
	
	public double getDouble(String key) {
		try {
			return super.getDouble(key);
		} catch (JSONException e) {}
		return 0;
	}
	
	public JSONArray getJSONArray(String key) {
		try {
			return super.getJSONArray(key);
		} catch (JSONException e) {}
		return null;
	}
	
	public JSONObject getJSONObject(String key) {
		try {
			return super.getJSONObject(key);
		} catch (JSONException e) {}
		return null;
	}
	
}
