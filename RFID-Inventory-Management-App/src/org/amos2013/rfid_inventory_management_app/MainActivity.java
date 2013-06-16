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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mti.rfid.minime.CMD_AntPortOp;
import com.mti.rfid.minime.CMD_FwAccess;
import com.mti.rfid.minime.CMD_Iso18k6cTagAccess;
import com.mti.rfid.minime.CMD_PwrMgt;
import com.mti.rfid.minime.MtiCmd;
import com.mti.rfid.minime.UsbCommunication;

/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity
{
	public static ArrayList<String> scannedTagsList = new ArrayList<String>();
	private MtiCmd mMtiCmd;
	private UsbCommunication mUsbCommunication = UsbCommunication.newInstance();
	private ArrayAdapter<String> scannedTagsAdapter;
	private UsbManager mManager;
	private PendingIntent mPermissionIntent;
	
	/** The Constant ACTION_USB_PERMISSION. */
	private static final String ACTION_USB_PERMISSION = "org.amos2013.rfid_inventory_management_app.USB_PERMISSION";
	
	/** The Constant PID. */
	private static final int PID = 49193;
	
	/** The Constant VID. */
	private static final int VID = 4901;
	
	private TextView textViewStatus;
	
	/** The usb receiver. */
	BroadcastReceiver usbReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			Toast.makeText(context, "Broadcast Receiver", Toast.LENGTH_SHORT).show();
			
			if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) 
			{					
				// will intercept by system
				Toast.makeText(context, "USB Attached", Toast.LENGTH_SHORT).show();
				UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				mUsbCommunication.setUsbInterface(mManager, device);
				setUsbState(true);
			} 
			else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) 
			{
				Toast.makeText(context, "USB Detached", Toast.LENGTH_SHORT).show();
				mUsbCommunication.setUsbInterface(null, null);
				setUsbState(false);
			} 
			else if (ACTION_USB_PERMISSION.equals(action)) 
			{
				synchronized(this) 
				{
					UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					
					Toast.makeText(context, "USB Permission", Toast.LENGTH_SHORT).show();
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) 
					{
						Toast.makeText(context, "USB Permission", Toast.LENGTH_SHORT).show();
							
						mUsbCommunication.setUsbInterface(mManager, device);
						setUsbState(true);
						sleep(400);
//						if(bSavedInst)
//							getReaderSn(true);
						setPowerLevel();
						setPowerState();
					} 	
					else 
					{
						finish();
					}
				}
			}
		}
	};
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        mManager = (UsbManager)getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        
        // register usb receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);					// will intercept by system
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, filter);
		
		final Button saveButton = (Button) findViewById(R.id.buttonSave);
		
		textViewStatus = (TextView) findViewById(R.id.textViewStatus);
		
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
					
					employeeChoicesList.add(0, "Please select");
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
		
		scannedTagsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.scanned_tags_list_element, scannedTagsList);
		listViewScannedTags.setAdapter(scannedTagsAdapter);
		
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
		final Handler handler = new Handler();
		
	    final int scantimes = 25;
		
	    // if reader is connected -> scan
		if(getUsbState()) 
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
			
			// start the scanning
			new Thread() 
			{
				int numTags;
				String tagId;

	    		public void run() 
	    		{
			    	scannedTagsList.clear();
			    	for (int i = 0; i < scantimes; i++) 
			    	{
			    		mMtiCmd = new CMD_Iso18k6cTagAccess.RFID_18K6CTagInventory(mUsbCommunication);
						CMD_Iso18k6cTagAccess.RFID_18K6CTagInventory finalCmd = (CMD_Iso18k6cTagAccess.RFID_18K6CTagInventory) mMtiCmd;
						
						if(finalCmd.setCmd(CMD_Iso18k6cTagAccess.Action.StartInventory)) 
						{
							tagId = finalCmd.getTagId();
							if(finalCmd.getTagNumber() > 0) 
							{
								if(!scannedTagsList.contains(tagId))
								{
									scannedTagsList.add(tagId);
								}
							}
							
							for(numTags = finalCmd.getTagNumber(); numTags > 1; --numTags) 
							{
								if(finalCmd.setCmd(CMD_Iso18k6cTagAccess.Action.NextTag)) 
								{
									tagId = finalCmd.getTagId();
									if(!scannedTagsList.contains(tagId))
									{
										scannedTagsList.add(tagId);
									}
								}
							}

							Collections.sort(scannedTagsList);
							handler.post(updateResult);
						} 
						else 
						{
							// error
							textViewStatus.setText("Errors");
						}
			    	}
			    	
	    			setPowerState();
	    		}
	    		
	    		// refresh the list of the scanned tags -> show result
	    		final Runnable updateResult = new Runnable() 
	    		{
					@Override
					public void run() 
					{
						scannedTagsAdapter.notifyDataSetChanged();
					}
	    		};
			}.start();
		} 
		else
		{
			Toast.makeText(this, "The Reader is not connected", Toast.LENGTH_SHORT).show();
		}
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
				
				try
				{
					DeviceDatabaseHandler deviceDatabaseHandler = DeviceDatabaseHandler.getInstance();
					deviceDatabaseHandler.updateRecordFromAppInDatabase(Integer.parseInt(rfidId), selectedRoom, selectedEmployee);
					textViewStatus.setText("- data saved -");
				}
				catch (IllegalArgumentException e)
				{
					textViewStatus.setText(e.getMessage());
				}
				catch (Exception e)
				{
					textViewStatus.setText(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() 
	{
		super.onResume();
		
		HashMap<String, UsbDevice> deviceList = mManager.getDeviceList();
		Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
		while(deviceIterator.hasNext()) {
			UsbDevice device = deviceIterator.next();
			if (device.getProductId() == PID && device.getVendorId() == VID) 
			{
				if(mManager.hasPermission(device))
					mManager.requestPermission(device, mPermissionIntent);
				break;
			}
		}
	}

	/**
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() 
	{
		super.onPause();

		if(textViewStatus.getText().equals("Connected"))
			mUsbCommunication.setUsbInterface(null, null);
	}

	/**
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();

		unregisterReceiver(usbReceiver);
	}
	
	/**
	 * Gets the usb state.
	 *
	 * @return the usb state
	 */
	private boolean getUsbState() 
	{
        return textViewStatus.getText().equals("Connected");
	}
	
	/**
	 * Sets the usb state.
	 *
	 * @param state the new usb state
	 */
	private void setUsbState(boolean state) 
	{
        if (state) 
		{
        	textViewStatus.setText("Connected");
        	textViewStatus.setTextColor(android.graphics.Color.GREEN);
		}
		else 
		{
			textViewStatus.setText("Disconnected");
			textViewStatus.setTextColor(android.graphics.Color.RED);
		}
	}
	
	/**
	 * Sets the power state.
	 */
	private void setPowerState() 
	{
		MtiCmd mMtiCmd = new CMD_PwrMgt.RFID_PowerEnterPowerState(mUsbCommunication);
		CMD_PwrMgt.RFID_PowerEnterPowerState finalCmd = (CMD_PwrMgt.RFID_PowerEnterPowerState) mMtiCmd;
	}
	
	/**
	 * Sets the power level.
	 */
	private void setPowerLevel() 
	{
		MtiCmd mMtiCmd = new CMD_AntPortOp.RFID_AntennaPortSetPowerLevel(mUsbCommunication);
		CMD_AntPortOp.RFID_AntennaPortSetPowerLevel finalCmd = (CMD_AntPortOp.RFID_AntennaPortSetPowerLevel) mMtiCmd;
		
		finalCmd.setCmd((byte)18);
	}

	/**
	 * Gets the reader sn.
	 *
	 * @param bState the b state
	 * @return the reader sn
	 */
	private void getReaderSn(boolean bState) 
	{
		MtiCmd mMtiCmd;

		if(bState) 
		{
			byte[] bSN = new byte[16];
	
		    for (int i = 0; i < 16; i++) 
		    {
				mMtiCmd = new CMD_FwAccess.RFID_MacReadOemData(mUsbCommunication);
				CMD_FwAccess.RFID_MacReadOemData finalCmd = (CMD_FwAccess.RFID_MacReadOemData) mMtiCmd;
				if(finalCmd.setCmd(i + 0x50))
				{
					bSN[i] = finalCmd.getData();
				}
			}
		} 

//		showFragment(Fragments.About, 0, null);
	}
	
	
	/**
	 * Sleep.
	 *
	 * @param millisecond the millisecond
	 */
	private void sleep(int millisecond) 
	{
		try
		{
			Thread.sleep(millisecond);
		} 
		catch (InterruptedException e) 
		{
			
		}
	}
}
