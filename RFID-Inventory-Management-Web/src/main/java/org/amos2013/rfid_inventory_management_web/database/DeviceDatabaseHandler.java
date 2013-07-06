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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * This class is used, to access the database
 */
public class DeviceDatabaseHandler implements Serializable
{
	private static final long serialVersionUID = 160180659812508857L;

	private final static String DATABASE_URL = "jdbc:postgresql://faui2o2j.informatik.uni-erlangen.de:5432/ss13-proj8";

	// the class ConfigLoader.java which loads the db-password, is not committed to git
	private final static String DATABASE_PW = ConfigLoader.getDatabasePassword();

	// Database Access Object, is a handler for reading and writing
	private Dao<DeviceDatabaseRecord, Integer> databaseHandlerDao;

	private static DeviceDatabaseHandler instance = null;
	
	private List<DeviceDatabaseRecord> databaseRecordList = null;
	
	private MetaDeviceDatabaseHandler metaDeviceDatabaseHandler = MetaDeviceDatabaseHandler.getInstance();
	
	
	/**
	 * Instantiates a new device database handler. Cannot be called from outside of this class
	 */
	private DeviceDatabaseHandler() 
	{ 
		try
		{
			databaseRecordList = getDatabaseRecordList();
		}
		catch (SQLException e)
		{
			databaseRecordList = new ArrayList<DeviceDatabaseRecord>();
		}
	}
	
	
    /**
     * Gets the single instance of DeviceDatabaseHandler.
     *
     * @return single instance of DeviceDatabaseHandler
     */
    public static DeviceDatabaseHandler getInstance() 
    {
        if (instance == null) 
        {
            instance = new DeviceDatabaseHandler();
        }
        
        return instance;
    }
	
