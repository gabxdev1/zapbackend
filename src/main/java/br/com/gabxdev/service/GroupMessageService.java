package br.com.gabxdev.service;

import br.com.gabxdev.repository.GroupMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupMessageService {

    private final GroupMessageRepository repository;
}
