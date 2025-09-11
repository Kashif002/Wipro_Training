package com.wipro;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.wipro.model.Course;
import com.wipro.model.ReportCard;
import com.wipro.model.Student;
import com.wipro.util.HibernateUtil;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	// to obtain/get the session from sessionfactory (It can be multiple session based on multiple services or work)
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		// To create a  transaction ( Transient + Persistent) -- Transient when you do not to reflect and save in backend  but persistent to save into a backend
	   Transaction tx = session.beginTransaction();
	   
//	   Student s =  new Student("Niti");
//	   
//	   s.addCourse(new Course("Java"));
//	   s.addCourse(new Course("PD"));
//	   
//	   ReportCard rc = new ReportCard(450);     
//	   s.setReportcard(rc);
//	   	   
//	   session.persist(s);
	   
	   Student s1 =  new Student("Kashif");
	   s1.addCourse(new Course("Java"));
	   s1.addCourse(new Course("Python"));
	   s1.addCourse(new Course("C++"));
	   s1.addCourse(new Course("SQL"));
	   
	   ReportCard rc1 = new ReportCard(395);     
	   s1.setReportcard(rc1);
	   	   
	   session.persist(s1);
	   
	   tx.commit();
	   
	   session.close();
	   
	   Session getSession = HibernateUtil.getSessionFactory().openSession();
	   Student stud = getSession.get(Student.class,1L);
	   System.out.println("Student Details :" + s1.getName());
	   s1.getCourses().forEach(c->System.out.println("Courses enrolled for : "+ c.getTitle()));
	   getSession.close(); 
    }
}