# 🛠️ Guia de Troubleshooting - Quiz de Sockets TCP

## 🔴 Problemas Comuns e Soluções

### Problema 1: "Conexão Recusada" - `ConnectException: Connection refused`

**Causa**: O servidor não está rodando ou está em outra porta.

**Solução**:
1. Verifique se o servidor está rodando:
   ```bash
   mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor"
   ```
2. Certifique-se de que está usando `localhost:5000`
3. Espere alguns segundos antes de conectar os clientes
4. Verifique se a porta 5000 não está em uso:
   ```bash
   netstat -ano | findstr :5000
   ```

---

### Problema 2: "Maven não encontrado"

**Causa**: Maven não está instalado ou não está no PATH.

**Solução**:
1. Instale Maven: https://maven.apache.org/download.cgi
2. Configure a variável `PATH` do sistema para incluir `maven/bin`
3. Verifique a instalação:
   ```bash
   mvn -v
   ```

---

### Problema 3: "Java version not recognized"

**Causa**: Java 17+ não está instalado ou não está no PATH.

**Solução**:
1. Instale Java 17+: https://www.oracle.com/java/technologies/downloads/
2. Configure `JAVA_HOME` para apontar para a instalação do Java
3. Adicione `JAVA_HOME\bin` ao PATH
4. Verifique:
   ```bash
   java -version
   javac -version
   ```

---

### Problema 4: "Compilation failure"

**Causa**: Erro na compilação do código.

**Solução**:
1. Limpe o projeto:
   ```bash
   mvn clean
   ```
2. Recompile:
   ```bash
   mvn compile
   ```
3. Se o erro persistir, verifique se todos os arquivos estão em `src/main/java/br/com/projeto/`:
   - Pergunta.java
   - Servidor.java
   - GerenciadorCliente.java
   - Cliente.java

---

### Problema 5: "Port already in use: 5000"

**Causa**: A porta 5000 já está sendo usada por outro processo.

**Solução Opção 1** - Libere a porta:
```bash
# Windows
netstat -ano | findstr :5000
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :5000
kill -9 <PID>
```

**Solução Opção 2** - Use outra porta (modifique o código):
Edite `Servidor.java` e `Cliente.java`:
```java
private static final int PORTA = 5001; // Altere de 5000 para 5001
```

---

### Problema 6: "Apenas 1 ou 2 clientes conectam"

**Causa**: Tempo limite ou cliente não consegue se conectar.

**Solução**:
1. Certifique-se de conectar os 3 clientes rapidamente após iniciar servidor
2. Não interrompa nenhum cliente antes de todos os 3 estarem conectados
3. Se um cliente falhar, reinicie todos (servidor e clientes)
4. Verifique a conectividade de rede local (localhost)

---

### Problema 7: "Entrada inválida"

**Causa**: Usuário digitou um número fora do intervalo 1-4.

**Solução**:
1. Digite um número de 1 a 4
2. Se o número está fora deste intervalo, será marcado como incorreto
3. Se não for um número inteiro, receberá mensagem de erro

---

### Problema 8: "Cliente desconectado antes de terminar"

**Causa**: Conexão foi perdida ou usuário fechou a janela.

**Solução**:
1. Se a conexão cair, o servidor continuará esperando outros clientes
2. Se 3º cliente desconecta, a barreira é quebrada e o servidor trata o erro
3. Reinicie o servidor e todos os clientes

---

### Problema 9: "Placar não aparece"

**Causa**: Conexão foi interrompida antes do final.

**Solução**:
1. Mantenha todos os 3 clientes conectados até o fim
2. Não feche nenhuma janela durante o quiz
3. Se problemas persistirem, reinicie tudo

---

### Problema 10: "Executar script .bat não funciona"

**Causa**: Script não tem permissão ou Maven não funciona em batch.

**Solução Opção 1** - Execute manualmente:
```bash
cd c:\Users\Lucas\projetos\quiz-tcp\quiz-sockets
mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor"
```

**Solução Opção 2** - Conceda permissão ao script:
1. Clique direito em `executar.bat`
2. Selecione "Propriedades"
3. Na aba "Segurança", clique "Desbloquear"

---

## 🔍 Verificações de Diagnóstico

### Verificar Java
```bash
java -version
javac -version
```
Esperado: Java 17+

### Verificar Maven
```bash
mvn -v
```
Esperado: Maven 3.9+

### Verificar Compilação
```bash
mvn clean compile -q
echo %ERRORLEVEL%
```
Esperado: 0 (sucesso)

### Verificar Conectividade
```bash
ping localhost
```
Esperado: Respostas OK

### Verificar Porta
```bash
netstat -ano | findstr :5000
```
Esperado: Vazio (porta disponível) ou seu processo Java

---

## 🐛 Debug Detalhado

### Ativar Logs Mais Detalhados no Servidor
```bash
mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor" -X
```

### Rastrear Conexões de Rede
```bash
# Windows
netstat -ano -p tcp

# Linux/Mac
netstat -an -p tcp | grep 5000
```

### Testar Conectividade Manualmente
```bash
# Terminal 1 - Servidor de teste
nc -l -p 5000

# Terminal 2 - Conectar
nc localhost 5000
```

---

## 💡 Dicas Avançadas

### Executar em Modo Daemon (Servidor em Background)
```bash
# Windows
start /B mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor"

# Linux/Mac
mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor" &
```

### Usar Diferentes Máquinas
Edite `Cliente.java`:
```java
private static final String HOST = "192.168.1.100"; // Altere de localhost
```
Execute servidor em uma máquina e clientes em outras.

### Profiling de Performance
```bash
mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor" \
  -Dexec.args="-Xmx512m"
```

### Logging em Arquivo
```bash
# Windows
mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor" > servidor.log 2>&1

# Linux/Mac
mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor" | tee servidor.log
```

---

## 📞 Contatos e Recursos

### Documentação Oficial
- [Oracle Java Docs](https://docs.oracle.com/en/java/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Java Networking](https://docs.oracle.com/javase/tutorial/networking/)

### Ferramentas Úteis
- **Wireshark**: Para analisar tráfego de rede
- **TCPView**: Para visualizar conexões TCP ativas
- **Visual VM**: Para profiling de aplicações Java
- **IntelliJ IDEA**: IDE profissional com suporte a Maven

---

## ✅ Checklist de Resolução

Se tiver problema, siga este checklist:

- [ ] Java 17+ está instalado? `java -version`
- [ ] Maven 3.9+ está instalado? `mvn -v`
- [ ] Projeto compila? `mvn clean compile`
- [ ] Porta 5000 está disponível? `netstat -ano | findstr :5000`
- [ ] Servidor iniciou? `mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor"`
- [ ] Clientes conseguem conectar? Veja logs do servidor
- [ ] Quiz completa sem interrupções?
- [ ] Placar é exibido corretamente?

Se tudo OK: ✅ Sistema funcionando!

---

**Última Atualização**: 2025  
**Versão**: 1.0

