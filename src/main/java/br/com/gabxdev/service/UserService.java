package br.com.gabxdev.service;

import br.com.gabxdev.model.User;
import br.com.gabxdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

}
