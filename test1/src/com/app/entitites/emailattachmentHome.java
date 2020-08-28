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
 * Home object for domain model class emailattachment.
 * @see .emailattachment
 * @author Hibernate Tools
 */
public class emailattachmentHome {

	private static final Log log = LogFactory.getLog(emailattachmentHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(emailattachment transientInstance) {
		log.debug("persisting emailattachment instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(emailattachment instance) {
		log.debug("attaching dirty emailattachment instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(emailattachment instance) {
		log.debug("attaching clean emailattachment instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(emailattachment persistentInstance) {
		log.debug("deleting emailattachment instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public emailattachment merge(emailattachment detachedInstance) {
		log.debug("merging emailattachment instance");
		try {
			emailattachment result = (emailattachment) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public emailattachment findById(int id) {
		log.debug("getting emailattachment instance with id: " + id);
		try {
			emailattachment instance = (emailattachment) sessionFactory.getCurrentSession().get("emailattachment", id);
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

	public List findByExample(emailattachment instance) {
		log.debug("finding emailattachment instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("emailattachment")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
