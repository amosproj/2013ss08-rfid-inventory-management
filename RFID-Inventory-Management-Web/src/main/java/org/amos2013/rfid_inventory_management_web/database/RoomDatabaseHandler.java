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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * This class is used, to access the database
 */
public class RoomDatabaseHandler
{
	private final static String DATABASE_URL = "jdbc:postgresql://faui2o2j.informatik.uni-erlangen.de:5432/ss13-proj8";

	// the class ConfigLoader.java which loads the db-password, is not committed to git
	private final static String DATABASE_PW = ConfigLoader.getDatabasePassword();

	// Database Access Object, is a handler for reading and writing
	private static Dao<RoomDatabaseRecord, String> databaseHandlerDao;


	/**
	 * Creates a database if there is no one existing
	 * @param connectionSource required for setting up db
	 * @throws Exception
	 */
	private static void setupDatabase(ConnectionSource connectionSource) throws Exception
	{
		databaseHandlerDao = DaoManager.createDao(connectionSource, RoomDatabaseRecord.class);

		// if the database is not existing create a new one
		if (databaseHandlerDao.isTableExists() == false)
		{
			try
			{
				// createTableIfNotExists is not working: always tries to create a new database
				// -> if block around
				TableUtils.createTableIfNotExists(connectionSource, RoomDatabaseRecord.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loops through one table of the database and reads the content
	 * @param	location	a string which filters the results after their location
	 * @return a list of Strings with all room numbers
	 * @throws SQLException when database connection close fails
	 */
	public static List<String> getRecordsFromDatabaseByLocation(String location) throws SQLException  // connection.close() can throw
	{
		ConnectionSource connectionSource = null;
		List<RoomDatabaseRecord> databaseRecords = null;
		ArrayList<String> resultList = new ArrayList<String>();

		try
		{
			// create our data-source for the database (url, user, pwd)
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			databaseHandlerDao = DaoManager.createDao(connectionSource, RoomDatabaseRecord.class);

			// read database records
			databaseRecords = databaseHandlerDao.queryForAll();
		}
		finally
		{
			// always destroy the data source which should close underlying connections
			if (connectionSource != null)
			{
				connectionSource.close();
			}
		}

		// add string for record to result list
		for (RoomDatabaseRecord record : databaseRecords)
		{
			if (record.getLocation().equals(location))
			{
				resultList.add(record.getName());
			}
		}

		return resultList;
	}
	
	/**
	 * Loops through one table of the database and reads the content 
	 * @return a string containing all records of roomTable
	 * @throws SQLException when database connection close fails
	 */
//	public static List<String> getRecordsFromDatabase() throws SQLException  // connection.close() can throw
	public static List<RoomDatabaseRecord> getRecordsFromDatabase() throws SQLException
	{
		ConnectionSource connectionSource = null;
		List<RoomDatabaseRecord> databaseRecords = null;
//		ArrayList<String> resultList = new ArrayList<String>(); 
		ArrayList<RoomDatabaseRecord> resultList = new ArrayList<RoomDatabaseRecord>(); 
		
		try
		{
			// create our data-source for the database (url, user, pwd)
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			databaseHandlerDao = DaoManager.createDao(connectionSource, RoomDatabaseRecord.class);

			// read database records
			databaseRecords = databaseHandlerDao.queryForAll();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return resultList;
		} 
		finally
		{
			// always destroy the data source which should close underlying connections
			if (connectionSource != null)
			{
				connectionSource.close();
			}
		}

		// if empty database, and return empty list
		if (databaseRecords == null)
		{
			return resultList;
		}

		// connect with meta data
//		for (RoomDatabaseRecord record : databaseRecords)
//		{
//			resultList.add(record.getName());
//			resultList.add(record.getLocation());
//		}
		
		//return resultList;
		return databaseRecords; 
		
	}
	
	/**
	 * Deletes a given row of the RoomTable.
	 *
	 * @param record the record
	 * @throws SQLException when database connection close() fails
	 * @throws IllegalArgumentException when null is passed as argument
	 */
	public static void deleteRecordFromDatabase(RoomDatabaseRecord record) throws SQLException, IllegalArgumentException // connection.close() can throw
	{
		ConnectionSource connectionSource = null;
		
		if (record == null)
		{
			throw new IllegalArgumentException("Parameter for deleteRecordFromDatabase is null.");
		}
		
		try
		{
			// create our data-source for the database (url, user, pwd)
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			databaseHandlerDao = DaoManager.createDao(connectionSource, RoomDatabaseRecord.class);

			// delete given database record
			databaseHandlerDao.delete(record);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		} 
		finally
		{
			// always destroy the data source which should close underlying connections
			if (connectionSource != null)
			{
				connectionSource.close();
			}
		}
	}
}

