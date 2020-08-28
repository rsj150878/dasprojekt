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
 * Home object for domain model class Import.
 * @see .Import
 * @author Hibernate Tools
 */
public class ImportHome {

	private static final Log log = LogFactory.getLog(ImportHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Import transientInstance){log.debug("persisting Import instance");try{sessionFactory.getCurrentSession().persist(transientInstance);log.debug("persist successful");}catch(

	RuntimeException re)
	{
		log.error("persist failed", re);
		throw re;
	}
	}

	public void attachDirty(
	Import instance){log.debug("attaching dirty Import instance");try{sessionFactory.getCurrentSession().saveOrUpdate(instance);log.debug("attach successful");}catch(

	RuntimeException re)
	{
		log.error("attach failed", re);
		throw re;
	}
	}

	public void attachClean(
	Import instance){log.debug("attaching clean Import instance");try{sessionFactory.getCurrentSession().lock(instance,LockMode.NONE);log.debug("attach successful");}catch(

	RuntimeException re)
	{
		log.error("attach failed", re);
		throw re;
	}
	}

	public void delete(
	Import persistentInstance){log.debug("deleting Import instance");try{sessionFactory.getCurrentSession().delete(persistentInstance);log.debug("delete successful");}catch(

	RuntimeException re)
	{
		log.error("delete failed", re);
		throw re;
	}}

	public
	Import merge(
	Import detachedInstance){log.debug("merging Import instance");try{
	Import result=(Import)sessionFactory.getCurrentSession().merge(detachedInstance);log.debug("merge successful");return result;}catch(
	RuntimeException re)
	{
		log.error("merge failed", re);
		throw re;
	}}

	public
	Import findById(
	ImportId id)
	{
        log.debug("getting Import instance with id: " + id);
        try {Import instance=(Import)sessionFactory.getCurrentSession().get("Import",id);if(instance==null){log.debug("get successful, no instance found");}else{log.debug("get successful, instance found");}return instance;}catch(
	RuntimeException re)
	{
		log.error("get failed", re);
		throw re;
	}
	}

	public List findByExample(
	Import instance){log.debug("finding Import instance by example");try{

	List results = sessionFactory.getCurrentSession()
                    .createCriteria("Import")
                    .add(Example.create(instance))
            .list();log.debug("find by example successful, result size: "+results.size());return results;}catch(
	RuntimeException re){log.error("find by example failed",re);throw re;
}}}
