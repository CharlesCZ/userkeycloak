package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.StatusCommand;

import java.util.List;

public interface StatusService {

    List<StatusCommand> findAll();
}
