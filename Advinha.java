import javax.swing.JOptionPane;
import java.util.Random;

public class Advinha {
    public static void main(String[] args) {
        Random random = new Random();
        int numeroSecreto = random.nextInt(100) + 1; // Número entre 1 e 100
        int tentativas = 0;
        int palpite = 0;

        JOptionPane.showMessageDialog(null, "Bem-vindo ao jogo de adivinhação! Tente adivinhar o número entre 1 e 100.");

        while (palpite != numeroSecreto) {
            String entrada = JOptionPane.showInputDialog("Digite seu palpite:");
            if (entrada == null) {
                JOptionPane.showMessageDialog(null, "Jogo encerrado. O número secreto era: " + numeroSecreto);
                return;
            }

            try {
                palpite = Integer.parseInt(entrada);
                tentativas++;

                if (palpite < numeroSecreto) {
                    JOptionPane.showMessageDialog(null, "Muito baixo! Tente novamente.");
                } else if (palpite > numeroSecreto) {
                    JOptionPane.showMessageDialog(null, "Muito alto! Tente novamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Parabéns! Você adivinhou o número em " + tentativas + " tentativas.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um número válido.");
            }
        }
    }
}