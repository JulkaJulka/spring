package com.lesson3.hwStorageFile;

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
    private GeneralDAO generalDAO;

    public GeneralService() {
    }

    public File put(Storage storage, File file) throws BadRequestException {
        checkLimitation(storage, file);
        return fileDAO.put(storage, file);

    }

    public void delete(Storage storage, File file) throws BadRequestException {
        fileDAO.delete(storage, file);
    }

    public File transferFile(Storage storageFrom, Storage storageTo, long id) throws BadRequestException {
        if (findById(storageFrom, id) == null)
            throw new BadRequestException("File id " + id + "doesn't exist in Storage id " + storageFrom.getId());
        checkLimitation(storageTo, findById(storageFrom, id));
        return fileDAO.transferFile(storageFrom, storageTo, id);
    }

    public List<File> transferAll(Storage storageFrom, Storage storageTo) throws BadRequestException {
        checkLimitsFiles(storageTo, storageFrom.getFiles());
        return fileDAO.transferAll(storageFrom, storageTo);
    }

    public T findObjectById(long id) {

        T t = (T) generalDAO.findById(id);
        return t;
    }

    public File findById(Storage storage, Long id) {
        if (storage.getFiles() == null)
            return null;

        for (int i = 0; i < storage.getFiles().size(); i++) {
            for (File file : storage.getFiles()) {
                if (file != null && id.equals(file.getId())) {
                    return file;
                }
            }
        }
        return null;

    }

    public boolean checkLimitation(Storage storage, File file) throws BadRequestException {

        File putFile = findById(storage, file.getId());

        if (putFile != null)
            throw new BadRequestException("File with Id " + file.getId() +
                    " already exists in storage " + storage.getId() + " Storage can't save files with the same ID. ");
        if (!checkLengthFileName(file.getName()))
            throw new BadRequestException("Name of file id " + file.getId() + " is wrong");
        if (!checkFormatsSupported(storage, file))
            throw new BadRequestException("Format " + file.getFormat() + " is not supported by storage " + storage.getId());
        if (!checkSizeFileWithSizeStorage(storage, file))
            throw new BadRequestException("Not enough space in storage with Id " + storage.getId());
        return true;
    }

    public boolean checkSizeFileWithSizeStorage(Storage storage, File file) {
        if (file.getSize() > storage.getStorageSize())
            return false;
        return true;

    }

    public static boolean checkLengthFileName(String word) {

        if (word.isEmpty())
            return false;
        int limitLengthOfNameFile = 10;

        if (word.length() > limitLengthOfNameFile)
            return false;
        return true;
    }

    public boolean checkLimitsFiles(Storage storageTo, List<File> files) throws BadRequestException {
        checkFormatsFiles(storageTo, files);
        if (storageTo.getStorageSize() < files.size())
            throw new BadRequestException("Size of files exceed the empty size of storageTo " + storageTo.getId());
        return true;
    }

    public boolean checkFormatsFiles(Storage storageTo, List<File> files) throws BadRequestException {
        String str = storageTo.getFormatsSupported();
        String[] formats = str.split(",");
        String wrongFormat = "";
        int index = 0;

        for (File fl : files) {
            wrongFormat = fl.getFormat();
            for (String el : formats) {
                if (fl.getFormat().equals(el)) {
                    index++;
                    break;
                }
            }
        }
        if (index != files.size())
            throw new BadRequestException("Storage id " + storageTo.getId() + " is not supported format " + wrongFormat);

        return true;
    }

    public boolean checkFormatsSupported(Storage storage, File file) {

        String[] arFormats = storage.getFormatsSupported().split(",");
        for (String el : arFormats) {
            if (el.equals(file.getFormat()))
                return true;
        }
        return false;
    }
}
