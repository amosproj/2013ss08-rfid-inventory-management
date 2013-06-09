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
import java.util.Comparator;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * This class defines the structure (the columns) of the database
 */
@DatabaseTable(tableName = "locationTable")
public class LocationDatabaseRecord implements Serializable
{
	private static final long serialVersionUID = -3987729244126922395L;

	// for QueryBuilder to be able to find the columns
	/** The Constant LOCATION_COLUMN. */
	public static final String LOCATION_COLUMN = "location";
	
	/** The Constant ID_COLUMN. */
	public static final String ID_COLUMN = "id";
	
	
	// Columns definition:
	@DatabaseField(columnName = LOCATION_COLUMN, canBeNull = false)
	private String location;
	
	@DatabaseField(columnName = ID_COLUMN, canBeNull = false, id = true)	// primary key
	private Integer id;

	/**
	 * Default constructor (empty)
	 *
	 */
	public LocationDatabaseRecord()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	/**
	 * Constructor for creating a new database record.
	 *
	 * @param id the id
	 * @param location the location
	 */
	public LocationDatabaseRecord(Integer id, String location)
	{
		this.id = id;
		this.location = location;
	}
	
	/**
	 * Gets the Location.
	 * @return the location
	 */
	public String getLocation()
	{
		return location;
	}

	/**
	 * Sets the location
	 * @param location Location to be set
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	/**
	 * Gets the ID.
	 * @return the ID
	 */
	public Integer getID()
	{
		return id;
	}

	/**
	 * Sets the ID
	 * @param id ID to be set
	 */
	public void setID(Integer id)
	{
		this.id = id;
	}

	/**
	 * Compares two Objects
	 * @return true if equal, false else
	 */
	@Override
	public boolean equals(Object other)
	{
		if (other == null || other.getClass() != getClass())
		{
			return false;
		}
		return id == (((LocationDatabaseRecord) other).id);
	}
	
	
	/**
	 * Returns a string, containing all fields separated by comma
	 * @return String representing the record
	 */
	@Override
	public String toString()
	{
		return location;
		
	}
	
	/**
	 * Gets a location record comparator, which compares two locations first by location.
	 *
	 * @return the location record comparator
	 */
	public static Comparator<LocationDatabaseRecord> getLocationRecordComparator()
	{
		return new Comparator<LocationDatabaseRecord>()
		{
			public int compare(LocationDatabaseRecord first, LocationDatabaseRecord second)
			{
				int result = first.getLocation().compareTo(second.getLocation());
				return result;
			}
		};
	}
	
}
