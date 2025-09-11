package com.wipro.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import com.wipro.entities.User;
import com.wipro.util.HibernateUtil;

@Repository
public class UserDAOImpl implements UserDAO {

    @Override
    public Integer save(User user) {
        Session session = null;
        Transaction tx = null;
        Integer id = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            id = (Integer) session.save(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
        return id;
    }


    @Override
    public void update(User user) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();

            session.merge(user);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(Integer id) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();

            User u = session.get(User.class, id);
            if (u != null) session.remove(u);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public User findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        User u = session.get(User.class, id);
        tx.commit();
        return u;
    }

    @Override
    public User findByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Query<User> q = session.createQuery("from User where email = :em", User.class);
        q.setParameter("em", email);
        User u = q.uniqueResult();
        tx.commit();
        return u;
    }

    @Override
    public User findByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Query<User> q = session.createQuery("from User where name = :nm", User.class);
        q.setParameter("nm", name);
        User u = q.uniqueResult();
        tx.commit();
        return u;
    }

    @Override
    public List<User> findAll() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List<User> list = session.createQuery("from User order by id desc", User.class).list();
        tx.commit();
        return list;
    }
}
