# 🎓 RESUMO EXECUTIVO - Exercícios de Sockets (TCP + UDP)

## 📦 O Que Foi Entregue

Um projeto em **Java 17** com **dois exercícios** sobre Sockets, totalmente funcional e bem documentado:

- **Exercício 1 (TCP)**: Quiz multi-thread com 3 clientes, sincronização e placar
- **Exercício 2 (UDP)**: Chat **P2P** para 2 usuários (cada lado é cliente + servidor UDP)

---

## 🎯 Funcionalidades Principais

| Funcionalidade | Status | Descrição |
|---|---|---|
| Servidor Multi-thread | ✅ | Aceita 3 clientes com thread individual |
| Quiz com 3 Perguntas | ✅ | Perguntas sobre Sockets TCP |
| Sincronização | ✅ | CyclicBarrier aguarda 3 respostas |
| Feedback Real-time | ✅ | ✅/❌ após cada resposta |
| Placar Final | ✅ | Calcula acertos e anuncia vencedor |
| TCP Sockets | ✅ | ServerSocket + Socket |
| I/O Streams | ✅ | BufferedReader + PrintWriter |
| Tratamento de Exceções | ✅ | IOException, InterruptedException, etc |
| Código Modular | ✅ | 4 classes bem separadas |
| Documentação Completa | ✅ | JavaDoc + 4 guias |
| Chat UDP P2P | ✅ | Dois peers trocando mensagens via UDP |
| Indicação de Remetente | ✅ | Nome + IP:porta em cada mensagem recebida |

---

## 📁 Arquivos Criados

### Código-Fonte Java (Exercício 1 + Exercício 2)

```
src/main/java/br/com/projeto/
├── Pergunta.java              (Modelo de pergunta)
├── Servidor.java              (Servidor TCP)
├── GerenciadorCliente.java    (Gerencia cada cliente em thread)
└── Cliente.java               (Cliente TCP)

src/main/java/br/com/projeto/udp/
├── ChatUdpPeer.java           (Chat UDP P2P)
├── ChatMensagem.java          (Modelo de mensagem)
└── ChatMensagemCodec.java     (Codec nome|texto)
```

### Documentação (5 arquivos)

```
├── README.md                  (Quick start)
├── DOCUMENTACAO.md            (Documentação completa)
├── IMPLEMENTACAO.md           (Detalhes de implementação)
├── TROUBLESHOOTING.md         (Solução de problemas)
└── RESUMO_EXECUTIVO.md        (Este arquivo)
```

### Utilitários

```
├── executar.bat               (Script de execução Windows)
├── pom.xml                    (Configuração Maven)
└── .mvn/wrapper/              (Maven Wrapper para ambiente)
```

---

## 🚀 Como Usar - Simplificado

### Opção 1: Script (Mais Fácil)
```bash
cd c:\Users\Lucas\projetos\quiz-tcp\quiz-sockets
executar.bat
```
Escolha a opção 3 para executar servidor + 3 clientes.

### Opção 2: Terminal Manual
**Terminal 1 - Servidor:**
```bash
mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor"
```

**Terminais 2, 3, 4 - Clientes:**
```bash
mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
```

### Opção 3: Chat UDP P2P (2 terminais)

1) Compile:
```bash
mvn clean compile
```

2) Terminal A:
```bash
java -cp target/classes br.com.projeto.udp.ChatUdpPeer 6000 localhost 6001 Alice
```

3) Terminal B:
```bash
java -cp target/classes br.com.projeto.udp.ChatUdpPeer 6001 localhost 6000 Bob
```

---

## 🏗️ Arquitetura em Detalhes

### Fluxo de Execução

