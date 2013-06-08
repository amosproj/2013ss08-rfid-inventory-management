/*
 * Copyright (c) 2013 by 
 * AMOS 2013 Group 8: RFID Inventory Management (Elektrobit)
 *
 * POs:
 *  Andreas Lutz
 *  Jana Riechert
 *  Kerstin Stern
 * 
 * SDs:
 *  Andreas Singer
 *  Liping Wang
 *  David Lehmeier
 *
 * This file is part of the RFID Inventory Management application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.amos2013.rfid_inventory_management_app;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button saveButton = (Button) findViewById(R.id.buttonSave);
		
		// set up spinners
		final Spinner spinnerLocation = (Spinner) findViewById(R.id.spinnerLocation);
		final Spinner spinnerRoom = (Spinner) findViewById(R.id.spinnerRoom);
		final Spinner spinnerEmployee = (Spinner) findViewById(R.id.spinnerEmployee);
		
		String[] locationList = new String[] { "Please select", "Tennenlohe (DE)", "Betholl (US)"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locationList);
	    spinnerLocation.setAdapter(adapter);
	    spinnerLocation.setEnabled(false);
		spinnerLocation.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View parent, int position, long id)
			{
				// if "Please select" is selected, don't enable the others
				if (position == 0)
				{
					spinnerRoom.setEnabled(false);
					spinnerEmployee.setEnabled(false);
				}
				else
				{
					spinnerRoom.setEnabled(true);
					spinnerEmployee.setEnabled(true);
					
					//fill room dropdown menu choices
					String selectedLocation = (String) spinnerLocation.getItemAtPosition(position);
					
					List<String> roomChoicesList = null;
					TextView textViewStatus = (TextView) findViewById(R.id.textViewStatus);
					try
					{
						roomChoicesList = RoomDatabaseHandler.getRecordsFromDatabaseByLocation(selectedLocation);
					} 
					catch (Exception e)
					{
						textViewStatus.setText(e.getMessage());
					}
					
					roomChoicesList.add(0, "Please select");
					spinnerRoom.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, roomChoicesList));
					
					// fill employee drop down choices
					List<String> employeeChoicesList = null;
					try
					{
						employeeChoicesList = EmployeeDatabaseHandler.getRecordsFromDatabaseByLocation(selectedLocation);
					} 
					catch (SQLException e)
					{
						textViewStatus.setText(e.getMessage());
					}
					
					employeeChoicesList.add("please select");
					spinnerEmployee.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, employeeChoicesList));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				
			}
		});	
		
		// if room and employee spinners have a selected item, enable the save button
		spinnerRoom.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
			{
				if (position != 0 && spinnerEmployee.getSelectedItemId() != 0)
				{
					saveButton.setEnabled(true);
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				
			}
		});
		
		spinnerEmployee.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
			{
				if (position != 0 && spinnerRoom.getSelectedItemId() != 0)
				{
					saveButton.setEnabled(true);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				
			}
		});
		
		// set up listview, where the scanned items are listed
		final ListView listViewScannedTags = (ListView) findViewById(R.id.listViewScannedTags);
		listViewScannedTags.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
			{
				LinearLayout inputLayout = (LinearLayout) findViewById(R.id.linearLayoutInput);
				inputLayout.setVisibility(LinearLayout.VISIBLE);
				
				Spinner locationSpinner = (Spinner) findViewById(R.id.spinnerLocation);
				locationSpinner.setEnabled(true);
			}
		});
		
		// show a message, when a tag id is long pressed
		listViewScannedTags.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id)
			{	
				Toast infoToast = Toast.makeText(getApplicationContext(), "This is record no. " + position, Toast.LENGTH_SHORT);
				infoToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				infoToast.show();
				
				// TODO read out infos to these ids
				return false;
			}
		});
	}
	
	/**
	 * Start stop button click.
	 * Changes the button text from start to stop and the other way around.
	 * Tells the reader to scan.
	 *
	 * @param view the view
	 */
	public void startStopButtonClick(View view)
	{
		Button startStopButton = (Button) findViewById(R.id.buttonStartStopScanning);
		
		ListView listViewScannedTags = (ListView) findViewById(R.id.listViewScannedTags);
		TextView textViewScannedTags = (TextView) findViewById(R.id.textViewScannedTags);

		// change text on click and show scan results
		if (startStopButton.getText().equals("Start scanning"))
		{
			startStopButton.setText("Stop scanning");
			
			textViewScannedTags.setVisibility(TextView.VISIBLE);
			listViewScannedTags.setVisibility(ListView.VISIBLE);
			listViewScannedTags.setEnabled(false);
		}
		else 
		{	
			startStopButton.setText("Start scanning");
			
			listViewScannedTags.setEnabled(true);
		}
		
		// TODO tell reader to scan
		// something like:
		// List<String> scannedTagsList = Reader.scan();
		
		// display results in the listview
		List<String> scannedTagsList = Arrays.asList("000120304000056", "012000040450054" );
		ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.scanned_tags_list_element, scannedTagsList);
		listViewScannedTags.setAdapter(adapter);
	}
	
	/**
	 * This handles the save button click.
	 * It will write the results to the Database
	 *
	 * @param view the view
	 */
	public void saveButtonClick(View view)
	{
		ListView listViewScannedTags = (ListView) findViewById(R.id.listViewScannedTags);
		Spinner spinnerRoom = (Spinner) findViewById(R.id.spinnerRoom);
		Spinner spinnerEmployee = (Spinner) findViewById(R.id.spinnerEmployee);
		
		SparseBooleanArray checked = listViewScannedTags.getCheckedItemPositions();
		
		for (int i = 0; i < checked.size(); ++i)
		{
			if (checked.get(i) == true)
			{
				String rfidId =  (String) listViewScannedTags.getItemAtPosition(i);
				String selectedRoom = (String) spinnerRoom.getSelectedItem();
				String selectedEmployee = (String) spinnerEmployee.getSelectedItem();
				// TODO save to database
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
