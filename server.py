import socket
import threading
import tkinter as tk
from tkinter.scrolledtext import ScrolledText

HOST = "0.0.0.0"
PORT = 5000
SENHA = "2706"

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server.bind((HOST, PORT))
server.listen()

conn_global = None
rodando = True

# =======================
# INTERFACE GRÁFICA
# =======================

janela = tk.Tk()
janela.title("Servidor")

area_chat = ScrolledText(janela, state='disabled', width=60, height=20)
area_chat.pack(padx=10, pady=10)

entrada = tk.Entry(janela, width=60)
entrada.pack(padx=10, pady=5)

def mostrar_mensagem(msg):
    area_chat.config(state='normal')
    area_chat.insert(tk.END, msg + "\n")
    area_chat.config(state='disabled')
    area_chat.see(tk.END)

def enviar_mensagem(event=None):
    global conn_global

    mensagem = entrada.get()
    entrada.delete(0, tk.END)

    if mensagem and conn_global:
        try:
            conn_global.sendall((mensagem + "\n").encode("utf-8"))
            mostrar_mensagem("Servidor: " + mensagem)
        except:
            mostrar_mensagem("Erro ao enviar mensagem.")

# ENTER envia mensagem
entrada.bind("<Return>", enviar_mensagem)

def encerrar_servidor():
    global rodando
    rodando = False
    try:
        server.close()
    except:
        pass
    janela.destroy()

botao_sair = tk.Button(janela, text="Encerrar Servidor", command=encerrar_servidor)
botao_sair.pack(pady=5)

# =======================
# THREAD DO CLIENTE
# =======================

def cliente_thread(conn, addr):
    global conn_global
    conn_global = conn

    mostrar_mensagem(f"Conectado: {addr}")

    try:
        conn.sendall("Digite a senha:\n".encode("utf-8"))

        conn_file = conn.makefile("r", encoding="utf-8")

        senha_recebida = conn_file.readline().strip()

        if senha_recebida != SENHA:
            conn.sendall("Senha incorreta.\n".encode("utf-8"))
            conn.close()
            mostrar_mensagem("Senha incorreta. Conexão encerrada.")
            return

        conn.sendall("Autenticado com sucesso!\n".encode("utf-8"))
        mostrar_mensagem("Cliente autenticado com sucesso!")

        while rodando:
            msg = conn_file.readline()

            if not msg:
                break

            msg = msg.strip()

            if msg.lower() == "sair":
                break

            mostrar_mensagem(f"Cliente: {msg}")

    except Exception as e:
        mostrar_mensagem("Erro na conexão.")

    conn.close()
    mostrar_mensagem("Sistema: Conexão encerrada.")

# =======================
# THREAD PARA ACEITAR CONEXÕES
# =======================

def aceitar_conexoes():
    while rodando:
        try:
            conn, addr = server.accept()
            threading.Thread(target=cliente_thread, args=(conn, addr), daemon=True).start()
        except:
            break

threading.Thread(target=aceitar_conexoes, daemon=True).start()

mostrar_mensagem("Servidor iniciado...")
mostrar_mensagem("Aguardando conexões...\n")

janela.mainloop()