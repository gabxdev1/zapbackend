package br.com.gabxdev.service;

import br.com.gabxdev.repository.FriendShipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendShipService {

    private final FriendShipRepository repository;
}
