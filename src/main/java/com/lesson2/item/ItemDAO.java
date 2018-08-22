package com.lesson2.item;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class ItemDAO {
    public static final String FIND_ITM_BY_ID_ITEM = "FROM Item WHERE id = :ID ";
    public static final String FIND_EQUAL_ITEM = "FROM Item WHERE name = :NAME AND dateCreated = :DATE_CREATED AND " +
            "lastUpdatedDate = :LAST_UPDATED_DATE";
    public static final String DELETE_ITM_BY_ID_ITEM = "DELETE FROM Item WHERE id = :ID";

    private SessionFactory sessionFactory;

    public ItemDAO() {
    }

    public Item save(Item item) throws HibernateException {

        Transaction tr = null;
        try (Session session = createSessionFactory().openSession()) {

            tr = session.getTransaction();

            tr.begin();

            session.save(item);

            tr.commit();
            return item;

        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new HibernateException("Save is failed");
        }
    }

    public Item update(Item item)  {

        Transaction tr = null;
        try (Session session = createSessionFactory().openSession()) {

            tr = session.getTransaction();

            tr.begin();

            session.update(item);

            tr.commit();
            return item;

        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new HibernateException("Update is failed");
        }

    }

    public void delete(long id) {
        Transaction tr = null;
        try (Session session = createSessionFactory().openSession()) {

            tr = session.getTransaction();
            tr.begin();

            Query queryDelHt = session.createQuery(DELETE_ITM_BY_ID_ITEM);
            queryDelHt.setParameter("ID", id);
            queryDelHt.executeUpdate();

            tr.commit();

        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new HibernateException("Delete is failed");
        }
    }

    public Item findById(Long id) throws HibernateException {
        try (Session session = createSessionFactory().openSession()) {

            Query query = session.createQuery(FIND_ITM_BY_ID_ITEM);
            query.setParameter("ID", id);

            if (query.uniqueResult() == null)
                return null;

            Item item = (Item) query.getSingleResult();

            return item;

        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new HibernateException("Something went wrong");
        }
    }

    public Item findEqualItemInDB(Item item) throws HibernateException {
        Transaction tr = null;
        try (Session session = createSessionFactory().openSession()) {

            Query query = session.createQuery(FIND_EQUAL_ITEM);
            query.setParameter("NAME", item.getName());
            query.setParameter("DATE_CREATED", item.getDateCreated());
            query.setParameter("LAST_UPDATED_DATE", item.getLastUpdatedDate());

            query.getResultList();


            if (query.uniqueResult() == null)
                return null;
            Item entity = (Item) query.getSingleResult();

            return entity;

        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new HibernateException("Something went wrong");
        }
    }


    public SessionFactory createSessionFactory() {

        //singleton pattern
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }
}
