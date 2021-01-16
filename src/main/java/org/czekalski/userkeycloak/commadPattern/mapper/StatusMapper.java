package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.StatusCommand;
import org.czekalski.userkeycloak.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatusMapper {

StatusMapper INSTANCE= Mappers.getMapper(StatusMapper.class);

Status statusCommandToStatus(StatusCommand statusCommand);

StatusCommand statusToStatusCommand(Status status);
}
