package greensopinion.restexample.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import greensopinion.restexample.domain.Article;
import greensopinion.restexample.domain.Blog;
import greensopinion.restexample.service.BlogService;
import greensopinion.restexample.test.domain.MockFactory;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test {@link BlogService}
 * 
 * @author David Green
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "/applicationContext-test.xml" })
@Transactional
public class BlogServiceTest {

	@PersistenceContext
	protected EntityManager entityManager;

	@Qualifier("main")
	@Autowired
	protected BlogService service;
	
	@Test	
	public void testCreateBlog() {
		Blog blog = service.createBlog(MockFactory.on(Blog.class).create(null));
		assertNotNull(blog);
		assertNotNull(blog.getId());
		assertNotNull(blog.getCreated());
		assertNotNull(blog.getModified());
		
		entityManager.clear();
		
		assertNotNull(entityManager.find(Blog.class, blog.getId()));
	}
	
	@Test
	public void testUpdateBlog() {
		Blog blog = MockFactory.on(Blog.class).create(entityManager);
		entityManager.flush();
		entityManager.clear();
		
		blog.setName(blog.getName()+"2");
		
		Blog updatedBlog = service.updateBlog(blog);
		assertNotNull(updatedBlog);
		assertNotSame(updatedBlog,blog);
		assertEquals(blog.getName(),updatedBlog.getName());
		assertEquals(blog.getId(),updatedBlog.getId());
	}
	
	@Test
	public void testGetBlog() {
		Blog blog = MockFactory.on(Blog.class).create(entityManager);
		entityManager.flush();
		entityManager.clear();
		
		Blog updatedBlog = service.getBlog(blog.getId());
		assertNotNull(updatedBlog);
		assertNotSame(updatedBlog,blog);
		assertEquals(blog.getName(),updatedBlog.getName());
		assertEquals(blog.getId(),updatedBlog.getId());
	}

	@Test
	public void testCreateArticle() {
		Blog blog = MockFactory.on(Blog.class).create(entityManager);
		
		Article article = MockFactory.on(Article.class).create(null);
		
		Article newArticle = service.createArticle(blog.getId(), article);
		assertNotNull(newArticle);
		assertNotNull(newArticle.getId());
		assertNotNull(newArticle.getCreated());
		assertEquals(article.getAuthor(),newArticle.getAuthor());
		assertEquals(article.getContent(),newArticle.getContent());
		assertEquals(article.getTitle(),newArticle.getTitle());		
	}
	
	@Test
	public void testListArticles() throws Exception {
		
		Blog blog = MockFactory.on(Blog.class).create(entityManager);
	
		Date initialSetCreationDate = new Date(System.currentTimeMillis()-(3*(24*60*60*1000)));
		
		final int numArticles = 10;
		for (int x = 0;x<numArticles;++x) {
			Article article = MockFactory.on(Article.class).create(null);
			article.setCreated(initialSetCreationDate);
			article.setBlog(blog);
			blog.getArticles().add(article);
			entityManager.persist(article);
		}
		
		
		Date now = new Date();
		
		for (int x = 0;x<numArticles;++x) {
			service.createArticle(blog.getId(), MockFactory.on(Article.class).create(null));
		}
		
		List<Article> articles = service.listArticles(blog.getId(), now);
		assertNotNull(articles);
		assertEquals(numArticles,articles.size());
	}
	
	@Test
	public void testGetArticle() {
		Blog blog = MockFactory.on(Blog.class).create(entityManager);
		Article newArticle = service.createArticle(blog.getId(), MockFactory.on(Article.class).create(null));
		assertNotNull(newArticle);
		assertNotNull(newArticle.getId());
		entityManager.clear();
		
		Article article = service.getArticle(newArticle.getId());
		assertNotNull(article);
		assertEquals(newArticle.getId(),article.getId());
		assertEquals(newArticle.getAuthor(), article.getAuthor());
		assertEquals(newArticle.getTitle(), article.getTitle());
		assertEquals(newArticle.getContent(), article.getContent());
	}
}
