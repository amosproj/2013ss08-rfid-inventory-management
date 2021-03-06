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

package org.amos2013.rfid_inventory_management_web.main;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite for running all the junit tests created
 */
@RunWith(Suite.class)
@SuiteClasses(
		{	// add all test classes here
			org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandlerTest.class,
			org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseRecordTest.class,
			org.amos2013.rfid_inventory_management_web.database.MetaDeviceDatabaseHandlerTest.class,
			org.amos2013.rfid_inventory_management_web.database.MetaDeviceDatabaseRecordTest.class,
			org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseHandlerTest.class,
			org.amos2013.rfid_inventory_management_web.database.EmployeeDatabaseRecordTest.class,
			org.amos2013.rfid_inventory_management_web.database.LocationDatabaseHandlerTest.class,
			org.amos2013.rfid_inventory_management_web.database.LocationDatabaseRecordTest.class,
			org.amos2013.rfid_inventory_management_web.database.RoomDatabaseHandlerTest.class,
			org.amos2013.rfid_inventory_management_web.database.RoomDatabaseRecordTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.AdminEmployeePageTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.AdminEmployeeEditPageTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.AdminRoomPageTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.AdminRoomEditPageTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.AdminLocationPageTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.AdminLocationEditPageTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.AdminListPageTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.DatabaseAccessAdminListFormTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.DatabaseAccessListFormTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.DatabaseAccessRoomFormTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.DatabaseAccessEmployeeFormTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.DatabaseAccessLocationFormTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.ListPageTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.SearchPageAdminListTest.class,
			org.amos2013.rfid_inventory_management_web.webparts.SearchPageListTest.class,
		})

/**
 * constructor for the test suite, should be empty
 */
public class AllTests
{
	/* do nothing here */
}
