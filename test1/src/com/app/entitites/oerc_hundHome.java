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
 * Home object for domain model class oerc_hund.
 * @see .oerc_hund
 * @author Hibernate Tools
 */
public class oerc_hundHome {

	private static final Log log = LogFactory.getLog(oerc_hundHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(oerc_hund transientInstance) {
		log.debug("persisting oerc_hund instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(oerc_hund instance) {
		log.debug("attaching dirty oerc_hund instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(oerc_hund instance) {
		log.debug("attaching clean oerc_hund instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(oerc_hund persistentInstance) {
		log.debug("deleting oerc_hund instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public oerc_hund merge(oerc_hund detachedInstance) {
		log.debug("merging oerc_hund instance");
		try {
			oerc_hund result = (oerc_hund) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public oerc_hund findById(int id) {
		log.debug("getting oerc_hund instance with id: " + id);
		try {
			oerc_hund instance = (oerc_hund) sessionFactory.getCurrentSession().get("oerc_hund", id);
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

	public List findByExample(oerc_hund instance) {
		log.debug("finding oerc_hund instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("oerc_hund").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
