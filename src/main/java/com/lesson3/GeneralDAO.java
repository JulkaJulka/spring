package com.lesson3;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public abstract class GeneralDAO<T> {
    private static SessionFactory sessionFactory;
    private static  String hql = "";


    public GeneralDAO() {
    }

   public abstract String setHql();
    public abstract String setHqlDelEntity();


    public T save(T t) throws HibernateException {

        Transaction tr = null;
        try (Session session = createSessionFactory().openSession()) {

            tr = session.getTransaction();
            tr.begin();

            session.save(t);

            tr.commit();
            return t;

        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new HibernateException("Save is failed");
        }
    }

    public T update(T t) throws HibernateException {

        Transaction tr = null;
        try (Session session = createSessionFactory().openSession()) {

            tr = session.getTransaction();
            tr.begin();

            session.update(t);

            tr.commit();
            return t;

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

            Query queryDelHt = session.createQuery(setHqlDelEntity());
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

    public T findById(long id) {
        try (Session session = createSessionFactory().openSession()) {

            Query query = session.createQuery(setHql());
            query.setParameter("ID", id);

            query.getResultList();

            if (query.uniqueResult() == null)
                return null;


            return (T) query.getSingleResult();

        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new HibernateException("Something went wrong");
        }
    }


    public static SessionFactory createSessionFactory() {

        //singleton pattern
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }
}
