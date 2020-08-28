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
 * Home object for domain model class veranstaltungs_richter.
 * @see .veranstaltungs_richter
 * @author Hibernate Tools
 */
public class veranstaltungs_richterHome {

	private static final Log log = LogFactory.getLog(veranstaltungs_richterHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(veranstaltungs_richter transientInstance) {
		log.debug("persisting veranstaltungs_richter instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(veranstaltungs_richter instance) {
		log.debug("attaching dirty veranstaltungs_richter instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(veranstaltungs_richter instance) {
		log.debug("attaching clean veranstaltungs_richter instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(veranstaltungs_richter persistentInstance) {
		log.debug("deleting veranstaltungs_richter instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public veranstaltungs_richter merge(veranstaltungs_richter detachedInstance) {
		log.debug("merging veranstaltungs_richter instance");
		try {
			veranstaltungs_richter result = (veranstaltungs_richter) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public veranstaltungs_richter findById(int id) {
		log.debug("getting veranstaltungs_richter instance with id: " + id);
		try {
			veranstaltungs_richter instance = (veranstaltungs_richter) sessionFactory.getCurrentSession()
					.get("veranstaltungs_richter", id);
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

	public List findByExample(veranstaltungs_richter instance) {
		log.debug("finding veranstaltungs_richter instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("veranstaltungs_richter")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
