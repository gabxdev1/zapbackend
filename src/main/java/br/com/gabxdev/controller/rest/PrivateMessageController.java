package br.com.gabxdev.controller.rest;

import br.com.gabxdev.dto.response.private_message.PrivateMessageResponse;
import br.com.gabxdev.service.private_message.PrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/private-message")
@RequiredArgsConstructor
public class PrivateMessageController {


    private final PrivateMessageService privateMessageService;

    @GetMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<PrivateMessageResponse>> findAllPrivateMessages() {
        var messagesBetweenUsers = privateMessageService.getAllPrivateMessages();

        return ResponseEntity.ok(messagesBetweenUsers);
    }

}
