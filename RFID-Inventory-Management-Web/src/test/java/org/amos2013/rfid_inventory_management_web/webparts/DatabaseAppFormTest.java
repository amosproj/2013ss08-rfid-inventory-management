package org.amos2013.rfid_inventory_management_web.webparts;

import junit.framework.Assert;
import org.amos2013.rfid_inventory_management_web.main.WicketApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for DatabaseAppForm
 */
public class DatabaseAppFormTest 
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
	 * tests the onSubmit() function
	 */
	@Test
	public void testOnSubmit() 
	{
		DatabaseAppForm databaseAppForm = new DatabaseAppForm("databaseAppForm");
		
		try
		{
			databaseAppForm.onSubmit();
		}
		catch (Exception ex)
		{
			Assert.fail("Expected no exception in onSubmit(), but got: " + ex.getMessage());
		}
	}

	/**
	 *	Tests the DataBaseAppForm constructor
	 */
	@Test
	public void testDatabaseAppForm() 
	{
		try
		{
			new DatabaseAppForm("databaseAppForm");
		}
		catch (Exception ex)
		{
			 Assert.fail("Expected no exception in DatabaseAccessForm(), but got: " + ex.getMessage());
		}
	}

}
