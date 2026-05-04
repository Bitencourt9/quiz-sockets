@echo off
setlocal EnableExtensions

REM Teste automatico do Quiz de Sockets TCP e do Chat UDP P2P

pushd "%~dp0"

echo ========================================
echo TESTE AUTOMATICO - Quiz de Sockets
echo ========================================
echo.

call mvn clean compile -q
if errorlevel 1 (
  echo [ERRO] Falha ao compilar o projeto.
  popd
  pause
  exit /b 1
)

del /q c1.txt c2.txt c3.txt servidor.txt udp_alice.txt udp_bob.txt 2>nul

echo [TESTE 1] Iniciando servidor TCP em segundo plano...
start "" /b cmd /c "java -cp target\classes br.com.projeto.Servidor > servidor.txt 2>&1"
timeout /t 2 /nobreak >nul

echo [TESTE 1] Iniciando 3 clientes TCP em paralelo...
start "" /b cmd /c "(echo 1&echo 2&echo 3) | java -cp target\classes br.com.projeto.Cliente > c1.txt 2>&1"
start "" /b cmd /c "(echo 2&echo 2&echo 1) | java -cp target\classes br.com.projeto.Cliente > c2.txt 2>&1"
start "" /b cmd /c "(echo 1&echo 1&echo 1) | java -cp target\classes br.com.projeto.Cliente > c3.txt 2>&1"

timeout /t 12 /nobreak >nul

echo.
echo ========================================
echo SAIDA DO SERVIDOR TCP:
echo ========================================
type servidor.txt
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

echo ========================================
echo TESTE 2 - VALIDACAO DO EXERCICIO 2
echo ========================================
echo.

echo [TESTE 2] Executando testes automatizados do exercicio 2 via Maven...
call mvn -q -Dtest=ChatMensagemCodecTest,ChatUdpIntegracaoTest test
if errorlevel 1 (
  echo [ERRO] Falha na validacao automatizada do exercicio 2.
  popd
  pause
  exit /b 1
)

echo.
echo ========================================
echo SAIDA DO TESTE ChatMensagemCodecTest:
echo ========================================
type target\surefire-reports\br.com.projeto.udp.ChatMensagemCodecTest.txt
echo.

echo ========================================
echo SAIDA DO TESTE ChatUdpIntegracaoTest:
echo ========================================
type target\surefire-reports\br.com.projeto.udp.ChatUdpIntegracaoTest.txt
echo.

popd
pause
