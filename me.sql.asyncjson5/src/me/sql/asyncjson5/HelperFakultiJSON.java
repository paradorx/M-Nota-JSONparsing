package me.sql.asyncjson5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

public class HelperFakultiJSON extends AsyncTask<String, String, String>{
		
	// JSON Node names
	private static final String TAG_FAKULTI = "fakultis";
	private static final String TAG_ID = "fakulti_id";
	private static final String TAG_NAME = "fakulti_name";
	
	// contacts JSONArray
	JSONArray fakulti = null;
	
	//receive context from caller
	private final Context receivedContext;
	
	public static final String FAKULTI_TABLE = "fakulti";
	private static final String TABLE_NAME="fakulti";
	
	@Override
	protected String doInBackground(String... urls) {
		
		for (String url : urls) {
			
			// Creating JSON Parser instance
			HelperHTTP jParser = new HelperHTTP();
			
			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(url);
			
			try {
				
				// Getting Array of Contacts
				fakulti = json.getJSONArray(TAG_FAKULTI);
				
				// looping through All Contacts
				for(int i = 0; i < fakulti.length(); i++){
					
					//get object in the array
					JSONObject c = fakulti.getJSONObject(i);
					
					// Storing each json item in variable
					String id = c.getString(TAG_ID);
					String name = c.getString(TAG_NAME);
					
					//create SQLite entry
					HelperSQL info = new HelperSQL(receivedContext);
			        info.open();
			        if(info.checkID(TABLE_NAME, id)){
			        	info.createFakultiEntry(id, name);
			        	publishProgress(id);
			        }
			        info.close();
					
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("JSONException Error", "JSONException Error: " + e.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				Log.e("SQLException Error", "SQLException Error: " + e.toString());
			}
			
		}
		
		return "no change";
		
	}
	
	@Override
	protected void onProgressUpdate(String... progress) {
	}
	
	@Override
	protected void onPostExecute(String accepted){
		//uses mainActivity class method to refresh the table
		((mainActivity)receivedContext).getTable();
	}
	
	//HelperJSON init
	public HelperFakultiJSON (Context c){
		
		receivedContext = c;
		
	}
	
}
