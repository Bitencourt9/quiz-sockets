# 🎯 Exercícios de Sockets (TCP + UDP) - Documentação

## 📋 Visão Geral

Este repositório contém **dois exercícios** sobre Sockets em Java 17:

- **Exercício 1 (TCP)**: Quiz multi-thread com 3 clientes, sincronização com `CyclicBarrier` e placar final
- **Exercício 2 (UDP)**: Chat **P2P** (cada lado atua como cliente + servidor UDP) para 2 usuários

## 🏗️ Arquitetura do Projeto (Exercício 1 - Quiz TCP)

### Classes Principais

1. **`Pergunta.java`**
   - Representa uma pergunta do quiz
   - Armazena enunciado, opções de resposta e resposta correta
   - Valida se uma resposta está correta

2. **`Servidor.java`**
   - Servidor principal que escuta na porta 5000
   - Aceita exatamente 3 conexões de clientes
   - Gerencia o ciclo do quiz (envio de perguntas, sincronização, placar)
   - Usa `CyclicBarrier` para sincronizar as respostas dos 3 clientes

3. **`GerenciadorCliente.java`**
   - Implementa `Runnable` para gerenciar cada cliente em thread separada
   - Comunica com o cliente via `BufferedReader` e `PrintWriter`
   - Processa respostas e calcula acertos
   - Sincroniza com os outros clientes através da barreira

4. **`Cliente.java`**
   - Cliente conecta ao servidor na porta 5000
   - Recebe perguntas e envia respostas
   - Exibe feedback e placar final

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.9+
- Terminal/Prompt de Comando

### Passo 1: Compilar o Projeto
```bash
cd c:\Users\Lucas\projetos\quiz-tcp\quiz-sockets
mvn clean compile
```

### Passo 2: Executar o Servidor
Abra um terminal e execute:
```bash
mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor"
```

Você verá:
```
🚀 Servidor iniciado na porta 5000
⏳ Aguardando 3 clientes...
```

### Passo 3: Conectar os Clientes (Em 3 terminais diferentes)

Terminal 2 - Cliente 1:
```bash
cd c:\Users\Lucas\projetos\quiz-tcp\quiz-sockets
mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
```

Terminal 3 - Cliente 2:
```bash
cd c:\Users\Lucas\projetos\quiz-tcp\quiz-sockets
mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
```

Terminal 4 - Cliente 3:
```bash
cd c:\Users\Lucas\projetos\quiz-tcp\quiz-sockets
mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
```

## 🎮 Fluxo de Jogo

1. **Conexão**: Cada cliente conecta ao servidor sequencialmente
2. **Aguarde**: O servidor aguarda os 3 clientes estarem prontos
3. **Quiz**: O servidor envia as 3 perguntas simultaneamente para todos
4. **Sincronização**: O servidor aguarda as 3 respostas antes de prosseguir
5. **Feedback**: Cada cliente recebe feedback imediato (✅ Correto / ❌ Incorreto)
6. **Placar**: Após todas as perguntas, o placar final é calculado e exibido
7. **Vencedor**: O cliente com maior número de acertos é anunciado como vencedor

## 📊 Exemplo de Execução

### Servidor (Saída)
```
🚀 Servidor iniciado na porta 5000
⏳ Aguardando 3 clientes...

✅ Cliente 1 conectado. Endereço: 127.0.0.1
✅ Cliente 2 conectado. Endereço: 127.0.0.1
✅ Cliente 3 conectado. Endereço: 127.0.0.1

📊 Todos os 3 clientes conectados. Iniciando o quiz...

--- ENVIANDO PERGUNTA 1 ---
--- ENVIANDO PERGUNTA 2 ---
--- ENVIANDO PERGUNTA 3 ---

========== CALCULANDO RESULTADO FINAL ==========
Cliente 1: 2 acerto(s)
Cliente 2: 3 acerto(s)
Cliente 3: 1 acerto(s)
================================================
🏆 VENCEDOR: Cliente 2 com 3 acerto(s)!
```

