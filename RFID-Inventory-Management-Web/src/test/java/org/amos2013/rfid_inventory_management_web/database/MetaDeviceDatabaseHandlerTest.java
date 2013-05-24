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
 * Unit test for {@link MetaDeviceDatabaseHandler} class
 */
public class MetaDeviceDatabaseHandlerTest
{

	/** The expected exception. */
	@Rule
	public ExpectedException exception = ExpectedException.none();


	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.MetaDeviceDatabaseHandler#writeRecordToDatabase(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws Exception not testing for this one
	 */
	@Test
	public final void testWriteRecordToDatabase() throws Exception
	{
		exception.expect(IllegalArgumentException.class);	// asserts that a IllegalArgumentException is thrown
		MetaDeviceDatabaseHandler.writeRecordToDatabase(null, null, null, null, null);
	}



	/**
	 * Test method for {@link org.amos2013.rfid_inventory_management_web.database.MetaDeviceDatabaseHandler#getRecordFromDatabaseByPartNumber(java.lang.String)}.
	 * @throws SQLException can be thrown
	 * @throws IllegalStateException is expected
	 */
	@Test
	public final void testgetRecordFromDatabaseByPartNumber() throws IllegalStateException, SQLException
	{
		MetaDeviceDatabaseRecord resultList = null;

		exception.expect(IllegalStateException.class);
		resultList = MetaDeviceDatabaseHandler.getRecordFromDatabaseByPartNumber("-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

		assertNull(resultList);
	}

}
