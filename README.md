# RMI Game Simulator
Simulador de game multiplayer desenvolvido para a disciplina de Redes Distribuídas, do curso de Engenharia de Software da PUCRS.

# Instalação e uso
Esta aplicação foi desenvolvida utilizando Java 11 (AdoptOpenJDK). Instruções de instalação em https://adoptopenjdk.net/installation.html (em inglês).

- Baixe ou clone este repositório
- Execute estes comandos:
  ```
  cd src
  javac *.java
  ```
- Para iniciar o servidor:
  ```
  java Server <numero_de_players>
  ```
- Para iniciar o(s) cliente(s):
  ```
  # Se não for especificada uma porta, será utilizada a 54321
  java Client <ip_servidor> [porta_callback]
  ```
  
  O servidor "iniciará o jogo" quando o número de jogadores especificado for atingido.
