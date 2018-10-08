package com.lesson3.hwStorageFile;

import org.springframework.beans.factory.annotation.Autowired;

public class FileService extends GeneralService<File> {
    @Autowired
    private FileDAO fileDAO;

    public FileService() {
    }

    @Override
    public File findObjectById(long id) {
        return fileDAO.findById(id);
    }

}
