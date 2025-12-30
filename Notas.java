import javax.swing.JOptionPane;

public class Notas {
    public static void main(String[] args) {

        int Notas = Integer.parseInt(JOptionPane.showInputDialog("Digite a quantidade de notas a serem inseridas:"));
        double somaNotas = 0.0;
        String input = JOptionPane.showInputDialog("Informe a nota do estudante! (0-10):");
        
        try {
            int calificacion = Integer.parseInt(input);
            String resultado;

            if (calificacion >= 90 && calificacion <= 100) {
                resultado = "A";
            } else if (calificacion >= 80 && calificacion < 90) {
                resultado = "B";
            } else if (calificacion >= 70 && calificacion < 80) {
                resultado = "C";
            } else if (calificacion >= 60 && calificacion < 70) {
                resultado = "D";
            } else if (calificacion >= 0 && calificacion < 60) {
                resultado = "F";
            } else {
                resultado = "Calificación inválida. Debe estar entre 0 y 100.";
            }

            JOptionPane.showMessageDialog(null, "La calificación es: " + resultado);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor ingrese un número entero.");
        }
    }
}