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
 * Home object for domain model class Kurskartenbesuche.
 * @see .Kurskartenbesuche
 * @author Hibernate Tools
 */
public class KurskartenbesucheHome {

	private static final Log log = LogFactory.getLog(KurskartenbesucheHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Kurskartenbesuche transientInstance) {
		log.debug("persisting Kurskartenbesuche instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Kurskartenbesuche instance) {
		log.debug("attaching dirty Kurskartenbesuche instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Kurskartenbesuche instance) {
		log.debug("attaching clean Kurskartenbesuche instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Kurskartenbesuche persistentInstance) {
		log.debug("deleting Kurskartenbesuche instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Kurskartenbesuche merge(Kurskartenbesuche detachedInstance) {
		log.debug("merging Kurskartenbesuche instance");
		try {
			Kurskartenbesuche result = (Kurskartenbesuche) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Kurskartenbesuche findById(int id) {
		log.debug("getting Kurskartenbesuche instance with id: " + id);
		try {
			Kurskartenbesuche instance = (Kurskartenbesuche) sessionFactory.getCurrentSession().get("Kurskartenbesuche",
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

	public List findByExample(Kurskartenbesuche instance) {
		log.debug("finding Kurskartenbesuche instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("Kurskartenbesuche")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
