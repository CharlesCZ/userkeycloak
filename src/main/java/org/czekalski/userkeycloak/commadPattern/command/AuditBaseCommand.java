package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class AuditBaseCommand {


    protected Timestamp createdDate;

    protected  String createdBy;

    protected  String lastModifiedBy;

    protected  Timestamp lastModifiedDate;

}
