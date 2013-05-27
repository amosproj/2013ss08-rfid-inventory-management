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

///import java.util.Arrays;
import java.util.List;

import org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.RoomDatabaseRecord;
//import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
//import org.apache.wicket.markup.html.form.Button;
//import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
//import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
//import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * Form that is displayed on the website. Used for reading and writing data from/ to the database
 */
public class DatabaseAccessRoomForm extends Form<Object>
{
	private static final long serialVersionUID = -381676090169064307L;
	
	private String statusMessage;
	
//	private String selectedSearchOption = "Location";
	
	/**
	 * Creates a Form Object.
	 * @param	id 							the name of this form, to use in html
	 * @param	previousSearchParameters	contains the selected search option and the search string from the previous query
	 */
	public DatabaseAccessRoomForm(String id)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this)); // sets the model to bind to the wicket ids
		List<RoomDatabaseRecord> databaseRecords = null;
		
//		add(new Label("statusMessage"));

			// get all database records and display in a listview
			try
			{
				databaseRecords = RoomDatabaseHandler.getRecordsFromDatabase();
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		
		
			
			add(new ListView<RoomDatabaseRecord>("roomRecordsReadListView", databaseRecords)
			{
				private static final long serialVersionUID = -8615269269439979462L;

				protected void populateItem(ListItem<RoomDatabaseRecord> item)
				{
					final RoomDatabaseRecord record = (RoomDatabaseRecord) item.getModelObject();
					item.add(new Label("recordNameLabel", record.getName()));
					item.add(new Label("recordLocationLabel", record.getLocation()));
					
					// adds a link to delete the current record item
					item.add(new Link<String>("deleteRecordLink")
					{
						private static final long serialVersionUID = 8585803517850396447L;

						public void onClick()
						{
							// call to delete the product
							try
							{
								//statusMessage = "Are you sure to delete this record?";
								RoomDatabaseHandler.deleteRecordFromDatabase(record);
							} 
							catch (Exception e)
							{
								e.printStackTrace();
							}
							// refreshes the page
							setResponsePage(AdminRoomPage.class);
						}
					});
					
					// adds a link to edit the current record item
					item.add(new Link<String>("editRecordLink")
				    {
						private static final long serialVersionUID = -4819371301700755014L;

						public void onClick()
						{
							// TODO new site to edit

						}
				    });
				}
			});
		}
	
	

}

