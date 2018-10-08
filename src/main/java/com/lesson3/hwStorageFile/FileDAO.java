package com.lesson3.hwStorageFile;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class FileDAO extends GeneralDAO<File> {
    public static final String FIND_FL_BY_ID_FILE = "FROM File WHERE ID = :ID ";
    public static final String DELETE_FL_BY_ID_FILE = "DELETE FROM File WHERE ID = :ID";

    @Autowired
    private StorageDAO storageDAO;

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
    public File save(Storage storage, File file) throws BadRequestException {

        Transaction tr = null;
        try (Session session = GeneralDAO.createSessionFactory().openSession()) {
            tr = session.getTransaction();
            tr.begin();

            if (  file.getStorage() != null) {
                throw new BadRequestException("File id " + file.getId() + " already is busy by Storage " + file.getStorage().getId());
            }

            file.setStorage(storage);
            update(file);

           storage.setStorageSize(storage.getStorageSize() + file.getSize());
            storageDAO.update(storage);

            tr.commit();

            return file;


        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new HibernateException("Save is failed");
        }
    }

}
