package me.json.parsing3;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import android.app.ListActivity;
import android.os.Bundle;

public class AndroidJSONParsingActivity extends ListActivity {
	
	// url to make request
	private static String url = "http://mnota.comuv.com/JSONtest.php";
	
	// JSON Node names
	private static final String TAG_FAKULTI = "fakultis";
	private static final String TAG_ID = "fakulti_id";
	private static final String TAG_NAME = "fakulti_name";
	
	// contacts JSONArray
	JSONArray fakulti = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		// Hashmap for ListView
		ArrayList<HashMap<String, String>> fakultiList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);
		
		try {
			
			// Getting Array of Contacts
			fakulti = json.getJSONArray(TAG_FAKULTI);
			
			// looping through All Contacts
			for(int i = 0; i < fakulti.length(); i++){
				JSONObject c = fakulti.getJSONObject(i);
				
				// Storing each json item in variable
				String id = c.getString(TAG_ID);
				String name = c.getString(TAG_NAME);
				
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				
				// adding each child node to HashMap key => value
				map.put(TAG_ID, id);
				map.put(TAG_NAME, name);

				// adding HashList to ArrayList
				fakultiList.add(map);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		/**
		 * Updating parsed JSON data into ListView
		 * */
		
		ListAdapter adapter = new SimpleAdapter(this, fakultiList,
				R.layout.list_item,
				new String[] { TAG_NAME, TAG_ID }, new int[] {
						R.id.name, R.id.fid });

		setListAdapter(adapter);
		
    }
}