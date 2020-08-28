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
 * Home object for domain model class veranstaltungs_stufe.
 * @see .veranstaltungs_stufe
 * @author Hibernate Tools
 */
public class veranstaltungs_stufeHome {

	private static final Log log = LogFactory.getLog(veranstaltungs_stufeHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(veranstaltungs_stufe transientInstance) {
		log.debug("persisting veranstaltungs_stufe instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(veranstaltungs_stufe instance) {
		log.debug("attaching dirty veranstaltungs_stufe instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(veranstaltungs_stufe instance) {
		log.debug("attaching clean veranstaltungs_stufe instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(veranstaltungs_stufe persistentInstance) {
		log.debug("deleting veranstaltungs_stufe instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public veranstaltungs_stufe merge(veranstaltungs_stufe detachedInstance) {
		log.debug("merging veranstaltungs_stufe instance");
		try {
			veranstaltungs_stufe result = (veranstaltungs_stufe) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public veranstaltungs_stufe findById(int id) {
		log.debug("getting veranstaltungs_stufe instance with id: " + id);
		try {
			veranstaltungs_stufe instance = (veranstaltungs_stufe) sessionFactory.getCurrentSession()
					.get("veranstaltungs_stufe", id);
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

	public List findByExample(veranstaltungs_stufe instance) {
		log.debug("finding veranstaltungs_stufe instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("veranstaltungs_stufe")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
