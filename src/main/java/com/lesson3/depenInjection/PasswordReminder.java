package com.lesson3.depenInjection;

public class PasswordReminder {
    private DbConnector dbConnector;

    public PasswordReminder(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void sendPassword(){
        //logic
    }
}
