package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.AuditBaseCommand;
import org.czekalski.userkeycloak.model.AuditBase;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

@MapperConfig
public interface AuditBaseMapper {

AuditBase auditBaseCommandToAuditBase(AuditBaseCommand auditBaseCommand);

AuditBaseCommand auditBaseToAuditBaseCommand(AuditBase auditBase);
}
