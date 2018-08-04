package com.tarlic.RPGDiceRoller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.tarlic.RPGDiceRoller.LogItem;
import com.tarlic.RPGDiceRoller.LogListAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class LogView extends AppCompatActivity {
	
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
		
		try {
		
			log = (Map<Date, String>) (this.getIntent().getSerializableExtra("log"));
		
		} catch(ClassCastException e) {Log.i("Error in LogView class: " , e.toString());}  
		
				
        showList(log);
        
        enableActionBar();        
	}
	
    private void enableActionBar() {
    	/*
    	 * To enable the app icon as an Up button, call setDisplayHomeAsUpEnabled().
    	 */
    	ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
	}

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                        // Add all of this activity's parents to the back stack
                        .addNextIntentWithParentStack(upIntent)
                        // Navigate up to the closest parent
                        .startActivities();
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                NavUtils.navigateUpTo(this, upIntent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
	private void showList(Map<Date, String> log) {
    	
    	/*
		 *  Order the Map (again...), creating the TreeMap that it was filled in RPGDiceRoller.java
		 */
		
		TreeMap<Date, String> orderedLog = new TreeMap<Date, String>(new DateComparator());
			for(Map.Entry<Date, String> entry : log.entrySet()) {
			orderedLog.put(entry.getKey(), entry.getValue());
		}
		
		ArrayList<LogItem> logItems = new ArrayList<LogItem>();
			
		for(TreeMap.Entry<Date, String> entry : orderedLog.entrySet()) {
			
			LogItem li = new LogItem();			
			li.setText(entry.getValue());			
			li.setDate(entry.getKey());
			
			logItems.add(li);
			
		}
    	
    	ListView lv = (ListView) findViewById(R.id.itemlist);
    	
    	lv.setAdapter(new LogListAdapter(this, logItems));
   }
	
};
