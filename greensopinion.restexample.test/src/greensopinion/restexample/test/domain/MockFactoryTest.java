package greensopinion.restexample.test.domain;

import static org.junit.Assert.*;

import greensopinion.restexample.domain.AbstractEntity;
import greensopinion.restexample.domain.Article;
import greensopinion.restexample.domain.Blog;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * test that the mock factories are working as expected
 * 
 * @author David Green
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "/applicationContext-test.xml" })
@Transactional
public class MockFactoryTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void testCreateBlog() {
		Blog blog = MockFactory.on(Blog.class).create(entityManager);
		assertNotNull(blog);
		assertNotNull(blog.getName());
		entityAssertions(blog);
		entityManager.flush();
	}
	@Test
	public void testCreateArticle() {
		Blog blog = MockFactory.on(Blog.class).create(entityManager);
		Article article = MockFactory.on(Article.class).create(null);
		article.setBlog(blog);
		entityManager.persist(article);
		assertNotNull(article);
		assertNotNull(article.getTitle());
		assertNotNull(article.getContent());
		entityAssertions(article);
		entityManager.flush();
	}
	private void entityAssertions(AbstractEntity entity) {
		assertNotNull(entity.getId());
		assertNotNull(entity.getCreated());
		assertNotNull(entity.getModified());
	}
}
