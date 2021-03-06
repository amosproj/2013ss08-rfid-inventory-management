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

import junit.framework.Assert;

import org.amos2013.rfid_inventory_management_web.main.WicketApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link DatabaseAccessAdminListForm}
 */
public class DatabaseAccessAdminListFormTest 
{
	/**
	 * Before running the tests, an application is required
	 */
	@Before
	public void setUp()
	{
		new WicketTester(new WicketApplication());
	}

	
	/**
	 *	Tests the {@link DatabaseAccessAdminListForm#DatabaseAccessAdminForm(String)} constructor 
	 */
	@Test
	public void testDatabaseAccessAdminForm() 
	{
		try
		{
			new DatabaseAccessAdminListForm("databaseAccessAdminForm", new PageParameters());
		}
		catch (Exception ex)
		{
			 Assert.fail("Expected no exception in DatabaseAccessAdminListForm(), but got: " + ex.getMessage());
		}
	}
}
