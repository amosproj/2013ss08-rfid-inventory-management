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
@DatabaseTable(tableName = "inventoryTable")
public class DatabaseRecord implements Serializable
{
	private static final long serialVersionUID = 3718871095899325629L;

	// for QueryBuilder to be able to find the columns
	public static final String ROOM_COLUMN = "room";
	public static final String OWNER_COLUMN = "owner";
	public static final String RFID_ID_COLUMN = "rfid_id";

	
	// Columns definition:
	@DatabaseField(columnName = RFID_ID_COLUMN, canBeNull = false, id = true)	// primary key
	private int rfid_id;

	@DatabaseField(columnName = ROOM_COLUMN, canBeNull = false)
	private String room;

	@DatabaseField(columnName = OWNER_COLUMN)
	private String owner;

	/**
	 * Default constructor (empty)
	 *
	 */
	public DatabaseRecord()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	/**
	 * Constructor for creating a new database record
	 */
	public DatabaseRecord(int rfid_id, String room, String owner)
	{
		this.rfid_id = rfid_id;
		this.room = room;
		this.owner = owner;
	}
	
	/**
	 * Gets the RFID Id
	 */
	public int getRFIDId()
	{
		return rfid_id;
	}

	/**
	 * Sets the RFID Id
	 * @param rfid_id Id to be set
	 */
	public void setRFIDId(int rfid_id)
	{
		this.rfid_id = rfid_id;
	}

	/**
	 * Gets the room
	 */
	public String getRoom()
	{
		return room;
	}

	/**
	 * Sets the room
	 * @param room Room to be set
	 */
	public void setRoom(String room)
	{
		this.room = room;
	}

	/**
	 * Gets the current owner of the device
	 */
	public String getOwner()
	{
		return owner;
	}

	/**
	 * Sets the current owner of the device
	 * @param owner Owner to be set
	 */
	public void setOwner(String owner)
	{
		this.owner = owner;
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
		return room.equals(((DatabaseRecord) other).room);
	}
	
	
	/**
	 * Returns a string, containing all fields separated by comma
	 * @return String representing the record
	 */
	@Override
	public String toString()
	{
		return rfid_id + ", " + room + ", " + owner;
		
	}
}
