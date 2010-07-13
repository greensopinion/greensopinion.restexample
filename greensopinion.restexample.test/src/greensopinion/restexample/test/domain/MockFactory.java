package greensopinion.restexample.test.domain;

import greensopinion.restexample.domain.Article;
import greensopinion.restexample.domain.Blog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

/**
 * A factory for domain objects that mocks their data.
 * Example usage:
 * <pre><code>
 * Blog blog = MockFactory.on(Blog.class).create(entityManager);
 * </code></pre>
 * @author David Green
 */
public abstract class MockFactory<T> {

	private static Map<Class<?>,MockFactory<?>> factories = new HashMap<Class<?>, MockFactory<?>>();
	static {
		register(new MockBlogFactory());
		register(new MockArticleFactory());
	}
	private static void register(MockFactory<?> mockFactory) {
		factories.put(mockFactory.domainClass,mockFactory);
	}
	@SuppressWarnings("unchecked")
	public static <T> MockFactory<T> on(Class<T> domainClass) {
		MockFactory<?> factory = factories.get(domainClass);
		if (factory == null) {
			throw new IllegalStateException("Did you forget to register a mock factory for "+domainClass.getClass().getName()+"?");
		}
		return (MockFactory<T>) factory;
	}
	
	private final Class<T> domainClass;

	private int seed;
	
	protected MockFactory(Class<T> domainClass) {
		if (domainClass.getAnnotation(Entity.class) == null) {
			throw new IllegalArgumentException();
		}
		this.domainClass = domainClass;
	}
	

	/**
	 * Create several objects
	 * @param entityManager the entity manager, or null if the mocked objects should not be persisted
	 * @param count the number of objects to create
	 * @return the created objects
	 */
	public List<T> create(EntityManager entityManager,int count) {
		List<T> mocks = new ArrayList<T>(count);
		for (int x = 0;x<count;++x) {
			T t = create(entityManager);
			mocks.add(t);
		}
		return mocks;
	}

	/**
	 * Create a single object
	 * @param entityManager the entity manager, or null if the mocked object should not be persisted
	 * @return the mocked object
	 */
	public T create(EntityManager entityManager) {
		T mock;
		try {
			mock = domainClass.newInstance();
		} catch (Exception e) {
			// must have a default constructor
			throw new IllegalStateException();
		}
		populate(++seed,mock);
		if (entityManager != null) {
			entityManager.persist(mock);
		}
		return mock;
	}

	/**
	 * Populate the given domain object with data
	 * @param seed a seed that may be used to create data
	 * @param mock the domain object to populate
	 */
	protected abstract void populate(int seed, T mock);
	

	private static class MockBlogFactory extends MockFactory<Blog> {
		
		public MockBlogFactory() {
			super(Blog.class);
		}
		
		@Override
		protected void populate(int seed, Blog mock) {
			mock.setName("Blog "+seed);
		}

	}

	private static class MockArticleFactory extends MockFactory<Article> {
		
		public MockArticleFactory() {
			super(Article.class);
		}
		
		@Override
		protected void populate(int seed, Article mock) {
			mock.setAuthor("First Last");
			mock.setTitle("Article "+seed);
			mock.setContent("article "+seed+" content");
		}

	}

}
