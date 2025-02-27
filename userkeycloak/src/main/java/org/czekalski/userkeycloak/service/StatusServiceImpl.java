package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.StatusCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.StatusMapper;
import org.czekalski.userkeycloak.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusServiceImpl implements StatusService{

private final StatusRepository statusRepository;
private final StatusMapper statusMapper;

    public StatusServiceImpl(StatusRepository statusRepository, StatusMapper statusMapper) {
        this.statusRepository = statusRepository;
        this.statusMapper = statusMapper;
    }


    @Override
    public List<StatusCommand> findAll(){
        return statusRepository.findAll()
                .stream().map(statusMapper::statusToStatusCommand).collect(Collectors.toList());
    }
}
