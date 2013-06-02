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

import java.util.Arrays;
import java.util.List;

import org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseRecord;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Form that is displayed on the website. Used for reading and written data from/ to the database
 */
public class DatabaseAccessListForm extends Form<Object>
{
	private static final long serialVersionUID = 2948880218956382827L;
	
	private String searchField;
	private String statusMessage;
	
	private static final List<String> SEARCH_OPTIONS = Arrays.asList(new String[] {"Employee", "Inventory Number", "Manufacturer", "Platform", "Room", "Type" });
	private String selectedSearchOption = "Employee";
	
	/**
	 * Creates a Form Object.
	 * @param	id 							the name of this form, to use in html
	 * @param	previousSearchParameters	contains the selected search option and the search string from the previous query
	 */
	public DatabaseAccessListForm(String id, final PageParameters previousSearchParameters)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this));
		List<DeviceDatabaseRecord> databaseRecords = null;
		
		DeviceDatabaseHandler deviceDatabaseHandler = DeviceDatabaseHandler.getInstance();
		
		// add search field
		add(new TextField<String>("searchField"));
		add(new DropDownChoice<String>("search_dropdown", new PropertyModel<String>(this, "selectedSearchOption"), SEARCH_OPTIONS));
		add(new Label("statusMessage"));
		
		if (previousSearchParameters != null)
		{
			String search_string = previousSearchParameters.get("search_string").toString();
			String search_option = previousSearchParameters.get("search_option").toString();
			
			// if /search is entered previousSearchParameters is not null! but the toString()s will return null
			// then just throw an RestartResponseAtInterceptPageException, which will redirect to /
			if ((search_string == null) && (search_option == null))
			{				
				throw new RestartResponseAtInterceptPageException(ListPage.class);
			}
		
			// keep the dropdown menu choice selected
			// get search type
			if (search_option.equals("inventory_number"))
			{
				selectedSearchOption = "Inventory Number";
			}
			else if (search_option.equals("manufacturer"))
			{
				selectedSearchOption = "Manufacturer";
			}
			else if (search_option.equals("type"))
			{
				selectedSearchOption = "Type";
			}
			else if (search_option.equals("platform"))
			{
				selectedSearchOption = "Platform";
			}
			else if (search_option.equals("room"))
			{
				selectedSearchOption = "Room";
			}
			
			// search for the string in the specified column
			try
			{
				databaseRecords = deviceDatabaseHandler.getRecordsByPartialStringAndColumn(search_string, search_option);
			} 
			catch (IllegalArgumentException e)
			{
				statusMessage = e.getMessage();
			}
			
			// found nothing and returns a status message
			if (databaseRecords == null || databaseRecords.isEmpty() || databaseRecords.toString().equals("[]"))
			{
				statusMessage = "No record found, while searching for: " + search_string;
			}	
		}
		else
		{
			// get all database records and display in a listview
			try
			{
				databaseRecords = deviceDatabaseHandler.getDatabaseRecordList();
			} 
			catch (Exception e)
			{
				statusMessage = e.getMessage();
			}
		}
		
		add(new ListView<DeviceDatabaseRecord>("recordsReadListView", databaseRecords)
		{
			private static final long serialVersionUID = 25754831191690183L;

			protected void populateItem(ListItem<DeviceDatabaseRecord> item)
			{
				final DeviceDatabaseRecord record = (DeviceDatabaseRecord) item.getModelObject();
				item.add(new Label("recordRFIDIdLabel", record.getRFIDId()));
				item.add(new Label("recordRoomLabel", record.getRoom()));
				item.add(new Label("recordEmployeeLabel", record.getEmployee()));
				item.add(new Label("recordPartNumberLabel", record.getPartNumber()));
				
				item.add(new Label("recordTypeLabel", record.getType()));
				item.add(new Label("recordCategoryLabel", record.getCategory()));
				item.add(new Label("recordManufacturerLabel", record.getManufacturer()));
				item.add(new Label("recordPlatformLabel", record.getPlatform()));
				
				item.add(new Label("recordSerialNumberLabel", record.getSerialNumber()));
				item.add(new Label("recordInventoryNumberLabel", record.getInventoryNumber()));
				item.add(new Label("recordOwnerLabel", record.getOwner()));
				item.add(new Label("recordCommentLabel", record.getComment()));
			}
		});
		
		Button searchButton = new Button("search_button") 
		{
			private static final long serialVersionUID = -1480472797060494747L;

			/*
			 * This is called, when the search button on the homepage is clicked.
			 * It will give the search parameters to the URL and opens a search page with a new iteration of this
			 * class which will perform the search.
			 */
			public void onSubmit() 
			{
				PageParameters searchParameters = new PageParameters();
				
				if (searchField == null)
				{
					statusMessage = "Please enter a search query";
					return;
				}
				
				// get search type
				String searchType;
				if (selectedSearchOption.equals("Inventory Number"))
				{
					searchType = "inventory_number";
				}
				else if (selectedSearchOption.equals("Manufacturer"))
				{
					searchType = "manufacturer";
				}
				else if (selectedSearchOption.equals("Type"))
				{
					searchType = "type";
				}
				else if (selectedSearchOption.equals("Platform"))
				{
					searchType = "platform";
				}
				else if (selectedSearchOption.equals("Room"))
				{
					searchType = "room";
				}
				else if (selectedSearchOption.equals("Employee"))
				{
					searchType = "employee";
				}
				else
				{
					statusMessage = "Unknown search type";
					return;
				}
				
				searchParameters.add("search_string", searchField);
				searchParameters.add("search_option", searchType);
				setResponsePage(SearchPageList.class, searchParameters); 	 
			}
		};
		
		add(searchButton);
		
		/*
		 * This will be executed, when the back button on the searchpage is clicked.
		 * It will go back to the whole list view
		 */
		if (previousSearchParameters != null)
		{
			Button backButton = new Button("back_button") 
			{
				private static final long serialVersionUID = 2207995377260273773L;

				public void onSubmit() 
				{
					setResponsePage(ListPage.class);
				}
			};
			add(backButton);
		}
	}
}
