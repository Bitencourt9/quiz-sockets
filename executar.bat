@echo off
REM =====================================================
REM Script para executar o Quiz de Sockets TCP
REM Plataforma: Windows
REM =====================================================

echo.
echo ╔════════════════════════════════════════════════════════╗
echo ║  Quiz de Sockets TCP - Launcher                        ║
echo ╚════════════════════════════════════════════════════════╝
echo.

REM Verifica se Maven está disponível
mvn -v >nul 2>&1
if errorlevel 1 (
    echo [ERRO] Maven não encontrado. Certifique-se de que Maven está instalado e no PATH.
    pause
    exit /b 1
)

REM Compila o projeto
echo [1] Compilando o projeto...
mvn clean compile -q
if errorlevel 1 (
    echo [ERRO] Compilação falhou.
    pause
    exit /b 1
)
echo [OK] Projeto compilado com sucesso!
echo.

REM Menu de opção
echo Escolha uma opção:
echo.
echo 1 - Executar Servidor (porta 5000)
echo 2 - Executar Cliente
echo 3 - Executar Servidor + 3 Clientes (em janelas separadas)
echo 4 - Sair
echo.

set /p choice="Digite a opção (1-4): "

if "%choice%"=="1" (
    echo.
    echo [INFO] Iniciando Servidor...
    echo [INFO] Aguardando 3 clientes...
    mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor"
) else if "%choice%"=="2" (
    echo.
    echo [INFO] Iniciando Cliente...
    echo [INFO] Conectando ao servidor em localhost:5000...
    mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
) else if "%choice%"=="3" (
    echo.
    echo [INFO] Iniciando Servidor em nova janela...
    start "Quiz TCP - Servidor" mvn exec:java -Dexec.mainClass="br.com.projeto.Servidor"
    
    timeout /t 2 /nobreak
    
    echo [INFO] Iniciando Cliente 1 em nova janela...
    start "Quiz TCP - Cliente 1" mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
    
    timeout /t 1 /nobreak
    
    echo [INFO] Iniciando Cliente 2 em nova janela...
    start "Quiz TCP - Cliente 2" mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
    
    timeout /t 1 /nobreak
    
    echo [INFO] Iniciando Cliente 3 em nova janela...
    start "Quiz TCP - Cliente 3" mvn exec:java -Dexec.mainClass="br.com.projeto.Cliente"
    
    echo.
    echo [OK] Servidor e 3 clientes iniciados em janelas separadas!
) else if "%choice%"=="4" (
    echo.
    echo [INFO] Saindo...
    exit /b 0
) else (
    echo.
    echo [ERRO] Opção inválida!
    pause
    exit /b 1
)

pause
