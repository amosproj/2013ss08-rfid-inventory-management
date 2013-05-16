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
import java.util.List;

import org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseRecord;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;

/**
 * Form that is displayed on the website. Used for reading and written data from/ to the database
 */
public class DatabaseAccessForm extends Form<Object>
{
	private static final long serialVersionUID = 2948880218956382827L;
	
	private Integer searchField; // use Integer instead of int, so the default value is null and not 0. so nothing will be displayed
	private String statusMessage;
	
	/**
	 * Creates a Form Object.
	 * @param id the name of this form, to use in html
	 */
	public DatabaseAccessForm(String id)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this));		

		// add search field
		add(new NumberTextField<Integer>("searchField"));	
		add(new Label("statusMessage"));
		
		// get all database records and display in a listview
		List<DeviceDatabaseRecord> databaseRecords = null;
		try
		{
			databaseRecords = DeviceDatabaseHandler.getRecordsFromDatabase();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		add(new ListView<DeviceDatabaseRecord>("recordsReadListView", databaseRecords)
		{
			private static final long serialVersionUID = 25754831191690183L;

			protected void populateItem(ListItem<DeviceDatabaseRecord> item)
			{
				final DeviceDatabaseRecord record = (DeviceDatabaseRecord) item.getModelObject();
				item.add(new Label("recordRFIDIdLabel", record.getRFIDId()));
				item.add(new Label("recordRoomLabel", record.getRoom()));
				item.add(new Label("recordOwnerLabel", record.getOwner()));			
				
				// adds a link to delete the current record item
				item.add(new Link<String>("deleteRecordLink")
			    {
					private static final long serialVersionUID = 8843977459555507386L;

					public void onClick()
					{
						// call to delete the product
						try
						{
							DeviceDatabaseHandler.deleteRecordFromDatabase(record);
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						}
						// refreshes the page
						setResponsePage(ListPage.class);
					}
			    });
			}
		});
	}
	

	/**
	 * This method is called, when the search button on the homepage it is clicked.
	 * It will perform a search
	 */
	public final void onSubmit()
	{	
		DeviceDatabaseRecord searchResultRecord = null;
		
		if (searchField == null)
		{
			statusMessage = "Please enter a number";
			return;
		}
		
		try
		{
			searchResultRecord = DeviceDatabaseHandler.getRecordFromDatabaseById(searchField);
		} 
		catch (IllegalArgumentException e)
		{
			statusMessage = e.getMessage();
			e.printStackTrace();
		}
		catch (IllegalStateException e)
		{
			statusMessage = e.getMessage();
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			statusMessage = e.getMessage();
			e.printStackTrace();
		}
		
		// display results
		if (searchResultRecord != null)
		{
			statusMessage = "Record found: " + searchResultRecord.toString();		
		}
		else
		{
			statusMessage = "No record found, while searching for: " + searchField;		
		}
	}
}
