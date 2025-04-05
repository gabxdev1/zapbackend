package br.com.gabxdev.controller;

import br.com.gabxdev.commons.HeaderUtils;
import br.com.gabxdev.mapper.GroupMapper;
import br.com.gabxdev.request.GroupPostRequest;
import br.com.gabxdev.request.GroupPutRequest;
import br.com.gabxdev.response.group.GroupPostResponse;
import br.com.gabxdev.response.group.GroupPutResponse;
import br.com.gabxdev.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService service;

    private final HeaderUtils headerUtil;

    private final GroupMapper mapper;

    @PostMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupPostResponse> createGroup(@Valid @RequestBody GroupPostRequest request) {
        var group = service.createGroup(request.name(), request.description(), request.usersId());

        var headerLocation = headerUtil.createHeaderLocation(String.valueOf(group.getId()));

        var groupMemberPostResponse = mapper.toGroupPostResponse(group);

        return ResponseEntity.created(headerLocation).body(groupMemberPostResponse);
    }

    @PutMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupPutResponse> createGroup(@Valid @RequestBody GroupPutRequest request) {
        var group = service.updateGroup(request.id(), request.name(), request.description());

        var groupMemberPutResponse = mapper.toGroupPutResponse(group);

        return ResponseEntity.ok(groupMemberPutResponse);
    }


}
