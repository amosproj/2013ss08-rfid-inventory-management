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
 * This class defines the structure (the columns) of the database of the device meta data
 */
@DatabaseTable(tableName = "metaDeviceTable")
public class MetaDeviceDatabaseRecord implements Serializable
{
	private static final long serialVersionUID = -5864531965004415818L;

	// for QueryBuilder to be able to find the columns
	/** The Constant CATEGORY_COLUMN. */
	public static final String CATEGORY_COLUMN = "category";

	/** The Constant TYPE_COLUMN. */
	public static final String TYPE_COLUMN = "type";

	/** The Constant ID_COLUMN. */
	public static final String ID_COLUMN = "id";

	/** The Constant PART_NUMBER_COLUMN. */
	public static final String PART_NUMBER_COLUMN = "part_number";

	/** The Constant MANUFACTURER_COLUMN. */
	public static final String MANUFACTURER_COLUMN = "manufacturer";

	/** The Constant PLATFORM_COLUMN. */
	public static final String PLATFORM_COLUMN = "platform";

	// Columns definition:
	@DatabaseField(columnName = ID_COLUMN, canBeNull = false, id = true)	// primary key // TODO auto generate the id!
	private int id;

	@DatabaseField(columnName = TYPE_COLUMN)
	private String type;

	@DatabaseField(columnName = CATEGORY_COLUMN)
	private String category;

	@DatabaseField(columnName = PART_NUMBER_COLUMN, canBeNull = false)
	private String part_number;

	@DatabaseField(columnName = MANUFACTURER_COLUMN)
	private String manufacturer;

	@DatabaseField(columnName = PLATFORM_COLUMN)
	private String platform;

	/**
	 * Default constructor (empty)
	 * this is called, when getting records from the database
	 */
	public MetaDeviceDatabaseRecord()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	/**
	 * Constructor for creating a new database record.
	 *
	 * @param category 		the category
	 * @param type 			the type
	 * @param part_number 	the part_number
	 * @param manufacturer the manufacturer
	 * @param platform 	the platform
	 * @throws IllegalArgumentException if the part number is null
	 * @throws IllegalStateException if the next free id is -1
	 */
	public MetaDeviceDatabaseRecord(String category, String type, String part_number, String manufacturer, String platform) throws IllegalArgumentException, IllegalStateException
	{
		if (part_number == null)
		{
			throw new IllegalArgumentException("MetaDeviceDatabaseRecord(): part number is null");
		}
		
		// see, if this part_number is already existing. if so, keep the id. else gerneate it
		MetaDeviceDatabaseHandler metaDeviceDatabaseHandler = MetaDeviceDatabaseHandler.getInstance();
		MetaDeviceDatabaseRecord oldRecord = metaDeviceDatabaseHandler.getRecordByPartNumber(part_number);
		
		if (oldRecord != null)
		{
			this.id = oldRecord.getId();
		}
		else 
		{
			this.id = metaDeviceDatabaseHandler.getNextFreeId();
			if (this.id == -1)
			{
				throw new IllegalStateException("MetaDeviceDatabaseHandler(): next free id is -1");
			}
		}
			
		this.category = category;
		this.type = type;
		this.part_number = part_number;
		this.manufacturer = manufacturer;
		this.platform = platform;
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
	 * Gets the id.
	 * @return the id
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public String getType()
	{
		if (this.type == null)
		{
			return "";
		}
		
		return this.type;
	}

	/**
	 * Gets the category.
	 * @return the category
	 */
	public String getCategory()
	{
		if (this.category == null)
		{
			return "";
		}
		
		return this.category;
	}

	/**
	 * Gets the manufacturer.
	 * @return the manufacturer
	 */
	public String getManufacturer()
	{
		if (this.manufacturer == null)
		{
			return "";
		}
		
		return this.manufacturer;
	}

	/**
	 * Gets the platform.
	 * @return the platform
	 */
	public String getPlatform()
	{
		if (this.platform == null)
		{
			return "";
		}
		
		return this.platform;
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
		return this.id == ((MetaDeviceDatabaseRecord) other).id;
	}
}
