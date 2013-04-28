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

package de.fau.cs.amos2013.proj8;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


public class TestDatabase {


	private final static String DATABASE_URL = "jdbc:postgresql://faui2o2j.informatik.uni-erlangen.de:5432/ss13-proj8";
	
	// add the database password here, but always erase it before making a commit !!!
	private final static String DATABASE_PW = "";

	private Dao<Location, Integer> locationDao;
	
	public static void TestDatabase(String room, String owner) throws Exception {
		new TestDatabase().Write(room, owner);
	}

	public void Write(String room, String owner) throws Exception {
		ConnectionSource connectionSource = null;
		try {
			// create our data-source for the database
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			setupDatabase(connectionSource);

			// write the given Strings
			String thisRoom = room;
			Location location = new Location(thisRoom);

			// persist the account object to the database
			locationDao.create(location);
			int id = location.getId();
			verifyDb(id, location);

			location.setOwner(owner);
			// update the database after changing the object
			locationDao.update(location);
			verifyDb(id, location);
		} finally {
			// destroy the data source which should close underlying connections
			if (connectionSource != null) {
				connectionSource.close();
			}
		}
	}

	/**
	 * Setup our database and DAOs
	 */
	private void setupDatabase(ConnectionSource connectionSource) throws Exception {

		locationDao = DaoManager.createDao(connectionSource, Location.class);

		// if you need to create the table
		try {
			TableUtils.createTable(connectionSource, Location.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Verify that the account stored in the database was the same as the expected object.
	 */
	private void verifyDb(int id, Location expected) throws SQLException, Exception {
		// make sure we can read it back
		Location location2 = locationDao.queryForId(id);
		if (location2 == null) {
			throw new Exception("Should have found id '" + id + "' in the database");
		}
		verifyLocation(expected, location2);
	}

	/**
	 * Verify that the account is the same as expected.
	 */
	private static void verifyLocation(Location expected, Location location2) {
		assertEquals("expected room does not equal location room", expected, location2);
		assertEquals("expected owner does not equal location room", expected.getOwner(), location2.getOwner());
	}

}
