package org.amos2013.rfid_inventory_management_web.database;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Junit test for class {@link RoomDatabaseHandler}
 */
public class RoomDatabaseHandlerTest
{
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

}
