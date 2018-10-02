package com.lesson3;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class GeneralService<T> {

    @Autowired
    private StorageDAO storageDAO;
    @Autowired
    private FileDAO fileDAO;
    //  @Autowired
    private GeneralDAO generalDAO;
    private static long SIZEMAX_STORAGE = 3000l;

    public GeneralService() {
    }

    public T findObjectById(long id) throws BadRequestException {
        if (id <= 0) throw new BadRequestException("Wrong id " + id);

        T t = (T) generalDAO.findById(id);

        return t;
    }


    public File save(Storage storage, File file) throws BadRequestException {

        validateInputData(storage, file);

        Transaction tr = null;
        try (Session session = GeneralDAO.createSessionFactory().openSession()) {
            tr = session.getTransaction();
            tr.begin();

            Storage foundStorage = storageDAO.findById(storage.getId());

            if (foundStorage == null)
                throw new BadRequestException("Storage with id " + storage.getId() + " doesn't exist in DB");

            File foundFile = fileDAO.findById(file.getId());

            if (foundFile == null) {
                checkLimitation(foundStorage, file);
                if (file.getStorage() == null) {
                    file.getStorage().setId(storage.getId());
                    fileDAO.save(file);
                    foundStorage.setStorageSize(foundStorage.getStorageSize() + file.getSize());
                    storageDAO.update(foundStorage);
                    tr.commit();
                    return file;
                }
            }
            if (foundFile.getStorage() != null) {
                throw new BadRequestException("File id " + file.getId() + " already is busy by Storage " + foundFile.getStorage().getId());
            }
            checkLimitation(foundStorage, foundFile);

            foundFile.setStorage(foundStorage);
            fileDAO.update(foundFile);

            foundStorage.setStorageSize(foundStorage.getStorageSize() + foundFile.getSize());
            storageDAO.update(foundStorage);

            tr.commit();

            return foundFile;


        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new HibernateException("Save is failed");
        }
    }

    public void delete(Storage storage, File file) throws BadRequestException {
        validateInputData(storage, file);

        Transaction tr = null;
        try (Session session = GeneralDAO.createSessionFactory().openSession()) {
            tr = session.getTransaction();
            tr.begin();

            File deleteFile = fileDAO.findById(storage, file.getId());
            if (deleteFile.equals(file)) {
                deleteFile.setStorage(null);
                fileDAO.update(deleteFile);

                Storage storageFrom = storageDAO.findById(storage.getId());
                storageFrom.setStorageSize(storageFrom.getStorageSize() - deleteFile.getSize());
                storageDAO.update(storageFrom);
            } else {
                throw new BadRequestException("File id " + file.getId() + " name " + file.getName() + " doesn't exist in storage id " + storage.getId());
            }
            tr.commit();
        } catch ( HibernateException e) {
            tr.rollback();
            System.err.println(e.getMessage());
            throw new HibernateException("Something went wrong");
        }
    }

    public File transferFile(Storage storageFrom, Storage storageTo, long id) throws BadRequestException {
        if(storageFrom == null)
            throw new BadRequestException("Wrong data of storage");
        validateInputData(storageTo,fileDAO.findById(id));

        Transaction tr = null;
        try (Session session = GeneralDAO.createSessionFactory().openSession()) {
            tr = session.getTransaction();
            tr.begin();

            File transferFile = fileDAO.findById(storageFrom, id);
            if (transferFile == null)
                throw new BadRequestException("File with id " + id + " is not found in Storage " + storageFrom.getId());


            transferFile. getStorage().setId(storageTo.getId());
            fileDAO.update(transferFile);

            storageFrom.setStorageSize(storageFrom.getStorageSize() - transferFile.getSize());
            storageDAO.update(storageFrom);

            storageTo.setStorageSize(storageTo.getStorageSize() + transferFile.getSize());
            storageDAO.update(storageTo);

            tr.commit();

            return transferFile;

        } catch (HibernateException e) {
            tr.rollback();
            System.err.println(e.getMessage());
            throw new HibernateException("Something went wrong");
        }
    }

    public List<File> transferAll(Storage storageFrom, Storage storageTo) throws HibernateException, BadRequestException {
        if (storageFrom == null || storageTo == null)
            throw new BadRequestException("You enter wrong data");

        Transaction tr = null;
        try (Session session = GeneralDAO.createSessionFactory().openSession()) {
            tr = session.getTransaction();
            tr.begin();

            Storage storageDBFrom = storageDAO.findById(storageFrom.getId());
            Storage storageDBTo = storageDAO.findById(storageTo.getId());

            if (storageDBFrom == null)
                throw new BadRequestException("Storage id " + storageFrom.getId() + " is not found in DB");
            if (storageDBTo == null)
                throw new BadRequestException("Storage id " + storageTo.getId() + " is not found in DB");

            if (storageDBFrom.getStorageSize() > SIZEMAX_STORAGE - storageDBTo.getStorageSize())
                throw new BadRequestException("Size of transfer files exceeds free size of storageTo " + storageTo.getId());


            Query queryFindFilesByStorage = session.createQuery("SELECT f FROM File f JOIN FETCH f.storage s WHERE s.id =  " + storageFrom.getId());

            List<File> transferFiles= queryFindFilesByStorage.getResultList();

            List<File> filesStorageFrom = new ArrayList<>();

            for (File fl : transferFiles){
               if(fileDAO.findById(storageDBTo, fl.getId()) != null)
                   throw new BadRequestException("Storage id " + storageDBTo.getId() + " can't contains the same files id " + fl.getId());
                checkLimitation(storageDBTo, fl);

                filesStorageFrom.add(transferFile(storageDBFrom, storageDBTo, fl.getId()));
            }
            tr.commit();
            return filesStorageFrom;

        } catch (HibernateException e) {
            tr.rollback();
            System.err.println(e.getMessage());
            throw new HibernateException("Something went wrong");
        }
    }


    public boolean checkFormatsSupported(Storage storage, File file) {

        if (file == null || storage.getFormatsSupported() == null)
            return false;
        boolean status = true;
        String[] arFormats = storage.getFormatsSupported().split(",");
        for (String el : arFormats) {
            if (el.contains(file.getFormat())) {
                status = true;
                break;
            } else {
                status = false;
            }
        }
        return status;
    }

    public static boolean checkIdStorage(Storage storage) {
        if (storage == null)
            return false;
        if (storage.getId() <= 0)
            return false;
        return true;

    }

    public void validateInputData(Storage storage, File file) throws BadRequestException {
        if (storage == null)
            throw new BadRequestException("Storage is not detected. Try again");
        if (file == null) {
            throw new BadRequestException("File is not detected. Try again ");

        }
    }

    public boolean checkLimitation(Storage storage, File file) throws BadRequestException {
        if (file == null)
            throw new BadRequestException("Putted file  is not detected");
        if (storage == null)
            throw new BadRequestException("Wrong data of storage");
        if (file.getStorage() != null && file.getStorage().getId() == storage.getId())
            throw new BadRequestException("File id " + file.getId() + " already exist in Storage id " + file.getStorage().getId());
        if (storage.getStorageSize() + file.getSize() > SIZEMAX_STORAGE)
            throw new BadRequestException("Not enough space in storage id " + storage.getId());
        if (!checkFormatsSupported(storage, file))
            throw new BadRequestException("Format " + file.getFormat() + " is not supported by storage " + storage.getId());
        if (!checkIdStorage(storage))
            throw new BadRequestException("Storage Id " + storage.getId() + " is wrong.");
        return true;
    }

}
