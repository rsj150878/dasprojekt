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
 * Home object for domain model class ausstellungscup_basis.
 * @see .ausstellungscup_basis
 * @author Hibernate Tools
 */
public class AusstellungsCupBasisDAO {

	private static final Log log = LogFactory.getLog(AusstellungsCupBasisDAO.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(AusstellungsCupBasis transientInstance) {
		log.debug("persisting ausstellungscup_basis instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AusstellungsCupBasis instance) {
		log.debug("attaching dirty ausstellungscup_basis instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AusstellungsCupBasis instance) {
		log.debug("attaching clean ausstellungscup_basis instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AusstellungsCupBasis persistentInstance) {
		log.debug("deleting ausstellungscup_basis instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AusstellungsCupBasis merge(AusstellungsCupBasis detachedInstance) {
		log.debug("merging ausstellungscup_basis instance");
		try {
			AusstellungsCupBasis result = (AusstellungsCupBasis) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AusstellungsCupBasis findById(int id) {
		log.debug("getting ausstellungscup_basis instance with id: " + id);
		try {
			AusstellungsCupBasis instance = (AusstellungsCupBasis) sessionFactory.getCurrentSession()
					.get("ausstellungscup_basis", id);
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

	public List findByExample(AusstellungsCupBasis instance) {
		log.debug("finding ausstellungscup_basis instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("ausstellungscup_basis")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
