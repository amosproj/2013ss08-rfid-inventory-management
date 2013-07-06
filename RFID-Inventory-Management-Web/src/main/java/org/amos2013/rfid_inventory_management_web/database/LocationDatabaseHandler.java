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

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * This class is used, to access the database.
 */
public class LocationDatabaseHandler implements Serializable
{
	private static final long serialVersionUID = -9076746906440733343L;

	/** The Constant DATABASE_URL. */
	private final static String DATABASE_URL = "jdbc:postgresql://faui2o2j.informatik.uni-erlangen.de:5432/ss13-proj8";

	// the class ConfigLoader.java which loads the db-password, is not committed to git
	/** The Constant DATABASE_PW. */
	private final static String DATABASE_PW = ConfigLoader.getDatabasePassword();

	// Database Access Object, is a handler for reading and writing
	/** The database handler dao. */
	private static Dao<LocationDatabaseRecord, Integer> databaseHandlerDao;

	/**
	 * Creates a database if there is no one existing.
	 *
	 * @param connectionSource required for setting up db
	 * @throws Exception the exception
	 */
	private static void setupDatabase(ConnectionSource connectionSource) throws Exception
	{
		databaseHandlerDao = DaoManager.createDao(connectionSource, LocationDatabaseRecord.class);

		// if the database is not existing create a new one
		if (databaseHandlerDao.isTableExists() == false)
		{
			try
			{
				// createTableIfNotExists is not working: always tries to create a new database
				// -> if block around
				TableUtils.createTableIfNotExists(connectionSource, LocationDatabaseRecord.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Loops through one table of the database and reads the content.
	 *
	 * @param location a string which filters the results after their location
	 * @return a list of Strings with all locations
	 * @throws SQLException when database connection close fails
	 */
	public static LocationDatabaseRecord getRecordsFromDatabaseByLocation(String location) throws SQLException  // connection.close() can throw
	{
		ConnectionSource connectionSource = null;
		List<LocationDatabaseRecord> databaseRecords = null;
		
		try
		{
			// create our data-source for the database (url, user, pwd)
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			databaseHandlerDao = DaoManager.createDao(connectionSource, LocationDatabaseRecord.class);
			
			// read database records
			databaseRecords = databaseHandlerDao.queryForEq("location",location);
		}
		finally
		{
			// always destroy the data source which should close underlying connections
			if (connectionSource != null)
			{
				connectionSource.close();
			}
		}
		
		if (databaseRecords.isEmpty())
		{
			return null;
		}
		
		return databaseRecords.get(0);
	}

	/**
	 * Gets the record from database by id.
	 *
	 * @param recordID the record id
	 * @return the record from database by id
	 * @throws SQLException the sQL exception
	 */
	public static LocationDatabaseRecord getRecordFromDatabaseByID(int recordID) throws SQLException  // connection.close() can throw
	{
		ConnectionSource connectionSource = null;
		List<LocationDatabaseRecord> databaseRecords = null;

		try
		{
			// create our data-source for the database (url, user, pwd)
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			databaseHandlerDao = DaoManager.createDao(connectionSource, LocationDatabaseRecord.class);

			// read database records
			databaseRecords = databaseHandlerDao.queryForEq("id", recordID);
		}
		finally
		{
			// always destroy the data source which should close underlying connections
			if (connectionSource != null)
			{
				connectionSource.close();
			}
		}

		if (databaseRecords.isEmpty())
		{
			return null;
		}
		
		return databaseRecords.get(0);
	}
	
	/**
	 * Loops through one table of the database and reads the content.
	 *
	 * @return a string containing all records of locationTable
	 * @throws SQLException when database connection close fails
	 */
	public static List<LocationDatabaseRecord> getRecordsFromDatabase() throws SQLException
	{
		ConnectionSource connectionSource = null;
		List<LocationDatabaseRecord> databaseRecords = null;
		
		try
		{
			// create our data-source for the database (url, user, pwd)
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			databaseHandlerDao = DaoManager.createDao(connectionSource, LocationDatabaseRecord.class);

			// read database records
			databaseRecords = databaseHandlerDao.queryForAll();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		} 
		finally
		{
			// always destroy the data source which should close underlying connections
			if (connectionSource != null)
			{
				connectionSource.close();
			}
		}
		
		// sort the list by location
		Collections.sort(databaseRecords, LocationDatabaseRecord.getLocationRecordComparator());
		
		return databaseRecords;
	}
	
	/**
	 * Deletes a given row of the locationTable.
	 *
	 * @param record the record
	 * @throws SQLException when database connection close() fails
	 * @throws IllegalArgumentException when null is passed as argument
	 */
	public static void deleteRecordFromDatabase(LocationDatabaseRecord record) throws SQLException, IllegalArgumentException // connection.close() can throw
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
			databaseHandlerDao = DaoManager.createDao(connectionSource, LocationDatabaseRecord.class);

			// delete given database record
			databaseHandlerDao.delete(record);
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new SQLException("Error with the database. Please check your internet connection.");
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
	
	/**
	 * Gets the next free id.
	 * Runs through the database
	 * @return the next free id
	 */
	private static int getNextFreeId()
	{
		int freeId = -1;
		
		try
		{
			databaseHandlerDao = DaoManager.createDao(new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW), LocationDatabaseRecord.class);
			
			for (int i = 0; i < Integer.MAX_VALUE; ++i)
			{
				if (!databaseHandlerDao.idExists(i))
				{
					freeId = i;
					break;
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return freeId;
	}
	
	/**
	 * Writes the given strings to the database.
	 *
	 * @param record the target record
	 * @throws SQLException when database connection close() fails
	 * @throws IllegalArgumentException when location is null
	 * @throws IllegalStateException if the next free id is -1
	 * @throws Exception when database setup fails
	 */
	public static void updateRecordInDatabase(LocationDatabaseRecord record) throws SQLException, IllegalArgumentException, IllegalStateException, Exception
	{
		ConnectionSource connectionSource = null;
		List<LocationDatabaseRecord> databaseRecords = null;

		if (record == null)
		{
			throw new IllegalArgumentException("The LocationDatabaseRecord is null");
		}
		
		try
		{
			// create data-source for the database
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// create a database, if non existing
			setupDatabase(connectionSource);
			
			//read all the records in the table in order to check check whether this input record has already in the table.
			databaseRecords = databaseHandlerDao.queryForAll();
			
			//firstly check whether this input record, including "location", has already in the table.
			for (LocationDatabaseRecord locationRecord : databaseRecords)
			{
				if (locationRecord.getLocation().equals(record.getLocation()))
				{
					// already existing -> do nothing
					connectionSource.close();
					throw new IllegalArgumentException("The location is already existing");
				}
			}
			
			// if this is the add command, set id
			if (record.getID() == null)
			{
				// generate id
				int nextFreeId = getNextFreeId();
				
				if (nextFreeId == -1)
				{
					throw new IllegalStateException("The next free id is -1.");
				}
				
				record.setID(nextFreeId);
			}
				
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
}
