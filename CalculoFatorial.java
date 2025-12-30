import javax.swing.JOptionPane;

public class CalculoFatorial {
    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Digite um número inteiro para calcular o fatorial:");
        
        try {
            int numero = Integer.parseInt(input);
            if (numero < 0) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um número inteiro não negativo.");
            } else {
                long fatorial = calcularFatorial(numero);
                JOptionPane.showMessageDialog(null, "O fatorial de " + numero + " é: " + fatorial);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, insira um número inteiro válido.");
        }
    }

    public static long calcularFatorial(int n) {
        long resultado = 1;
        for (int i = 2; i <= n; i++) {
            resultado *= i;
        }
        return resultado;
    }
}