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
public class DatabaseHandler
{
	private final static String DATABASE_URL = "jdbc:postgresql://faui2o2j.informatik.uni-erlangen.de:5432/ss13-proj8";

	// the class ConfigLoader.java which loads the db-password, is not committed to git
	private final static String DATABASE_PW = ConfigLoader.getDatabasePassword();

	// Database Access Object, is a handler for reading and writing
	private static Dao<DatabaseRecord, Integer> databaseHandlerDao;

	
	/**
	 * Creates a database if there is no one existing
	 * @param connectionSource required for setting up db
	 * @throws Exception
	 */
	private static void setupDatabase(ConnectionSource connectionSource) throws Exception
	{
		databaseHandlerDao = DaoManager.createDao(connectionSource, DatabaseRecord.class);
	
		// if the database is not existing create a new one
		if (databaseHandlerDao.isTableExists() == false)
		{
			try
			{
				// createTableIfNotExists is not working: always tries to create a new database
				// -> if block around
				TableUtils.createTableIfNotExists(connectionSource, DatabaseRecord.class);
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Writes the given strings to the database
	 * @param rfid_id int to set
	 * @param room string to set
	 * @param owner string to set
	 * @throws SQLException when database connection close() fails
	 * @throws IllegalArgumentException when room or owner is null
	 * @throws Exception when database setup fails
	 */
	public static void writeRecordToDatabase(int rfid_id, String room, String owner) throws SQLException, IllegalArgumentException, Exception
	{
		if (room == null || owner == null)
		{
			throw new IllegalArgumentException("At least one of the arguments for creating a DatabaseRecord is null.");
		}
		
		ConnectionSource connectionSource = null;
		try
		{
			// create data-source for the database
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// create a database, if non existing
			setupDatabase(connectionSource);

			// write the given Strings
			DatabaseRecord record = new DatabaseRecord(rfid_id, room, owner);

			// writes to the database: create if new id, or update if existing
			databaseHandlerDao.createOrUpdate(record);
		} 
		finally
		{
			// destroy the data source which should close underlying connections
			if (connectionSource != null)
			{
				connectionSource.close();
			}
		}
	}

	
	/**
	 * Searches and returns the record by its id 
	 * @param rfidId int id to search for
	 * @return a string containing all records, null when nothing was found
	 * @throws SQLException when error occurs with the database
	 * @throws IllegalStateException when null or more than one record is returned from the database
	 */
	public static DatabaseRecord getRecordFromDatabaseById(int rfidId) throws IllegalStateException, SQLException
	{
		ConnectionSource connectionSource = null;
		List<DatabaseRecord> databaseRecords = null;
		DatabaseRecord resultRecord = null; 
		
		try
		{
			// create our data-source for the database (url, user, pwd)
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			databaseHandlerDao = DaoManager.createDao(connectionSource, DatabaseRecord.class);
			
			// read database records
			databaseRecords = databaseHandlerDao.queryForEq("rfid_id", rfidId);
		} 
		catch (SQLException e)
		{
			return null;
		} 
		finally
		{
			// always destroy the data source which should close underlying connections
			if (connectionSource != null)
			{
				connectionSource.close();
			}
		}
		
		// if empty database, return empty
		if (databaseRecords == null)
		{
			throw new IllegalStateException("Error while serarching for RFID ID " + rfidId + ": no list was returned!");
		}
		
		// if more than one entry, a error has occured, because the id is unique!
		if (databaseRecords.size() > 1)
		{
			throw new IllegalStateException("Error while serarching for RFID ID " + rfidId + ": more than one result was found!");
		}
		
		// if not empty, return first (and hopefully only result) else return null
		if (databaseRecords.isEmpty() == false)
		{
			resultRecord = databaseRecords.get(0);
		}
		
		return resultRecord;
	}
	
	
	/**
	 * Loops through one table of the database and reads the content 
	 * @return a string containing all records
	 * @throws SQLException when database connection close fails
	 */
	public static List<DatabaseRecord> getRecordsFromDatabase() throws SQLException  // connection.close() can throw
	{
		ConnectionSource connectionSource = null;
		List<DatabaseRecord> databaseRecords = null;
		ArrayList<DatabaseRecord> resultList = new ArrayList<DatabaseRecord>(); 
		DatabaseRecord recordString = null;
		
		try
		{
			// create our data-source for the database (url, user, pwd)
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			databaseHandlerDao = DaoManager.createDao(connectionSource, DatabaseRecord.class);

			// read database records
			databaseRecords = databaseHandlerDao.queryForAll();
		} 
		catch (Exception e)
		{
			resultList.add(recordString);
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

		// if empty database, add "empty" and return
		if (databaseRecords == null)
		{
			resultList.add(recordString);
			return resultList;
		}

		// add string for record to result list
		for (DatabaseRecord record : databaseRecords)
		{
			resultList.add(record);
		}
		
		return resultList;
	}
	
	
	/**
	 * Deletes a given row of the table.
	 *
	 * @param record the record
	 * @throws SQLException when database connection close() fails
	 * @throws IllegalArgumentException when null is passed as argument
	 */
	public static void deleteRecordFromDatabase(DatabaseRecord record) throws SQLException, IllegalArgumentException // connection.close() can throw
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
			databaseHandlerDao = DaoManager.createDao(connectionSource, DatabaseRecord.class);

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
