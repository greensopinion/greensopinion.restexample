/*******************************************************************************
 * Copyright (c) 2010 David Green.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Green - initial API and implementation
 *******************************************************************************/
package greensopinion.restexample.test.web;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

/**
 * A listener that is used to set the parent context when loading an application context.
 * In this way we can make a unit test context visible to a web application context.
 * 
 * @author David Green
 *
 */
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
