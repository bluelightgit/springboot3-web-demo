package com.mySpring.demo.Configuration.Bean;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class AppConfig {

    
    // @Bean
    // @Override
    // public LocalSessionFactoryBean entityManagerFactory extends HibernateExceptionTranslator
    //     implements FactoryBean<SessionFactory>, ResourceLoaderAware, BeanFactoryAware, InitializingBean, DisposableBean {
        
    //     LocalSessionFactoryBean em = new LocalSessionFactoryBean();
    //     // em.setDataSource(dataSource());
    //     // em.setPackagesToScan(new String[] { "com.mySpring.demo.Models" });

    //     // JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    //     // em.setJpaVendorAdapter(vendorAdapter);
    //     // em.setJpaProperties(additionalProperties());

    //     return em;
    // }

    // @Bean
    // public DataSource dataSource(){
    //     DriverManagerDataSource dataSource = new DriverManagerDataSource();
    //     try {
    //         dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    //         dataSource.setUrl("jdbc:mysql://localhost:3306/mydatabase");
    //         dataSource.setUsername("root");
    //         dataSource.setPassword("123456");
    //     } catch (Exception e) {
    //         System.out.println(e);
    //         System.exit(0);
    //     }
    //     return dataSource;
    // }

    // Properties additionalProperties() {
    //     Properties properties = new Properties();
    //     properties.setProperty("hibernate.hbm2ddl.auto", "update");
    //     properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

    //     return properties;
    // }
}
