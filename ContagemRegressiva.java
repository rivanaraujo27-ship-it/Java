import javax.swing.JOptionPane;

public class ContagemRegressiva {
    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Digite um número inteiro para iniciar a contagem regressiva:");
        
        try {
            int numero = Integer.parseInt(input);
            
            StringBuilder contagem = new StringBuilder("Contagem regressiva:\n");
            for (int i = numero; i >= 0; i -= 2) {
                contagem.append(i).append("\n");
            }
            
            JOptionPane.showMessageDialog(null, contagem.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, insira um número inteiro válido.");
        }
    }
}