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
import org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseRecord;
import org.amos2013.rfid_inventory_management_web.main.ConfirmationClickLink;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Form that is displayed on the website. Used for reading and writing data from/ to the database
 */
public class DatabaseAccessAdminListForm extends Form<Object>
{
	private static final long serialVersionUID = 2948880218956382827L;
	
	private String searchField;
	private String statusMessage;
	
	private static final List<String> SEARCH_OPTIONS = Arrays.asList(new String[] {"All Columns", "Employee", "RFID-Id", "Room",
			"Part Number", "Type", "Category", "Manufacturer", "Platform", "Comment",
			"Serial Number", "Inventory Number", "Owner", "Status", "Annotation", "ID", "Received From", "Returned To", "ESN"});
	private String selectedSearchOption = "All Columns";
	
	/**
	 * Creates a Form Object.
	 * @param	id 							the name of this form, to use in html
	 * @param	previousSearchParameters	contains the selected search option and the search string from the previous query
	 */
	public DatabaseAccessAdminListForm(String id, final PageParameters previousSearchParameters)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this));
		List<DeviceDatabaseRecord> databaseRecords = null;
		
		// if /search is entered previousSearchParameters is not null! but the toString()s will return null
		// then just throw an RestartResponseAtInterceptPageException, which will redirect to /admin
		if (previousSearchParameters != null 
					// if /admin/search or /admin/search/ (in the second case pageParameters.isEmpty() is false, because the last "/" is a parameter)
				&& ((previousSearchParameters.get("message").isNull() && previousSearchParameters.get("search_string").isNull() 
							&& previousSearchParameters.get("search_option").isNull() && getRequest().getUrl().getPath().startsWith("admin/search"))
						// if /admin/search?search_option=a
						|| (previousSearchParameters.get("search_string").isNull() && previousSearchParameters.get("search_option").isNull() == false)
						// if /admin/search?search_string=a
						|| (previousSearchParameters.get("search_option").isNull() && previousSearchParameters.get("search_string").isNull() == false)
						// if /admin/search?message=a
						|| (previousSearchParameters.get("message").isNull() == false && getRequest().getUrl().getPath().startsWith("admin/search"))))
		{
			throw new RestartResponseAtInterceptPageException(AdminListPage.class, null);
		}
			
		final DeviceDatabaseHandler deviceDatabaseHandler = DeviceDatabaseHandler.getInstance();
		
		// add search field
		add(new TextField<String>("searchField"));
		add(new DropDownChoice<String>("search_dropdown", new PropertyModel<String>(this, "selectedSearchOption"), SEARCH_OPTIONS));
		add(new Label("statusMessage"));
		
		//add button, that adds a new record to the deviceTable when clicked
		final Button deviceAddButton = new Button("deviceAddButton")
		{
			private static final long serialVersionUID = -8736242207101015483L;

			@Override
			public void onSubmit()
			{
				PageParameters editParameter = new PageParameters();
				editParameter.add("function", "add");
				setResponsePage(AdminListEditPage.class, editParameter);
			}
		};
		add(deviceAddButton);
		
		if (previousSearchParameters != null 
				&& (previousSearchParameters.get("search_string").isNull() == false && previousSearchParameters.get("search_option").isNull() == false))
		{
			String search_string = previousSearchParameters.get("search_string").toString();
			String search_option = previousSearchParameters.get("search_option").toString();
			
			// keep the dropdown menu choice selected
			// get search type
			if (search_option.equals("all_columns"))
			{
				selectedSearchOption = "All Columns";
			}
			else if (search_option.equals("rfid_id"))
			{
				selectedSearchOption = "RFID-Id";
			}
			else if (search_option.equals("room"))
			{
				selectedSearchOption = "Room";
			}
			else if (search_option.equals("employee"))
			{
				selectedSearchOption = "Employee";
			}
			else if (search_option.equals("part_number"))
			{
				selectedSearchOption = "Part Number";
			}
			else if (search_option.equals("type"))
			{
				selectedSearchOption = "Type";
			}
			else if (search_option.equals("category"))
			{
				selectedSearchOption = "Category";
			}
			else if (search_option.equals("manufacturer"))
			{
				selectedSearchOption = "Manufacturer";
			}
			else if (search_option.equals("platform"))
			{
				selectedSearchOption = "Platform";
			}
			else if (search_option.equals("comment"))
			{
				selectedSearchOption = "Comment";
			}
			else if (search_option.equals("serial_number"))
			{
				selectedSearchOption = "Serial Number";
			}
			else if (search_option.equals("inventory_number"))
			{
				selectedSearchOption = "Inventory Number";
			}
			else if (search_option.equals("owner"))
			{
				selectedSearchOption = "Owner";
			}
			else if (search_option.equals("status"))
			{
				selectedSearchOption = "Status";
			}
			else if (search_option.equals("annotation"))
			{
				selectedSearchOption = "Annotation";
			}
			else if (search_option.equals("id"))
			{
				selectedSearchOption = "ID";
			}
			else if (search_option.equals("received_from"))
			{
				selectedSearchOption = "Received From";
			}
			else if (search_option.equals("returned_to"))
			{
				selectedSearchOption = "Returned To";
			}
			else if (search_option.equals("esn"))
			{
				selectedSearchOption = "ESN";
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
			if (previousSearchParameters.get("message").isNull() == false)
			{
				statusMessage = previousSearchParameters.get("message").toString();
			}
			
			// get all database records and display in a listview
			try
			{
				databaseRecords = deviceDatabaseHandler.getDatabaseRecordList();
			} 
			catch (SQLException e)
			{
				statusMessage = "Error with the database connection. Please check your internet connection.";
				databaseRecords = new ArrayList<DeviceDatabaseRecord>();
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
				item.add(new Label("recordCommentLabel", record.getComment()));
				
				item.add(new Label("recordSerialNumberLabel", record.getSerialNumber()));
				item.add(new Label("recordInventoryNumberLabel", record.getInventoryNumber()));
				item.add(new Label("recordOwnerLabel", record.getOwner()));
				item.add(new Label("recordStatusLabel", record.getStatus()));
				item.add(new Label("recordAnnotationLabel", record.getAnnotation()));
				item.add(new Label("recordIdLabel", record.getId()));
				item.add(new Label("recordReceivedFromLabel", record.getReceivedFrom()));
				item.add(new Label("recordReturnedToLabel", record.getReturnedTo()));
				item.add(new Label("recordEsnLabel", record.getEsn()));
				
				
				// adds a link to delete the current record item
				
				item.add(new ConfirmationClickLink<String>("deleteRecordLink", "Do you want to delete this record?")
				{
					private static final long serialVersionUID = 8843977459555507386L;

					@Override
					public void onClick(AjaxRequestTarget arg0)
					{
						String resultMessage = "The device record was deleted.";
						// call to delete the product
						try
						{
							DeviceDatabaseHandler deviceDatabaseHandler = DeviceDatabaseHandler.getInstance();
							// getInstance() has to be called here again, to avoid NotSerializableExceptions 
							deviceDatabaseHandler.deleteRecordFromDatabase(record);
						} 
						catch (IllegalArgumentException e)
						{
							e.printStackTrace();
							resultMessage = "An error occured. The record is null";
						}
						catch (SQLException e)
						{
							resultMessage = "Error with the database. Please check your internet connection.";
						}
						
						
						// refreshes the page
						PageParameters pageParameters = new PageParameters();
						pageParameters.add("message", resultMessage);
						setResponsePage(AdminListPage.class, pageParameters);						
					}
				});
				
				// adds a link to edit the current record item
				item.add(new Link<String>("editRecordLink")
			    {
					private static final long serialVersionUID = 8843977459555507386L;

					public void onClick()
					{
						PageParameters editParameter = new PageParameters();
						editParameter.add("function", "update");
						editParameter.add("rfidID", record.getRFIDId());
				        setResponsePage(AdminListEditPage.class, editParameter);
					}
			    });
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
				if (selectedSearchOption.equals("All Columns"))
				{
					searchType = "all_columns";
				}
				else if (selectedSearchOption.equals("RFID-Id"))
				{
					searchType = "rfid_id";
				}
				else if (selectedSearchOption.equals("Room"))
				{
					searchType = "room";
				}
				else if (selectedSearchOption.equals("Employee"))
				{
					searchType = "employee";
				}
				else if (selectedSearchOption.equals("Part Number"))
				{
					searchType = "part_number";
				}
				else if (selectedSearchOption.equals("Type"))
				{
					searchType = "type";
				}
				else if (selectedSearchOption.equals("Category"))
				{
					searchType = "category";
				}
				else if (selectedSearchOption.equals("Manufacturer"))
				{
					searchType = "manufacturer";
				}
				else if (selectedSearchOption.equals("Platform"))
				{
					searchType = "platform";
				}
				else if (selectedSearchOption.equals("Comment"))
				{
					searchType = "comment";
				}
				else if (selectedSearchOption.equals("Serial Number"))
				{
					searchType = "serial_number";
				}
				else if (selectedSearchOption.equals("Inventory Number"))
				{
					searchType = "inventory_number";
				}
				else if (selectedSearchOption.equals("Owner"))
				{
					searchType = "owner";
				}
				else if (selectedSearchOption.equals("Status"))
				{
					searchType = "status";
				}
				else if (selectedSearchOption.equals("Annotation"))
				{
					searchType = "annotation";
				}
				else if (selectedSearchOption.equals("ID"))
				{
					searchType = "id";
				}
				else if (selectedSearchOption.equals("Received From"))
				{
					searchType = "received_from";
				}
				else if (selectedSearchOption.equals("Returned To"))
				{
					searchType = "returned_to";
				}
				else if (selectedSearchOption.equals("ESN"))
				{
					searchType = "esn";
				}				
				else
				{
					statusMessage = "Unknown search type";
					return;
				}
				
				searchParameters.add("search_string", searchField);
				searchParameters.add("search_option", searchType);
				setResponsePage(SearchPageAdminList.class, searchParameters); 	 
			}
		};
		add(searchButton);
		
		/*
		 * This will be executed, when the back button on the search page is clicked.
		 * It will go back to the whole list view
		 */
		if (previousSearchParameters != null 
				&& (previousSearchParameters.get("search_string").isNull() == false && previousSearchParameters.get("search_option").isNull() == false))
		{
			Button backButton = new Button("back_button") 
			{
				private static final long serialVersionUID = 2207995377260273773L;

				public void onSubmit() 
				{
					setResponsePage(AdminListPage.class, null);
				}
			};
			add(backButton);
		}
	}
}