### Cliente (Saída)
```
✅ Conectado ao servidor em localhost:5000

Bem-vindo ao Quiz de Sockets TCP!
ID do Cliente: 1
Aguardando os outros clientes...

--- PERGUNTA 1 ---
O que é um Socket?
1. Uma interface entre a aplicação e a rede
2. Um tipo de conexão serial
3. Uma camada de criptografia
4. Um protocolo de roteamento

Digite o número da sua resposta (1-4): 1
✅ Correto!

--- PERGUNTA 2 ---
Qual é a principal diferença entre TCP e UDP?
1. TCP é mais rápido
2. TCP é orientado à conexão e garante entrega, UDP é não orientado e não garante
3. UDP é mais seguro
4. Não há diferença significativa

Digite o número da sua resposta (1-4): 2
✅ Correto!

--- PERGUNTA 3 ---
Qual classe Java é usada para criar um servidor que aguarda conexões?
1. Socket
2. InputStream
3. ServerSocket
4. DatagramSocket

Digite o número da sua resposta (1-4): 2
❌ Incorreto! A resposta correta era: 3

========== PLACAR FINAL ==========
Cliente 1: 2 acerto(s) <- Você
Cliente 2: 3 acerto(s) 🏆 VENCEDOR!
Cliente 3: 1 acerto(s)
==================================

Obrigado por participar! Conexão será encerrada...

✅ Desconectado do servidor.
```

## 🔧 Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Java | 17 | Linguagem principal |
| Maven | 3.9+ | Build tool |
| ServerSocket | JDK | Criar servidor TCP |
| Socket | JDK | Comunicação cliente-servidor |
| CyclicBarrier | JDK | Sincronização entre threads |
| ExecutorService | JDK | Gerenciar thread pool |
| BufferedReader | JDK | Ler dados do socket |
| PrintWriter | JDK | Enviar dados pelo socket |

## 📐 Padrões de Design Utilizados

1. **Producer-Consumer**: O servidor produz perguntas e os clientes consomem e respondem
2. **Thread Pool**: ExecutorService gerencia threads dos clientes
3. **Barrier Synchronization**: CyclicBarrier sincroniza os clientes em cada pergunta
4. **Observer Pattern**: Cada cliente recebe feedback imediato das respostas

## ⚙️ Sincronização em Detalhe

A sincronização entre os 3 clientes é feita através de `CyclicBarrier`:

```java
CyclicBarrier barreira = new CyclicBarrier(3); // 3 clientes

// Em cada thread de cliente:
barreira.await(); // Aguarda todos chegarem neste ponto
```

**Fluxo:**
1. Cliente 1, 2 e 3 recebem a pergunta
2. Eles respondem nos seus tempos próprios
3. Quando um cliente responde, ele chama `barreira.await()`
4. Quando o 3º cliente responde, a barreira libera todos
5. Servidor prossegue para a próxima pergunta

## 🐛 Tratamento de Exceções

- **IOException**: Capturada em operações de rede (leitura/escrita de sockets)
- **InterruptedException**: Capturada quando uma thread é interrompida
- **NumberFormatException**: Capturada quando o usuário digita um valor inválido
- **BrokenBarrierException**: Capturada quando a barreira é quebrada (cliente desconecta)

## 📝 Perguntas do Quiz

As 3 perguntas sobre Sockets TCP são:

1. **O que é um Socket?**
   - Resposta: Uma interface entre a aplicação e a rede

2. **Qual é a principal diferença entre TCP e UDP?**
   - Resposta: TCP é orientado à conexão e garante entrega, UDP é não orientado e não garante

3. **Qual classe Java é usada para criar um servidor que aguarda conexões?**
   - Resposta: ServerSocket

---

## 💬 Exercício 2 - Chat UDP P2P

## 🎯 Objetivo

Implementar um **aplicativo de chat usando UDP** onde **dois usuários** conseguem trocar mensagens.

Requisitos atendidos:
- ✅ Troca de mensagens via UDP
- ✅ Cada mensagem recebida indica **quem enviou** (nome + IP:porta) e a mensagem
- ✅ Arquitetura **P2P** (cada lado abre um socket UDP e também envia mensagens)

