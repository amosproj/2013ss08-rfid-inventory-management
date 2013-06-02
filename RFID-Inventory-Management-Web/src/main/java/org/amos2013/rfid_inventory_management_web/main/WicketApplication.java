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

package org.amos2013.rfid_inventory_management_web.main;

import org.amos2013.rfid_inventory_management_web.webparts.AdminListEditPage;
import org.amos2013.rfid_inventory_management_web.webparts.AdminRoomEditPage;
import org.amos2013.rfid_inventory_management_web.webparts.AppPage;
import org.amos2013.rfid_inventory_management_web.webparts.ListPage;
import org.amos2013.rfid_inventory_management_web.webparts.AdminListPage;
import org.amos2013.rfid_inventory_management_web.webparts.AdminRoomPage;
import org.amos2013.rfid_inventory_management_web.webparts.SearchPageAdminList;
import org.amos2013.rfid_inventory_management_web.webparts.SearchPageList;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * For deploying on Tomcat -> export a .war file with Maven:
 * Command line in your project folder: "mvn package"
 * Then rename to ROOT.war, put inside the webapps folder of Tomcat
 * and (re)start the server. It will be deployed automatically.
 * Then direct your browser to the root directory of the server
 * (e.g. http://localhost:8080 or http://proj8.ss13.osramos.de/)
 * 
 * @see org.amos2013.rfid_inventory_management_web.main.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
	/**
	 * @see org.apache.wicket.Application#getAppPage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		// the homepage will now be set in the init()
		return null;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
		// mount homepage and pages without the PageComponentInfo being displayed
		mount(new HidePageComponentInfoMounter("/app", AppPage.class));
		mount(new HidePageComponentInfoMounter("/admin", AdminListPage.class));
		mount(new HidePageComponentInfoMounter("/admin/edit", AdminListEditPage.class));
		mount(new HidePageComponentInfoMounter("/admin/room", AdminRoomPage.class));
		mount(new HidePageComponentInfoMounter("/admin/room/edit", AdminRoomEditPage.class));
		mount(new HidePageComponentInfoMounter("/admin/search", SearchPageAdminList.class));
		mount(new HidePageComponentInfoMounter("/search", SearchPageList.class));
		mount(new HideHomePageComponentInfoMounter(ListPage.class));
	}
}
