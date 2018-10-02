package com.lesson3;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class FileDAO extends GeneralDAO<File> {
    public static final String FIND_FL_BY_ID_FILE = "FROM File WHERE ID = :ID ";
    public static final String DELETE_FL_BY_ID_FILE = "DELETE FROM File WHERE ID = :ID";

    public FileDAO() {
    }

    @Override
    public String setHql() {
        return FIND_FL_BY_ID_FILE;
    }

    @Override
    public String setHqlDelEntity() {
        return DELETE_FL_BY_ID_FILE;
    }

    public File findById(Storage storage, long id) {

        try (Session session = createSessionFactory().openSession()) {

            Query query = session.createQuery("SELECT f FROM File f JOIN FETCH f.storage s WHERE s.id =  " + storage.getId() +
                    " and f.id = " + id);

            if (query.uniqueResult() == null)
                return null;

            return (File) query.getSingleResult();

        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new HibernateException("Something went wrong");

        }
    }
}
