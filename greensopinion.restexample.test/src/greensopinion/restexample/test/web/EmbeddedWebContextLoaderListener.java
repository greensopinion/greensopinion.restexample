package greensopinion.restexample.test.web;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

public class EmbeddedWebContextLoaderListener extends ContextLoaderListener {

	private static ApplicationContext applicationContext;

	@Override
	protected ApplicationContext loadParentContext(ServletContext servletContext) {
		if (applicationContext != null) {
			return applicationContext;
		}
		return super.loadParentContext(servletContext);
	}

	public static void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}
	
	public static void clearContext() {
		applicationContext = null;
	}
	
}
