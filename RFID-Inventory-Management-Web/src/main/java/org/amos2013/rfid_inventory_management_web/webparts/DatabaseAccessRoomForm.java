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

import java.util.List;

import org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.RoomDatabaseRecord;
import org.amos2013.rfid_inventory_management_web.main.ConfirmationClickLink;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Form that is displayed on the website. Used for reading and writing rooms from/ to the database
 */
public class DatabaseAccessRoomForm extends Form<Object>
{
	private static final long serialVersionUID = -381676090169064307L;
	
	private String statusMessage;
	
	/**
	 * Creates a Form Object.
	 * @param id the name of this form, to use in html
	 */
	public DatabaseAccessRoomForm(String id, final PageParameters pageParameter)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this)); // sets the model to bind to the wicket ids
		List<RoomDatabaseRecord> databaseRecords = null;
		
		add(new Label("statusMessage"));
		
		//if this page shows the edited record, it will show "The edited record was saved."
		if (pageParameter != null && pageParameter.get("message").isNull() == false)
		{
			statusMessage = pageParameter.get("message").toString();
		}
		
		//add button, that adds a new record to the roomTable when clicked
		final Button roomAddButton = new Button("roomAddButton")
		{
			private static final long serialVersionUID = 5362847094125103725L;
			
			@Override
			public void onSubmit()
			{
				PageParameters editParameter = new PageParameters();
				editParameter.add("function", "add");
				setResponsePage(AdminRoomEditPage.class, editParameter);
			}
		};
		add(roomAddButton); 
		
		// get all database records and display in a listview
		try
		{
			databaseRecords = RoomDatabaseHandler.getRecordsFromDatabase();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			statusMessage = e.getMessage();
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
				item.add(new ConfirmationClickLink<String>("deleteRecordLink", "Do you really want to delete this record?")
				{
					private static final long serialVersionUID = 8585803517850396447L;

					@Override
					public void onClick(AjaxRequestTarget arg0)
					{
						// call to delete the product
						try
						{
							RoomDatabaseHandler.deleteRecordFromDatabase(record);
						} 
						catch (Exception e)
						{
							e.printStackTrace();
							statusMessage = "An error occured.";
							return;
						}
						
						// refreshes the page
						PageParameters pageParameters = new PageParameters();
						pageParameters.add("message", "The room was deleted.");
						setResponsePage(AdminRoomPage.class, pageParameters);
					}
				});
				
				// adds a link to edit the current record item
				item.add(new Link<String>("editRecordLink")
			    {
					private static final long serialVersionUID = -4819371301700755014L;

					public void onClick()
					{
						// pass record id as parameter
						PageParameters editParameter = new PageParameters();
						editParameter.add("function", "update");
						editParameter.add("recordID", record.getID());
				        setResponsePage(AdminRoomEditPage.class, editParameter);
					}
			    });
			}
		});
	}
}

