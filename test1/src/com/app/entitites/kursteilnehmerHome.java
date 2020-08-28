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
 * Home object for domain model class kursteilnehmer.
 * @see .kursteilnehmer
 * @author Hibernate Tools
 */
public class kursteilnehmerHome {

	private static final Log log = LogFactory.getLog(kursteilnehmerHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(kursteilnehmer transientInstance) {
		log.debug("persisting kursteilnehmer instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(kursteilnehmer instance) {
		log.debug("attaching dirty kursteilnehmer instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(kursteilnehmer instance) {
		log.debug("attaching clean kursteilnehmer instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(kursteilnehmer persistentInstance) {
		log.debug("deleting kursteilnehmer instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public kursteilnehmer merge(kursteilnehmer detachedInstance) {
		log.debug("merging kursteilnehmer instance");
		try {
			kursteilnehmer result = (kursteilnehmer) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public kursteilnehmer findById(int id) {
		log.debug("getting kursteilnehmer instance with id: " + id);
		try {
			kursteilnehmer instance = (kursteilnehmer) sessionFactory.getCurrentSession().get("kursteilnehmer", id);
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

	public List findByExample(kursteilnehmer instance) {
		log.debug("finding kursteilnehmer instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("kursteilnehmer")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
