package org.czekalski.userkeycloak.model;

import lombok.Data;

import javax.persistence.*;


import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "status_table")
@Data
@Entity
public class Status {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    private  String name;

    private Boolean payed;


}
