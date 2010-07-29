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
package greensopinion.restexample.test.jpa;

import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

/**
 * An entity manager factory that ensures that a singleton EntityManager is created.
 * The singleton is provided so that access to the entity manager always uses the same
 * instance, regardless of context and/or thread.  The way that tests are structured ensures
 * that concurrent access cannot occur, even though multiple threads use the same instance.
 * 
 * By taking this approach we can mock up our data in our test and have web container threads
 * see the same data.
 * 
 * To avoid side-effects between tests we disallow commit.
 * 
 * @author David Green
 */
public class TestEntityManagerFactory implements EntityManagerFactory {
	public class TestTransaction implements EntityTransaction {
		private EntityTransaction delegate;
		
		public TestTransaction(EntityTransaction delegate) {
			this.delegate = delegate;
		}

		@Override
		public void begin() {
			if (!delegate.isActive()){ 
				delegate.begin();
			}
		}

		@Override
		public void commit() {
			// don't commit
		}

		public boolean getRollbackOnly() {
			return delegate.getRollbackOnly();
		}

		public boolean isActive() {
			return delegate.isActive();
		}

		public void rollback() {
			// don't rollback
		}

		public void setRollbackOnly() {
			delegate.setRollbackOnly();
		}


	}

	public class TestEntityManager implements EntityManager {

		private final EntityManager delegate;
		private boolean open;
		public TestEntityManager(EntityManager delegate) {
			this.delegate = delegate;
			open = delegate.isOpen();
			++referenceCount;
		}

		public void clear() {
			delegate.clear();
		}

		public void close() {
			if (open) {
				open = false;
				if (--referenceCount == 0) {
					if (delegate.getTransaction().isActive()) {
						delegate.getTransaction().rollback();
					}
					delegate.clear();	
				}
			}
		}

		public boolean contains(Object arg0) {
			return delegate.contains(arg0);
		}

		public <T> TypedQuery<T> createNamedQuery(String arg0, Class<T> arg1) {
			return delegate.createNamedQuery(arg0, arg1);
		}

		public Query createNamedQuery(String arg0) {
			return delegate.createNamedQuery(arg0);
		}

		public Query createNativeQuery(String arg0, Class arg1) {
			return delegate.createNativeQuery(arg0, arg1);
		}

		public Query createNativeQuery(String arg0, String arg1) {
			return delegate.createNativeQuery(arg0, arg1);
		}

		public Query createNativeQuery(String arg0) {
			return delegate.createNativeQuery(arg0);
		}

		public <T> TypedQuery<T> createQuery(CriteriaQuery<T> arg0) {
			return delegate.createQuery(arg0);
		}

		public <T> TypedQuery<T> createQuery(String arg0, Class<T> arg1) {
			return delegate.createQuery(arg0, arg1);
		}

		public Query createQuery(String arg0) {
			return delegate.createQuery(arg0);
		}

		public void detach(Object arg0) {
			delegate.detach(arg0);
		}

		public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2,
				Map<String, Object> arg3) {
			return delegate.find(arg0, arg1, arg2, arg3);
		}

		public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2) {
			return delegate.find(arg0, arg1, arg2);
		}

		public <T> T find(Class<T> arg0, Object arg1, Map<String, Object> arg2) {
			return delegate.find(arg0, arg1, arg2);
		}

		public <T> T find(Class<T> arg0, Object arg1) {
			return delegate.find(arg0, arg1);
		}

		public void flush() {
			delegate.flush();
		}

		public CriteriaBuilder getCriteriaBuilder() {
			return delegate.getCriteriaBuilder();
		}

		public Object getDelegate() {
			return delegate.getDelegate();
		}

		public EntityManagerFactory getEntityManagerFactory() {
			return delegate.getEntityManagerFactory();
		}

		public FlushModeType getFlushMode() {
			return delegate.getFlushMode();
		}

		public LockModeType getLockMode(Object arg0) {
			return delegate.getLockMode(arg0);
		}

		public Metamodel getMetamodel() {
			return delegate.getMetamodel();
		}

		public Map<String, Object> getProperties() {
			return delegate.getProperties();
		}

		public <T> T getReference(Class<T> arg0, Object arg1) {
			return delegate.getReference(arg0, arg1);
		}

		public EntityTransaction getTransaction() {
			return new TestTransaction(delegate.getTransaction());
		}

		public boolean isOpen() {
			return open && delegate.isOpen();
		}

		public void joinTransaction() {
			delegate.joinTransaction();
		}

		public void lock(Object arg0, LockModeType arg1,
				Map<String, Object> arg2) {
			delegate.lock(arg0, arg1, arg2);
		}

		public void lock(Object arg0, LockModeType arg1) {
			delegate.lock(arg0, arg1);
		}

		public <T> T merge(T arg0) {
			return delegate.merge(arg0);
		}

		public void persist(Object arg0) {
			delegate.persist(arg0);
		}

		public void refresh(Object arg0, LockModeType arg1,
				Map<String, Object> arg2) {
			delegate.refresh(arg0, arg1, arg2);
		}

		public void refresh(Object arg0, LockModeType arg1) {
			delegate.refresh(arg0, arg1);
		}

		public void refresh(Object arg0, Map<String, Object> arg1) {
			delegate.refresh(arg0, arg1);
		}

		public void refresh(Object arg0) {
			delegate.refresh(arg0);
		}

		public void remove(Object arg0) {
			delegate.remove(arg0);
		}

		public void setFlushMode(FlushModeType arg0) {
			delegate.setFlushMode(arg0);
		}

		public void setProperty(String arg0, Object arg1) {
			delegate.setProperty(arg0, arg1);
		}

		public <T> T unwrap(Class<T> arg0) {
			return delegate.unwrap(arg0);
		}


	}

	private EntityManagerFactory delegate;

	
	private EntityManager theEntityManager;
	private int referenceCount = 0;
	
	public EntityManagerFactory getDelegate() {
		return delegate;
	}

	public void setDelegate(EntityManagerFactory delegate) {
		this.delegate = delegate;
	}

	public void close() {
		delegate.close();
	}

	public EntityManager createEntityManager() {
		if (theEntityManager == null) {
			theEntityManager = delegate.createEntityManager();
		}
		return new TestEntityManager(theEntityManager);
	}

	public EntityManager createEntityManager(Map arg0) {
		return createEntityManager();
	}

	public Cache getCache() {
		return delegate.getCache();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return delegate.getCriteriaBuilder();
	}

	public Metamodel getMetamodel() {
		return delegate.getMetamodel();
	}

	public PersistenceUnitUtil getPersistenceUnitUtil() {
		return delegate.getPersistenceUnitUtil();
	}

	public Map<String, Object> getProperties() {
		return delegate.getProperties();
	}

	public boolean isOpen() {
		return delegate.isOpen();
	}
	
	
}
