package com.employee;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import jakarta.persistence.PersistenceException;

public class EmployeeHibernateApi {

    // Connection details live outside the code; override the location with -Demployee.config=<path>
    private static final String CONFIG_PATH_PROPERTY = "employee.config";
    private static final String DEFAULT_CONFIG_PATH = "src/target/employee.properties";

    private static SessionFactory sessionFactory;

    public void insert(EmployeePojo p) throws SQLException {
        try {
            getSessionFactory().inTransaction(session -> session.persist(p));
        } catch (PersistenceException e) {
            throw new SQLException("Could not save employee with id " + p.getId(), e);
        }
    }

    // Returns null when no employee has this id
    public EmployeePojo select(int id) throws SQLException {
        try {
            return getSessionFactory().fromTransaction(session -> session.find(EmployeePojo.class, id));
        } catch (PersistenceException e) {
            throw new SQLException("Could not read employee with id " + id, e);
        }
    }

    public List<EmployeePojo> selectAll() throws SQLException {
        try {
            return getSessionFactory().fromTransaction(session ->
                    session.createSelectionQuery("from EmployeePojo order by id", EmployeePojo.class).getResultList());
        } catch (PersistenceException e) {
            throw new SQLException("Could not read employee list", e);
        }
    }

    public void update(int id, EmployeePojo p) throws SQLException {
    //TODO implement this method
    }

    // Returns false when no employee has this id
    public boolean delete(int id) throws SQLException {
        try {
            return getSessionFactory().fromTransaction(session -> {
                EmployeePojo p = session.find(EmployeePojo.class, id);
                if (p == null) {
                    return false;
                }
                session.remove(p);
                return true;
            });
        } catch (PersistenceException e) {
            throw new SQLException("Could not delete employee with id " + id, e);
        }
    }

    private static synchronized SessionFactory getSessionFactory() throws SQLException {
        if (sessionFactory == null) {
            Properties db = loadConnectionProperties();
            // Settings, URL, username and mappings come from hibernate.cfg.xml; the password from employee.properties
            sessionFactory = new Configuration()
                    .configure()
                    .setProperty("hibernate.connection.password", db.getProperty("jdbc.password"))
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    private static Properties loadConnectionProperties() throws SQLException {
        Path path = Path.of(System.getProperty(CONFIG_PATH_PROPERTY, DEFAULT_CONFIG_PATH));
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(path)) {
            props.load(in);
        } catch (IOException e) {
            throw new SQLException("Could not read database config from " + path.toAbsolutePath(), e);
        }
        return props;
    }
}
