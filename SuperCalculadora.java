import javax.swing.JOptionPane;

public class SuperCalculadora {
    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Digite um número inteiro:");
        int numero = Integer.parseInt(input);

        int raizQuadrada = (int) Math.sqrt(numero);
        int cubo = (int) Math.pow(numero, 3);
        double raizCubica = Math.cbrt(numero);
        double valorAbsoluto = Math.abs(numero);

        String mensagem = "Analisando o número " + numero + ":\n"
                + "Raiz Quadrada: " + raizQuadrada + "\n"
                + "Cubo: " + cubo + "\n"
                + "Raiz Cúbica: " + String.format("%.2f", raizCubica) + "\n"
                + "Valor Absoluto: " + String.format("%.2f", valorAbsoluto);

        JOptionPane.showMessageDialog(null, mensagem);
    }
}
