package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;

import java.util.Date;

@Data
public class AuditBaseCommand {


    protected Date createdDate;

    protected  String createdBy;

    protected  String lastModifiedBy;

    protected  Date lastModifiedDate;

}
