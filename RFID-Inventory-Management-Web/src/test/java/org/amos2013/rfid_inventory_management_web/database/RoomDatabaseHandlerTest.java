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

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Junit test for class {@link RoomDatabaseHandler}
 */
public class RoomDatabaseHandlerTest
{
	/** The expected exception. */
	@Rule 
	public ExpectedException exception = ExpectedException.none();
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler#getRecordsFromDatabaseByLocation(String)}.
	 */
	@Test
	public void testGetRecordsFromDatabaseByLocation()
	{
		List<String> resultList = null;
		try
		{
			resultList = RoomDatabaseHandler.getRecordsFromDatabaseByLocation("empty");
		}
		catch (Exception ex)
		{
			Assert.fail("Expected no exception, but got: " + ex.getMessage());
		}
		
		assertNotNull(resultList);
	}
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler#getRecordFromDatabaseByID(int)}.
	 */
	@Test
	public void testGetRecordFromDatabaseByID()
	{
		RoomDatabaseRecord resultRecord = null;
		try
		{
			resultRecord = RoomDatabaseHandler.getRecordFromDatabaseByID(-1111111);
		}
		catch (Exception ex)
		{
			Assert.fail("Expected no exception, but got: " + ex.getMessage());
		}
		
		assertNull(resultRecord);
	}
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler#getRecordsFromDatabase()}.
	 */
	@Test
	public void testGetRecordsFromDatabase()
	{
		List<RoomDatabaseRecord> resultList = null;
		try
		{
			resultList = RoomDatabaseHandler.getRecordsFromDatabase();
		}
		catch (Exception ex)
		{
			Assert.fail("Expected no exception, but got: " + ex.getMessage());
		}
		
		assertNotNull(resultList);
	}
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler#deleteRecordFromDatabase(RoomDatabaseRecord)}.
	 * @throws SQLException will not be expected
	 * @throws IllegalArgumentException will be expected
	 */
	@Test
	public void testDeleteRecordFromDatabase() throws IllegalArgumentException, SQLException
	{
		exception.expect(IllegalArgumentException.class);
		RoomDatabaseHandler.deleteRecordFromDatabase(null);
	}

	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandler#updateRecordInDatabase(RoomDatabaseRecord)}.
	 * @throws Exception will not be expected
	 * @throws SQLException will not be expected
	 * @throws IllegalArgumentException will be expected
	 */
	@Test
	public void testUpdateRecordInDatabase() throws IllegalArgumentException, SQLException, Exception
	{
		exception.expect(IllegalArgumentException.class);
		RoomDatabaseHandler.updateRecordInDatabase(null);
	}
}
