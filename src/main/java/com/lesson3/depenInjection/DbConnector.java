package com.lesson3.depenInjection;

public interface DbConnector {
    void connect();
    void save();
    void disconnect();
}
