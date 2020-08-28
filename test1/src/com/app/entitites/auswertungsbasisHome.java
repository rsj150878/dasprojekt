package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:34 by Hibernate Tools 5.4.3.Final

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class auswertungsbasis.
 * @see .auswertungsbasis
 * @author Hibernate Tools
 */
public class auswertungsbasisHome {

	private static final Log log = LogFactory.getLog(auswertungsbasisHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(auswertungsbasis transientInstance) {
		log.debug("persisting auswertungsbasis instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(auswertungsbasis instance) {
		log.debug("attaching dirty auswertungsbasis instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(auswertungsbasis instance) {
		log.debug("attaching clean auswertungsbasis instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(auswertungsbasis persistentInstance) {
		log.debug("deleting auswertungsbasis instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public auswertungsbasis merge(auswertungsbasis detachedInstance) {
		log.debug("merging auswertungsbasis instance");
		try {
			auswertungsbasis result = (auswertungsbasis) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public auswertungsbasis findById(java.lang.String id) {
		log.debug("getting auswertungsbasis instance with id: " + id);
		try {
			auswertungsbasis instance = (auswertungsbasis) sessionFactory.getCurrentSession().get("auswertungsbasis",
					id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(auswertungsbasis instance) {
		log.debug("finding auswertungsbasis instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("auswertungsbasis")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
