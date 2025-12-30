import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class QualIdade {
    public static void main(String[] args) {

        List<String> nomes = new ArrayList<>();
        List<Integer> idades = new ArrayList<>();
        List<String> classificacoes = new ArrayList<>();

        while (true) {
            String nome = JOptionPane.showInputDialog("Digite o nome (ou 'sair' para encerrar):");
            if (nome == null || nome.equalsIgnoreCase("sair")) {
                break;
            }

            String idadeStr = JOptionPane.showInputDialog("Digite a idade de " + nome + ":");
            int idade = Integer.parseInt(idadeStr);

            String classificacao;
            if (idade < 12) {
                classificacao = "Criança";
            } else if (idade < 20) {
                classificacao = "Adolescente";
            } else if (idade < 60) {
                classificacao = "Adulto";
            } else {
                classificacao = "Idoso";
            }

            nomes.add(nome);
            idades.add(idade);
            classificacoes.add(classificacao);
        }
        StringBuilder resultado = new StringBuilder("Classificações:\n");
        for (int i = 0; i < nomes.size(); i++) {
            resultado.append(nomes.get(i))
                     .append(" - ")
                     .append(idades.get(i))
                     .append(" anos - ")
                     .append(classificacoes.get(i))
                     .append("\n");
        }
        JOptionPane.showMessageDialog(null, resultado.toString());
    }
}
