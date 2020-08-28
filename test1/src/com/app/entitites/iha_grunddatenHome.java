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
 * Home object for domain model class iha_grunddaten.
 * @see .iha_grunddaten
 * @author Hibernate Tools
 */
public class iha_grunddatenHome {

	private static final Log log = LogFactory.getLog(iha_grunddatenHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(iha_grunddaten transientInstance) {
		log.debug("persisting iha_grunddaten instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(iha_grunddaten instance) {
		log.debug("attaching dirty iha_grunddaten instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(iha_grunddaten instance) {
		log.debug("attaching clean iha_grunddaten instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(iha_grunddaten persistentInstance) {
		log.debug("deleting iha_grunddaten instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public iha_grunddaten merge(iha_grunddaten detachedInstance) {
		log.debug("merging iha_grunddaten instance");
		try {
			iha_grunddaten result = (iha_grunddaten) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public iha_grunddaten findById(iha_grunddatenId id) {
		log.debug("getting iha_grunddaten instance with id: " + id);
		try {
			iha_grunddaten instance = (iha_grunddaten) sessionFactory.getCurrentSession().get("iha_grunddaten", id);
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

	public List findByExample(iha_grunddaten instance) {
		log.debug("finding iha_grunddaten instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("iha_grunddaten")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
