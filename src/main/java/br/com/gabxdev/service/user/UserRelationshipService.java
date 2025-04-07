package br.com.gabxdev.service.user;

import br.com.gabxdev.repository.FriendshipRepository;
import br.com.gabxdev.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRelationshipService {

    public final FriendshipRepository friendshipRepository;

    private final GroupMemberRepository groupMemberRepository;

    public Set<String> getRelatedUserEmails(Long userId) {
        Set<String> relatedEmails = new HashSet<>();

        relatedEmails.addAll(
                friendshipRepository.findAllEmailFriendshipByUserId(userId)
        );

        relatedEmails.addAll(
                groupMemberRepository.findMemberEmailsByUserId(userId)
        );

        return relatedEmails;
    }
}
