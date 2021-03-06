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

// TODO: Auto-generated Javadoc
/**
 * This class defines the structure (the columns) of the database.
 */
@DatabaseTable(tableName = "uniqueDeviceTable")
public class DeviceDatabaseRecord implements Serializable
{
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3718871095899325629L;
	
	/** The meta device database record. */
	private MetaDeviceDatabaseRecord metaDeviceDatabaseRecord = null;

	// for QueryBuilder to be able to find the columns
	/** The Constant ROOM_COLUMN. */
	public static final String ROOM_COLUMN = "room";
	
	/** The Constant EMPLOYEE_COLUMN. */
	public static final String EMPLOYEE_COLUMN = "employee";
	
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
	
	/** The Constant STATUS_COLUMN. */
	public static final String STATUS_COLUMN = "status";
	
	/** The Constant ANNOTATION_COLUMN. */
	public static final String ANNOTATION_COLUMN = "annotation";
	
	/** The Constant ID_COLUMN. */
	public static final String ID_COLUMN = "id";
	
	/** The Constant RECEIVED_FROM_COLUMN. */
	public static final String RECEIVED_FROM_COLUMN = "received_from";
	
	/** The Constant RETURNED_TO_COLUMN. */
	public static final String RETURNED_TO_COLUMN = "returned_to";
	
	/** The Constant ESN_COLUMN. */
	public static final String ESN_COLUMN = "esn";

	
	// Columns definition:
	/** The rfid_id. */
	@DatabaseField(columnName = RFID_ID_COLUMN, canBeNull = false, id = true)	// primary key
	private String rfid_id;

	/** The room. */
	@DatabaseField(columnName = ROOM_COLUMN)
	private String room;

	/** The employee. */
	@DatabaseField(columnName = EMPLOYEE_COLUMN)
	private String employee;
	
	/** The part_number. */
	@DatabaseField(columnName = PART_NUMBER_COLUMN, canBeNull = false)
	private String part_number;
	
	/** The serial_number. */
	@DatabaseField(columnName = SERIAL_NUMBER_COLUMN)
	private String serial_number;
	
	/** The inventory_number. */
	@DatabaseField(columnName = INVENTORY_NUMBER_COLUMN)
	private String inventory_number;
	
	/** The owner. */
	@DatabaseField(columnName = OWNER_COLUMN)
	private String owner;
	
	/** The status. */
	@DatabaseField(columnName = STATUS_COLUMN)
	private String status;
	
	/** The annotation. */
	@DatabaseField(columnName = ANNOTATION_COLUMN)
	private String annotation;
	
	/** The id. */
	@DatabaseField(columnName = ID_COLUMN)
	private String id;
	
	/** The received_from. */
	@DatabaseField(columnName = RECEIVED_FROM_COLUMN)
	private String received_from;
	
	/** The returned_to. */
	@DatabaseField(columnName = RETURNED_TO_COLUMN)
	private String returned_to;
	
	/** The esn. */
	@DatabaseField(columnName = ESN_COLUMN)
	private String esn;
		
	/**
	 * Default constructor (empty).
	 */
	public DeviceDatabaseRecord()
	{
		// gets the corresponding meta data record from the other table in order to be able to access them 
		// in this class via the getters and setters
		setMetaDeviceDatabaseRecordViaPartNumber();
	}


	/**
	 * Instantiates a new device database record.
	 *
	 * @param rfid_id the rfid_id
	 * @param room the room
	 * @param employee the employee
	 * @param part_number the part_number
	 * @param serial_number the serial_number
	 * @param inventory_number the inventory_number
	 * @param owner the owner
	 * @param status the status
	 * @param annotation the annotation
	 * @param id the id
	 * @param received_from the received_from
	 * @param returned_to the returned_to
	 * @param esn the esn
	 */
	public DeviceDatabaseRecord(String rfid_id, String room, String employee, String part_number, String serial_number, String inventory_number, String owner, String status, String annotation, String id, String received_from, String returned_to, String esn)
	{
		this.rfid_id = rfid_id;
		this.room = room;
		this.employee = employee;
		this.part_number = part_number;
		this.serial_number = serial_number;
		this.inventory_number = inventory_number;
		this.owner = owner;
		this.status = status;
		this.annotation = annotation;
		this.id = id;
		this.received_from = received_from;
		this.returned_to = returned_to;
		this.esn = esn;
		
		// gets the corresponding meta data record from the other table in order to be able to access them 
		// in this class via the getters and setters
		setMetaDeviceDatabaseRecordViaPartNumber();
	}
	
	
	/**
	 * Sets the meta device database record from the local list.
	 *
	 * @throws IllegalStateException the illegal state exception
	 */
	public void setMetaDeviceDatabaseRecordViaPartNumber() throws IllegalStateException
	{
		MetaDeviceDatabaseHandler metaDeviceDatabaseHandler = MetaDeviceDatabaseHandler.getInstance();
		metaDeviceDatabaseRecord = metaDeviceDatabaseHandler.getRecordByPartNumber(this.part_number);
	}
	
	/**
	 * Sets the meta device data base record.
	 *
	 * @param metaDeviceDatabaseRecord the new meta device database record
	 */
	public void setMetaDeviceDataBaseRecord(MetaDeviceDatabaseRecord metaDeviceDatabaseRecord)
	{
		this.metaDeviceDatabaseRecord = metaDeviceDatabaseRecord;
	}
	
