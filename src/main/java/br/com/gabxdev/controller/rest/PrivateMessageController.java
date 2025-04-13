package br.com.gabxdev.controller.rest;

import br.com.gabxdev.dto.response.privateMessage.PrivateMessageGetResponse;
import br.com.gabxdev.service.private_message.PrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/private-message")
@RequiredArgsConstructor
public class PrivateMessageController {


    private final PrivateMessageService privateMessageService;

    @GetMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Slice<PrivateMessageGetResponse>> getPrivateMessages(
            @RequestParam Long receivedId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        var messagesBetweenUsers = privateMessageService.getMessagesBetweenUsers(receivedId, page, size);

        return ResponseEntity.ok(messagesBetweenUsers);
    }

}
