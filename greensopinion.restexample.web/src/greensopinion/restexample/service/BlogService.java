package greensopinion.restexample.service;

import greensopinion.restexample.domain.Article;
import greensopinion.restexample.domain.Blog;

import java.util.Date;
import java.util.List;

public interface BlogService {

	public Blog createBlog(Blog blog);

	public Blog updateBlog(Blog blog);

	public Blog getBlog(Long blogId);

	public List<Article> listArticles(Long blogId, Date since);

	public Article createArticle(Long blogId, Article article);

	public Article updateArticle(Article article);

	public Article getArticle(Long articleId);
}
