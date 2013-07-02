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

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for {@link DeviceDatabaseHandler} class
 */
public class DeviceDatabaseHandlerTest
{
	/** The expected exception. */
	@Rule 
	public ExpectedException exception = ExpectedException.none();
	
	private DeviceDatabaseHandler deviceDatabaseHandler = DeviceDatabaseHandler.getInstance();
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler#deleteRecordFromDatabase()}.
	 * @throws IllegalArgumentException excepting this exception here 
	 */
	@Test
	public final void testDeleteRecordFromDatabase() throws IllegalArgumentException
	{
		exception.expect(IllegalArgumentException.class);
		try
		{
			deviceDatabaseHandler.deleteRecordFromDatabase(null);
		}
		catch (SQLException ex)
		{
			Assert.fail("Expected no exception, but got: " + ex.getMessage());	
		}
	}
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler#getDatabaseRecordList()}.
	 */
	@Test
	public final void testGetDatabaseRecordList()
	{	
		List<DeviceDatabaseRecord> resultList = null;
		try
		{
			resultList = deviceDatabaseHandler.getDatabaseRecordList();
		}
		catch (Exception ex)
		{
			Assert.fail("Expected no exception, but got: " + ex.getMessage());
		}
		
		assertNotNull(resultList);
	}
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler#getRecordFromDatabaseById(String)}.
	 */
	@Test
	public final void getRecordFromDatabaseById()
	{	
		DeviceDatabaseRecord resultRecord = null;
		try
		{
			resultRecord = deviceDatabaseHandler.getRecordFromDatabaseById("-123456");
		}
		catch (Exception ex)
		{
			Assert.fail("Expected no exception, but got: " + ex.getMessage());
		}
		
		assertNull(resultRecord);
	}
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler#getRecordsByPartialStringAndColumn(String, String)}.
	 * @throws IllegalStateException is expected
	 */
	@Test
	public final void testGetRecordsByPartialStringAndColumn() throws IllegalStateException
	{	
		List<DeviceDatabaseRecord> resultList = null;

		exception.expect(IllegalArgumentException.class);
		deviceDatabaseHandler.getRecordsByPartialStringAndColumn("search", "option123456");
	    
		try
	    {
	    	deviceDatabaseHandler.getRecordsByPartialStringAndColumn("search", "employee");
	    }
	    catch (IllegalArgumentException ex)
	    {
	        Assert.fail("Expected no exception, but got: " + ex.getMessage());
	    }
	    
		assertNotNull(resultList);
	}
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler#updateCompleteRecordInDatabase(DeviceDatabaseRecord)}.
	 * @throws Exception not testing for this one
	 */
	@Test
	public final void testUpdateCompleteRecordInDatabase() throws Exception
	{
		exception.expect(IllegalArgumentException.class);	// asserts that a IllegalArgumentException is thrown
		deviceDatabaseHandler.updateCompleteRecordInDatabase(null);
	}
	
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler#updateRecordFromAppInDatabase(String, String, String)}.
	 * @throws Exception not testing for this one
	 */
	@Test
	public final void testUpdateRecordFromAppInDatabase() throws Exception
	{
		boolean updated = deviceDatabaseHandler.updateRecordFromAppInDatabase(null);
		assertEquals(false, updated);
	}
}