## 🏗️ Arquitetura (UDP)

Cada peer executa a mesma aplicação (`ChatUdpPeer`) e possui dois fluxos paralelos:

1. **Recepção** (thread dedicada)
   - Bloqueia em `socket.receive()` aguardando datagramas
   - Ao receber, decodifica o payload e imprime:
     - Nome do remetente (enviado no payload)
     - IP e porta de origem (informação do pacote UDP)
     - Texto da mensagem

2. **Envio** (thread principal)
   - Lê o teclado (`System.in`)
   - Envia mensagens para o `hostRemoto:portaRemota` usando `socket.send()`

## 🧱 Classes Principais (UDP)

1. **`ChatUdpPeer.java`**
   - Classe executável do Exercício 2
   - Abre o `DatagramSocket` na porta local
   - Cria a thread recebedora
   - Envia mensagens para o peer remoto

2. **`ChatMensagem.java`**
   - Modelo simples: nome do remetente + texto

3. **`ChatMensagemCodec.java`**
   - Codifica/decodifica mensagens em UTF-8
   - Formato do payload: `nome|texto`

## 🚀 Como Executar (UDP)

### Pré-requisitos
- Java 17+
- Porta UDP liberada no firewall local (para testes locais, normalmente basta)

### Passo 1: Compilar
```bash
mvn clean compile
```

### Passo 2: Abrir 2 terminais (P2P)

Terminal A (Alice):
```bash
java -cp target/classes br.com.projeto.udp.ChatUdpPeer 6000 localhost 6001 Alice
```

Terminal B (Bob):
```bash
java -cp target/classes br.com.projeto.udp.ChatUdpPeer 6001 localhost 6000 Bob
```

### Observações
- Use **portas diferentes** em cada lado (ex.: 6000 e 6001)
- Para usar em máquinas diferentes, substitua `localhost` pelo IP da outra máquina

## 📩 Exemplo de Saída (UDP)

Terminal A:
```
📨 Chat UDP P2P iniciado!
👤 Você: Alice
📍 Local: 0.0.0.0:6000
🎯 Remoto: 127.0.0.1:6001

📩 De Bob (127.0.0.1:6001): Oi, Alice!
```

Terminal B:
```
📨 Chat UDP P2P iniciado!
👤 Você: Bob
📍 Local: 0.0.0.0:6001
🎯 Remoto: 127.0.0.1:6000

📩 De Alice (127.0.0.1:6000): Oi, Bob!
```

## 🔧 Tecnologias Utilizadas (UDP)

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Java | 17 | Linguagem principal |
| DatagramSocket | JDK | Socket UDP (receber/enviar) |
| DatagramPacket | JDK | Pacotes UDP |
| InetAddress | JDK | Resolução de host/endereço |
| Thread | JDK | Thread recebedora (receive) |

## 🐛 Tratamento de Exceções (UDP)

- **SocketException**
  - Porta local em uso
  - Socket fechado durante `receive()`
- **UnknownHostException**
  - Host remoto inválido
- **IOException**
  - Falha ao enviar/receber datagramas

## 🎯 Melhorias Futuras

- [ ] Adicionar mais perguntas (usar banco de dados)
- [ ] Permitir customizar número de clientes e perguntas
- [ ] Adicionar sistema de ranking persistente
- [ ] Implementar interface gráfica (GUI) com Swing/JavaFX
- [ ] Adicionar autenticação de usuários
- [ ] Salvar logs de partidas
- [ ] Implementar timeout para respostas

## 📞 Suporte

Em caso de problemas:
1. Verifique se a porta 5000 está disponível
2. Certifique-se de que Java 17+ está instalado: `java -version`
3. Execute `mvn clean` antes de compilar novamente
4. Verifique a conectividade de rede (localhost deve ser acessível)

---

**Versão**: 1.0  
**Autor**: Desenvolvedor Java  
**Data**: 2025  
**Linguagem**: Java 17  
