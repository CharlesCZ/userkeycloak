package org.czekalski.userkeycloak.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;


@Table(name = "order_table")
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "user",callSuper = true)
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Orders extends AuditBase {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nazwa")
    private String nazwa;



    public String toString() {
        return "Orders(id=" + this.getId() + ", nazwa=" + this.getNazwa() +  ")";
    }
}
