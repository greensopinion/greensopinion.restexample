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
package greensopinion.restexample.web;

import greensopinion.restexample.domain.Article;
import greensopinion.restexample.domain.Blog;
import greensopinion.restexample.service.BlogService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * A REST web service client for accessing a {@link BlogService}
 * 
 * @author David Green
 */
@Service
@Qualifier("client")
public class BlogServiceClient implements BlogService {

	public static class Result {
		private Blog blog;
		private Article article;
		private List<Article> articles;

		public Blog getBlog() {
			return blog;
		}

		public void setBlog(Blog blog) {
			this.blog = blog;
		}

		public Article getArticle() {
			return article;
		}

		public void setArticle(Article article) {
			this.article = article;
		}

		public List<Article> getArticleList() {
			return articles;
		}

		public void setArticleList(List<Article> articles) {
			this.articles = articles;
		}

	}

	@Autowired
	protected RestTemplate template;
	private String baseUrl;

	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	protected String computeUrl(String relativePath) {
		if (baseUrl == null) {
			throw new IllegalStateException();
		}
		return baseUrl + "/" + relativePath;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public Blog createBlog(Blog blog) {
		Result result = template.postForObject(computeUrl("blog"), blog, Result.class);
		if (result.getBlog() != null) {
			return result.getBlog();
		}
		throw new IllegalStateException("Unexpected result");
	}

	@Override
	public Blog updateBlog(Blog blog) {
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("blogId", blog.getId().toString());
		Result result = template.postForObject(computeUrl("blog/{blogId}"), blog, Result.class, variables);
		if (result.getBlog() != null) {
			return result.getBlog();
		}
		throw new IllegalStateException("Unexpected result");
	}

	@Override
	public Blog getBlog(Long blogId) {
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("blogId", blogId.toString());
		Result result = template.getForObject(computeUrl("blog/{blogId}"), Result.class, variables);
		if (result.getBlog() != null) {
			return result.getBlog();
		}
		throw new IllegalStateException("Unexpected result");
	}

	@Override
	public List<Article> listArticles(Long blogId, Date since) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("blogId", blogId.toString());
		variables.put("since", dateFormat.format(since));
		Result result = template.getForObject(computeUrl("blog/{blogId}/articles/{since}"), Result.class, variables);
		if (result.getArticleList() != null) {
			return result.getArticleList();
		}
		throw new IllegalStateException("Unexpected result");
	}

	@Override
	public Article createArticle(Long blogId, Article article) {
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("blogId", blogId.toString());
		Result result = template.postForObject(computeUrl("blog/{blogId}/article"), article, Result.class, variables);
		if (result.getArticle() != null) {
			return result.getArticle();
		}
		throw new IllegalStateException("Unexpected result");
	}

	@Override
	public Article updateArticle(Article article) {
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("articleId", article.getId().toString());
		Result result = template.postForObject(computeUrl("article/{articleId}"), article, Result.class, variables);
		if (result.getArticle() != null) {
			return result.getArticle();
		}
		throw new IllegalStateException("Unexpected result");
	}

	@Override
	public Article getArticle(Long articleId) {
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("articleId", articleId.toString());
		Result result = template.getForObject(computeUrl("article/{articleId}"), Result.class, variables);
		if (result.getArticle() != null) {
			return result.getArticle();
		}
		throw new IllegalStateException("Unexpected result");
	}

}
