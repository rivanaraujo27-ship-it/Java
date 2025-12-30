import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class CalendarioPT extends JFrame {
    private final Locale LOCALE_PT = new Locale("pt", "PT");

    private JLabel titulo;
    private JTextArea area;
    private JComboBox<String> comboMes;
    private JComboBox<Integer> comboAno;

    private YearMonth ymAtual;

    public CalendarioPT() {
        setTitle("Calendário (pt-PT)");
        setSize(420, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        ymAtual = YearMonth.now();

        // Topo: navegação + seletores
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnAnterior = new JButton("« Anterior");
        JButton btnProximo = new JButton("Próximo »");

        // Meses em pt-PT
        String[] meses = new String[12];
        for (int m = 1; m <= 12; m++) {
            meses[m - 1] = YearMonth.of(2000, m).getMonth().getDisplayName(TextStyle.FULL, LOCALE_PT);
            // capitalizar primeira letra (alguns LAF mostram em minúsculas)
            meses[m - 1] = meses[m - 1].substring(0,1).toUpperCase(LOCALE_PT) + meses[m - 1].substring(1);
        }
        comboMes = new JComboBox<>(meses);
        comboMes.setSelectedIndex(ymAtual.getMonthValue() - 1);

        comboAno = new JComboBox<>();
        for (int a = 1900; a <= 2100; a++) comboAno.addItem(a);
        comboAno.setSelectedItem(ymAtual.getYear());

        titulo = new JLabel("", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        topo.add(btnAnterior);
        topo.add(comboMes);
        topo.add(comboAno);
        topo.add(btnProximo);

        add(titulo, BorderLayout.NORTH);
        add(topo, BorderLayout.SOUTH);

        // Centro: área monoespaçada para grelha
        area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 16));
        add(new JScrollPane(area), BorderLayout.CENTER);

        // Ações
        btnAnterior.addActionListener((ActionEvent e) -> {
            ymAtual = ymAtual.minusMonths(1);
            syncSeletores();
            atualizarCalendario();
        });

        btnProximo.addActionListener((ActionEvent e) -> {
            ymAtual = ymAtual.plusMonths(1);
            syncSeletores();
            atualizarCalendario();
        });

        comboMes.addActionListener(e -> {
            int mes = comboMes.getSelectedIndex() + 1;
            int ano = (Integer) comboAno.getSelectedItem();
            ymAtual = YearMonth.of(ano, mes);
            atualizarCalendario();
        });

        comboAno.addActionListener(e -> {
            int mes = comboMes.getSelectedIndex() + 1;
            int ano = (Integer) comboAno.getSelectedItem();
            ymAtual = YearMonth.of(ano, mes);
            atualizarCalendario();
        });

        atualizarCalendario();
        setVisible(true);
    }

    private void syncSeletores() {
        comboMes.setSelectedIndex(ymAtual.getMonthValue() - 1);
        comboAno.setSelectedItem(ymAtual.getYear());
    }

    private void atualizarCalendario() {
        // Cabeçalho com nome do mês e ano em pt-PT
        String nomeMes = ymAtual.getMonth().getDisplayName(TextStyle.FULL, LOCALE_PT);
        nomeMes = nomeMes.substring(0,1).toUpperCase(LOCALE_PT) + nomeMes.substring(1);
        titulo.setText(nomeMes + " de " + ymAtual.getYear());

        StringBuilder sb = new StringBuilder();
        // Semana começando em Segunda (PT): Seg Ter Qua Qui Sex Sáb Dom
        sb.append(" Seg Ter Qua Qui Sex Sáb Dom\n");

        LocalDate primeiro = ymAtual.atDay(1);
        int diaSemanaPrimeiro = mapSegundaComoZero(primeiro.getDayOfWeek()); // 0..6

        // Espaços até o primeiro dia
        for (int i = 0; i < diaSemanaPrimeiro; i++) {
            sb.append("    ");
        }

        int diasNoMes = ymAtual.lengthOfMonth();
        for (int dia = 1; dia <= diasNoMes; dia++) {
            sb.append(String.format("%3d ", dia));
            int posicao = (diaSemanaPrimeiro + dia) % 7;
            if (posicao == 0) sb.append("\n");
        }

        area.setText(sb.toString());
    }

    /**
     * Converte DayOfWeek (MON=1..SUN=7) para índice com Segunda=0, ..., Domingo=6
     */
    private int mapSegundaComoZero(DayOfWeek dow) {
        int val = dow.getValue(); // MON=1..SUN=7
        return (val + 6) % 7;     // MON->0, TUE->1, ..., SUN->6
    }

    public static void main(String[] args) {
        // Opcional: visual "nativo"
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(CalendarioPT::new);
    }
}

