package br.com.gabxdev.service;

import br.com.gabxdev.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository repository;
}
