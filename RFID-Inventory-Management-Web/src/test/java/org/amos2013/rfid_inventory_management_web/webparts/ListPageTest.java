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

package org.amos2013.rfid_inventory_management_web.webparts;

import org.amos2013.rfid_inventory_management_web.main.WicketApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class ListPageTest
{
	private WicketTester wicketTester;

	/**
	 * create a WicketApplication for testing
	 */
	@Before
	public void setUp()
	{
		wicketTester = new WicketTester(new WicketApplication());
	}

	/**
	 * tests if the homepage renders
	 */
	@Test
	public void homepageRendersSuccessfully()
	{
		//start and render the test page
		wicketTester.startPage(ListPage.class);

		//assert rendered page class
		wicketTester.assertRenderedPage(ListPage.class);
	}
}
