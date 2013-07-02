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

import org.junit.Test;

/**
 * Tests the {@link DeviceDatabaseRecord} class
 */
public class DeviceDatabaseRecordTest
{
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseRecord#DeviceDatabaseRecord(String, 
	 * 	String, String, String, String, String, String, String, String, String, String, String, String)}.
	 */
	@Test
	public final void testDeviceDatabaseRecordConstructor()
	{
		String room = "42.42";
		String employee = "Musterfrau";
		String rfid = "01234567890";
		String partNumber = "1234";
		String serialNumber = "04a";
		String inventoryNumber = "56565";
		String owner = "sebl";
		String status = "online";
		String annotation = "no comment";
		String id = "1";
		String receivedFrom = "john";
		String returnedTo = "me";
		String esn = "1-5-3-6-3-3";
		
		DeviceDatabaseRecord testRecord = new DeviceDatabaseRecord(rfid, room, employee, partNumber, serialNumber, inventoryNumber, 
				owner, status, annotation, id, receivedFrom, returnedTo, esn);
		
		assertEquals(room, testRecord.getRoom());
		assertEquals(employee, testRecord.getEmployee());
		assertEquals(rfid, testRecord.getRFIDId());
		assertEquals(partNumber, testRecord.getPartNumber());
		assertEquals(serialNumber, testRecord.getSerialNumber());
		assertEquals(owner, testRecord.getOwner());
		assertEquals(status, testRecord.getStatus());
		assertEquals(annotation, testRecord.getAnnotation());
		assertEquals(id, testRecord.getId());
		assertEquals(receivedFrom, testRecord.getReceivedFrom());
		assertEquals(returnedTo, testRecord.getReturnedTo());
		assertEquals(esn, testRecord.getEsn());
		
		assertEquals("", testRecord.getType());
		assertEquals("", testRecord.getCategory());
		assertEquals("", testRecord.getManufacturer());
		assertEquals("", testRecord.getPlatform());
	}
	
	@Test
	public final void testSetters()
	{
		String room = "42.42";
		String employee = "Musterfrau";
		String rfid = "01234567890";
		String partNumber = "1234";
		String serialNumber = "04a";
		String inventoryNumber = "56565";
		String owner = "sebl";
		String status = "online";
		String annotation = "no comment";
		String id = "1";
		String receivedFrom = "john";
		String returnedTo = "me";
		String esn = "1-5-3-6-3-3";
		
		DeviceDatabaseRecord testRecord = new DeviceDatabaseRecord(rfid, room, employee, partNumber, serialNumber, inventoryNumber, 
				owner, status, annotation, id, receivedFrom, returnedTo, esn);
		
		String newEmployee = "Hans";
		String newRoom = "Halle 1";
		
		testRecord.setEmployee(newEmployee);
		testRecord.setRoom(newRoom);
		
		assertEquals(newEmployee, testRecord.getEmployee());
		assertEquals(newRoom, testRecord.getRoom());
	}
}
