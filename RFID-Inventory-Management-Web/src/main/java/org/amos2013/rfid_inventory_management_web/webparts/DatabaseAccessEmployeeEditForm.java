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
import java.util.List;

import org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseRecord;
import org.amos2013.rfid_inventory_management_web.database.LocationDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.LocationDatabaseRecord;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValueConversionException;

/**
 * Form that is displayed on /admin/employee/edit. Used to update the record in employeeTable
 */
public class DatabaseAccessEmployeeEditForm extends Form<Object>
{
	private static final long serialVersionUID = -1193158177351257195L;

	private String statusMessage;

	private Integer employeeID = null;
	private String employeeName;

	private List<String> locationDropDownChoices = new ArrayList<String>();
	private String selectedLocation = "Please select";

	private EmployeeDatabaseRecord employeeRecord;
	String function;

	/**
	 * Creates a Form Object to submit updates to the database.
	 *
	 * @param id the name of this form, to use it in html
	 * @param pageParameter the page parameter
	 */
	public DatabaseAccessEmployeeEditForm(String id, final PageParameters pageParameter)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this));	// sets the model to bind to the wicket ids
		
		if (pageParameter != null)
		{
			// if /admin/employee/edit or only /admin/employee/edit&recordID= is entered, go back
			if ((pageParameter.get("recordID").isNull() == true && pageParameter.get("function").isNull() == true)
					|| (pageParameter.get("recordID").isNull() == false && pageParameter.get("function").isNull() == true))
			{
				throw new RestartResponseAtInterceptPageException(AdminEmployeePage.class, null);				
			}
			
			function = pageParameter.get("function").toString();
			if (function.equals("update"))
			{
				try
				{
					// get the record
					employeeID = pageParameter.get("recordID").toInteger();
				}
				catch (StringValueConversionException e)
				{
					// is thrown if no integer is entered after the /edit?recordID=
					throw new RestartResponseAtInterceptPageException(AdminEmployeePage.class, null);			
				}
				
				try
				{
					employeeRecord = EmployeeDatabaseHandler.getRecordFromDatabaseByID(employeeID);
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					PageParameters statusPageParameter = new PageParameters();
					statusPageParameter.add("message", "Error with the database connection"); 
					throw new RestartResponseAtInterceptPageException(AdminEmployeePage.class, statusPageParameter);								
				}
	
				if (employeeRecord == null)
				{
					PageParameters statusPageParameter = new PageParameters();
					statusPageParameter.add("message", "Error: the record is not found"); 
					throw new RestartResponseAtInterceptPageException(AdminEmployeePage.class, statusPageParameter);		
				}
				
				employeeName = employeeRecord.getName();
				selectedLocation = employeeRecord.getLocation();
			}
			
			add(new Label("statusMessage"));
			
			final TextField<String> nameTextField = new TextField<String>("employeeName");
			add(nameTextField);
			
			DropDownChoice<String> locationDropDown = new DropDownChoice<String> ("locationDropdown", new PropertyModel<String>(this, "selectedLocation"), locationDropDownChoices);
			locationDropDown.setEnabled(true);
			add(locationDropDown);
			
			//fill location dropdown menu choices
			locationDropDownChoices.clear();
			List<LocationDatabaseRecord> locationDatabaseRecords = null;
			List<String> locations = new ArrayList<String>();
			try
			{
				locationDatabaseRecords = LocationDatabaseHandler.getRecordsFromDatabase();
			}
			catch (Exception e)
			{
				statusMessage = e.getMessage();
			}

			if (locationDatabaseRecords == null)
			{
				statusMessage = "ERROR: location list is null";
				locationDatabaseRecords = new ArrayList<LocationDatabaseRecord>();
			}
			
			// get strings
			for (LocationDatabaseRecord record : locationDatabaseRecords)
			{
				locations.add(record.getLocation());
			}
			
			locationDropDownChoices.add("Please select");
			locationDropDownChoices.addAll(locations);
			locationDropDown.setChoices(locationDropDownChoices);
			
			final Button submitButton = new Button("employeeSubmitButton")
			{
				private static final long serialVersionUID = -1616761226247933012L;

				public void onSubmit()
				{
					// catch invalid input first
					if (employeeName == null || selectedLocation.equals("Please select"))
					{
						statusMessage = "Please enter a name and a location.";
						return;
					}
					
					// write to the database
					try
					{
						EmployeeDatabaseRecord record = new EmployeeDatabaseRecord(employeeID, employeeName, selectedLocation);
						EmployeeDatabaseHandler.updateRecordInDatabase(record);
						
						PageParameters statusPageParameter = new PageParameters();
						
						if (function.equals("update"))
						{
							statusPageParameter.add("message", "The employee was updated.");
						}
						else
						{
							statusPageParameter.add("message", "The new employee was added.");
						} 
						
						setResponsePage(AdminEmployeePage.class, statusPageParameter);
					}
					catch (IllegalArgumentException e)
					{
						statusMessage = e.getMessage();
						return;
					}
					catch (IllegalStateException e)
					{
						statusMessage = e.getMessage();
						e.printStackTrace();
						return;
					}
					catch (SQLException e)
					{
						statusMessage = "An error with the database occured.";
						e.printStackTrace();
						return;
					}
					catch (Exception e)
					{
						statusMessage = "An error occured!";
						e.printStackTrace();
						return;
					}
				}
			};
			add(submitButton);
			
			final Button employeeCancelButton = new Button("employeeCancelButton")
			{
				private static final long serialVersionUID = -1926515273886994648L;

				@Override
				public void onSubmit()
				{
					setResponsePage(AdminEmployeePage.class, null);
				}
			};
			add(employeeCancelButton);
		}
		else
		{
			// no page parameter: go back
			setResponsePage(AdminEmployeePage.class, null);
		}
	}
}
