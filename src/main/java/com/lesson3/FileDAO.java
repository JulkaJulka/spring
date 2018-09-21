package com.lesson3;

public class FileDAO extends GeneralDAO<File> {
    public static final String FIND_FL_BY_ID_FILE = "FROM File WHERE ID = :ID ";
    public static final String DELETE_FL_BY_ID_FILE = "DELETE FROM File WHERE ID = :ID";


    public File findById(long id) {
        return findById(FIND_FL_BY_ID_FILE, id);
    }


    public void delete(long id) {
        delete(DELETE_FL_BY_ID_FILE, id);
    }
}
