package com.lesson3;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;



public class GeneralService <T>{

    @Autowired
    private FileDAO fileDAO;
    @Autowired
    private StorageDAO storageDAO;
    private GeneralDAO generalDAO;


    public GeneralService() {
    }

    public File findFileById(File file){
        return fileDAO.findById(file.getId());

    }

    public Storage findStorageById(long id){
        return storageDAO.findById(id);

    }
    public File save(Storage storage, File file) throws BadRequestException {

        if (file == null)
            throw new BadRequestException("Putted file  is not detected");

        Transaction tr = null;
        try (Session session = GeneralDAO.createSessionFactory().openSession()){
            tr = session.getTransaction();
            tr.begin();

            Storage foundStorage = storageDAO.findById(storage.getId());

            if (foundStorage == null)
                throw new BadRequestException("Storage with id " + storage.getId() + " doesn't exist in DB");

            File foundFile = fileDAO.findById(file.getId());

            if (foundFile == null) {
                checkLimitation(foundStorage, file);
                if (file.getStorage().getId() == 0) {
                    file.getStorage().setId(storage.getId());
                    fileDAO.save(file);
                    foundStorage.setStorageSize(foundStorage.getStorageSize() + file.getSize());
                    storageDAO.update(foundStorage);
                    tr.commit();
                    return file;
                }
            }

            checkLimitation(foundStorage, foundFile);
            if (foundFile.getStorage().getId() != 0) {
                return null;
            }
            foundFile.getStorage().setId(storage.getId());
            fileDAO.update(foundFile);

            foundStorage.setStorageSize(foundStorage.getStorageSize() + foundFile.getSize());
            storageDAO.update(foundStorage);

            tr.commit();
            return foundFile;


        } catch ( HibernateException e) {
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new HibernateException("Save is failed");
        }
    }


    public boolean checkFormatsSupported(Storage storage, File file) throws BadRequestException {

        if (file == null || storage.getFormatsSupported() == null)
            return false;
        boolean status = true;
        for (String el : storage.getFormatsSupported()) {
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

    public boolean checkLimitation(Storage storage, File file) throws BadRequestException {
        if (file == null)
            throw new BadRequestException("Putted file  is not detected");
        if (storage == null)
            throw new BadRequestException("Wrong data of storage");
        if (file.getStorage().getId() == storage.getId())
            throw new BadRequestException("File id " + file.getId() + " already exist in Storage id " + file.getStorage().getId());
        if (storage.getStorageSize() + file.getSize() > 3000)
            throw new BadRequestException("Not enough space in storage id " + storage.getId());
        if (!checkFormatsSupported(storage, file))
            throw new BadRequestException("Format " + file.getFormat() + " is not supported by storage " + storage.getId());
        if (!checkIdStorage(storage))
            throw new BadRequestException("Storage Id " + storage.getId() + " is wrong.");
        return true;
    }

}
