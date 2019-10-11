package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import org.czekalski.userkeycloak.model.Order;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Data
public class OrderAddressCommand {


    private Long id;

    private String City;
    private String Street;


    private Integer houseNr;

    private Integer apartment;
    private String telephone;



    private String user;


    List<OrderCommand> orderCommands=new ArrayList<>();
}
