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

import org.amos2013.rfid_inventory_management_web.database.DatabaseHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * Form that is displayed on the website. Used for writing data to the database
 */
public class DatabaseAppForm extends Form<Object>
{
	private static final long serialVersionUID = 2948880218956382827L;

	private int rfid_id;
	private String room;
	private String owner;
	private String status;


	/**
	 * Creates a Form Object to submit updates to the database
	 */
	public DatabaseAppForm(String id)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this));
		add(new TextField<Object>("rfid_id"));
		add(new TextField<Object>("room"));
		add(new TextField<Object>("owner"));
		add(new Label("status"));
	}

	/**
	 * This method is called, when the submit button on the homepage it is clicked.
	 * It will write the entered record into the database
	 */
	public final void onSubmit()
	{
		try
		{
			DatabaseHandler.writeRecordToDatabase(rfid_id, room, owner);
			status = "Data saved";
		}
		catch (IllegalArgumentException e)
		{
			status = "Please enter a room and a name";
		}
		catch (Exception e)
		{
			status = "An error occured!";
			e.printStackTrace();
		}
	}
}
