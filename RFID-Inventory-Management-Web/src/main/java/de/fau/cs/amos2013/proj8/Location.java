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

package de.fau.cs.amos2013.proj8;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Example location object that is persisted to disk by the DAO and other example classes.
 */
@DatabaseTable(tableName = "location")
public class Location {

	// for QueryBuilder to be able to find the fields
	public static final String ROOM_FIELD_NAME = "room";
	public static final String OWNER_FIELD_NAME = "owner";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(columnName = ROOM_FIELD_NAME, canBeNull = false)
	private String room;

	@DatabaseField(columnName = OWNER_FIELD_NAME)
	private String owner;

	Location() {
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	public Location(String room) {
		this.room = room;
	}

	public Location(String room, String owner) {
		this.room = room;
		this.owner = owner;
	}

	public int getId() {
		return id;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != getClass()) {
			return false;
		}
		return room.equals(((Location) other).room);
	}
}
