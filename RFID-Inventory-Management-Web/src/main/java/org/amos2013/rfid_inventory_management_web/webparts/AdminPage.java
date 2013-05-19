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

import java.io.IOException;
import java.util.List;

import org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseHandler;
import org.amos2013.rfid_inventory_management_web.database.DeviceDatabaseRecord;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

public class AdminPage extends MainPage
{

	private static final long serialVersionUID = 931941446133830339L;

	//private int counter;

    /**
     * Construct.
     */
    public AdminPage() throws IOException
    {
                
        //private String searchField;
    	//private static final List<String> SEARCH_OPTIONS = Arrays.asList(new String[] {"rfid_id", "room", "owner" });
    	//private String statusMessage;
    	
    	//private String selected = "rfid_id";

    	List<DeviceDatabaseRecord> databaseRecords = null;
		try
		{
			databaseRecords = DeviceDatabaseHandler.getRecordsFromDatabase();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

        // add a container
        WebMarkupContainer container = new WebMarkupContainer("container");
        add(container);
        container.add(new CounterLabel("recordsReadListView", databaseRecords));

        // add a form
        Form<?> form = new Form<Void>("form");
        add(form);

        // add the textfield that will update the counter value
        form.add(new TextField<String>("counter", new PropertyModel<String>(this, "counter"),
            String.class).setRequired(true));

        // add button that will broadcast counter update event
        form.add(new AjaxButton("submit")
        {
			private static final long serialVersionUID = -8194180069776977224L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
                send(getPage(), Broadcast.BREADTH, new CounterUpdate(target));
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
            }

        });
    }

    /**
     * An event payload that represents a counter update
     */
    public class CounterUpdate
    {
        private final AjaxRequestTarget target;

        /**
         * Constructor
         * 
         * @param target
         */
        public CounterUpdate(AjaxRequestTarget target)
        {
            this.target = target;
        }

        /** @return ajax request target */
        public AjaxRequestTarget getTarget()
        {
            return target;
        }
    }

    /**
     * A label that renders the value of the page's counter variable. Also listens to
     * {@link CounterUpdate} event and updates itself.
     */
    public class CounterLabel extends Label
    {
		private static final long serialVersionUID = -7297418270218339098L;

		/**
         * Construct.
         * 
         * @param id
         */
        public CounterLabel(String id, List<DeviceDatabaseRecord> databaseRecords)
        {
            super(id, new PropertyModel<String>(AdminPage.this, "counter"));
            setOutputMarkupId(true);
            
            ListView<DeviceDatabaseRecord> test = new ListView<DeviceDatabaseRecord>(id, databaseRecords)
            {
				private static final long serialVersionUID = 6763095385797269964L;

				public void populateItem(ListItem<DeviceDatabaseRecord> item)
            	{
            		final DeviceDatabaseRecord record = (DeviceDatabaseRecord) item.getModelObject();
            		item.add(new Label("recordRFIDIdLabel", record.getRFIDId()));
            		item.add(new Label("recordRoomLabel", record.getRoom()));
            		item.add(new Label("recordOwnerLabel", record.getOwner()));			
            	}
            };
        }

        /**
         * @see org.apache.wicket.Component#onEvent(org.apache.wicket.event.IEvent)
         */
        @Override
        public void onEvent(IEvent<?> event)
        {
            super.onEvent(event);

            // check if this is a counter update event and if so repaint self
            if (event.getPayload() instanceof CounterUpdate)
            {
                CounterUpdate update = (CounterUpdate)event.getPayload();
                update.getTarget().add(this);
            }
        }

    }
}