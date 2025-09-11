package com.wipro.doa;

import com.wipro.model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Student student) {
        Session session = sessionFactory.getCurrentSession();
        session.save(student);
    }

    @Override
    public Student findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Student.class, id);
    }

    @Override
    public List<Student> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Student", Student.class).getResultList();
    }

    @Override
    public void update(Student student) {
        Session session = sessionFactory.getCurrentSession();
        session.update(student);
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Student student = session.get(Student.class, id);
        if (student != null) {
            session.delete(student);
        }
    }
}