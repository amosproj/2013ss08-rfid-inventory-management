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
 * Junit test for class {@link EmployeeDatabaseHandler}
 */
public class EmployeeDatabaseHandlerTest
{
	/** The expected exception. */
	@Rule 
	public ExpectedException exception = ExpectedException.none();
	
	/**
	 * Test for {@link EmployeeDatabaseHandler#getRecordsFromDatabaseByLocation(String)}
	 */
	@Test
	public void testGetRecordsFromDatabaseByLocation()
	{
		List<String> resultList = null;
		try
		{
			resultList = EmployeeDatabaseHandler.getRecordsFromDatabaseByLocation("empty");
		}
		catch (Exception ex)
		{
			Assert.fail("Expected no exception, but got: " + ex.getMessage());
		}
	
		assertNotNull(resultList);
	}

	/**
	 * Test for {@link EmployeeDatabaseHandler#updateRecordInDatabase(EmployeeDatabaseRecord)}.
	 *
	 * @throws IllegalArgumentException the illegal argument exception is not expected
	 * @throws IllegalStateException the illegal state exception is not expected
	 * @throws SQLException the sQL exception is not expected
	 * @throws Exception the exception is not expected
	 */
	@Test
	public void testUpdateRecordInDatabase() throws IllegalArgumentException, IllegalStateException, SQLException, Exception
	{
		exception.expect(IllegalArgumentException.class);		
		EmployeeDatabaseHandler.updateRecordInDatabase(null);
	}
	
	/**
	 * Test for {@link EmployeeDatabaseHandler#deleteRecordFromDatabase(EmployeeDatabaseRecord)}.
	 *
	 * @throws IllegalArgumentException the illegal argument exception is excpected
	 * @throws SQLException the sQL exception is not expected
	 */
	@Test
	public void testDeleteRecordFromDatabase() throws IllegalArgumentException, SQLException
	{
		exception.expect(IllegalArgumentException.class);		
		EmployeeDatabaseHandler.deleteRecordFromDatabase(null);
	}
	
	/**
	 * Test for {@link EmployeeDatabaseHandler#getRecordsFromDatabase()}
	 */
	@Test
	public void testGetRecordsFromDatabase()
	{
		List<EmployeeDatabaseRecord> resultList = null;
		try
		{
			resultList = EmployeeDatabaseHandler.getRecordsFromDatabase();
		}
		catch (SQLException e)
		{
			Assert.fail("Didn't expect an exception, but got: " + e.getMessage());
		}
		
		assertNotNull(resultList);
	}
	
	/**
	 * Test for {@link EmployeeDatabaseHandler#getRecordFromDatabaseByID(int)}
	 */
	@Test
	public void testGetRecordFromDatabaseByID()
	{
		EmployeeDatabaseRecord resultRecord = null;
		try
		{
			resultRecord = EmployeeDatabaseHandler.getRecordFromDatabaseByID(-111112);
		}
		catch (SQLException e)
		{
			Assert.fail("Didn't expect an exception, but got: " + e.getMessage());
		}
		
		assertNull(resultRecord);
	}
}
