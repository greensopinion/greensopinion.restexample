package greensopinion.restexample.web;

import greensopinion.restexample.domain.Article;
import greensopinion.restexample.domain.Blog;
import greensopinion.restexample.service.BlogService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Qualifier("webservice")
public class BlogServiceController implements BlogService {

	@Qualifier("main")
	@Autowired
	private BlogService service;

	@RequestMapping(value = "/blog", method = RequestMethod.POST)
	@Override
	public Blog createBlog(@RequestBody Blog blog) {
		return shallowCopy(service.createBlog(blog));
	}

	private Blog shallowCopy(Blog blog) {
		Blog copy = new Blog();
		copy.setArticles(null);
		copy.setCreated(blog.getCreated());
		copy.setId(blog.getId());
		copy.setModified(blog.getModified());
		copy.setName(blog.getName());
		return copy;
	}

	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	@RequestMapping(value = "/blog/{blogId}", method = RequestMethod.POST)
	@Override
	public Blog updateBlog(@RequestBody Blog blog) {
		return shallowCopy(service.updateBlog(blog));
	}

	@RequestMapping(value = "/blog/{blogId}", method = RequestMethod.GET)
	@Override
	public Blog getBlog(@PathVariable("blogId") Long blogId) {
		return shallowCopy(service.getBlog(blogId));
	}

	@RequestMapping(value = "/blog/{blogId}/articles/{since}", method = RequestMethod.GET)
	public List<Article> listArticles(@PathVariable("blogId") Long blogId, @PathVariable("since") String since) {
		Date date;
		try {
			date = dateFormat.parse(since);
		} catch (ParseException e) {
			date = null;
		}
		return listArticles(blogId, date);
	}

	@Override
	public List<Article> listArticles(Long blogId, Date since) {
		return shallowCopy(service.listArticles(blogId, since));
	}

	private List<Article> shallowCopy(List<Article> listArticles) {
		List<Article> copies = new ArrayList<Article>(listArticles.size());
		for (Article article : listArticles) {
			copies.add(shallowCopy(article));
		}
		return copies;
	}

	private Article shallowCopy(Article article) {
		Article copy = new Article();
		copy.setId(article.getId());
		copy.setCreated(article.getCreated());
		copy.setModified(article.getModified());
		copy.setPublished(article.getPublished());
		copy.setAuthor(article.getAuthor());
		copy.setTitle(article.getTitle());
		copy.setContent(article.getContent());
		return copy;
	}

	@RequestMapping(value = "/blog/{blogId}/article", method = RequestMethod.POST)
	@Override
	public Article createArticle(@PathVariable("blogId") Long blogId, @RequestBody Article article) {
		return shallowCopy(service.createArticle(blogId, article));
	}

	@RequestMapping(value = "/article/{articleId}", method = RequestMethod.POST)
	@Override
	public Article updateArticle(@RequestBody Article article) {
		return shallowCopy(service.updateArticle(article));
	}

	@RequestMapping(value = "/article/{articleId}", method = RequestMethod.GET)
	@Override
	public Article getArticle(@PathVariable("articleId") Long articleId) {
		return shallowCopy(service.getArticle(articleId));
	}

}
