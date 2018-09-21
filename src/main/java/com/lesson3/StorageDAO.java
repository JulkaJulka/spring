package com.lesson3;

public class StorageDAO extends GeneralDAO<Storage> {
    public static final String FIND_ST_BY_ID_STORAGE = "FROM Storage WHERE ID = :ID ";
    public static final String DELETE_ST_BY_ID_STORAGE = "DELETE FROM Storage WHERE ID = :ID";

    public Storage findById(long id){
        return findById(FIND_ST_BY_ID_STORAGE, id);
    }

    public void delete(long id) {
        delete(DELETE_ST_BY_ID_STORAGE, id);
    }

}
