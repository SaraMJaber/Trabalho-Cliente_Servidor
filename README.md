Trabalho Cliente-Servidor


1. Descrição do Projeto

Este projeto consiste na implementação de uma aplicação baseada na arquitetura Cliente-Servidor, com comunicação via protocolo TCP.

O sistema é composto por:

Um servidor desenvolvido em Python, responsável por aguardar conexões, autenticar o cliente e gerenciar a troca de mensagens.

Um cliente desenvolvido em Java, com interface gráfica, responsável por se conectar ao servidor e permitir o envio e recebimento de mensagens.

O objetivo do trabalho é demonstrar, de forma prática, os conceitos de redes de computadores, comunicação via sockets, autenticação básica e integração entre diferentes linguagens de programação.



2. Linguagens Utilizadas

Java – Desenvolvimento do cliente com interface gráfica (Swing) e comunicação via socket.

Python – Desenvolvimento do servidor utilizando sockets TCP.


3. Como Executar o Servidor e o Cliente
   
3.1 Pré-requisitos

Java JDK 8 ou superior instalado.

Python 3.6 ou superior instalado.

Ambos os dispositivos conectados à mesma rede (caso não seja utilizado localhost).


3.2 Executando o Servidor (Python)

Abra o terminal na pasta do projeto.

Execute o seguinte comando:


python server.py


O servidor será iniciado e ficará aguardando conexões na porta 5000.

Se estiver executando cliente e servidor na mesma máquina, utilize o endereço IP:

127.0.0.1

Se estiver executando em máquinas diferentes, utilize o endereço IP da máquina onde o servidor está em execução (por exemplo: 192.168.0.X).


3.3 Executando o Cliente (Java)

Compile o arquivo do cliente:

javac ClienteFrame.java

Execute o programa:

java ClienteFrame

Ao abrir a interface:

Informe o endereço IP do servidor.

Informe a senha definida no servidor.

Após autenticação, será possível trocar mensagens.



4. Explicação da Criptografia Utilizada

O projeto não utiliza criptografia avançada.

A aplicação implementa apenas um mecanismo simples de autenticação por senha, onde:

O cliente envia a senha ao servidor.

O servidor compara a senha recebida com um valor previamente definido.

Caso a senha esteja correta, a comunicação é permitida.

Caso contrário, a conexão é encerrada.

A senha é transmitida em texto simples, sem qualquer mecanismo de criptografia ou hash. 



5. Arquitetura da Solução

A solução segue o modelo clássico de arquitetura Cliente-Servidor.


5.1 Servidor (Python)

Cria um socket TCP.

Fica em estado de escuta na porta 5000.

Aceita conexões de clientes.

Realiza validação da senha.

Gerencia o envio e recebimento de mensagens.

Mantém a comunicação até que uma das partes finalize a conexão.


5.2 Cliente (Java)

Interface gráfica desenvolvida com Swing.

Estabelece conexão com o servidor via socket TCP.

Envia a senha para autenticação.

Permite o envio e recebimento de mensagens em tempo real.



6. Fluxo de Comunicação

O cliente inicia a conexão TCP com o servidor.

O servidor aceita a conexão.

O cliente envia a senha para autenticação.

O servidor valida a senha.

Após autenticação bem-sucedida, inicia-se a troca de mensagens.

A comunicação permanece ativa até que uma das partes encerre a conexão.

