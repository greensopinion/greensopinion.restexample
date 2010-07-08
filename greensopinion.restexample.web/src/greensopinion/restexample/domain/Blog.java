package greensopinion.restexample.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Blog extends AbstractEntity {
	private String name;
	private List<Article> articles = new ArrayList<Article>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST }, mappedBy = "blog")
	@OrderBy("created")
	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

}
