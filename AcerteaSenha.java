import javax.swing.JOptionPane;

public class AcerteaSenha {
    public static void main(String[] args) {
        String senhaCorreta = "Segredo1465";
        String senhaUsuario;
        int tentativas = 0;
        int maxTentativas = 3;

        do {
            senhaUsuario = JOptionPane.showInputDialog("Digite a senha:");

            if (senhaUsuario == null) {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return;
            }

            tentativas++;

            if (senhaUsuario.equals(senhaCorreta)) {
                JOptionPane.showMessageDialog(null, "Senha correta! Acesso concedido.");
                return;
            } else {
                JOptionPane.showMessageDialog(null, "Senha incorreta. Tente novamente.");
            }
        } while (tentativas < maxTentativas);

        JOptionPane.showMessageDialog(null, "Número máximo de tentativas atingido. Acesso negado.");
    }
}