# ⚡ Zap - Um Clone do WhatsApp

Zap é um sistema de chat ao vivo inspirado no WhatsApp, desenvolvido com tecnologias modernas e arquitetura escalável. A principal proposta é permitir a comunicação em tempo real entre usuários, com suporte a mensagens privadas e em grupo.

### 🌐 Link de Demonstração

🔗 Acesse: [https://zap.gabxdev.com.br](https://zap.gabxdev.com.br)  
📌 Versão atual: `v1.0.0`  
☁️ Hospedado na AWS

---

## 🚀 Funcionalidades

- ✅ Chat privado em tempo real
- ✅ Criação de grupos com múltiplos usuários
- ✅ Notificações de leitura e recebimento de mensagens
- ✅ Presença de usuário (online e última vez visto)
- ✅ Envio e recebimento de mensagens via WebSocket + RabbitMQ
- ✅ Entre outras...

---

## 🧱 Arquitetura

### 🔹 Layered Architecture

A aplicação backend é organizada em camadas:

- **Controller**: expõe APIs REST e WebSocket.
- **Service**: camada de lógica de negócio.
- **Repository**: persistência com Spring Data JPA.
- **DTOs e Mappers**: conversão de entidades e segurança de dados.

### 🔸 Event-Driven Architecture

A comunicação entre partes da aplicação é feita por eventos, com uso de RabbitMQ como mensageiro:

- Eventos são disparados para troca de mensagens ou mudanças de status.
- Consumidores processam os eventos assincronamente.
- Redução de acoplamento, aumento de desempenho e escalabilidade.

---

## 🛠️ Tecnologias Utilizadas

### Backend
- Java 21
- Spring Boot
- WebSocket (STOMP + JWT)
- RabbitMQ
- PostgreSQL
- Spring Security

### Frontend
- HTML
- CSS
- TypeScript
- Angular

---

## 🧠 Repositórios

- 🔧 Backend: [github.com/gabxdev1/zapbackend](https://github.com/gabxdev1/zapbackend)
- 🎨 Frontend: [github.com/gabxdev1/zapfrontend](https://github.com/gabxdev1/zapfrontend)
