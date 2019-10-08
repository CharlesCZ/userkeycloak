package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class AuditBaseCommand {


    protected Date createdDate;

    protected  String createdBy;

    protected  String lastModifiedBy;

    protected  Date lastModifiedDate;

}
