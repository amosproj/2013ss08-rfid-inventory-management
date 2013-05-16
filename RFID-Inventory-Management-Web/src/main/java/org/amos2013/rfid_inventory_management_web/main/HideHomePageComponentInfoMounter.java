package org.amos2013.rfid_inventory_management_web.main;

import org.apache.wicket.core.request.handler.ListenerInterfaceRequestHandler;
import org.apache.wicket.core.request.mapper.HomePageMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.info.PageComponentInfo;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;

/**
 * This class allows to mount the homepage in the {@link WicketApplication} without displaying
 * a {@link PageComponentInfo} in the URLs (e.g. "?0")
 *
 */
public class HideHomePageComponentInfoMounter extends HomePageMapper
{
	
	/**
	 * Instantiates a new mounter which hides the homepage's PageComponentInfo.
	 *
	 * @param pageClass the page class
	 */
	public HideHomePageComponentInfoMounter(Class<? extends IRequestablePage> pageClass)
	{
		super(pageClass, new PageParametersEncoder());
	}
	
	@Override
	protected void encodePageComponentInfo(Url url, PageComponentInfo pageComponentInfo)
	{
		// do nothing, to hide the PageComponentInfo
	}
	
	@Override 
	public Url mapHandler(IRequestHandler requestHandler)
	{
		if (requestHandler instanceof ListenerInterfaceRequestHandler)
		{
			return null;
		}
		else
		{
			return super.mapHandler(requestHandler);
		}
	}
}
