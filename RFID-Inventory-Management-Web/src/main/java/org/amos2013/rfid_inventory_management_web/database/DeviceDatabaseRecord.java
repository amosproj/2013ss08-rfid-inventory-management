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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.amos2013.rfid_inventory_management_web.database.MetaDeviceDatabaseHandler;

/**
 * This class defines the structure (the columns) of the database
 */
@DatabaseTable(tableName = "deviceTable")
public class DeviceDatabaseRecord implements Serializable
{
	private static final long serialVersionUID = 3718871095899325629L;
	
	MetaDeviceDatabaseRecord record = null;

	// for QueryBuilder to be able to find the columns
	/** The Constant ROOM_COLUMN. */
	public static final String ROOM_COLUMN = "room";
	
	/** The Constant HOLDER_COLUMN. */
	public static final String HOLDER_COLUMN = "holder";
	
	/** The Constant RFID_ID_COLUMN. */
	public static final String RFID_ID_COLUMN = "rfid_id";
	
	/** The Constant PART_NUMBER_COLUMN. */
	public static final String PART_NUMBER_COLUMN = "part_number";
	
	/** The Constant SERIAL_NUMBER_COLUMN. */
	public static final String SERIAL_NUMBER_COLUMN = "serial_number";
	
	/** The Constant INVENTORY_NUMBER_COLUMN. */
	public static final String INVENTORY_NUMBER_COLUMN = "inventory_number";
	
	/** The Constant OWNER_COLUMN. */
	public static final String OWNER_COLUMN = "owner";
	
	/** The Constant COMMENT_COLUMN. */
	public static final String COMMENT_COLUMN = "comment";

	
	// Columns definition:
	@DatabaseField(columnName = RFID_ID_COLUMN, canBeNull = false, id = true)	// primary key
	private int rfid_id;

	@DatabaseField(columnName = ROOM_COLUMN, canBeNull = false)
	private String room;

	@DatabaseField(columnName = HOLDER_COLUMN)
	private String holder;
	
	@DatabaseField(columnName = PART_NUMBER_COLUMN)
	private String part_number;
	
	@DatabaseField(columnName = SERIAL_NUMBER_COLUMN)
	private String serial_number;
	
	@DatabaseField(columnName = INVENTORY_NUMBER_COLUMN)
	private String inventory_number;
	
	@DatabaseField(columnName = OWNER_COLUMN)
	private String owner;
	
	@DatabaseField(columnName = COMMENT_COLUMN)
	private String comment;
	
	/**
	 * Default constructor (empty)
	 *
	 */
	public DeviceDatabaseRecord()
	{
		// gets the corresponding meta data record from the other table in order to be able to access them in this class via the getters and setters
		try
		{
			record = MetaDeviceDatabaseHandler.getRecordFromDatabaseByPartNumber(part_number);
		} catch (IllegalStateException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Constructor for creating a new database record.
	 *
	 * @param rfid_id the rfid_id
	 * @param room the room
	 * @param holder the holder
	 */
	public DeviceDatabaseRecord(int rfid_id, String room, String holder)
	{
		this.rfid_id = rfid_id;
		this.room = room;
		this.holder = holder;
		
		// gets the corresponding meta data record from the other table in order to be able to access them in this class via the getters and setters
		try
		{
			record = MetaDeviceDatabaseHandler.getRecordFromDatabaseByPartNumber(part_number);
		} catch (IllegalStateException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the type.
	 * @return the type
	 */
	public String getType()
	{
		if (record == null)
		{
			return null;
		} 
		else return record.getType();
	}

	/**
	 * Sets the type
	 * @param type Type to be set
	 */
	public void setType(String type)
	{
		record.setType(type);
	}
	
	/**
	 * Gets the category.
	 * @return the category
	 */
	public String getCategory()
	{
		if (record == null)
		{
			return null;
		} 
		else return record.getCategory();
	}

	/**
	 * Sets the category
	 * @param category Category to be set
	 */
	public void setCategory(String category)
	{
		record.setCategory(category);
	}
	
	/**
	 * Gets the manufacturer.
	 * @return the manufacturer
	 */
	public String getManufacturer()
	{
		if (record == null)
		{
			return null;
		} 
		else return record.getManufacturer();
	}

	/**
	 * Sets the manufacturer
	 * @param manufacturer Manufacturer to be set
	 */
	public void setManufacturer(String manufacturer)
	{
		record.setManufacturer(manufacturer);
	}
	
	/**
	 * Gets the platform.
	 * @return the platform
	 */
	public String getPlatform()
	{
		if (record == null)
		{
			return null;
		} 
		else return record.getPlatform();
	}

	/**
	 * Sets the platform
	 * @param platform Platform to be set
	 */
	public void setPlatform(String platform)
	{
		record.setPlatform(platform);
	}
	
	/**
	 * Gets the part number.
	 * @return the part number
	 */
	public String getPart_number()
	{
		return part_number;
	}

	/**
	 * Sets the part number
	 * @param part_number Part number to be set
	 */
	public void setPart_number(String part_number)
	{
		this.part_number = part_number;
	}

	/**
	 * Gets the serial number.
	 * @return the serial number
	 */
	public String getSerial_number()
	{
		return serial_number;
	}

	/**
	 * Sets the serial number
	 * @param serial_number Serial number to be set
	 */
	public void setSerial_number(String serial_number)
	{
		this.serial_number = serial_number;
	}

	/**
	 * Gets the inventory number.
	 * @return the inventory number
	 */
	public String getInventory_number()
	{
		return inventory_number;
	}

	/**
	 * Sets the inventory number
	 * @param inventory_number Inventory number to be set
	 */
	public void setInventory_number(String inventory_number)
	{
		this.inventory_number = inventory_number;
	}

	/**
	 * Gets the owner.
	 * @return the owner
	 */
	public String getOwner()
	{
		return owner;
	}

	/**
	 * Sets the owner
	 * @param owner Owner to be set
	 */
	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	/**
	 * Gets the comment.
	 * @return the comment
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * Sets the comment
	 * @param comment Comment to be set
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * Gets the RFID Id.
	 * @return the rFID id
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
	 * Gets the room.
	 * @return the room
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
	 * Gets the current holder of the device.
	 * @return the holder
	 */
	public String getHolder()
	{
		return holder;
	}

	/**
	 * Sets the current holder of the device
	 * @param holder Holder to be set
	 */
	public void setHolder(String holder)
	{
		this.holder = holder;
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
		return room.equals(((DeviceDatabaseRecord) other).room);
	}
}
