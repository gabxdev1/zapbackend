package br.com.gabxdev.controller.rest;

import br.com.gabxdev.dto.response.group_message.GroupMessageResponse;
import br.com.gabxdev.dto.response.group_message.GroupMessageStatusResponse;
import br.com.gabxdev.mapper.GroupMessageMapper;
import br.com.gabxdev.service.group_message.GroupMessageService;
import br.com.gabxdev.service.group_message.GroupMessageStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/group-messages")
public class GroupMessageController {

    private final GroupMessageService groupMessageService;

    private final GroupMessageMapper groupMessageMapper;

    private final GroupMessageStatusService groupMessageStatusService;

    @GetMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<GroupMessageResponse>> findAllGroupMessages() {
        var allGroupMessages = groupMessageService.findAllGroupMessages();

        var response = groupMessageMapper.toGroupMessageResponse(allGroupMessages);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/status", headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<GroupMessageStatusResponse>> findAllGroupMessageStatus() {
        var allGroupMessagesStatus = groupMessageStatusService.findAllGroupMessageStatusForUser();

        var response = groupMessageMapper.toGroupMessageStatusResponse(allGroupMessagesStatus);

        return ResponseEntity.ok(response);
    }
}