	/**
	 * Gets the meta device database record.
	 *
	 * @return the meta device database record
	 */
	public MetaDeviceDatabaseRecord getMetaDeviceDatabaseRecord()
	{
		return this.metaDeviceDatabaseRecord;
	}

	/**
	 * Gets the RFID Id.
	 * @return the rFID id
	 */
	public String getRFIDId()
	{
		return this.rfid_id;
	}

	/**
	 * Gets the room.
	 * @return the room
	 */
	public String getRoom()
	{
		if (this.room == null)
		{
			return "";
		}
		
		return this.room;
	}
	
	/**
	 * Gets the current employee of the device.
	 * @return the employee
	 */
	public String getEmployee()
	{
		if (this.employee == null)
		{
			return "";
		}
		
		return this.employee;
	}
	
	/**
	 * Gets the part number.
	 * @return the part number
	 */
	public String getPartNumber()
	{
		if (this.part_number == null)
		{
			return "";
		}
		
		return this.part_number;
	}
	
	/**
	 * Gets the type.
	 * @return the type
	 */
	public String getType()
	{
		if (this.metaDeviceDatabaseRecord == null)
		{
			return "";
		} 

		return this.metaDeviceDatabaseRecord.getType();
	}
	
	/**
	 * Gets the category.
	 * @return the category
	 */
	public String getCategory()
	{
		if (this.metaDeviceDatabaseRecord == null)
		{
			return "";
		} 

		return this.metaDeviceDatabaseRecord.getCategory();
	}
	
	/**
	 * Gets the manufacturer.
	 * @return the manufacturer
	 */
	public String getManufacturer()
	{
		if (this.metaDeviceDatabaseRecord == null)
		{
			return "";
		} 

		return this.metaDeviceDatabaseRecord.getManufacturer();
	}
	
	/**
	 * Gets the platform.
	 * @return the platform
	 */
	public String getPlatform()
	{
		if (this.metaDeviceDatabaseRecord == null)
		{
			return "";
		} 
		
		return this.metaDeviceDatabaseRecord.getPlatform();
	}
	
	/**
	 * Gets the comment.
	 * @return the comment
	 */
	public String getComment()
	{
		if (this.metaDeviceDatabaseRecord == null)
		{
			return "";
		} 
		
		return this.metaDeviceDatabaseRecord.getComment();
	}
	
	/**
	 * Gets the serial number.
	 * @return the serial number
	 */
	public String getSerialNumber()
	{
		if (this.serial_number == null)
		{
			return "";
		}
			
		return this.serial_number;
	}

	/**
	 * Gets the inventory number.
	 * @return the inventory number
	 */
	public String getInventoryNumber()
	{
		if (this.inventory_number == null)
		{
			return "";
		}
		
		return this.inventory_number;
	}

	/**
	 * Gets the owner.
	 * @return the owner
	 */
	public String getOwner()
	{
		if (this.owner == null)
		{
			return "";
		}
		
		return this.owner;
	}
	
	/**
	 * Gets the status.
	 * @return the status
	 */
	public String getStatus()
	{
		if (this.status == null)
		{
			return "";
		}
		
		return this.status;
	}
	
	/**
	 * Gets the annotation.
	 * @return the annotation
	 */
	public String getAnnotation()
	{
		if (this.annotation == null)
		{
			return "";
		}
		
		return this.annotation;
	}
	
	/**
	 * Gets the id.
	 * @return the id
	 */
	public String getId()
	{
		if (this.id == null)
		{
			return "";
		}
		
		return this.id;
	}

	/**
	 * Gets the received_from.
	 * @return the received_from
	 */
	public String getReceivedFrom()
	{
		if (this.received_from == null)
		{
			return "";
		}
		
		return this.received_from;
	}
	
	/**
	 * Gets the returned_to.
	 * @return the returned_to
	 */
	public String getReturnedTo()
	{
		if (this.returned_to == null)
		{
			return "";
		}
		
		return this.returned_to;
	}
	
	/**
	 * Gets the esn.
	 * @return the esn
	 */
	public String getEsn()
	{
		if (this.esn == null)
		{
			return "";
		}
		
		return this.esn;
	}
	
	/**
	 * Sets the employee.
	 *
	 * @param employee the new employee
	 */
	public void setEmployee(String employee)
	{
		this.employee = employee;
	}
	
	/**
	 * Sets the room.
	 *
	 * @param room the new room
	 */
	public void setRoom(String room)
	{
		this.room = room;
	}
	
	/**
	 * Compares two Objects.
	 *
	 * @param other the other
	 * @return true if equal, false else
	 */
	@Override
	public boolean equals(Object other)
	{
		if (other == null || other.getClass() != getClass())
		{
			return false;
		}
		return this.rfid_id == ((DeviceDatabaseRecord) other).rfid_id;
	}
	
	/**
	 * Gets a device record comparator, which compares two rfid ids.
	 *
	 * @return the device record comparator
	 */
	public static Comparator<DeviceDatabaseRecord> getDeviceRecordComparator()
	{
		return new Comparator<DeviceDatabaseRecord>()
		{
			public int compare(DeviceDatabaseRecord first, DeviceDatabaseRecord second)
			{
				String firstString = new String(first.getRFIDId());
				String secondString = new String(second.getRFIDId());
				
				return firstString.compareTo(secondString);
			}
		};
	}
}
