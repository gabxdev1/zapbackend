package br.com.gabxdev.service;

import br.com.gabxdev.repository.GroupMessageReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupMessageReadService {

    private final GroupMessageReadRepository repository;
}
