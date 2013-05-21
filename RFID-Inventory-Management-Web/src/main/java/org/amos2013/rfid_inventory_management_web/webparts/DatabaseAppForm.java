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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Form that is displayed on the app-website. Used for writing data to the database
 */
public class DatabaseAppForm extends Form<Object>
{
	private static final long serialVersionUID = 2948880218956382827L;

	private Integer rfid_id; // use Integer instead of int, so the default value is null and not 0. so nothing will be displayed
	private String statusMessage;
	
	private List<String> roomDropDownCoices = new ArrayList<String>();
	private List<String> ownerDropDownCoices = new ArrayList<String>();
	// TODO later on we should probably get the contents for the following string from the database
	private List<String> locationDropDownCoices = Arrays.asList(new String[] {"Tennenlohe (DE)", "Bothell (US)"});
	private String selected_location = "Tennenlohe (DE)";
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
				
		// add all forms
		add(new DropDownChoice<String>("locationDropdown", new PropertyModel<String>(this, "selected_location"), locationDropDownCoices));
		
		add(new Label("statusMessage"));
		
		final NumberTextField<Integer> rfidIDTextField = new NumberTextField<Integer>("rfid_id");
		rfidIDTextField.setEnabled(false);
		add(rfidIDTextField);
		
		final DropDownChoice<String> roomDropdown = new DropDownChoice<String>("roomDropdown", new PropertyModel<String>(this, "selected_room"), roomDropDownCoices);
		roomDropdown.setEnabled(false);
		add(roomDropdown);
		
		
		final DropDownChoice<String> ownerDropdown = new DropDownChoice<String>("ownerDropdown", new PropertyModel<String>(this, "selected_owner"), ownerDropDownCoices);
		ownerDropdown.setEnabled(false);
		add(ownerDropdown);
		
		
		final Button submitButton = new Button("submitButton")
		{
			private static final long serialVersionUID = -5629488022279166778L;

			@Override
			public void onSubmit()
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
		};
		submitButton.setEnabled(false);
		add(submitButton);
		
		Button saveLocationButton = new Button("saveLocationButton", new Model("Save location")){
			private static final long serialVersionUID = -7748276309230758113L;

			/**
			 * clicking this button, will enable the input fields and dynamically add the choices to the dropdown menu
			 */
			@Override
			public void onSubmit()
			{
				roomDropdown.setEnabled(true);
				ownerDropdown.setEnabled(true);
				submitButton.setEnabled(true);
				rfidIDTextField.setEnabled(true);
				
				//clear dropdown menu choices
				roomDropDownCoices.clear();
				ownerDropDownCoices.clear();
				
				//fill room dropdown menu choices
				List<String> roomDatabaseRecords = null;
				try
				{
					roomDatabaseRecords = RoomDatabaseHandler.getRecordsFromDatabaseByLocation(selected_location);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				roomDropDownCoices.add("please select");
				roomDropDownCoices.addAll(roomDatabaseRecords);
				roomDropdown.setChoices(roomDropDownCoices);
				
				// fill owner drop down choices
				List<String> employeeDatabaseRecords = null;
				try
				{
					employeeDatabaseRecords = EmployeeDatabaseHandler.getRecordsFromDatabaseByLocation(selected_location);
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				
				ownerDropDownCoices.add("please select");
				ownerDropDownCoices.addAll(employeeDatabaseRecords);
				ownerDropdown.setChoices(ownerDropDownCoices);
			}
		};
		add(saveLocationButton);
	}
}

