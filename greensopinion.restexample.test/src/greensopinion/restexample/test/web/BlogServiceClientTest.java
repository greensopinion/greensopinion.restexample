package greensopinion.restexample.test.web;

import java.io.File;

import junit.framework.TestResult;
import greensopinion.restexample.service.BlogService;
import greensopinion.restexample.test.service.BlogServiceTest;
import greensopinion.restexample.test.util.TestUtil;
import greensopinion.restexample.web.BlogServiceClient;
import greensopinion.restexample.web.BlogServiceController;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test {@link BlogServiceClient}, verifies over-the-wire behaviour
 * 
 * @author David Green
 * 
 * @see BlogService
 * @see BlogServiceClient
 * @see BlogServiceController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BlogServiceClientTest extends BlogServiceTest {
	@Autowired
	private BlogServiceClient blogServiceClient;

	@Autowired
	private WebApplicationContainer webContainer;

	@Before
	public void before() {
		webContainer.setWebRoot(computeWebRoot());
		webContainer.start();

		blogServiceClient.setBaseUrl(webContainer.getBaseUrl()+"api");
		// use the client instead of the service directly
		service = blogServiceClient;
	}

	private File computeWebRoot() {
		File folder = new File(TestUtil.computeClasspathRoot(BlogServiceClientTest.class).getParentFile(),"resources/"+BlogServiceClientTest.class.getSimpleName());
		if (!folder.exists()) {
			throw new IllegalStateException();
		}
		return folder;
	}

	@After
	public void after() {
		if (webContainer.isStarted()) {
			webContainer.stop();
		}
	}

}
