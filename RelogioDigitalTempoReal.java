import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RelogioDigitalTempoReal extends JFrame {
    private JLabel labelHora;
    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public RelogioDigitalTempoReal() {
        setTitle("Relógio Digital");
        setSize(300, 120);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        labelHora = new JLabel("", SwingConstants.CENTER);
        labelHora.setFont(new Font("Arial", Font.BOLD, 26));
        labelHora.setForeground(Color.BLUE);
        add(labelHora, BorderLayout.CENTER);

        // Atualiza o relógio a cada segundo
        Timer timer = new Timer(1000, e -> atualizarHora());
        timer.start();

        atualizarHora(); // Atualiza imediatamente
        setVisible(true);
    }

    private void atualizarHora() {
        LocalDateTime agora = LocalDateTime.now();
        labelHora.setText(agora.format(formato));
    }

    public static void main(String[] args) {
        new RelogioDigitalTempoReal();
    }
}
