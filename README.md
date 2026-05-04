# Exercícios de Sockets (TCP + UDP) 🎯💬

Projeto em Java 17 com **dois exercícios** sobre Sockets:

- **Exercício 1 (TCP)**: Quiz multi-thread com 3 clientes, sincronização e placar final
- **Exercício 2 (UDP)**: Chat **P2P** (cada lado é cliente + servidor UDP) para 2 usuários

## ⚡ Quick Start

### ✅ Exercício 1 - Quiz TCP (3 clientes)

### Terminal 1 - Inicie o Servidor
```bash
mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor"
```

### Terminal 2 - Cliente 1
```bash
mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
```

### Terminal 3 - Cliente 2
```bash
mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
```

### Terminal 4 - Cliente 3
```bash
mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
```

### ✅ Exercício 2 - Chat UDP P2P (2 usuários)

1) Compile (uma vez):
```bash
mvn clean compile
```

2) Abra **2 terminais** e rode um peer em cada lado:

Terminal A (Alice):
```bash
java -cp target/classes br.com.projeto.udp.ChatUdpPeer 6000 localhost 6001 Alice
```

Terminal B (Bob):
```bash
java -cp target/classes br.com.projeto.udp.ChatUdpPeer 6001 localhost 6000 Bob
```

Cada mensagem recebida mostra **quem enviou** (nome + IP:porta) e o texto.

## 🚀 Guia de Execução Atualizado

### Requisitos
- Java 17+
- Maven 3.9+
- Windows, Linux ou macOS

### 1. Compilar o projeto
No terminal, dentro da pasta do projeto:

```bash
mvn clean compile
```

Se quiser validar tudo antes de executar:

```bash
mvn test
```

### 2. Executar pelo script no Windows
Use o launcher:

```cmd
executar.bat
```

O menu oferece:

- Opção 1: executar o servidor do Exercício 1
- Opção 2: executar um cliente do Exercício 1
- Opção 3: iniciar o servidor e os 3 clientes do Exercício 1 em janelas separadas
- Opção 4: iniciar o chat UDP do Exercício 2 com Alice e Bob
- Opção 5: sair

### 3. Exercício 1 - Quiz TCP

#### Servidor
Em um terminal:

```bash
mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor"
```

#### Clientes
Abra 3 terminais e execute o mesmo comando em cada um:

```bash
mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
```

#### Fluxo
1. O servidor aguarda 3 clientes.
2. Cada cliente responde às 3 perguntas.
3. O servidor calcula o placar final.
4. O vencedor é exibido para todos.

### 4. Exercício 2 - Chat UDP P2P

#### Compilar
Se ainda não compilou:

```bash
mvn clean compile
```

#### Terminal A - Alice
```bash
java -cp target/classes br.com.projeto.udp.ChatUdpPeer 6000 localhost 6001 Alice
```

#### Terminal B - Bob
```bash
java -cp target/classes br.com.projeto.udp.ChatUdpPeer 6001 localhost 6000 Bob
```

#### Uso
- Digite mensagens normalmente e pressione Enter.
- Para sair, digite:

```bash
/sair
```

#### O que acontece
- Cada peer abre uma porta local UDP.
- As mensagens enviadas aparecem no outro terminal.
- Cada mensagem recebida mostra o nome do remetente e o IP:porta de origem.

### 5. Teste automático
Para validar o projeto inteiro:

```cmd
teste_automatico.bat
```

Esse script:
- compila o projeto
- executa o cenário automático do Exercício 1
- valida o Exercício 2 com os testes JUnit e de integração UDP

### 6. Resumo rápido
- Exercício 1: quiz TCP com 3 clientes e servidor em `localhost:5000`
- Exercício 2: chat UDP P2P entre dois peers, usando portas 6000 e 6001 por padrão
- Script principal: [executar.bat](executar.bat)
- Teste automático: [teste_automatico.bat](teste_automatico.bat)

## 📚 Documentação Completa

Veja [DOCUMENTACAO.md](DOCUMENTACAO.md) para informações detalhadas sobre arquitetura, sincronização e fluxo de jogo.

## 🏗️ Estrutura do Projeto

```
src/main/java/br/com/projeto/
├── Servidor.java           # Servidor TCP multi-thread
├── Cliente.java            # Cliente TCP
├── GerenciadorCliente.java # Gerencia cada cliente em thread
└── Pergunta.java           # Modelo de pergunta

src/main/java/br/com/projeto/udp/
├── ChatUdpPeer.java         # Chat UDP P2P (peer)
├── ChatMensagem.java        # Modelo de mensagem
└── ChatMensagemCodec.java   # Codec (nome|texto)
```

## ✨ Características

✅ **Multi-thread**: Cada cliente em thread separada  
✅ **Sincronização**: CyclicBarrier para aguardar respostas  
✅ **Feedback Real-time**: Respostas imediatas (✅/❌)  
✅ **Placar Final**: Cálculo automático de vencedor  
✅ **Tratamento de Exceções**: IOException, InterruptedException, etc.  
✅ **Modular**: Código bem organizado e comentado  
✅ **Java 17**: Compilado com JDK 17  

✅ **UDP Chat P2P**: 2 peers trocando mensagens via DatagramSocket  
✅ **Indica remetente**: nome + IP:porta em cada mensagem recebida  

## 🎮 Como Funciona

### Exercício 1 (TCP)
1. Servidor aguarda 3 conexões (porta 5000)
2. Envia 3 perguntas
3. Aguarda 3 respostas com sincronização
4. Envia feedback imediato (correto/incorreto)
5. Calcula placar e anuncia vencedor

### Exercício 2 (UDP)
1. Cada peer abre um UDP socket na sua porta local
2. Uma thread fica em `receive()` exibindo mensagens recebidas
3. O usuário digita e o peer faz `send()` para o outro lado

## 💻 Requisitos

- Java 17+
- Maven 3.9+

## 📝 Perguntas do Quiz

1. O que é um Socket?
2. Diferença entre TCP e UDP?
3. Classe para criar servidor TCP?

---

**Mais detalhes**: Veja [DOCUMENTACAO.md](DOCUMENTACAO.md)
