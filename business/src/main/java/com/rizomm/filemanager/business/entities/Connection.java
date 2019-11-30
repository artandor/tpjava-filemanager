package com.rizomm.filemanager.business.entities;

public interface Connection {
    Connection getConnection();

    Connection closeConnection();
}
