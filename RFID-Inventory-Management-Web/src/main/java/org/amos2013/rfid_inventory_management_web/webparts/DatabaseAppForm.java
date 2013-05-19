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

package org.amos2013.rfid_inventory_management_web.webparts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseRecord;
import org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.RoomDatabaseRecord;
import org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseRecord;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Form that is displayed on the website. Used for writing data to the database
 */
public class DatabaseAppForm extends Form<Object>
{
	private static final long serialVersionUID = 2948880218956382827L;

	private Integer rfid_id; // use Integer instead of int, so the default value is null and not 0. so nothing will be displayed
	private String room;
	private String owner;
	private String statusMessage;
	
	private List<String> options_room = Arrays.asList(new String[] {"please select" });
	private List<String> options_owner = Arrays.asList(new String[] {"please select" });
	private String selected_room = "please select";
	private String selected_owner = "please select";


	/**
	 * Creates a Form Object to submit updates to the database.
	 * @param id the name of this form, to use it in html
	 */
	public DatabaseAppForm(String id)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this));	// sets the model to bind to the wicket ids
		
		// get all room numbers from room database records as a list of strings
		List<String> roomDatabaseRecords = null;
		try
		{
			roomDatabaseRecords = RoomDatabaseHandler.getRecordsFromDatabaseByLocation("Tennenlohe (DE)");
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//merge both lists
		List<String> filled_options_room = new ArrayList<String>();
		filled_options_room.addAll(options_room);
		filled_options_room.addAll(roomDatabaseRecords);
	
		
		// get all employee names from employee database records as a list of strings
		List<String> employeeDatabaseRecords = null;
		try
		{
			employeeDatabaseRecords = EmployeeDatabaseHandler.getRecordsFromDatabaseByLocation("Tennenlohe (DE)");
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		// merge both lists
		List<String> filled_options_owner = new ArrayList<String>();
		filled_options_owner.addAll(options_owner);
		filled_options_owner.addAll(employeeDatabaseRecords);
				
		// add all forms
		add(new NumberTextField<Integer>("rfid_id"));
		add(new DropDownChoice<String>("room", new PropertyModel<String>(this, "selected_room"), filled_options_room));
		add(new DropDownChoice<String>("owner", new PropertyModel<String>(this, "selected_owner"), filled_options_owner));
		add(new Label("statusMessage"));
	}

	/**
	 * This method is called, when the submit button on the homepage it is clicked.
	 * It will write the entered record into the database
	 */
	public final void onSubmit()
	{
		// catch invalid input first
		if (rfid_id == null)
		{
			statusMessage = "Please enter an RFID Id into the field.";
			return;
		}
		
		if (selected_room == null || selected_owner == null || selected_room.isEmpty() || selected_owner.isEmpty() || selected_room == "please select" || selected_owner == "please select")
		{
			statusMessage = "Please enter a room and an owner.";
			return;
		}
		
		// write to the database
		try
		{
			DeviceDatabaseHandler.writeRecordToDatabase(rfid_id, selected_room, selected_owner);
			statusMessage = "Data saved";
		}
		catch (IllegalArgumentException e)
		{
			
			statusMessage = e.getMessage();
		}
		catch (Exception e)
		{
			statusMessage = "An error occured!";
			e.printStackTrace();
		}
	}
}
