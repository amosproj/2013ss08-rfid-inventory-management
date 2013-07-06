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
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;

import org.amos2013.rfid_inventory_management_web.database.LocationDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.LocationDatabaseRecord;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
//import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
//import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValueConversionException;

/**
 * Form that is displayed on /admin/location/edit. Used to update the record in locationTable
 */
public class DatabaseAccessLocationEditForm extends Form<Object>
{
	private static final long serialVersionUID = 8300102457162433623L;

	private String statusMessage;

	private Integer locationID = null;
	private String location;

	// TODO later on we should probably get the contents for the following string from the database
	//private List<String> locationDropDownCoices = new ArrayList<String>(Arrays.asList("Tennenlohe (DE)", "Bothell (US)"));
	//private String selectedLocation = "Tennenlohe (DE)";

	private LocationDatabaseRecord locationRecord;
	String function;

	/**
	 * Creates a Form Object to submit updates to the database.
	 *
	 * @param id the name of this form, to use it in html
	 * @param pageParameter the page parameter
	 */
	public DatabaseAccessLocationEditForm(String id, final PageParameters pageParameter)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this));	// sets the model to bind to the wicket ids
		
		if (pageParameter != null)
		{
			// if /admin/location/edit or only /admin/location/edit&recordID= is entered, go back
			if ((pageParameter.get("recordID").isNull() == true && pageParameter.get("function").isNull() == true)
					|| (pageParameter.get("recordID").isNull() == false && pageParameter.get("function").isNull() == true))
			{
				throw new RestartResponseAtInterceptPageException(AdminLocationPage.class, null);				
			}
			
			function = pageParameter.get("function").toString();
			if (function.equals("update"))
			{
				try
				{
					// get the record
					locationID = pageParameter.get("recordID").toInteger();
				}
				catch (StringValueConversionException e)
				{
					// is thrown if no integer is entered after the /edit?recordID=
					throw new RestartResponseAtInterceptPageException(AdminLocationPage.class, null);			
				}
				
				try
				{
					locationRecord = LocationDatabaseHandler.getRecordFromDatabaseByID(locationID);
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					PageParameters statusPageParameter = new PageParameters();
					statusPageParameter.add("message", "Error with the database. Please check your internet connection."); 
					throw new RestartResponseAtInterceptPageException(AdminLocationPage.class, statusPageParameter);								
				}
	
				if (locationRecord == null)
				{
					PageParameters statusPageParameter = new PageParameters();
					statusPageParameter.add("message", "Error: the record is not found"); 
					throw new RestartResponseAtInterceptPageException(AdminLocationPage.class, statusPageParameter);		
				}
				
				location = locationRecord.getLocation();
			}
			
			add(new Label("statusMessage"));
			
			final TextField<String> locationTextField = new TextField<String>("location");
			add(locationTextField);
			
			//add(new DropDownChoice<String>("locationDropdown", new PropertyModel<String>(this, "selectedLocation"), locationDropDownCoices));
			
			final Button submitButton = new Button("locationSubmitButton")
			{
				private static final long serialVersionUID = -1186920567456585139L;

				public void onSubmit()
				{
					// catch invalid input first
					if (location == null)
					{
						statusMessage = "Please enter a location.";
						return;
					}
					
					// write to the database
					try
					{
						LocationDatabaseRecord record = new LocationDatabaseRecord(locationID, location);
						LocationDatabaseHandler.updateRecordInDatabase(record);
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
						statusMessage = "An error with the database occured. Please check your internet connection.";
						e.printStackTrace();
						return;
					}
					catch (Exception e)
					{
						statusMessage = "An error occured!";
						e.printStackTrace();
						return;
					}
					
					PageParameters statusPageParameter = new PageParameters();
					
					if (function.equals("update"))
					{
						statusPageParameter.add("message", "The location was updated.");
					}
					else
					{
						statusPageParameter.add("message", "The new location was added.");
					} 
					
					setResponsePage(AdminLocationPage.class, statusPageParameter);
				}
			};
			add(submitButton);
			
			final Button locationCancelButton = new Button("locationCancelButton")
			{
				private static final long serialVersionUID = -7253326900738732029L;

				@Override
				public void onSubmit()
				{
					setResponsePage(AdminLocationPage.class, null);
				}
			};
			add(locationCancelButton);
		}
		else
		{
			// no page parameter: go back
			setResponsePage(AdminLocationPage.class, null);
		}
	}

}
