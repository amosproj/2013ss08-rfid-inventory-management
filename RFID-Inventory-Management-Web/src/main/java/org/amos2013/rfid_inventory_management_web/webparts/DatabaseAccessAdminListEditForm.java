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
import org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.MetaDeviceDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.MetaDeviceDatabaseRecord;
import org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
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
 * Form that is displayed on /admin/edit. Used to update the record in hardware list
 */
public class DatabaseAccessAdminListEditForm extends Form<Object>
{
	private static final long serialVersionUID = 2663584118784785557L;

	private String statusMessage;
	private String function;

	// DeviceDatabaseRecord fields
	private Integer rfidIDInputField;
	private String selectedRoom = "Please select";
	private String selectedEmployee = "Please select";
	private String partNumberInputField;
	private String serialNumberInputField;
	private String inventoryNumberInputField;
	private String ownerInputField;
	private String commentInputField;
	
	// MetaDeviceDatabaseRecord fields;
	private String metaPartNumberInputField;
	private String typeInputField;
	private String categoryInputField;
	private String manufacturerInputField;
	private String platformInputField;
	
	private List<String> roomDropDownChoices = new ArrayList<String>();
	private List<String> employeeDropDownChoices = new ArrayList<String>();
	
	// TODO later on we should probably get the contents for the following string from the database
	private List<String> locationDropDownChoices = Arrays.asList(new String[] {"Please select", "Tennenlohe (DE)", "Bothell (US)"});
	private String selectedLocation = "Please select";

	private DeviceDatabaseRecord deviceRecord;

