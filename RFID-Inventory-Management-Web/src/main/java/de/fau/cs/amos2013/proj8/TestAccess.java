package de.fau.cs.amos2013.proj8;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


public class TestAccess {

	private final static String DATABASE_URL = "jdbc:postgresql://faui2o2j.informatik.uni-erlangen.de:5432/ss13-proj8";
	
	// add the database password here, but always erase it before making a commit !!!
	private final static String DATABASE_PW = "";

		public static String Result() throws Exception {
		ConnectionSource connectionSource = null;
		try {
			// create our data-source for the database
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			Dao<Location, Integer> locationDao = DaoManager.createDao(connectionSource, Location.class);

			List<Location> locations = locationDao.queryForAll();
			
			String result = "";
			for (Location location2 : locations) {
				String room = location2.getRoom();
				String owner = location2.getOwner();
				result += room + " " + owner + " | ";
			}
			if (result == "") {
				result = "empty";
			}
						
			return result;
		} finally {
			// destroy the data source which should close underlying connections
			if (connectionSource != null) {
				connectionSource.close();
			}
		}
	}
	
}
