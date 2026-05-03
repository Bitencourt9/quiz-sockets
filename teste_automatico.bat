@echo off
REM Script de teste automático do Quiz de Sockets TCP
REM Envia respostas para os 3 clientes com diferentes padrões de acertos

echo ========================================
echo TESTE AUTOMATICO - Quiz de Sockets TCP
echo ========================================
echo.
echo Aguardando o servidor conectar os clientes...
timeout /t 3 /nobreak

REM Cliente 1 - Respostas: 1, 2, 3 (TODAS CORRETAS = 3 acertos)
echo.
echo [TESTE] Conectando Cliente 1 com 3 acertos...
(
  echo 1
  echo 2
  echo 3
) | java -cp target/quiz-sockets-1.0-SNAPSHOT.jar br.com.projeto.Cliente > c1.txt 2>&1

REM Cliente 2 - Respostas: 2, 2, 1 (2 CORRETAS = 2 acertos)
echo [TESTE] Conectando Cliente 2 com 2 acertos...
(
  echo 2
  echo 2
  echo 1
) | java -cp target/quiz-sockets-1.0-SNAPSHOT.jar br.com.projeto.Cliente > c2.txt 2>&1

REM Cliente 3 - Respostas: 1, 1, 1 (1 CORRETA = 1 acerto)
echo [TESTE] Conectando Cliente 3 com 1 acerto...
(
  echo 1
  echo 1
  echo 1
) | java -cp target/quiz-sockets-1.0-SNAPSHOT.jar br.com.projeto.Cliente > c3.txt 2>&1

echo.
echo ========================================
echo SAIDA DO CLIENTE 1 (3 acertos esperados):
echo ========================================
type c1.txt
echo.

echo ========================================
echo SAIDA DO CLIENTE 2 (2 acertos esperados):
echo ========================================
type c2.txt
echo.

echo ========================================
echo SAIDA DO CLIENTE 3 (1 acerto esperado):
echo ========================================
type c3.txt
echo.
pause
