import javax.swing.JOptionPane;


public class Vetores {
    public static void main(String[] args) {
        // Declaração do vetor para armazenar os números como strings
        String[] numeroStrings = new String[10];

        // Loop para coletar os nomes dos usuários
        for (int i = 0; i < numeroStrings.length; i++) {
            numeroStrings[i] = JOptionPane.showInputDialog("Digite o número " + (i + 1) + ":");
        }

        // Construção da mensagem para exibir os nomes
        StringBuilder mensagem = new StringBuilder("Números digitados:\nTotal de casas de N " + numeroStrings.length + "\n");
        for (String numeroString : numeroStrings) {
            mensagem.append(numeroString).append("\n");
        }

        // Exibição dos nomes em uma caixa de diálogo
        JOptionPane.showMessageDialog(null, mensagem.toString());
    }
}
