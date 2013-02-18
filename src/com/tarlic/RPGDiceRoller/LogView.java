package com.tarlic.RPGDiceRoller;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class LogView extends ListActivity {
	
	/** Called when the activity is first created. */	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the View layer
		setContentView(R.layout.list_view);		
		
		/*
		 * A TreeMap is passed to this Activity, but the app crashes
		 * when a TreeMap is get using the next line
		 */
		
		//TreeMap<Date, String> log = (TreeMap<Date, String>) getIntent().getSerializableExtra("log");		
		
		/*
		 * For this reason, the solution was to get a Map and to order again the Map.
		*/
		
		Map<Date, String> log = null;
		
		try{
		
			log = (Map<Date, String>) (this.getIntent().getSerializableExtra("log"));
		
		} catch(ClassCastException e) {Log.i("Error in LogView class: " , e.toString());}  
		
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		
		// Order the Map (again...), creating the TreeMap that it was filled in RPGDiceRoller.java
		
		TreeMap<Date, String> orderedLog = new TreeMap<Date, String>(new DateComparator());
			for(Map.Entry<Date, String> entry : log.entrySet()) {
			orderedLog.put(entry.getKey(), entry.getValue());
		}
		
		for(TreeMap.Entry<Date, String> entry : orderedLog.entrySet()) {
			
			HashMap<String,String> temp = new HashMap<String,String>();
			
			  Date key = entry.getKey();
			  String value = entry.getValue();
  
			  temp.put("field", DateFormat.getDateTimeInstance().format(key));
		        temp.put("value", value);
		        list.add(temp);
			}
		
        showList(list);
		
	}
	
    private void showList(ArrayList<HashMap<String,String>> list) {
    	
   	 ListAdapter adapter = new SimpleAdapter(
                this, // Context                 
                list,                                            
                R.layout.row_view,  // Specify the row template to use
                new String[] { "field", "value" },           
                new int[] {R.id.field, R.id.value});  // Parallel array of which template objects to bind to those columns.
        
        setListAdapter(adapter);
   }
	
};
