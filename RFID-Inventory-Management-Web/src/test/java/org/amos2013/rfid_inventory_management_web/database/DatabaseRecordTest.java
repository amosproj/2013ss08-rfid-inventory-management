/**
 * 
 */
package org.amos2013.rfid_inventory_management_web.database;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author David
 *
 */
public class DatabaseRecordTest
{
	private String room = "42.42";
	private String owner = "Musterfrau";
	private int id = 42;

	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.DatabaseRecord#DatabaseRecord(int, java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testDatabaseRecordIntStringString()
	{
		DatabaseRecord tester = new DatabaseRecord(id, room, owner);
		
		assertEquals(room, tester.getRoom());
		assertEquals(owner, tester.getOwner());
		assertEquals(id, tester.getRFIDId());
	}

}
