package com.wipro.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import org.h2.tools.Server;


import com.wipro.entity.ReportCard;
import com.wipro.entity.Student;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.wipro")
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        /*
         * ------------------- H2 Database -------------------
         * Uncomment these lines if you want to use H2
         */
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");

        /*
         * ------------------- MySQL Database -------------------
         * Uncomment these lines if you want to use MySQL
         */
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/HIBORM");
        dataSource.setUsername("root");
        dataSource.setPassword("My@SQL*%$23");

        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setAnnotatedClasses(Student.class, ReportCard.class);
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    
    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();

        /*
         * ------------------- H2 Hibernate Dialect -------------------
         * Use this with H2 Database
         */
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
//        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        /*
         * ------------------- MySQL Hibernate Dialect -------------------
         * Use this with MySQL Database
         */

        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");

        return hibernateProperties;
    }


    @Bean
    public HibernateTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

//    /*
//     * ------------------- H2 Web Console -------------------
//     * Only needed when using H2
//     * URL: http://localhost:8082
//     */
//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public org.h2.tools.Server h2WebConsole() throws SQLException {
//        System.out.println("H2 started at http://localhost:8082");
//        return org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
//    }
}
