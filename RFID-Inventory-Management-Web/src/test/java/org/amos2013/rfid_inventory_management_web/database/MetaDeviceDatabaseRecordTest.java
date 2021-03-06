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

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests the {@link MetaDeviceDatabaseRecord} class
 */
public class MetaDeviceDatabaseRecordTest
{
	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.MetaDeviceDatabaseRecord#MetaDeviceDatabaseRecord(String, String, String, String, String)}.
	 */
	@Test
	public final void testMetaDeviceDatabaseRecordConstructor()
	{
		String category = "test";
		String type = "test";
		String part_number = "test";
		String manufacturer = "test";
		String platform = "test";
		String comment = "test";

		MetaDeviceDatabaseRecord testRecord = null;
		try
		{
			testRecord = new MetaDeviceDatabaseRecord(category, type, part_number, manufacturer, platform,comment);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Assert.fail();
		}

		assertEquals(category, testRecord.getCategory());
		assertEquals(type, testRecord.getType());
		assertEquals(part_number, testRecord.getPartNumber());
		assertEquals(manufacturer, testRecord.getManufacturer());
		assertEquals(platform, testRecord.getPlatform());

	}

}
