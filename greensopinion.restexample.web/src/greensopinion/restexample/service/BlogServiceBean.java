package greensopinion.restexample.service;

import greensopinion.restexample.domain.Article;
import greensopinion.restexample.domain.Blog;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A {@link BlogService} that uses JPA as its backing data store.
 * 
 * @author David Green
 */
@Service("blogService")
@Transactional
@Qualifier("main")
public class BlogServiceBean implements BlogService {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Blog createBlog(Blog blog) {
		entityManager.persist(blog);
		entityManager.flush();
		return blog;
	}

	@Override
	public Blog updateBlog(Blog blog) {
		if (!entityManager.contains(blog)) {
			Blog managedBlog = entityManager.find(Blog.class, blog.getId());
			managedBlog.setName(blog.getName());
			blog = managedBlog;
		}
		entityManager.flush();
		return blog;
	}

	@Override
	public Blog getBlog(Long blogId) {
		return entityManager.find(Blog.class, blogId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Article> listArticles(Long blogId, Date since) {
		return entityManager
				.createQuery("select e from " + Article.class.getSimpleName() + " e where e.created >= :date")
				.setParameter("date", since).getResultList();
	}

	@Override
	public Article createArticle(Long blogId, Article article) {
		Blog blog = getBlog(blogId);
		article.setBlog(blog);
		blog.getArticles().add(article);
		entityManager.persist(article);
		entityManager.flush();
		return article;
	}

	@Override
	public Article updateArticle(Article article) {
		if (!entityManager.contains(article)) {
			Article managedArticle = entityManager.find(Article.class, article.getId());
			managedArticle.setAuthor(article.getAuthor());
			managedArticle.setContent(article.getContent());
			managedArticle.setTitle(article.getTitle());
			managedArticle.setPublished(article.getPublished());
			article = managedArticle;
		}
		entityManager.flush();
		return article;
	}

	@Override
	public Article getArticle(Long articleId) {
		return entityManager.find(Article.class, articleId);
	}
}