	/**
	 * Creates a Form Object to submit updates to the database.
	 *
	 * @param id the name of this form, to use it in html
	 * @param pageParameter the page parameter
	 */
	public DatabaseAccessAdminListEditForm(String id, final PageParameters pageParameter)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this));	// sets the model to bind to the wicket ids
		
		if (pageParameter != null)
		{
			// if /admin/edit or only /admin/edit&rfidID= is entered, go back
			if ((pageParameter.get("rfidID").isNull() == true && pageParameter.get("function").isNull() == true)
					|| (pageParameter.get("rfidID").isNull() == false && pageParameter.get("function").isNull() == true))
			{
				throw new RestartResponseAtInterceptPageException(AdminListPage.class, null);				
			}
			
			// if this is the update function, prefill input fields
			function = pageParameter.get("function").toString();
			
			if (function.equals("update"))
			{
				try
				{
					// get the record
					rfidIDInputField = pageParameter.get("rfidID").toInteger();
				}
				catch (StringValueConversionException e)
				{
					// is thrown if no integer is entered after the /edit?recordID=
					throw new RestartResponseAtInterceptPageException(AdminListPage.class, null);			
				}
				
				try
				{
					DeviceDatabaseHandler deviceDatabaseHandler = DeviceDatabaseHandler.getInstance();
					deviceRecord = deviceDatabaseHandler.getRecordFromDatabaseById(rfidIDInputField.toString());
				}
				catch (SQLException e)
				{
					e.printStackTrace();				
					PageParameters statusPageParameter = new PageParameters();
					statusPageParameter.add("message", "Error with the database connection"); 
					throw new RestartResponseAtInterceptPageException(AdminListPage.class, statusPageParameter);
				}
	
				if (deviceRecord == null)
				{
					PageParameters statusPageParameter = new PageParameters();
					statusPageParameter.add("message", "Error: the record is not found"); 
					throw new RestartResponseAtInterceptPageException(AdminListPage.class, statusPageParameter);		
				}
				
				selectedRoom = deviceRecord.getRoom();
				if (selectedRoom == null || selectedRoom.isEmpty())
				{
					selectedRoom = "Please select";
				}
				
				selectedEmployee = deviceRecord.getEmployee();
				if (selectedEmployee == null || selectedEmployee.isEmpty())
				{
					selectedEmployee = "Please select";
				}
				
				partNumberInputField = deviceRecord.getPartNumber();
				serialNumberInputField = deviceRecord.getSerialNumber();
				inventoryNumberInputField = deviceRecord.getInventoryNumber();
				ownerInputField = deviceRecord.getOwner();
				commentInputField = deviceRecord.getComment();
				
				metaPartNumberInputField = partNumberInputField;
				typeInputField = deviceRecord.getType();
				categoryInputField = deviceRecord.getCategory();
				manufacturerInputField = deviceRecord.getManufacturer();
				platformInputField = deviceRecord.getPlatform();
			}
			
			// add components
			add(new Label("statusMessage"));
			
			final DropDownChoice<String> roomDropDown = new DropDownChoice<String>("roomDropDown", new PropertyModel<String>(this, "selectedRoom"), roomDropDownChoices);
			roomDropDown.setEnabled(false);
			add(roomDropDown);
			
			final DropDownChoice<String> employeeDropDown = new DropDownChoice<String>("employeeDropDown", new PropertyModel<String>(this, "selectedEmployee"), employeeDropDownChoices);
			employeeDropDown.setEnabled(false);
			add(employeeDropDown);
			
			DropDownChoice<String> locationDropDown = new DropDownChoice<String>("locationDropDown", new PropertyModel<String>(this, "selectedLocation"), locationDropDownChoices)
				{
					private static final long serialVersionUID = 134567452542543L;
		            
					/*
					 * required, otherwise no notifications will be spread if the selection changes
					 */
					protected boolean wantOnSelectionChangedNotifications() 
					{
		                return true;
		            }
					
					@Override
					protected void onSelectionChanged(String newSelection)
					{
						super.onSelectionChanged(newSelection);
						
						if (newSelection.equals("Please select"))
						{
							employeeDropDown.setEnabled(false);
							roomDropDown.setEnabled(false);
						}
						else
						{
							employeeDropDown.setEnabled(true);
							roomDropDown.setEnabled(true);
							
							//clear dropdown menu choices
							roomDropDownChoices.clear();
							employeeDropDownChoices.clear();
							
							roomDropDownChoices.add("Please select");
							employeeDropDownChoices.add("Please select");
							
							//fill room dropdown menu choices
							List<String> roomDatabaseRecords = null;
							try
							{
								roomDatabaseRecords = RoomDatabaseHandler.getRecordsFromDatabaseByLocation(selectedLocation);
							} 
							catch (Exception e)
							{
								statusMessage = e.getMessage();
								e.printStackTrace();									
							}
							
							roomDropDownChoices.addAll(roomDatabaseRecords);
							roomDropDown.setChoices(roomDropDownChoices);
							
							// fill owner drop down choices
							List<String> employeeDatabaseRecords = null;
							try
							{
								employeeDatabaseRecords = EmployeeDatabaseHandler.getRecordsFromDatabaseByLocation(selectedLocation);
							} 
							catch (SQLException e)
							{
								statusMessage = e.getMessage();
								e.printStackTrace();
							}
							
							employeeDropDownChoices.addAll(employeeDatabaseRecords);
							employeeDropDown.setChoices(employeeDropDownChoices);
						}
					}
				};
			add(locationDropDown);
			
			final TextField<String> rfidIDTextField = new TextField<String>("rfidIDInputField");
			add(rfidIDTextField);
			
			final TextField<String> serialNumberTextField = new TextField<String>("serialNumberInputField");
			add(serialNumberTextField);
			
			final TextField<String> inventoryNumberTextField = new TextField<String>("inventoryNumberInputField");
			add(inventoryNumberTextField);
			
			final TextField<String> ownerTextField = new TextField<String>("ownerInputField");
			add(ownerTextField);
			
			final TextField<String> commentTextField = new TextField<String>("commentInputField");
			add(commentTextField);

			// meta data components
			final TextField<String> metaPartNumberTextField = new TextField<String>("metaPartNumberInputField");
			metaPartNumberTextField.setOutputMarkupId(true); // needed, to add this component to the AjaxRequestTarget, in order to chance the value dynamically			
			metaPartNumberTextField.setEnabled(false);
			add(metaPartNumberTextField);
			
			final TextField<String> typeTextField = new TextField<String>("typeInputField");
			typeTextField.setOutputMarkupId(true); // needed, to add this component to the AjaxRequestTarget, in order to chance the value dynamically
			add(typeTextField);
			
			final TextField<String> categoryTextField = new TextField<String>("categoryInputField");
			categoryTextField.setOutputMarkupId(true); // needed, to add this component to the AjaxRequestTarget, in order to chance the value dynamically			
			add(categoryTextField);
			
			final TextField<String> manufacturerTextField = new TextField<String>("manufacturerInputField");
			manufacturerTextField.setOutputMarkupId(true); // needed, to add this component to the AjaxRequestTarget, in order to chance the value dynamically
			add(manufacturerTextField);
			
			final TextField<String> platformTextField = new TextField<String>("platformInputField");
			platformTextField.setOutputMarkupId(true); // needed, to add this component to the AjaxRequestTarget, in order to chance the value dynamically
			add(platformTextField);			
			
			
			final TextField<String> partNumberTextField = new TextField<String>("partNumberInputField");
			partNumberTextField.add(new OnChangeAjaxBehavior()
			{
				private static final long serialVersionUID = -5057519909275714633L;
				
				/*
				 * is called, when the part number is changed, to fill in the meta data 
				 * @see org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior#onUpdate(org.apache.wicket.ajax.AjaxRequestTarget)
				 */
				@Override
				protected void onUpdate(AjaxRequestTarget arg0)
				{
					MetaDeviceDatabaseHandler metaDeviceDatabaseHandler = MetaDeviceDatabaseHandler.getInstance();
					MetaDeviceDatabaseRecord record = metaDeviceDatabaseHandler.getRecordByPartNumber(partNumberInputField);

					// add components to AjaxRequestTarget to be changed dynamically
					arg0.add(typeTextField);
					arg0.add(categoryTextField);
					arg0.add(manufacturerTextField);
					arg0.add(platformTextField);
					arg0.add(metaPartNumberTextField);
					
					// if a valid part number was entered
					if (record != null)
					{
						// set new values
						typeInputField = record.getType();
						categoryInputField = record.getCategory();
						manufacturerInputField = record.getManufacturer();
						platformInputField = record.getPlatform();
					}
					else 
					{
						typeInputField = null;
						categoryInputField = null;
						manufacturerInputField = null;
						platformInputField = null;
					}
					
					metaPartNumberInputField = partNumberInputField;
				}
			});
			add(partNumberTextField);
			
			final Button submitButton = new Button("submitButton")
			{
				private static final long serialVersionUID = -8667685714029200942L;

				public void onSubmit()
				{
					// catch invalid input first
					if (rfidIDInputField == null || partNumberInputField == null)
					{
						statusMessage = "Please enter a RFID ID and a part number.";
						return;
					}
					
					// write to the database: generate records from input data
					String employeeToSet = null; 
					String roomToSet = null; 
					
					if (!selectedLocation.equals("Please select") && !selectedEmployee.equals("Please select"))
					{
						employeeToSet = selectedEmployee;
					}

					if (!selectedLocation.equals("Please select") && !selectedRoom.equals("Please select"))
					{
						roomToSet = selectedRoom;
					}
					
					try
					{
						DeviceDatabaseRecord record = new DeviceDatabaseRecord(rfidIDInputField, roomToSet, employeeToSet, partNumberInputField, 
								serialNumberInputField, inventoryNumberInputField, ownerInputField, commentInputField);
						// only set a new meta record, if there were changes (this will keep the unique id)
						if (!record.getCategory().equals(categoryInputField) || !record.getType().equals(typeInputField) 
								|| !record.getPartNumber().equals(metaPartNumberInputField) || !record.getManufacturer().equals(manufacturerInputField)
								|| !record.getPlatform().equals(platformInputField))
						{
							MetaDeviceDatabaseRecord metaRecord = null;
							
							try
							{
								metaRecord = new MetaDeviceDatabaseRecord(categoryInputField, typeInputField, partNumberInputField, 
										manufacturerInputField, platformInputField);								
							}
							catch (IllegalArgumentException e)
							{
								statusMessage = "Error: part number is null!";
								return;
							}
							catch (IllegalStateException ex)
							{
								statusMessage = "Error: next free Id in the meta device database table is -1.";
								return;
							}
							
							record.setMetaDeviceDataBaseRecord(metaRecord);
						}
						
						DeviceDatabaseHandler.getInstance().updateCompleteRecordInDatabase(record);
						
						PageParameters statusPageParameter = new PageParameters();
						
						if (function.equals("update"))
						{
							statusPageParameter.add("message", "The device record was updated.");
						}
						else
						{
							statusPageParameter.add("message", "The new device record was added.");
						}
						
						setResponsePage(AdminListPage.class, statusPageParameter);
					}
					catch (SQLException e)
					{
						statusMessage = "An error with the database occured.";
						e.printStackTrace();
					}
					catch (IllegalArgumentException e)
					{
						statusMessage = e.getMessage();
						e.printStackTrace();
					}
					catch (Exception e)
					{
						statusMessage = "An error occured!";
						e.printStackTrace();
					}
				}
			};
			add(submitButton);
			
			final Button cancelButton = new Button("cancelButton")
			{
				private static final long serialVersionUID = 3622397615908992618L;

				@Override
				public void onSubmit()
				{
					setResponsePage(AdminListPage.class, null);
				}
			};
			add(cancelButton);
		}
		else
		{
			// no page parameter: go back
			setResponsePage(AdminListPage.class, null);
		}
	}
}
