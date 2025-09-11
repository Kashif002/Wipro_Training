package com.wipro.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import com.wipro.entities.Task;
import com.wipro.util.HibernateUtil;

@Repository
public class TaskDAOImpl implements TaskDAO {

    @Override
    public Integer save(Task task) {
        Session session = null;
        Transaction tx = null;
        Integer id = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            id = (Integer) session.save(task);
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
    public void update(Task task) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.merge(task);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Task t = session.get(Task.class, id);
            if (t != null) session.remove(t);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Task findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Task.class, id);
        }
    }

    @Override
    public List<Task> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Task order by id desc", Task.class).list();
        }
    }

    @Override
    public List<Task> findByUser(Integer userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Task> q = session.createQuery(
                "from Task where assignedTo.id = :uid order by id desc", Task.class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }


    @Override
    public List<Task> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Task> query = session.createQuery(
                "from Task where status = :status order by dueDate", Task.class);
            query.setParameter("status", status);
            return query.list();
        }
    }

    @Override
    public List<Task> findByAssignedBy(Integer adminId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Task> query = session.createQuery(
                "from Task where assignedBy.id = :adminId order by dueDate", Task.class);
            query.setParameter("adminId", adminId);
            return query.list();
        }
    }
}