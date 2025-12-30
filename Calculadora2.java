import javax.swing.JOptionPane;

public class Calculadora2 {
    public static void main(String[] args) {
        while (true) {
            String op = JOptionPane.showInputDialog(null,
                "Selecione a operação:\n1) Soma\n2) Subtração\n3) Multiplicação\n4) Divisão\n5) Sair",
                "Calculadora", JOptionPane.QUESTION_MESSAGE);
            if (op == null) break; // usuário cancelou
            op = op.trim();
            if (op.equals("5") || op.equalsIgnoreCase("sair")) break;

            int choice;
            try {
                choice = Integer.parseInt(op);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida.");
                continue;
            }

            String s1 = JOptionPane.showInputDialog(null, "Digite o primeiro número:", "Calculadora", JOptionPane.QUESTION_MESSAGE);
            if (s1 == null) break;
            String s2 = JOptionPane.showInputDialog(null, "Digite o segundo número:", "Calculadora", JOptionPane.QUESTION_MESSAGE);
            if (s2 == null) break;

            double a, b;
            try {
                a = Double.parseDouble(s1);
                b = Double.parseDouble(s2);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida: use números.");
                continue;
            }

            double result = 0;
            boolean valid = true;
            switch (choice) {
                case 1: result = a + b; break;
                case 2: result = a - b; break;
                case 3: result = a * b; break;
                case 4:
                    if (b == 0) {
                        JOptionPane.showMessageDialog(null, "Erro: divisão por zero.");
                        valid = false;
                    } else {
                        result = a / b;
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida.");
                    valid = false;
            }

            if (valid) {
                JOptionPane.showMessageDialog(null, "Resultado: " + result, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}