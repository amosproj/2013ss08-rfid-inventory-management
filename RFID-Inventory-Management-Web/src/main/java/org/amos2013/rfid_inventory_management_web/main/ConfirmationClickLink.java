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

import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;

/**
 * The Class ConfirmationClickLink.
 * This will show a confirmation window, when the link is clicked
 *
 * @param <T> the generic type
 */
public abstract class ConfirmationClickLink<T> extends AjaxLink<T>
{
	private static final long serialVersionUID = 2349263584196734535L;
	private final String text;

	/**
	 * Instantiates a new confirmation click link.
	 *
	 * @param id the name of this object
	 * @param text the text to be shown in the confirmation window
	 */
	public ConfirmationClickLink(String id, String text)
	{
		super( id );
		this.text = text;
	}

	/**
	 * @see org.apache.wicket.ajax.markup.html.AjaxLink#updateAjaxAttributes(org.apache.wicket.ajax.attributes.AjaxRequestAttributes)
	 */
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
	{
		super.updateAjaxAttributes(attributes);

		AjaxCallListener ajaxCallListener = new AjaxCallListener();
		ajaxCallListener.onPrecondition("return confirm('" + text + "');");
		attributes.getAjaxCallListeners().add(ajaxCallListener);
	}
}
