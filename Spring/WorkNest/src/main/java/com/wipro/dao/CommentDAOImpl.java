package com.wipro.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import com.wipro.entities.Comment;
import com.wipro.util.HibernateUtil;

@Repository
public class CommentDAOImpl implements CommentDAO {

    @Override
    public Integer save(Comment comment) {
        Session session = null;
        Transaction tx = null;
        Integer id = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            id = (Integer) session.save(comment);
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
    public void update(Comment comment) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(comment);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void delete(Integer id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Comment comment = session.get(Comment.class, id);
            if (comment != null) session.remove(comment);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Comment findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Comment.class, id);
        }
    }

    @Override
    public List<Comment> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Comment order by createdAt desc", Comment.class).list();
        }
    }

    @Override
    public List<Comment> findByTask(Integer taskId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Comment> query = session.createQuery(
                "from Comment where task.id = :taskId order by createdAt desc", Comment.class);
            query.setParameter("taskId", taskId);
            return query.list();
        }
    }

    @Override
    public List<Comment> findByUser(Integer userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Comment> query = session.createQuery(
                "from Comment where user.id = :userId order by createdAt desc", Comment.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }
}