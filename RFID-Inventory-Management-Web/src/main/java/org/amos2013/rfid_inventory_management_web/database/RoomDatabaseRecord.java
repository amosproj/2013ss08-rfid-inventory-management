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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * This class defines the structure (the columns) of the database
 */
@DatabaseTable(tableName = "roomTable")
public class RoomDatabaseRecord implements Serializable
{
	private static final long serialVersionUID = 3718871095899325629L;

	// for QueryBuilder to be able to find the columns
	/** The Constant NAME_COLUMN. */
	public static final String NAME_COLUMN = "name";
	
	/** The Constant LOCATION_COLUMN. */
	public static final String LOCATION_COLUMN = "location";
	
	
	// Columns definition:
	@DatabaseField(columnName = NAME_COLUMN, canBeNull = false, id = true)	// primary key
	private String name;

	@DatabaseField(columnName = LOCATION_COLUMN, canBeNull = false)
	private String location;

	/**
	 * Default constructor (empty)
	 *
	 */
	public RoomDatabaseRecord()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	/**
	 * Constructor for creating a new database record.
	 *
	 * @param name the name
	 * @param location the location
	 */
	public RoomDatabaseRecord(String name, String location)
	{
		this.name = name;
		this.location = location;
	}
	
	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name
	 * @param name Name to be set
	 */
	public void setName(String name)
	{
		this.name = name;
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
		return name.equals(((RoomDatabaseRecord) other).name);
	}
	
	
	/**
	 * Returns a string, containing all fields separated by comma
	 * @return String representing the record
	 */
	@Override
	public String toString()
	{
		return name + ", " + location;
		
	}
}
