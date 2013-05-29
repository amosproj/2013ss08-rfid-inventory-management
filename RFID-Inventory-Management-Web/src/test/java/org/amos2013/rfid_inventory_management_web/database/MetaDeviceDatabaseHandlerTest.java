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

package org.amos2013.rfid_inventory_management_web.database;

import static org.junit.Assert.assertNull;
import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit test for {@link MetaDeviceDatabaseHandler} class
 */
public class MetaDeviceDatabaseHandlerTest
{
	private MetaDeviceDatabaseHandler metaDeviceDatabaseHandler = MetaDeviceDatabaseHandler.getInstance();
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.MetaDeviceDatabaseHandler#getDatabaseRecordList()}.
	 */
	@Test
	public final void testGetDatabaseRecordList()
	{
		try
		{
			metaDeviceDatabaseHandler.getDatabaseRecordList();			
		}
		catch (Exception e)
		{
			Assert.fail("No exception excpected, but caught: " + e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.MetaDeviceDatabaseHandler#getRecordByPartNumber(String)}.
	 * @throws IllegalStateException is not expected
	 */
	@Test
	public final void testGetRecordByPartNumber() throws IllegalStateException
	{
		MetaDeviceDatabaseRecord resultRecord = null;
		
		resultRecord = metaDeviceDatabaseHandler.getRecordByPartNumber("");
		
		assertNull(resultRecord);
	}
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.MetaDeviceDatabaseHandler#updateRecordInDatabase(MetaDeviceDatabaseRecord)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public final void testUpdateRecordInDatabase() throws Exception
	{
		try
		{
			metaDeviceDatabaseHandler.updateRecordInDatabase(null);			
		}
		catch (Exception ex)
		{
			Assert.fail("No exception excpected, but caught: " + ex.getMessage());
		}
	}
}
