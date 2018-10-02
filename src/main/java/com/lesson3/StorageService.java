package com.lesson3;

import org.springframework.beans.factory.annotation.Autowired;

public class StorageService extends GeneralService<Storage> {

    @Autowired
    private StorageDAO storageDAO;
    public StorageService() {
    }

    public Storage findObjectById(long id)  {

        return storageDAO.findById( id);
    }
}
