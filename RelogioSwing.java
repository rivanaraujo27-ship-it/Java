import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
public class RelogioSwing extends JFrame {

    private JLabel dLabelata;
    private JLabel horaLabel;

    public RelogioSwing() {
        setTitle("Relógio Digital");
        setSize(250, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2,1));

        dLabelata = new JLabel();
        horaLabel = new JLabel();

        add(dLabelata);
        add(horaLabel);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarRelogio();
            }
        });
        timer.start();

        atualizarRelogio();
    }
    private void atualizarRelogio() {
        Calendar calendario = new GregorianCalendar();

        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH) + 1; // Janeiro é 0
        int ano = calendario.get(Calendar.YEAR);

        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        int segundo = calendario.get(Calendar.SECOND);

        String dataTexto = String.format("Data: %02d/%02d/%04d", dia, mes, ano);
        String horaTexto = String.format("Hora: %02d:%02d:%02d", hora, minuto, segundo);

        dLabelata.setText(dataTexto);
        horaLabel.setText(horaTexto);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RelogioSwing relogio = new RelogioSwing();
                relogio.setVisible(true);
            }
        });
    }
}

    