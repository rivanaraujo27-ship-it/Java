import javax.swing.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ControledeVendas {

    private static final Locale LOCALE_PT = new Locale("pt", "PT");
    private static final NumberFormat CURRENCY_FMT = NumberFormat.getCurrencyInstance(LOCALE_PT);

    private static class Produto {
        String nome;
        double preco;

        Produto(String nome, double preco) {
            this.nome = nome;
            this.preco = preco;
        }

        @Override
        public String toString() {
            return nome + " - " + CURRENCY_FMT.format(preco);
        }
    }

    private static class Venda {
        Produto produto;
        int quantidade;

        Venda(Produto produto, int quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }

        double total() {
            return produto.preco * quantidade;
        }

        @Override
        public String toString() {
            return produto.nome +
                    " | Qt: " + quantidade +
                    " | Unit: " + CURRENCY_FMT.format(produto.preco) +
                    " | Total: " + CURRENCY_FMT.format(total());
        }
    }

    private final List<Produto> produtos = new ArrayList<>();
    private final List<Venda> vendas = new ArrayList<>();

    public void iniciar() {
        // roda em thread do Swing por segurança
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "Bem-vindo ao Sistema de Controle de Vendas!");
            cadastrarProdutos();
            menuPrincipal();
        });
    }

    private void menuPrincipal() {
        while (true) {
            String escolha = JOptionPane.showInputDialog(
                    null,
                    "MENU:\n" +
                            "1 - Registrar Venda\n" +
                            "2 - Listar Vendas\n" +
                            "3 - Gerir Produtos (adicionar / listar)\n" +
                            "0 - Sair\n\n" +
                            "Escolha uma opção:",
                    "Menu",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (escolha == null) {
                if (confirmarSaida()) return;
                else continue;
            }

            switch (escolha.trim()) {
                case "1":
                    registrarVenda();
                    break;
                case "2":
                    listarVendas();
                    break;
                case "3":
                    gerirProdutos();
                    break;
                case "0":
                    if (confirmarSaida()) return;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
            }
        }
    }

    private boolean confirmarSaida() {
        int resp = JOptionPane.showConfirmDialog(null, "Deseja realmente sair?", "Confirmar", JOptionPane.YES_NO_OPTION);
        return resp == JOptionPane.YES_OPTION;
    }

    private void cadastrarProdutos() {
        while (true) {
            String nome = JOptionPane.showInputDialog(null,
                    "Cadastrar produto (deixe vazio ou pressione Cancel para terminar):",
                    "Cadastrar Produto",
                    JOptionPane.QUESTION_MESSAGE);

            if (nome == null || nome.trim().isEmpty()) break;

            Double preco = pedirPreco(nome);
            if (preco == null) continue;

            produtos.add(new Produto(nome.trim(), preco));
            JOptionPane.showMessageDialog(null, "Produto cadastrado: " + nome + " - " + CURRENCY_FMT.format(preco));
        }
    }

    private void gerirProdutos() {
        String[] opcoes = {"Adicionar produto", "Listar produtos", "Voltar"};
        int sel = JOptionPane.showOptionDialog(null, "Gerir Produtos", "Produtos",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);

        if (sel == 0) {
            String nome = JOptionPane.showInputDialog(null, "Nome do produto:");
            if (nome == null || nome.trim().isEmpty()) return;
            Double preco = pedirPreco(nome);
            if (preco == null) return;
            produtos.add(new Produto(nome.trim(), preco));
            JOptionPane.showMessageDialog(null, "Produto adicionado: " + nome + " - " + CURRENCY_FMT.format(preco));
        } else if (sel == 1) {
            listarProdutosDialog();
        }
    }

    private void listarProdutosDialog() {
        if (produtos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum produto cadastrado.");
            return;
        }
        StringBuilder sb = new StringBuilder("Produtos cadastrados:\n\n");
        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            sb.append(i + 1).append(" - ").append(p.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private Double pedirPreco(String contextoNome) {
        while (true) {
            String precoStr = JOptionPane.showInputDialog(null,
                    "Digite o preço unitário do produto \"" + contextoNome + "\" (ex: 12.50 ou 12,50):",
                    "Preço",
                    JOptionPane.QUESTION_MESSAGE);

            if (precoStr == null) return null;

            precoStr = precoStr.trim().replace(",", ".");
            if (precoStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preço vazio. Tente novamente.");
                continue;
            }

            try {
                double preco = Double.parseDouble(precoStr);
                if (preco < 0) {
                    JOptionPane.showMessageDialog(null, "Preço não pode ser negativo.");
                    continue;
                }
                return preco;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valor inválido. Use formato numérico (ex: 12.50 ou 12,50).");
            }
        }
    }

    private void registrarVenda() {
        if (produtos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum produto cadastrado. Cadastre antes de registar vendas.");
            return;
        }

        Produto[] lista = produtos.toArray(new Produto[0]);
        Produto selecionado = (Produto) JOptionPane.showInputDialog(null,
                "Escolha o produto:",
                "Registrar Venda",
                JOptionPane.QUESTION_MESSAGE,
                null,
                lista,
                lista[0]);

        if (selecionado == null) return;

        Integer quantidade = pedirQuantidade();
        if (quantidade == null) return;

        vendas.add(new Venda(selecionado, quantidade));
        JOptionPane.showMessageDialog(null, "Venda registrada:\n" + selecionado.nome + " x " + quantidade + "\nTotal: " + CURRENCY_FMT.format(selecionado.preco * quantidade));
    }

    private Integer pedirQuantidade() {
        while (true) {
            String qtdStr = JOptionPane.showInputDialog(null, "Digite a quantidade vendida (número inteiro):", "Quantidade", JOptionPane.QUESTION_MESSAGE);
            if (qtdStr == null) return null;
            qtdStr = qtdStr.trim();
            if (qtdStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Quantidade vazia. Tente novamente.");
                continue;
            }
            try {
                int q = Integer.parseInt(qtdStr);
                if (q <= 0) {
                    JOptionPane.showMessageDialog(null, "Quantidade deve ser maior que zero.");
                    continue;
                }
                return q;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantidade inválida. Digite um número inteiro.");
            }
        }
    }

    private void listarVendas() {
        if (vendas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma venda registrada.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("VENDAS REGISTRADAS:\n\n");
        double soma = 0.0;
        for (Venda v : vendas) {
            sb.append(v.toString()).append("\n");
            soma += v.total();
        }
        sb.append("\nTOTAL GERAL: ").append(CURRENCY_FMT.format(soma));
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public static void main(String[] args) {
        Locale.setDefault(LOCALE_PT); // opcional
        new ControledeVendas().iniciar();
    }
}