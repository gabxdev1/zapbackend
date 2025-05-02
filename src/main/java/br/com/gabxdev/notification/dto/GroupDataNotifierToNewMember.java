package br.com.gabxdev.notification.dto;

import br.com.gabxdev.dto.response.group_message.GroupMessageResponse;
import br.com.gabxdev.dto.response.group_message.GroupMessageStatusResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record GroupDataNotifierToNewMember(
        String userEmail,

        List<GroupMessageResponse> groupMessageResponses
) {
}
