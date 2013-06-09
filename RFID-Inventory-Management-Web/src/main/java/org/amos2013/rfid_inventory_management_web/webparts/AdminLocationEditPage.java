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

import java.io.IOException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
/**
 * This class defines a sub-website from /admin/location, which creates a Form to update the
 * record in locationTable.
 */
public class AdminLocationEditPage extends MainPage
{
	private static final long serialVersionUID = 2813704650439574021L;

	/**
	 * The constructor creates a website which contains a Form to update the record in locationTable.
	 *
	 * @param editParameter the edit parameter
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public AdminLocationEditPage(final PageParameters editParameter) throws IOException
	{
		add(new DatabaseAccessLocationEditForm("databaseAccessLocationEditForm", editParameter));
	}
}
