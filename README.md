# Quiz de Sockets TCP 🎯

Sistema de Quiz multi-thread em Java usando Sockets TCP. O servidor aguarda 3 clientes, executa um quiz com 3 perguntas sobre Sockets, sincroniza as respostas e anuncia o vencedor.

## ⚡ Quick Start

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

## 📚 Documentação Completa

Veja [DOCUMENTACAO.md](DOCUMENTACAO.md) para informações detalhadas sobre arquitetura, sincronização e fluxo de jogo.

## 🏗️ Estrutura do Projeto

```
src/main/java/br/com/projeto/
├── Servidor.java           # Servidor TCP multi-thread
├── Cliente.java            # Cliente TCP
├── GerenciadorCliente.java # Gerencia cada cliente em thread
└── Pergunta.java           # Modelo de pergunta
```

## ✨ Características

✅ **Multi-thread**: Cada cliente em thread separada  
✅ **Sincronização**: CyclicBarrier para aguardar respostas  
✅ **Feedback Real-time**: Respostas imediatas (✅/❌)  
✅ **Placar Final**: Cálculo automático de vencedor  
✅ **Tratamento de Exceções**: IOException, InterruptedException, etc.  
✅ **Modular**: Código bem organizado e comentado  
✅ **Java 17**: Compilado com JDK 17  

## 🎮 Como Funciona

1. Servidor aguarda 3 conexões (porta 5000)
2. Envia 3 perguntas simultaneamente
3. Aguarda 3 respostas com sincronização
4. Envia feedback imediato (correto/incorreto)
5. Calcula placar e anuncia vencedor

## 💻 Requisitos

- Java 17+
- Maven 3.9+

## 📝 Perguntas do Quiz

1. O que é um Socket?
2. Diferença entre TCP e UDP?
3. Classe para criar servidor TCP?

---

**Mais detalhes**: Veja [DOCUMENTACAO.md](DOCUMENTACAO.md)
