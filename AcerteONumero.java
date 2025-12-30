import javax.swing.JOptionPane;

public class AcerteONumero {
    public static void main(String[] args) {
        int numeroSecreto = (int) (Math.random() * 100) + 1;
        int tentativas = 0;
        int palpite = 0;
        int pares = 0;
        int impares = 0;

        JOptionPane.showMessageDialog(null, "Bem-vindo ao jogo Acerte o Número!\nTente adivinhar o número entre 1 e 100.");
        while (palpite != numeroSecreto) {
            String entrada = JOptionPane.showInputDialog("Digite seu palpite:");
            if (entrada == null) {
                JOptionPane.showMessageDialog(null, "Jogo encerrado. O número secreto era: " + numeroSecreto);
                return;
            }

            try {
                palpite = Integer.parseInt(entrada);
                tentativas++;

                if (palpite % 2 == 0) {
                    pares++;    
                } else {
                    impares++;
                }
                if (palpite < 1 || palpite > 100) {
                    JOptionPane.showMessageDialog(null, "Por favor, digite um número entre 1 e 100.");
                } else if (palpite < numeroSecreto) {
                    JOptionPane.showMessageDialog(null, "Muito baixo! Tente novamente.");
                } else if (palpite > numeroSecreto) {
                    JOptionPane.showMessageDialog(null, "Muito alto! Tente novamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Parabéns! Você acertou o número " + numeroSecreto + " em " + tentativas + " tentativas.");
                    JOptionPane.showMessageDialog(null, "Quantidade de números pares digitados: " + pares + "\nQuantidade de números ímpares digitados: " + impares);}
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, digite um número válido.");
            }
        }
    }
}