import javax.swing.JOptionPane;

public class MediaAluno {
    public static void main(String[] args) {
        // Ler nome
        String nome = JOptionPane.showInputDialog("Digite o nome do aluno:");
        if (nome == null) { // usuário cancelou
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return;
        }

        // Ler e validar quantidade de notas (1 a 10)
        int quantidadeNotas = 0;
        while (true) {
            String qStr = JOptionPane.showInputDialog("Digite a quantidade de notas a serem inseridas (1 a 10):");
            if (qStr == null) {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return;
            }
            try {
                quantidadeNotas = Integer.parseInt(qStr);
                quantidadeNotas = Math.abs(quantidadeNotas);
                if (quantidadeNotas < 1) quantidadeNotas = 1;
                if (quantidadeNotas > 10) quantidadeNotas = 10;
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida! Por favor, digite um número inteiro.");
            }
        }

        double somaNotas = 0.0;
        int notasNegativas = 0; // < 10
        int notasPositivas = 0; // >= 10

        // Ler cada nota com validação: deve ser > 0 e <= 20
        for (int i = 1; i <= quantidadeNotas; i++) {
            double nota = 0.0;
            while (true) {
                String notaStr = JOptionPane.showInputDialog("Digite a nota " + i + " (maior que 0 e até 20):");
                if (notaStr == null) {
                    JOptionPane.showMessageDialog(null, "Operação cancelada.");
                    return;
                }
                try {
                    nota = Double.parseDouble(notaStr);
                    if (nota > 0.0 && nota <= 20.0) {
                        break; // nota válida
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Nota inválida! A nota deve ser maior que 0 e menor ou igual a 20.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Entrada inválida! Por favor, insira um número válido.");
                }
            }

            // adicionar e contabilizar
            somaNotas += nota;
            if (nota < 10.0) {
                notasNegativas++;
            } else {
                notasPositivas++;
            }
        }

        double media = somaNotas / quantidadeNotas;

        // Mensagem de aprovação/reprovação considerando 10.0 como limiar
        String mensagemFinal;
        if (media >= 10.0) {
            mensagemFinal = "Parabéns " + nome + "! Você foi aprovado 😀\n";
        } else {
            mensagemFinal = "Infelizmente " + nome + ", você foi reprovado 😟\n";
        }

        // Relatório final
        mensagemFinal += "\n====== RELATÓRIO ======\n"
                + "Quantidade de notas inseridas: " + quantidadeNotas + "\n"
                + "Média final: " + String.format("%.2f", media) + "\n"
                + "Notas abaixo de 10: " + notasNegativas + "\n"
                + "Notas iguais ou acima de 10: " + notasPositivas + "\n";

        JOptionPane.showMessageDialog(null, mensagemFinal);
    }
}
