package org.amos2013.rfid_inventory_management_web.main;

import org.apache.wicket.core.request.handler.ListenerInterfaceRequestHandler;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.info.PageComponentInfo;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;

/**
 * This class allows to mount pages in the {@link WicketApplication} without displaying
 * a {@link PageComponentInfo} in the URLs (e.g. "?0")
 *
 */
public class HidePageComponentInfoMounter extends MountedMapper
{
	
	/**
	 * Instantiates a new mounter to hide the PageComponentInfo .
	 *
	 * @param mountPath the mount path
	 * @param pageClass the page class
	 */
	public HidePageComponentInfoMounter(String mountPath, Class<? extends IRequestablePage> pageClass)
	{
		super(mountPath, pageClass, new PageParametersEncoder());
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
