package br.com.gabxdev.notification.notifier;

import br.com.gabxdev.dto.response.group.GroupGetResponse;
import br.com.gabxdev.notification.dto.GroupDataNotifierToNewMember;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyGroupCreated(List<String> emails, GroupGetResponse message) {
        emails.forEach(email -> {
            messagingTemplate.convertAndSendToUser(
                    email,
                    "/queue/group-created",
                    message);
        });
    }

    public void notifyNewMember(List<String> emails, GroupGetResponse message, List<GroupDataNotifierToNewMember> groupDataNotifierToNewMember) {
        emails.forEach(email -> {
            messagingTemplate.convertAndSendToUser(
                    email,
                    "/queue/group-created",
                    message);
        });

        groupDataNotifierToNewMember.forEach(groupDataNotifier -> {
            messagingTemplate.convertAndSendToUser(
                    groupDataNotifier.userEmail(),
                    "/queue/group-message",
                    groupDataNotifier.groupMessageResponses());
        });
    }
}
