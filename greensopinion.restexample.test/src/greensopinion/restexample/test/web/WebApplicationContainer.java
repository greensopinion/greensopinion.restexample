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

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.net.SocketFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import winstone.Launcher;

/**
 * A lightweight web container (webserver) that can be started/stopped within the context of a single unit test.
 * Usage:
 * <pre><code>
 * 
		webContainer.setWebRoot(computeWebRoot());
		webContainer.start();
		try {
			URL url = new URL(webContainer.getBaseUrl(),"index.html");
		} finally {
			webContainer.stop();
		}
 * </code></pre>
 * @author David Green
 */
public class WebApplicationContainer {

	private static final Random random = new Random(System.currentTimeMillis());
	
	@Autowired
	private ApplicationContext applicationContext;

	private Launcher winstoneLauncher;

	private File webRoot;

	private int port;
	
	/**
	 * start the webserver, guarantees that the webserver is started upon return.
	 * @see #stop()
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void start() {
		if (winstoneLauncher != null) {
			throw new IllegalStateException("already started");
		}
		port = findAvailablePort();
		
		
		Logger log = Logger.getLogger(WebApplicationContainer.class.getName());
		log.fine("Starting web container on "+getBaseUrl());
		
		Map args = new HashMap();
		try {
			args.put("ajp13Port", "-1");
			args.put("useJasper", "false");
			args.put("webroot", webRoot.getAbsolutePath());
			args.put("httpPort", String.valueOf(port));
			Launcher.initLogger(args);


			EmbeddedWebContextLoaderListener.setApplicationContext(applicationContext);

			// start winstone
			winstoneLauncher = new Launcher(args);

			// wait for Winstone to finish starting up
			// we do that by attempting to connect via socket
			final int maxRetries = 150;
			for (int x = 0; x < maxRetries; ++x) {
				if (testForSuccessfulStartup()) {
					break;
				}
				if (x == maxRetries - 1) {
					throw new IllegalStateException(String.format("Connection to localhost:%s failed.  Did the web container start up successfully?"));
				}
				// wait and then try again
				Thread.sleep(100L);
			}
			Logger.getLogger(WebApplicationContainer.class.getName()).info("Started web container at "+getBaseUrl());
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private int findAvailablePort() {
		// grab some random port which is likely to be open
		int port = 9100 + Math.abs(random.nextInt() % 10000);
		for (int x  = 0;x<100;++x) {
			if (testAvailablePort(port)) {
				return port;
			}
		}
		throw new IllegalStateException("Cannot find available port");
	}
	
	private boolean testAvailablePort(int port) {
		try {
			Socket socket = SocketFactory.getDefault().createSocket();
			try {
				socket.bind(new InetSocketAddress("localhost", port));
				return true;
			} catch (IOException e){ 
				return false;
			} finally {
				socket.close();
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private boolean testForSuccessfulStartup() {
		// test to see if the web container is listening on the address/port combo
		try {
			Socket socket = SocketFactory.getDefault().createSocket("localhost", port);
			socket.close();
			return true;
		} catch (UnknownHostException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * stop the web container
	 * @see #start()
	 */
	public void stop() {
		if (winstoneLauncher == null) {
			throw new IllegalStateException();
		}
		EmbeddedWebContextLoaderListener.clearContext();
		winstoneLauncher.shutdown();
		winstoneLauncher = null;
	}
	
	/**
	 * indicate if the container is started
	 */
	public boolean isStarted() {
		return winstoneLauncher != null;
	}

	public File getWebRoot() {
		return webRoot;
	}

	public void setWebRoot(File webRoot) {
		this.webRoot = webRoot;
	}

	public String getBaseUrl() {
		return String.format("http://localhost:%s/", port);
	}

}
