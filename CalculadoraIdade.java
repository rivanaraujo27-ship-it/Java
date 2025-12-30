import java.time.LocalDate;
import java.time.Period;
import javax.swing.JOptionPane;

public class CalculadoraIdade {
    public static void main(String[] args) {
        String nome = JOptionPane.showInputDialog("Digite seu nome:");
        JOptionPane.showMessageDialog(null, "Olá, " + nome + "!");
        
        String anoNascimentoStr = JOptionPane.showInputDialog("Digite o ano de nascimento:");
        int anoNascimento = Integer.parseInt(anoNascimentoStr);
        
        int anoAtual = LocalDate.now().getYear();
        LocalDate dataNascimento = LocalDate.of(anoNascimento, 1, 1);
        LocalDate dataAtual = LocalDate.now();
        
        Period periodo = Period.between(dataNascimento, dataAtual);
        
        JOptionPane.showMessageDialog(null, "Sua idade é: " + periodo.getYears() + " anos." + nome +
                                            "\nVocê nasceu em: " + anoNascimento + 
                                            "\nO ano atual é: " + anoAtual);
                                            
    }
}