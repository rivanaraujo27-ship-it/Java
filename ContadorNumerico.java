import javax.swing.JOptionPane;

public class ContadorNumerico {
    public static void main(String[] args) {
        int inicio = Integer.parseInt(JOptionPane.showInputDialog("Digite o número inicial:"));
        int fim = Integer.parseInt(JOptionPane.showInputDialog("Digite o número final:"));
        int passo = Integer.parseInt(JOptionPane.showInputDialog("Digite o valor do passo:"));

        if (passo <= 0) {
            JOptionPane.showMessageDialog(null, "Passo inválido! Considerando passo = 1.");
            passo = 1;
        }

        StringBuilder resultado = new StringBuilder("Contagem:\n");

        if (inicio < fim) {
            // Contagem crescente
            for (int i = inicio; i <= fim; i += passo) {
                resultado.append(i).append(" \uD83D\uDC49 ");
            }
        } else {
            // Contagem decrescente
            for (int i = inicio; i >= fim; i -= passo) {
                resultado.append(i).append(" \uD83D\uDC49 ");
            }
        }

        resultado.append("\uD83C\uDFC1 Fim!");

        JOptionPane.showMessageDialog(null, resultado.toString());
    }
}
