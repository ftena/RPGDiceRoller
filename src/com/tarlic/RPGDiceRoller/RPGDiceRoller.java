package com.tarlic.RPGDiceRoller;

import java.sql.Date;
import java.util.Random;
import java.util.TreeMap;

import com.tarlic.RPGDiceRoller.ShakeDetector.OnShakeListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.support.v7.app.ActionBarActivity;

public class RPGDiceRoller extends ActionBarActivity {

	static final String PLUS = "+";
	static final String MINUS = "-";
	
	static final String ZERO = "0";
	
	final TreeMap<Date, String> log = new TreeMap<Date, String>(new DateComparator());
	
	/*
	 *  The following are used for the shake detection.
	 */
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private ShakeDetector mShakeDetector;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);    	
    	setContentView(R.layout.main_view);
    	
    	/* Disable the Android OnScreen keyboard when the
    	 * activity is opened.
    	*/
    	
    	 getWindow().setSoftInputMode(
    			    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	
    	/*
    	 * Disable the input for some text fields    	 
    	 */
    	disableEditTextResults(R.id.EditTextResultsD2);
    	disableEditTextResults(R.id.EditTextResultsD3);
    	disableEditTextResults(R.id.EditTextResultsD4);
    	disableEditTextResults(R.id.EditTextResultsD6);
    	disableEditTextResults(R.id.EditTextResultsD8);
    	disableEditTextResults(R.id.EditTextResultsD10);
    	disableEditTextResults(R.id.EditTextResultsD100);
    	disableEditTextResults(R.id.EditTextResultsD12);
    	disableEditTextResults(R.id.EditTextResultsD20);
    	
		// ShakeDetector initialization
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mShakeDetector = new ShakeDetector();
		mShakeDetector.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake(int count) {				
				handleShakeEvent(count);
			}
		});		
    }
    
    public void disableEditTextResults(int idEditTextResults) {
    	EditText editTextResults = (EditText) findViewById(idEditTextResults);
    	
    	editTextResults.setKeyListener(null);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.triforce_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public void buttonPlusMinusClick(View view)  
    {  
    	Button b = (Button)view;
    	
    	if ( b.getText().toString().equals(PLUS)) { b.setText(R.string.minus_text); }
    	else if ( b.getText().toString().equals(MINUS)) { b.setText(R.string.plus_text); }    
    }  
        
    public void buttonRollClick (View view)
    {    	
    	roll(view.getId());
    }    
    
    private void processButtonRollClick(int idEditTextNumber, int idEditTextModifier,
    									int idEditTextResults, int dieType,
    									int idButtonPlusMinus) {
    	
    	Random r = new Random();
    	
    	Integer number = 0;
    	Integer modifier = 0;
    	Integer results = 0;
    	Integer resultsTmp = 0;
    	
    	EditText editTextNumber = (EditText) findViewById(idEditTextNumber);
		EditText editTextModifier = (EditText) findViewById(idEditTextModifier);
		EditText editTextResults = (EditText) findViewById(idEditTextResults);   		
		Button buttonPlusMinus = (Button) findViewById(idButtonPlusMinus);
			    		
		try {
			number = Integer.parseInt(editTextNumber.getText().toString());			
		} catch(NumberFormatException nfe) {
		   System.out.println("Could not parse " + nfe);		  
		} 
		
		try {
			modifier = Integer.parseInt(editTextModifier.getText().toString());			
		} catch(NumberFormatException nfe) {
		   System.out.println("Could not parse " + nfe);		  
		}
		
		// If both fields aren't zeros		
		if (number != 0 || modifier != 0)
		{
			for (Integer i = number; i > 0; i--)
			{
				resultsTmp = r.nextInt(dieType) + 1;
				results = resultsTmp + results;    			
			}
			
			// Check if the modifier is to add or subtract
			if (buttonPlusMinus.getText().toString().equals(PLUS))
				results = results + modifier;
			else if (buttonPlusMinus.getText().toString().equals(MINUS))
				results = results - modifier;
			else showMsg(buttonPlusMinus.getText().toString());
					
			editTextResults.setText(String.valueOf(results));
			
			String logString = null;
			
			// Begin the log and check if the button has plus or minus sign		
			if (buttonPlusMinus.getText().toString().equals(PLUS))
				logString = String.valueOf(number) + "d" + String.valueOf(dieType) + " + ";						
			else if (buttonPlusMinus.getText().toString().equals(MINUS))
				logString = String.valueOf(number) + "d" + String.valueOf(dieType) + " - ";		
			else showMsg(buttonPlusMinus.getText().toString());
			
			// if modifier is blank, it is necessary to add the 0 manually
			logString = logString + String.valueOf(modifier) + " = " + String.valueOf(results);
			
			// add logString to the TreeMap
			log.put(new Date(System.currentTimeMillis()), logString);
		} else { // Both fields are zeros
			editTextResults.setText(ZERO);
		}
   }
    
    public void buttonViewLogClick(View view) {			
    	startLogActivity();		
	}
       
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_log:
        	startLogActivity();
        	return true;
        case R.id.menu_roll:      
            rollDice();
            return true;
        case R.id.menu_help:      
            showHelp();
            return true;        
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void startLogActivity() {
    	try{  
    		
		    Intent intent = new Intent(this, LogView.class);
		    
		    intent.putExtra("log", log);
		    
		    startActivity(intent);
		
	    } catch(Exception e) {Log.i("Error in RPGDiceRoller class: " , e.toString());}
    }
    
    private void showHelp() {
       	
    	AlertDialog.Builder builder;
    	AlertDialog alertDialog;

    	// The next line makes the application crashes
    	//Context mContext = getApplicationContext();
    	LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
    	View layout = inflater.inflate(R.layout.dialog_help,
    	                               (ViewGroup) findViewById(R.id.dialog_help_layout_root));

    	TextView text = (TextView) layout.findViewById(R.id.TextHelpDialog);
    	text.setText(R.string.dialog_help);

    	builder = new AlertDialog.Builder(this);
    	builder.setView(layout)    	 
         .setCancelable(false)      
         .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                  dialog.cancel();
             }
         });
    	
    	alertDialog = builder.create();    	
    	
    	
    	alertDialog.show();

   }

    @SuppressWarnings("unused")
	@Deprecated
    private void showExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.dialog_exit)
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   RPGDiceRoller.this.finish();
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });


        AlertDialog alert = builder.create();

        alert.show();
   }

    private void handleShakeEvent(int count)
    {	
    	rollDice();
    }

    private void rollDice() {
    	// Roll all dice
    	roll(R.id.ButtonRollD2);
    	roll(R.id.ButtonRollD3);
    	roll(R.id.ButtonRollD4);
    	roll(R.id.ButtonRollD6);
    	roll(R.id.ButtonRollD8);
    	roll(R.id.ButtonRollD10);
    	roll(R.id.ButtonRollD100);
    	roll(R.id.ButtonRollD12);
    	roll(R.id.ButtonRollD20);
    }
    
    private void roll(int id) {
    	if ( id == R.id.ButtonRollD2 ) {	
    		processButtonRollClick(R.id.EditTextNumberD2, R.id.EditTextModifierD2,
    					R.id.EditTextResultsD2, Integer.parseInt(getString(R.string.d2text)),
    					R.id.ButtonPlusMinusD2);
    	} else if ( id == R.id.ButtonRollD3 )
    	{	
    		processButtonRollClick(R.id.EditTextNumberD3, R.id.EditTextModifierD3,
    					R.id.EditTextResultsD3, Integer.parseInt(getString(R.string.d3text)),
    					R.id.ButtonPlusMinusD3);    	
    	} else if ( id == R.id.ButtonRollD4 )
    	{	
    		processButtonRollClick(R.id.EditTextNumberD4, R.id.EditTextModifierD4,
    					R.id.EditTextResultsD4, Integer.parseInt(getString(R.string.d4text)),
    					R.id.ButtonPlusMinusD4);
    	} else if ( id == R.id.ButtonRollD6 )
    	{	
    		processButtonRollClick(R.id.EditTextNumberD6, R.id.EditTextModifierD6,
    					R.id.EditTextResultsD6, Integer.parseInt(getString(R.string.d6text)),
    					R.id.ButtonPlusMinusD6);
    	} else if ( id == R.id.ButtonRollD8 )
    	{	
    		processButtonRollClick(R.id.EditTextNumberD8, R.id.EditTextModifierD8,
    					R.id.EditTextResultsD8, Integer.parseInt(getString(R.string.d8text)),
    					R.id.ButtonPlusMinusD8);
    	} else if ( id == R.id.ButtonRollD10 )
    	{	
    		processButtonRollClick(R.id.EditTextNumberD10, R.id.EditTextModifierD10,
    					R.id.EditTextResultsD10, Integer.parseInt(getString(R.string.d10text)),
    					R.id.ButtonPlusMinusD10);
    	} else if ( id == R.id.ButtonRollD100 )
    	{	
    		processButtonRollClick(R.id.EditTextNumberD100, R.id.EditTextModifierD100,
    					R.id.EditTextResultsD100, Integer.parseInt(getString(R.string.d100text)),
    					R.id.ButtonPlusMinusD100);
    	} else if ( id == R.id.ButtonRollD12 )
    	{	
    		processButtonRollClick(R.id.EditTextNumberD12, R.id.EditTextModifierD12,
    					R.id.EditTextResultsD12, Integer.parseInt(getString(R.string.d12text)),
    					R.id.ButtonPlusMinusD12);
    	    	
    	} else if ( id == R.id.ButtonRollD20 )
    	{	
    		processButtonRollClick(R.id.EditTextNumberD20, R.id.EditTextModifierD20,
    					R.id.EditTextResultsD20, Integer.parseInt(getString(R.string.d20text)),
    					R.id.ButtonPlusMinusD20);
    	}
    	
	}
    
    private void showMsg(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg)
               .setCancelable(true)
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                           dialog.cancel();
                   }
               });

        AlertDialog alert = builder.create();

        alert.show();
   }
    
	@Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);        
    }
 
    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
   
    
};

