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
 * Home object for domain model class ausstellungscup_komprimiert.
 * @see .ausstellungscup_komprimiert
 * @author Hibernate Tools
 */
public class ausstellungscup_komprimiertHome {

	private static final Log log = LogFactory.getLog(ausstellungscup_komprimiertHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(ausstellungscup_komprimiert transientInstance) {
		log.debug("persisting ausstellungscup_komprimiert instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ausstellungscup_komprimiert instance) {
		log.debug("attaching dirty ausstellungscup_komprimiert instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ausstellungscup_komprimiert instance) {
		log.debug("attaching clean ausstellungscup_komprimiert instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ausstellungscup_komprimiert persistentInstance) {
		log.debug("deleting ausstellungscup_komprimiert instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ausstellungscup_komprimiert merge(ausstellungscup_komprimiert detachedInstance) {
		log.debug("merging ausstellungscup_komprimiert instance");
		try {
			ausstellungscup_komprimiert result = (ausstellungscup_komprimiert) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ausstellungscup_komprimiert findById(int id) {
		log.debug("getting ausstellungscup_komprimiert instance with id: " + id);
		try {
			ausstellungscup_komprimiert instance = (ausstellungscup_komprimiert) sessionFactory.getCurrentSession()
					.get("ausstellungscup_komprimiert", id);
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

	public List findByExample(ausstellungscup_komprimiert instance) {
		log.debug("finding ausstellungscup_komprimiert instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("ausstellungscup_komprimiert")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