```
SERVIDOR                          CLIENTE 1    CLIENTE 2    CLIENTE 3
    |                                  |            |            |
    ├─ Inicia ServerSocket:5000       |            |            |
    ├─ Aguarda conexão ────────────────┼────────────┼────────────┤
    |                                  |            |            |
    ├─ Aceita Cliente 1 ────────────── ✓            |            |
    ├─ Cria GerenciadorCliente 1      |            |            |
    ├─ Inicia Thread 1                |            |            |
    |                                  |            |            |
    ├─ Aceita Cliente 2 ───────────────┼────────────┼────────────┤
    ├─ Cria GerenciadorCliente 2      |            |            |
    ├─ Inicia Thread 2                |            ✓            |
    |                                  |            |            |
    ├─ Aceita Cliente 3 ───────────────┼────────────┼────────────┤
    ├─ Cria GerenciadorCliente 3      |            |            |
    ├─ Inicia Thread 3                |            |            ✓
    |                                  |            |            |
    ├─ ENVIA PERGUNTA 1 ───────────────┼────────────┼────────────┤
    │  (aguarda com CyclicBarrier)    |            |            |
    │                                  │ responde   │ responde   │ responde
    │                                  ├────────────┼────────────┤
    │  Feedback: ✅/❌ ◄───────────────┼────────────┼────────────┤
    │                                  |            |            |
    ├─ ENVIA PERGUNTA 2 (idem) ────────┼────────────┼────────────┤
    ├─ ENVIA PERGUNTA 3 (idem) ────────┼────────────┼────────────┤
    |                                  |            |            |
    ├─ CALCULA PLACAR                 |            |            |
    ├─ ENVIA PLACAR FINAL ─────────────┼────────────┼────────────┤
    │  🏆 VENCEDOR                     |            |            |
    |                                  |            |            |
    ├─ ENCERRA CONEXÕES ───────────────┼────────────┼────────────┤
    |                                  ✗            ✗            ✗
    └─ Fim

SINCRONIZAÇÃO: CyclicBarrier aguarda os 3 clientes em cada pergunta
```

### Componentes

**Servidor.java**
- Escuta na porta 5000
- Aceita 3 conexões
- Gerencia o fluxo do quiz
- Sincroniza com CyclicBarrier
- Calcula placar

**GerenciadorCliente.java** (x3 threads)
- Comunica com 1 cliente
- Processa resposta
- Sincroniza com barreira
- Calcula acertos individuais

**Cliente.java** (x3 instâncias)
- Conecta ao servidor
- Recebe e exibe perguntas
- Lê respostas do usuário
- Envia respostas
- Exibe placar

**Pergunta.java**
- Armazena pergunta/opções
- Valida respostas corretas

---

## 🔐 Sincronização Explicada

### CyclicBarrier

```java
CyclicBarrier barreira = new CyclicBarrier(3); // 3 clientes

// Em cada thread de cliente:
void processarPergunta(Pergunta pergunta, int numero) {
    // Envia pergunta
    escritor.println(pergunta);
    
    // Lê resposta
    String resposta = leitor.readLine();
    
    // SINCRONIZA: Aguarda os outros 2 clientes
    barreira.await(); // ← Bloqueia até que os 3 cheguem aqui
    
    // Continua depois que todos responderam
}

// Servidor reseta barreira para próxima pergunta
barreira.reset();
```

**Por que?**
- Garante que servidor só continua quando TODOS responderam
- Impede que um cliente "rápido" vire o quiz sozinho
- Mantém fair play

---

## 💾 Armazenamento de Dados

```java
// Servidor armazena:
Map<Integer, Integer> placarFinal;
// Exemplo: {1: 2, 2: 3, 3: 1} // Cliente ID : Acertos

// Cliente armazena:
int[] respostas = new int[3];
// Exemplo: {1, 2, 0} // Respostas para P1, P2, P3 (índices 0-3)
```

---

## 🔍 Tratamento de Erros

