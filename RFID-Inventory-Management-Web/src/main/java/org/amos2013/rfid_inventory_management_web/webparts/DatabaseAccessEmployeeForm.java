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

import org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseRecord;
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
 * Form that is displayed on the website. Used for reading and writing employees from/ to the database
 */
public class DatabaseAccessEmployeeForm extends Form<Object>
{
	private static final long serialVersionUID = -4875920952370395619L;
	private String statusMessage;
	
	
	/**
	 * Creates a Form Object.
	 * @param id the name of this form, to use in html
	 */
	public DatabaseAccessEmployeeForm(String id, final PageParameters pageParameter)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this)); // sets the model to bind to the wicket ids
		List<EmployeeDatabaseRecord> databaseRecords = null;
			
		add(new Label("statusMessage"));
		
		//if this page shows the edited record, it will show "The edited record was saved."
		if (pageParameter != null && pageParameter.get("message").isNull() == false)
		{
			statusMessage = pageParameter.get("message").toString();
		}
		
		//add button, that adds a new record to the employeeTable when clicked
		final Button employeeAddButton = new Button("employeeAddButton")
		{
			private static final long serialVersionUID = -2309020398453139352L;

			@Override
			public void onSubmit()
			{
				PageParameters editParameter = new PageParameters();
				editParameter.add("function", "add");
				setResponsePage(AdminEmployeeEditPage.class, editParameter);
			}
		};
		add(employeeAddButton); 
		
		// get all database records and display in a listview
		try
		{
			databaseRecords = EmployeeDatabaseHandler.getRecordsFromDatabase();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			statusMessage = e.getMessage();
		}
		
		add(new ListView<EmployeeDatabaseRecord>("employeeRecordsReadListView", databaseRecords)
		{
			private static final long serialVersionUID = 3164356346909399445L;

			protected void populateItem(ListItem<EmployeeDatabaseRecord> item)
			{
				final EmployeeDatabaseRecord record = (EmployeeDatabaseRecord) item.getModelObject();
				item.add(new Label("recordNameLabel", record.getName()));
				item.add(new Label("recordLocationLabel", record.getLocation()));
				
				// adds a link to delete the current record item
				item.add(new ConfirmationClickLink<String>("deleteRecordLink", "Do you really want to delete this record?")
				{
					private static final long serialVersionUID = -8301447184775119472L;

					@Override
					public void onClick(AjaxRequestTarget arg0)
					{
						String resultMessage = "The employee was deleted.";
						
						// call to delete the product
						try
						{
							EmployeeDatabaseHandler.deleteRecordFromDatabase(record);
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
						setResponsePage(AdminEmployeePage.class, pageParameters);
					}
				});
				
				// adds a link to edit the current record item
				item.add(new Link<String>("editRecordLink")
			    {
					private static final long serialVersionUID = -2311167802339670665L;

					public void onClick()
					{
						// pass record id as parameter
						PageParameters editParameter = new PageParameters();
						editParameter.add("function", "update");
						editParameter.add("recordID", record.getID());
				        setResponsePage(AdminEmployeeEditPage.class, editParameter);
					}
			    });
			}
		});
	}
}