	/**
	 * Creates a database if there is no one existing
	 * @param connectionSource required for setting up db
	 * @throws Exception
	 */
	private void setupDatabase(ConnectionSource connectionSource) throws Exception
	{
		databaseHandlerDao = DaoManager.createDao(connectionSource, DeviceDatabaseRecord.class);
	
		// if the database is not existing create a new one
		if (databaseHandlerDao.isTableExists() == false)
		{
			try
			{
				// createTableIfNotExists is not working: always tries to create a new database
				// -> if block around
				TableUtils.createTableIfNotExists(connectionSource, DeviceDatabaseRecord.class);
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Writes the given strings to the database.
	 *
	 * @param record the record
	 * @throws SQLException when database connection close() fails
	 * @throws IllegalArgumentException when room or owner is null
	 * @throws Exception when database setup fails
	 */
	public void updateCompleteRecordInDatabase(DeviceDatabaseRecord record) throws SQLException, IllegalArgumentException, Exception
	{
		if (record == null)
		{
			throw new IllegalArgumentException("The DeviceDatabaseRecord is null");
		}
		
		ConnectionSource connectionSource = null;
		try
		{
			// create data-source for the database
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// create a database, if non existing
			setupDatabase(connectionSource);
			// writes to the database: create if new id, or update if existing
			databaseHandlerDao.createOrUpdate(record);
			
			// also update meta data (MetaDeviceDatabaseRecord)
			metaDeviceDatabaseHandler.updateRecordInDatabase(record.getMetaDeviceDatabaseRecord());
			// and update the local list
			metaDeviceDatabaseHandler.getDatabaseRecordList();
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
	 * Updates the record in the database if it is existing.
	 *
	 * @param record the record
	 * @return true, if successful, false if not existing
	 * @throws SQLException the sQL exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws Exception the exception
	 */
	public boolean updateRecordFromAppInDatabase(DeviceDatabaseRecord record) throws SQLException, IllegalArgumentException, Exception
	{
		if (record == null)
		{
			return false;
		}
		
		int result;
		ConnectionSource connectionSource = null;
		try
		{
			// create data-source for the database
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// create a database, if non existing
			setupDatabase(connectionSource);

			// check if in the database
			DeviceDatabaseRecord recordInDatabase = getRecordFromDatabaseById(record.getRFIDId());
			if (recordInDatabase == null)	// if not in the database, don't continue with updating, as it would return false 
			{
				return false;
			}
						
			// writes to the database: update if existing
			result = databaseHandlerDao.update(record);
			
			// also update meta data (MetaDeviceDatabaseRecord)
			metaDeviceDatabaseHandler.updateRecordInDatabase(record.getMetaDeviceDatabaseRecord());
			// and update the local list
			metaDeviceDatabaseHandler.getDatabaseRecordList();
		}
		finally
		{
			// destroy the data source which should close underlying connections
			if (connectionSource != null)
			{
				connectionSource.close();
			}
		}
		
		if (result != 1)
		{
			return false;
		}
		else 
		{
			return true;
		}
	}
	
	/**
	 * Searches and returns all records that match the search string in the specified column.
	 *
	 * @param search_input the string fragment that is searched for
	 * @param search_option the column of the DB-table where the method should search in
	 * @return a list of the type DeviceDatabaseRecord containing all records, an empty list when nothing was found
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public List<DeviceDatabaseRecord> getRecordsByPartialStringAndColumn(String search_input, String search_option) throws IllegalArgumentException
	{
		List<DeviceDatabaseRecord> resultDatabaseRecords = new ArrayList<DeviceDatabaseRecord>();
		
		if (search_input == null || search_option == null)
		{
			throw new IllegalArgumentException("Search string or option are null");
		}
		
		// loop through all records
		
		for (DeviceDatabaseRecord record : databaseRecordList)
		{
			if (search_option.equals("all_columns"))
			{
				if (record.getRFIDId().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getRoom().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getEmployee().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getPartNumber().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getType().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getCategory().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getManufacturer().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getPlatform().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getComment().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getSerialNumber().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getInventoryNumber().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getOwner().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getStatus().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getAnnotation().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getId().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getReceivedFrom().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getReturnedTo().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
				else if (record.getEsn().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("rfid_id"))
			{
				if (record.getRFIDId().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("room"))
			{
				if (record.getRoom().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("employee"))
			{
				if (record.getEmployee().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("part_number"))
			{
				if (record.getPartNumber().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("type"))
			{
				if (record.getType().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("category"))
			{
				if (record.getCategory().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("manufacturer"))
			{
				if (record.getManufacturer().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("platform"))
			{
				if (record.getPlatform().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("comment"))
			{
				if (record.getComment().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("serial_number"))
			{
				if (record.getSerialNumber().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("inventory_number"))
			{
				if (record.getInventoryNumber().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("owner"))
			{
				if (record.getOwner().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("status"))
			{
				if (record.getStatus().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("annotation"))
			{
				if (record.getAnnotation().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("id"))
			{
				if (record.getId().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("received_from"))
			{
				if (record.getReceivedFrom().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("returned_to"))
			{
				if (record.getReturnedTo().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else if (search_option.equals("esn"))
			{
				if (record.getEsn().contains(search_input) == true)
				{
					resultDatabaseRecords.add(record);
				}
			}
			else 
			{
				throw new IllegalArgumentException("Invalid search option: " + search_option);
			}
		}
		
		return resultDatabaseRecords;
	}
	
	/**
	 * Gets the record with the rfid id from the database 
	 * @return the device record
	 * @throws SQLException when database connection close fails
	 */
	public DeviceDatabaseRecord getRecordFromDatabaseById(String rfidID) throws SQLException  // connection.close() can throw
	{
		ConnectionSource connectionSource = null;
		List<DeviceDatabaseRecord> databaseRecords = null;
		
		try
		{
			// create our data-source for the database (url, user, pwd)
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			databaseHandlerDao = DaoManager.createDao(connectionSource, DeviceDatabaseRecord.class);
			
			// read database records
			databaseRecords = databaseHandlerDao.queryForEq("rfid_id", rfidID);
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
		
		// if empty database, and return empty list
		if (databaseRecords == null || databaseRecords.isEmpty() || databaseRecords.size() > 1)
		{
			return null;
		}
		
		//get meta device data
		databaseRecords.get(0).setMetaDeviceDatabaseRecordViaPartNumber();
		
		return databaseRecords.get(0);
	}
	
	/**
	 * Loops through one table of the database and reads the content 
	 * @return a string containing all records
	 * @throws SQLException when database connection close fails
	 */
	private List<DeviceDatabaseRecord> getRecordsFromDatabase() throws SQLException  // connection.close() can throw
	{
		ConnectionSource connectionSource = null;
		List<DeviceDatabaseRecord> databaseRecords = null;
		ArrayList<DeviceDatabaseRecord> resultList = new ArrayList<DeviceDatabaseRecord>(); 
		
		try
		{
			// create our data-source for the database (url, user, pwd)
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			databaseHandlerDao = DaoManager.createDao(connectionSource, DeviceDatabaseRecord.class);

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

		// if empty database, and return empty list
		if (databaseRecords == null)
		{
			return resultList;
		}
		
		// connect with meta data
		for (DeviceDatabaseRecord record : databaseRecords)
		{
			record.setMetaDeviceDatabaseRecordViaPartNumber();
		}
		
		return databaseRecords;
	}
	
	
	/**
	 * Deletes a given row of the table.
	 *
	 * @param record the record
	 * @throws SQLException when database connection close() fails
	 * @throws IllegalArgumentException when null is passed as argument
	 */
	public void deleteRecordFromDatabase(DeviceDatabaseRecord record) throws SQLException, IllegalArgumentException // connection.close() can throw
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
			databaseHandlerDao = DaoManager.createDao(connectionSource, DeviceDatabaseRecord.class);

			// delete given database record
			databaseHandlerDao.delete(record);
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
	}

	/**
	 * Pulls and returns the database record list.
	 *
	 * @return the database record list
	 * @throws SQLException when there is no connection
	 */
	public List<DeviceDatabaseRecord> getDatabaseRecordList() throws SQLException
	{
		databaseRecordList = getRecordsFromDatabase();
		
		Collections.sort(databaseRecordList, DeviceDatabaseRecord.getDeviceRecordComparator());
		
		return databaseRecordList;
	}
}
