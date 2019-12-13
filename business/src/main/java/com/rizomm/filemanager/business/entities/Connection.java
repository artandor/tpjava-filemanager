package com.rizomm.filemanager.business.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String host;

    private String userEmail;

    public abstract Connection getConnection();

    public abstract Connection closeConnection();
}
