package me.sql.asyncjson5;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** CLASS TO HANDLE SQLite PROCESS */
public class HelperSQL {
	
	public static final String DATABASE_NAME = "mnotasql.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String FAKULTI_TABLE = "fakulti";
	public static final String FAKULTI_ID = "fakulti_id";
	public static final String FAKULTI_NAME = "fakulti_name";
	
	public static final String PROGRAM_TABLE = "program";
	public static final String PROGRAM_ID = "program_id";
	public static final String PROGRAM_NAME = "program_name";
	
	public static final String SUBJEK_TABLE = "subjek";
	public static final String SUBJEK_ID = "subjek_id";
	public static final String SUBJEK_NAME = "subjek_name";
	
	private DbHelper ourDbHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private class DbHelper extends SQLiteOpenHelper{
		
		//database init
		public DbHelper(Context dbContext) {
			super(dbContext, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "+FAKULTI_TABLE+" ("
					+FAKULTI_ID+" TEXT PRIMARY KEY, "
					+FAKULTI_NAME+" TEXT NOT NULL"
					+");");
			
			db.execSQL("CREATE TABLE "+PROGRAM_TABLE+" ("
					+PROGRAM_ID+" TEXT PRIMARY KEY, "
					+PROGRAM_NAME+" TEXT NOT NULL, "
					+FAKULTI_ID+" TEXT NOT NULL, FOREIGN KEY ("+FAKULTI_ID+") REFERENCES "+FAKULTI_TABLE+" ("+FAKULTI_ID+")"
					+");");
			
			db.execSQL("CREATE TABLE "+SUBJEK_TABLE+" ("
					+SUBJEK_ID+" TEXT PRIMARY KEY, "
					+SUBJEK_NAME+" TEXT NOT NULL, "
					+PROGRAM_ID+" TEXT NOT NULL, FOREIGN KEY ("+PROGRAM_ID+") REFERENCES "+PROGRAM_TABLE+" ("+PROGRAM_ID+")"
					+");");
			
			db.execSQL("INSERT INTO '"+FAKULTI_TABLE+"'"
					+" SELECT 'FEP' AS '"+FAKULTI_ID+"', 'FAKULTI EKONOMI DAN PENGURUSAN' AS '"+FAKULTI_NAME+"'"
					+" UNION SELECT 'FFARMASI', 'FAKULTI FARMASI'"
					+" UNION SELECT 'FKAB', 'FAKULTI KEJURUTERAAN DAN ALAM BINA'"
					+" UNION SELECT 'FPEN', 'FAKULTI PENDIDIKAN'"
					+" UNION SELECT 'FPI', 'FAKULTI PENGAJIAN ISLAM'"
					+" UNION SELECT 'FPERGIGIAN', 'FAKULTI PERGIGIAN'"
					+" UNION SELECT 'FPERUBATAN', 'FAKULTI PERUBATAN'"
					+" UNION SELECT 'FST', 'FAKULTI SAINS DAN TEKNOLOGI'"
					+" UNION SELECT 'FSK', 'FAKULTI SAINS KESIHATAN'"
					+" UNION SELECT 'FSSK', 'FAKULTI SAINS SOSIAL DAN KEMANUSIAAN'"
					+" UNION SELECT 'FTSM', 'FAKULTI TEKNOLOGI DAN SAINS MAKLUMAT'"
					+" UNION SELECT 'FUU', 'FAKULTI UNDANG-UNDANG'"
					+" UNION SELECT 'PPS', 'PUSAT PENGAJIAN SISWAZAH PERNIAGAAN';");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			db.execSQL("DROP TABLE IF EXIST "+FAKULTI_TABLE);
			db.execSQL("DROP TABLE IF EXIST "+PROGRAM_TABLE);
			db.execSQL("DROP TABLE IF EXIST "+SUBJEK_TABLE);
			onCreate(db);
			
		}
		
	}
	
	//sqlhelper init
	public HelperSQL (Context c){
		ourContext = c;
	}
	
	//open database
	public HelperSQL open() throws SQLException{
		ourDbHelper = new DbHelper(ourContext);
		ourDatabase = ourDbHelper.getWritableDatabase();
		return this;
	}
	
	//close database
	public void close(){
		ourDbHelper.close();
	}

	//create entry for fakulti
	public void createFakultiEntry(String id, String name) throws SQLException {
		
		ourDatabase.execSQL("INSERT OR IGNORE INTO "+FAKULTI_TABLE
				+" ("+FAKULTI_ID+", "+FAKULTI_NAME+") "
				+"VALUES ('"+id+"', '"+name+"');");
				
	}
	
	//create entry for program/subject
	public long createEntry(String tableName, String id, String name, String parentTable, String parentID) throws SQLException {
		
		ContentValues cv= new ContentValues();
		cv.put(tableName+"_id", id);
		cv.put(tableName+"_name", name);
		cv.put(parentTable+"_id", parentID);
		
		return ourDatabase.insertOrThrow(tableName, null, cv);
		
	}
	
	//check if ID exist
	public boolean checkID(String tableName, String id) throws SQLException{
		
		String[] columns = new String[]{ "*" };
		Cursor c = ourDatabase.query(tableName, columns, tableName+"_id=\""+id+"\"", null, null, null, null);
		
		if(c != null){
			return true;
		}
		
		return false;
		
	}
	
	//return ALL name list of the requested table
	public ArrayList<String> getListName(String RECEIVED_TABLE_NAME) throws SQLException{

		ArrayList<String> TABLE_LIST = new ArrayList<String>();
		String[] columns = new String[]{ "*" };
		String result;

		Cursor c = ourDatabase.query(RECEIVED_TABLE_NAME, columns, null, null, null, null, null);
		int i_name =  c.getColumnIndex(RECEIVED_TABLE_NAME+"_name");

		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			result = c.getString(i_name);
			TABLE_LIST.add(result);
		}

		return TABLE_LIST;

	}

}