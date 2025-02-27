package org.czekalski.userkeycloak.model;


import lombok.Data;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


@Data
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditBase {


    @CreatedDate
    @Column(name = "created_date")
    protected Timestamp createdDate;

    @CreatedBy
    @Column(name = "created_by")
    protected  String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    protected  String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    protected Timestamp lastModifiedDate;


}
