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

package de.fau.cs.amos2013.proj8;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;

public class LocForm extends Form<Object>{
	private static final long serialVersionUID = 1L;
	
	private String room;
	private String owner;
	private String status;
	private String result;
	private boolean success = false;
	
	public LocForm(String id) {
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this));
		add(new TextField<Object>("room"));
		add(new TextField<Object>("owner"));
		add(new Label("status"));
		add(new Label("result"));
		
			
		try {
			result = TestAccess.Result();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final void onSubmit() {
		try {
			TestDatabase.TestDatabase(room, owner);
			success = true;
		} catch (Exception e) {
			if (!success) {
				status = "Data saved!";
			} else {
				e.printStackTrace();
				status = "An error occured!";
			}
		}
		
	}
}
