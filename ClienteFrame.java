package Cliente;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ClienteFrame extends JFrame {

    private JTextField campoMensagem;
    private JTextArea areaChat;
    private JButton botaoEnviar;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private boolean conexaoEncerrada = false; // controle para não duplicar mensagem

    public ClienteFrame() {
        criarTelaLogin();
    }

    // ===== TELA DE LOGIN =====
    private void criarTelaLogin() {

        JTextField campoIP = new JTextField("127.0.0.1");
        JPasswordField campoSenha = new JPasswordField();

        JPanel painel = new JPanel(new GridLayout(2, 2, 5, 5));
        painel.add(new JLabel("IP do Servidor:"));
        painel.add(campoIP);
        painel.add(new JLabel("Senha:"));
        painel.add(campoSenha);

        int opcao = JOptionPane.showConfirmDialog(
                null,
                painel,
                "Conectar ao Servidor",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcao == JOptionPane.OK_OPTION) {
            conectar(campoIP.getText(), new String(campoSenha.getPassword()));
        } else {
            System.exit(0);
        }
    }

    // ===== CONEXÃO =====
    private void conectar(String ip, String senha) {
        try {
            socket = new Socket(ip, 5000);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            in.readLine(); // pedido de senha
            out.println(senha);

            String resposta = in.readLine();

            if (!resposta.contains("Autenticado")) {
                JOptionPane.showMessageDialog(this, "Senha incorreta!");
                socket.close();
                System.exit(0);
            }

            criarChat();
            areaChat.append("Servidor: " + resposta + "\n");

            // Thread para receber mensagens
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {

                        if (!msg.startsWith("Servidor:")) {
                            msg = "Servidor: " + msg;
                        }

                        areaChat.append(msg + "\n");
                    }

                    fecharConexao();

                } catch (Exception e) {
                    fecharConexao();
                }
            }).start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor.");
            System.exit(0);
        }
    }

    // ===== TELA DO CHAT =====
    private void criarChat() {

        setTitle("Cliente");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        areaChat = new JTextArea();
        areaChat.setEditable(false);
        areaChat.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(areaChat);
        scroll.setBorder(BorderFactory.createTitledBorder("Chat"));

        JPanel painelInferior = new JPanel(new BorderLayout(5, 5));

        JLabel label = new JLabel("Envie sua mensagem:");
        campoMensagem = new JTextField();
        botaoEnviar = new JButton("Enviar");

        painelInferior.add(label, BorderLayout.NORTH);
        painelInferior.add(campoMensagem, BorderLayout.CENTER);
        painelInferior.add(botaoEnviar, BorderLayout.EAST);

        add(scroll, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);

        botaoEnviar.addActionListener(e -> enviarMensagem());
        campoMensagem.addActionListener(e -> enviarMensagem()); // ENTER envia

        setVisible(true);
    }

    // ===== ENVIO DE MENSAGEM =====
    private void enviarMensagem() {

        String mensagem = campoMensagem.getText().trim();

        if (!mensagem.isEmpty()) {

            if (mensagem.equalsIgnoreCase("sair")) {
                out.println(mensagem);
                areaChat.append("Você: sair\n");
                fecharConexao();
                return;
            }

            out.println(mensagem);
            areaChat.append("Você: " + mensagem + "\n");

            campoMensagem.setText("");
        }
    }

    // ===== FECHAR CONEXÃO =====
    private void fecharConexao() {

        if (conexaoEncerrada) return; // impede duplicação

        conexaoEncerrada = true;

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception ignored) {}

        areaChat.append("Sistema: Conexão encerrada.\n");

        campoMensagem.setEnabled(false);
        botaoEnviar.setEnabled(false);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new ClienteFrame());
    }
}