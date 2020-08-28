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
 * Home object for domain model class veranstaltungs_teilnehmer.
 * @see .veranstaltungs_teilnehmer
 * @author Hibernate Tools
 */
public class veranstaltungs_teilnehmerHome {

	private static final Log log = LogFactory.getLog(veranstaltungs_teilnehmerHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(veranstaltungs_teilnehmer transientInstance) {
		log.debug("persisting veranstaltungs_teilnehmer instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(veranstaltungs_teilnehmer instance) {
		log.debug("attaching dirty veranstaltungs_teilnehmer instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(veranstaltungs_teilnehmer instance) {
		log.debug("attaching clean veranstaltungs_teilnehmer instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(veranstaltungs_teilnehmer persistentInstance) {
		log.debug("deleting veranstaltungs_teilnehmer instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public veranstaltungs_teilnehmer merge(veranstaltungs_teilnehmer detachedInstance) {
		log.debug("merging veranstaltungs_teilnehmer instance");
		try {
			veranstaltungs_teilnehmer result = (veranstaltungs_teilnehmer) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public veranstaltungs_teilnehmer findById(int id) {
		log.debug("getting veranstaltungs_teilnehmer instance with id: " + id);
		try {
			veranstaltungs_teilnehmer instance = (veranstaltungs_teilnehmer) sessionFactory.getCurrentSession()
					.get("veranstaltungs_teilnehmer", id);
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

	public List findByExample(veranstaltungs_teilnehmer instance) {
		log.debug("finding veranstaltungs_teilnehmer instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("veranstaltungs_teilnehmer")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
