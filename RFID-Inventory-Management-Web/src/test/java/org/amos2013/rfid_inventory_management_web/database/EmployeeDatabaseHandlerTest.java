package org.amos2013.rfid_inventory_management_web.database;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Junit test for class {@link EmployeeDatabaseHandler}
 */
public class EmployeeDatabaseHandlerTest
{
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

}
