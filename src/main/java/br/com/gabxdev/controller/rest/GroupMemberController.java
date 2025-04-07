package br.com.gabxdev.controller.rest;

import br.com.gabxdev.dto.response.groupMember.GroupMemberDeleteRequest;
import br.com.gabxdev.dto.response.groupMember.GroupMemberDemotePostRequest;
import br.com.gabxdev.dto.response.groupMember.GroupMemberPostRequest;
import br.com.gabxdev.dto.response.groupMember.GroupMemberPromotePostRequest;
import br.com.gabxdev.service.GroupMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/groups/members")
@RequiredArgsConstructor
@Slf4j
public class GroupMemberController {

    private final GroupMemberService service;

    @PostMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> addGroupMembers(@Valid @RequestBody GroupMemberPostRequest request) {
        service.addMembers(request.groupId(), request.membersId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> removeGroupMember(@Valid @RequestBody GroupMemberDeleteRequest request) {
        log.info("Remove group member request: {}", request);

        service.removeMember(request.groupId(), request.memberToRemoveId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/promote", headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> promoteGroupMemberToModerator(@Valid @RequestBody GroupMemberPromotePostRequest request) {
        service.promoteToModerator(request.groupId(), request.userToPromoteId());

        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/demote", headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> demoteGroupMemberFromModerator(@Valid @RequestBody GroupMemberDemotePostRequest request) {
        service.demoteFromModerator(request.groupId(), request.userToDemoteId());

        return ResponseEntity.ok().build();
    }
}
