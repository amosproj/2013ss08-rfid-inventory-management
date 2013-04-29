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

import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;


public class TestAccess {

	private final static String DATABASE_URL = "jdbc:postgresql://faui2o2j.informatik.uni-erlangen.de:5432/ss13-proj8";

	// the class ConfigLoader.java which loads the db-password, is not committed to git
	private final static String DATABASE_PW = ConfigLoader.Result();

	/**
	 * Loops through one table of the database and writes the content into a string
	 */
	public static String Result() throws Exception {
		ConnectionSource connectionSource = null;
		try {
			// create our data-source for the database
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			Dao<Location, Integer> locationDao = DaoManager.createDao(connectionSource, Location.class);

			List<Location> locations = locationDao.queryForAll();

			String result = "";
			for (Location location2 : locations) {
				String room = location2.getRoom();
				String owner = location2.getOwner();
				result += room + " | " + owner + ";";
			}
			if (result == "") {
				result = "empty";
			}

			return result;
		} finally {
			// destroy the data source which should close underlying connections
			if (connectionSource != null) {
				connectionSource.close();
			}
		}
	}

}
