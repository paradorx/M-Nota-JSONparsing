package me.sql.asyncjson5;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class mainActivity extends ListActivity implements OnClickListener {
	
	/**
     * Declaration *
     ** * * * * * **/
	
	private static final String TABLE_NAME="fakulti";
	public static ArrayList<String> TABLE_LIST = new ArrayList<String>();
	private static ArrayAdapter<String> aAdapter;
	Handler handler = new Handler();
	
	// url to make request
	private static String[] urlLIST = {"http://mnota.comuv.com/JSONtest.php"};
	
	
    /**
     * Main Activity *
     ** * * * * * * **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	//set Activity View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		//set button listener
        findViewById(R.id.buttonUpdate).setOnClickListener(this);
        findViewById(R.id.buttonRefresh).setOnClickListener(this);
        
        getTable();
        
    }

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.buttonUpdate:
			if(isNetworkAvailable()){
				
				HelperFakultiJSON fJSON = new HelperFakultiJSON(this);
				fJSON.execute(urlLIST);
			    
			}else{
				Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_LONG).show();
			}
			break;
		
		case R.id.buttonRefresh:
			getTable();
			break;
		}
		
	}
	
	public void getTable(){
		
		try{
			
			HelperSQL data = new HelperSQL(this);
			data.open();
			TABLE_LIST = (data.getListName(TABLE_NAME));
			data.close();
			
		} catch (SQLException e){
			e.printStackTrace();
			Log.e("SQLException Error", "SQLException Error: " + e.toString());
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
		aAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, TABLE_LIST);
        aAdapter.setNotifyOnChange(true);
        setListAdapter(aAdapter);
		
	}
	
	public boolean isNetworkAvailable() {

    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
		    return true;
		}
		
		return false;
    	
    }
	
	Runnable r = new Runnable() { 
        public void run() { 
       	 getTable(); 
        }
	};
	
	
	
}