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
package greensopinion.restexample.service;

import greensopinion.restexample.domain.Article;
import greensopinion.restexample.domain.Blog;

import java.util.Date;
import java.util.List;

/**
 * a means of accessing a {@link Blog}.
 * 
 * @author David Green
 */
public interface BlogService {
	/**
	 * create a new blog
	 * 
	 * @param blog
	 *            the blog to create
	 * @return the blog with a populated id and values as it was persisted
	 */
	public Blog createBlog(Blog blog);

	/**
	 * modify a blog
	 * 
	 * @param blog
	 *            the blog to modify
	 * @return the blog with modified values as it was persisted
	 */
	public Blog updateBlog(Blog blog);

	/**
	 * get a blog by its id
	 * 
	 * @param blogId
	 *            the identity of the blog
	 * @return the blog, or null if it could not be found
	 */
	public Blog getBlog(Long blogId);

	/**
	 * List articles of the blog since the given date
	 * 
	 * @param blogId
	 *            the identity of the blog
	 * @param since
	 *            the date from which articles should be returned
	 * @return articles newer than the given date
	 */
	public List<Article> listArticles(Long blogId, Date since);

	/**
	 * create a new article for the given blog
	 * 
	 * @param blogId
	 *            the identity of the blog
	 * @param article
	 *            the article to create
	 * @return the article with values as it was created
	 */
	public Article createArticle(Long blogId, Article article);

	/**
	 * modify an article
	 * 
	 * @param article
	 *            the article to modify
	 * @return the article with values as it was modified
	 */
	public Article updateArticle(Article article);

	/**
	 * get an article by its identity
	 * 
	 * @return the article, or null if there was no such article
	 */
	public Article getArticle(Long articleId);
}
