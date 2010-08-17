package it.cilea.osd.jdyna.utils;

import it.cilea.osd.jdyna.service.ISearchDynaService;
import it.cilea.osd.jdyna.service.SearchService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class RigeneraIndiciSearch {
	public static void main(String[] args) {
		ApplicationContext context = null;
		try {
			String conf0 = args[0];
			String conf1 = args[1];
			String conf2 = args[2];
			String conf3 = args[3];
			String conf4 = args[4];			
			
			context = new FileSystemXmlApplicationContext(
					new String[] {conf0,conf1,conf2,conf3,conf4});
			ISearchDynaService searchService = (ISearchDynaService) context.getBean(
					"searchService");
			searchService.purgeAndCreateIndex();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (context != null){
				context.publishEvent(new ContextClosedEvent(context));
			}
		}
	}
}
