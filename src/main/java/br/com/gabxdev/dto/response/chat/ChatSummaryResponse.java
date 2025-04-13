package br.com.gabxdev.dto.response.chat;

public record ChatSummaryResponse(
        Long id, // ID do usuário sender

        String name,// Nome do usuário

        MessageSummaryResponse lastMessage // Última mensagem trocada
) {
}
