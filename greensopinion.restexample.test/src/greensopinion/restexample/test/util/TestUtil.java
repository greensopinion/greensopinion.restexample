package greensopinion.restexample.test.util;

import java.io.File;
import java.net.URL;

public class TestUtil {

	public static File computeClasspathRoot(Class<?> clazz) {
		try {
			URL url = clazz.getResource(clazz.getSimpleName()+".class");
			String uri = url.toURI().toString();
			
			if (uri.startsWith("file:")) {
				String path = uri.substring(5);
				path = path.replace('\\', '/'); // backslash to slash
				path = path.replaceAll("%20", " "); // unescape spaces
				path = path.replaceAll("/{2,}", "/"); // eliminate consecutive slashes
				if (path.matches("/[a-zA-Z]:/.*")) { // paths with drive letters should not start with slash
					path = path.substring(1);
				}
				String classPathPart = clazz.getName().replace('.', '/')+".class";
				if (!path.endsWith(classPathPart)) {
					throw new IllegalStateException();
				}
				path = path.substring(0,path.length()-classPathPart.length());
				File file = new File(path);
				if (!file.exists() || !file.isDirectory()) {
					throw new IllegalStateException(file.getAbsolutePath());
				}
				if (!file.getName().equals("bin")) {
					throw new IllegalStateException("Unexpected location "+file);
				}
				return file;
			} else {
				throw new IllegalStateException("Unsupported format: "+uri);
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