```
┌─ IOException ────────────────────────────┐
│  ├─ Desconexão de cliente (readLine=null)
│  ├─ Erro ao enviar dados
│  └─ Erro ao receber dados
│
├─ InterruptedException ──────────────────┤
│  ├─ Thread é interrompida
│  └─ CyclicBarrier pode interromper
│
├─ NumberFormatException ────────────────┤
│  ├─ Usuário digitou não-numérico
│  └─ Tratado: entrada marcada como inválida
│
└─ BrokenBarrierException ──────────────┘
   └─ Cliente desconecta durante espera na barreira
```

---

## 📊 Exemplo de Saída Real

### Servidor
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

### Cliente
```
✅ Conectado ao servidor em localhost:5000

Bem-vindo ao Quiz de Sockets TCP!
ID do Cliente: 2
Aguardando os outros clientes...

--- PERGUNTA 1 ---
O que é um Socket?
1. Uma interface entre a aplicação e a rede
2. Um tipo de conexão serial
3. Uma camada de criptografia
4. Um protocolo de roteamento

Digite o número da sua resposta (1-4): 1
✅ Correto!

... (Perguntas 2 e 3)

========== PLACAR FINAL ==========
Cliente 1: 2 acerto(s)
Cliente 2: 3 acerto(s) <- Você 🏆 VENCEDOR!
Cliente 3: 1 acerto(s)
==================================

Obrigado por participar! Conexão será encerrada...
```

### Chat UDP (P2P)

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

---

## ✨ Diferenciais da Implementação

1. **Thread-Safe**: Estruturas de dados sincronizadas
2. **Elegante**: Uso apropriado de CyclicBarrier
3. **Profissional**: ExecutorService para thread pool
4. **Robusto**: Múltiplas camadas de tratamento de erro
5. **Documentado**: JavaDoc 100%, 4 guias
6. **Testável**: Estrutura modular
7. **Escalável**: Fácil adicionar mais clientes/perguntas
8. **UX Friendly**: Emojis e formatação clara

---

## 🎓 O Que Você Aprende

- ✅ Programação de Sockets TCP
- ✅ Programação de Sockets UDP (DatagramSocket/DatagramPacket)
- ✅ Multi-threading em Java
- ✅ Sincronização de threads
- ✅ I/O de rede
- ✅ Tratamento de exceções
- ✅ Padrões de design
- ✅ Boas práticas Java

---

## 📋 Checklist Final

- ✅ Exercício 1 (TCP) implementado (quiz + 3 clientes)
- ✅ Exercício 2 (UDP) implementado (chat P2P + 2 usuários)
- ✅ 3 perguntas sobre Sockets
- ✅ Compilação sem erros
- ✅ Servidor e Cliente funcionando
- ✅ Sincronização com CyclicBarrier
- ✅ Feedback imediato ✅/❌
- ✅ Placar final com vencedor
- ✅ Mensagens UDP indicam remetente (nome + IP:porta)
- ✅ Tratamento completo de exceções
- ✅ Documentação completa
- ✅ Script de execução
- ✅ Guia de troubleshooting

---

## 🚀 Próximos Passos (Opcional)

1. **Expandir**: Adicionar mais perguntas
2. **Persistência**: Salvar resultados em banco de dados
3. **GUI**: Criar interface gráfica
4. **Autenticação**: Adicionar login de usuário
5. **Ranking**: Sistema de pontuação persistente
6. **Timeout**: Implementar limite de tempo por pergunta
7. **Dificuldade**: Adicionar níveis de dificuldade
8. **Multiplayer Remote**: Suportar múltiplos servidores

---

## 📞 Suporte

- **Documentação**: [DOCUMENTACAO.md](DOCUMENTACAO.md)
- **Troubleshooting**: [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
- **Implementação**: [IMPLEMENTACAO.md](IMPLEMENTACAO.md)
- **Quick Start**: [README.md](README.md)

---

## ✅ Status Final

**Sistema**: ✅ COMPLETO  
**Compilação**: ✅ SUCESSO  
**Funcionamento**: ✅ TESTADO  
**Documentação**: ✅ COMPLETA  
**Versão**: 1.0  
**Data**: 2025  

---

**Aproveite os exercícios! 🎮**

