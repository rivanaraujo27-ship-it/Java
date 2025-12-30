import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.Random;
import java.awt.geom.Ellipse2D;
import java.awt.Point;
import java.awt.Rectangle;

public class JogodoBotao extends JFrame {

    // Valores do jogo
    private long totalGasto = 0;
    private long ganhoPorSegundo = 1;
    private long segundos = 0;

    // Preços iniciais
    private long precoFarmacia = 50_000;
    private long precoCafe = 10_000;
    private long precoSupermercado = 500_000;
    private long precoIndustria = 20_000_000;

    // Incrementos por upgrade
    private long incFarmacia = 50;
    private long incCafe = 10;
    private long incSupermercado = 500;
    private long incIndustria = 5_000;

    // Componentes
    private JLabel lblInfo;
    private CookieButton btnPrincipal;
    private JButton btnFarmacia, btnCafe, btnSupermercado, btnIndustria;

    private JPanel painelCentro;

    private Timer timerPrincipal;
    private Timer timerFarmaciaHide, timerCafeHide, timerSuperHide, timerIndustriaHide;

    private Random random = new Random();
    private long lastClickTime = -1; // para medir velocidade do clique

    public JogodoBotao() {
        super("Jogo do Botão - 1 Bilhão de €");

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(10, 12, 30));

        // ===== TOPO: INFO =====
        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 18));
        lblInfo.setForeground(Color.WHITE);

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(new Color(25, 30, 60));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelTopo.add(lblInfo);
        atualizarTextoInfo();
        add(painelTopo, BorderLayout.NORTH);

        // ===== CENTRO: COOKIE FIXO + UPGRADES ALEATÓRIOS =====
        painelCentro = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradiente de fundo
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(30, 35, 80),
                        getWidth(), getHeight(), new Color(10, 10, 35));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        painelCentro.setLayout(null); // posição absoluta
        add(painelCentro, BorderLayout.CENTER);

        // Botão principal tipo cookie (fixo no centro)
        btnPrincipal = new CookieButton("+ €");
        btnPrincipal.addActionListener(e -> cliquePrincipal());
        painelCentro.add(btnPrincipal);

        // reposicionar sempre que o painel mudar de tamanho
        painelCentro.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionarCookieNoCentro();
            }
        });

        // Botões de upgrade
        btnFarmacia = new JButton();
        btnCafe = new JButton();
        btnSupermercado = new JButton();
        btnIndustria = new JButton();

        estilizarBotaoUpgrade(btnFarmacia, new Color(52, 152, 219));
        estilizarBotaoUpgrade(btnCafe, new Color(241, 196, 15));
        estilizarBotaoUpgrade(btnSupermercado, new Color(230, 126, 34));
        estilizarBotaoUpgrade(btnIndustria, new Color(155, 89, 182));

        btnFarmacia.setVisible(false);
        btnCafe.setVisible(false);
        btnSupermercado.setVisible(false);
        btnIndustria.setVisible(false);

        painelCentro.add(btnFarmacia);
        painelCentro.add(btnCafe);
        painelCentro.add(btnSupermercado);
        painelCentro.add(btnIndustria);

        atualizarTextoUpgrades();

        btnFarmacia.addActionListener(e -> comprarFarmacia());
        btnCafe.addActionListener(e -> comprarCafe());
        btnSupermercado.addActionListener(e -> comprarSupermercado());
        btnIndustria.addActionListener(e -> comprarIndustria());

        // ===== TIMER PRINCIPAL (1s) =====
        timerPrincipal = new Timer(1000, e -> {
            segundos++;
            totalGasto += ganhoPorSegundo;
            atualizarTextoInfo();

            if (totalGasto >= 1_000_000_000L) {
                encerrarJogo();
            }
        });
        timerPrincipal.start();

        // Iniciar loop dos upgrades (aparecem/desaparecem até o fim do jogo)
        iniciarLoopFarmacia();
        iniciarLoopCafe();
        iniciarLoopSupermercado();
        iniciarLoopIndustria();

        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // garantir cookie centralizado ao abrir
        posicionarCookieNoCentro();
    }

    // ===================================================
    // POSICIONAMENTO
    // ===================================================
    private void posicionarCookieNoCentro() {
        int w = painelCentro.getWidth();
        int h = painelCentro.getHeight();
        if (w <= 0 || h <= 0) return;

        int bw = 220;
        int bh = 220;
        int x = (w - bw) / 2;
        int y = (h - bh) / 2;
        btnPrincipal.setBounds(x, y, bw, bh);
    }

    private void posicionarUpgradeAleatorio(JButton botao) {
        // Não pode aparecer em cima do cookie (com margem)
        Dimension pref = botao.getPreferredSize();
        int bw = pref.width;
        int bh = pref.height;

        int panelW = painelCentro.getWidth();
        int panelH = painelCentro.getHeight();

        if (panelW <= 0 || panelH <= 0) return;

        Rectangle cookie = btnPrincipal.getBounds();
        int margem = 15;

        Rectangle areaCookie = new Rectangle(
                cookie.x - margem,
                cookie.y - margem,
                cookie.width + 2 * margem,
                cookie.height + 2 * margem
        );

        int maxTentativas = 30;
        for (int i = 0; i < maxTentativas; i++) {
            int margemTela = 10;
            int maxX = panelW - bw - margemTela;
            int maxY = panelH - bh - margemTela;
            if (maxX <= margemTela) maxX = margemTela + 1;
            if (maxY <= margemTela) maxY = margemTela + 1;

            int x = margemTela + random.nextInt(maxX - margemTela);
            int y = margemTela + random.nextInt(maxY - margemTela);

            Rectangle candidato = new Rectangle(x, y, bw, bh);
            if (!candidato.intersects(areaCookie)) {
                botao.setBounds(x, y, bw, bh);
                return;
            }
        }

        // fallback: se não achar, põe no canto
        botao.setBounds(10, 10, bw, bh);
    }

    // ===================================================
    // LÓGICA DO BOTÃO PRINCIPAL (VELOCIDADE)
    // ===================================================
    private void cliquePrincipal() {
        long agora = System.currentTimeMillis();
        long delta = (lastClickTime < 0) ? Long.MAX_VALUE : (agora - lastClickTime);
        lastClickTime = agora;

        long incremento;
        if (delta < 150) {
            incremento = 1000;
        } else if (delta < 300) {
            incremento = 100;
        } else if (delta < 500) {
            incremento = 50;
        } else if (delta < 800) {
            incremento = 20;
        } else if (delta < 1200) {
            incremento = 10;
        } else {
            incremento = 5;
        }

        ganhoPorSegundo += incremento;
        totalGasto += incremento;
        atualizarTextoInfo();
    }

    // ===================================================
    // LOOP DOS UPGRADES (MENOS CONSTÂNCIA, DESAPARECEM RÁPIDO)
    // ===================================================
    private void iniciarLoopFarmacia() {
        agendarMostrarUpgrade(btnFarmacia, "farmacia");
    }

    private void iniciarLoopCafe() {
        agendarMostrarUpgrade(btnCafe, "cafe");
    }

    private void iniciarLoopSupermercado() {
        agendarMostrarUpgrade(btnSupermercado, "super");
    }

    private void iniciarLoopIndustria() {
        agendarMostrarUpgrade(btnIndustria, "industria");
    }

    private void agendarMostrarUpgrade(JButton botao, String tipo) {
        // Fica escondido por mais tempo (2,5 a 6 segundos)
        int delayOff = 2500 + random.nextInt(3500); // 2,5–6s
        Timer t = new Timer(delayOff, e -> mostrarUpgrade(botao, tipo));
        t.setRepeats(false);
        t.start();
    }

    private void mostrarUpgrade(JButton botao, String tipo) {
        posicionarUpgradeAleatorio(botao);
        botao.setVisible(true);

        // Visível bem pouco (0,5 a 1,2 segundos)
        int tempoVisivel = 500 + random.nextInt(700); // 0,5–1,2s

        Timer esconder = new Timer(tempoVisivel, e -> {
            botao.setVisible(false);
            agendarMostrarUpgrade(botao, tipo); // looping contínuo
        });
        esconder.setRepeats(false);
        esconder.start();

        switch (tipo) {
            case "farmacia" -> timerFarmaciaHide = esconder;
            case "cafe" -> timerCafeHide = esconder;
            case "super" -> timerSuperHide = esconder;
            case "industria" -> timerIndustriaHide = esconder;
        }
    }

    // ===================================================
    // COMPRAS (CLICOU A TEMPO)
    // ===================================================
    private void comprarFarmacia() {
        if (timerFarmaciaHide != null) timerFarmaciaHide.stop();
        btnFarmacia.setVisible(false);

        totalGasto += precoFarmacia;
        ganhoPorSegundo += incFarmacia;
        precoFarmacia *= 2;

        atualizarTextoInfo();
        atualizarTextoUpgrades();
        agendarMostrarUpgrade(btnFarmacia, "farmacia");
    }

    private void comprarCafe() {
        if (timerCafeHide != null) timerCafeHide.stop();
        btnCafe.setVisible(false);

        totalGasto += precoCafe;
        ganhoPorSegundo += incCafe;
        precoCafe *= 2;

        atualizarTextoInfo();
        atualizarTextoUpgrades();
        agendarMostrarUpgrade(btnCafe, "cafe");
    }

    private void comprarSupermercado() {
        if (timerSuperHide != null) timerSuperHide.stop();
        btnSupermercado.setVisible(false);

        totalGasto += precoSupermercado;
        ganhoPorSegundo += incSupermercado;
        precoSupermercado *= 2;

        atualizarTextoInfo();
        atualizarTextoUpgrades();
        agendarMostrarUpgrade(btnSupermercado, "super");
    }

    private void comprarIndustria() {
        if (timerIndustriaHide != null) timerIndustriaHide.stop();
        btnIndustria.setVisible(false);

        totalGasto += precoIndustria;
        ganhoPorSegundo += incIndustria;
        precoIndustria *= 2;

        atualizarTextoInfo();
        atualizarTextoUpgrades();
        agendarMostrarUpgrade(btnIndustria, "industria");
    }

    // ===================================================
    // VISUAL
    // ===================================================
    private void estilizarBotaoUpgrade(JButton botao, Color cor) {
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Arial", Font.BOLD, 13));
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }

    private void atualizarTextoInfo() {
        lblInfo.setText(String.format(
                "<html><b>Total:</b> €%,d &nbsp;&nbsp;|&nbsp;&nbsp; <b>Ganho:</b> €%,d/s &nbsp;&nbsp;|&nbsp;&nbsp; <b>Tempo:</b> %ds</html>",
                totalGasto, ganhoPorSegundo, segundos));
    }

    private void atualizarTextoUpgrades() {
        btnFarmacia.setText("<html>🏥 Farmácia<br>Preço: €" + String.format("%,d", precoFarmacia) +
                "<br>+€" + incFarmacia + "/s</html>");
        btnCafe.setText("<html>☕ Café<br>Preço: €" + String.format("%,d", precoCafe) +
                "<br>+€" + incCafe + "/s</html>");
        btnSupermercado.setText("<html>🛒 Supermercado<br>Preço: €" + String.format("%,d", precoSupermercado) +
                "<br>+€" + incSupermercado + "/s</html>");
        btnIndustria.setText("<html>🏭 Indústria<br>Preço: €" + String.format("%,d", precoIndustria) +
                "<br>+€" + incIndustria + "/s</html>");
    }

    private void encerrarJogo() {
        timerPrincipal.stop();
        JOptionPane.showMessageDialog(this,
                "Parabéns! Chegaste a 1 Bilhão de Euros em " + segundos + " segundos!",
                "Vitória",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    // ===================================================
    // BOTÃO "COOKIE" ARREDONDADO
    // ===================================================
    private class CookieButton extends JButton {
        private Point[] chips;

        public CookieButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.BLACK);
            setFont(new Font("Arial", Font.BOLD, 18));
            setPreferredSize(new Dimension(220, 220));

            // gera posições fixas para os "chips"
            chips = new Point[12];
            Random r = new Random();
            for (int i = 0; i < chips.length; i++) {
                chips[i] = new Point(20 + r.nextInt(140), 20 + r.nextInt(140));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int diameter = Math.min(getWidth(), getHeight()) - 10;
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;

            // desenha cookie
            g2.setColor(new Color(210, 180, 140));
            g2.fill(new Ellipse2D.Double(x, y, diameter, diameter));

            // borda
            g2.setColor(new Color(160, 120, 80));
            g2.setStroke(new BasicStroke(4f));
            g2.draw(new Ellipse2D.Double(x, y, diameter, diameter));

            // "chocolate chips" fixos
            g2.setColor(new Color(90, 60, 40));
            for (Point p : chips) {
                int cx = x + p.x;
                int cy = y + p.y;
                g2.fillOval(cx, cy, 12, 12);
            }

            // texto
            String txt = getText();
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (getWidth() - fm.stringWidth(txt)) / 2;
            int ty = getHeight() / 2 + fm.getAscent();
            g2.setColor(Color.BLACK);
            g2.drawString(txt, tx, ty);

            g2.dispose();
        }

        @Override
        public boolean contains(int x, int y) {
            int radius = Math.min(getWidth(), getHeight()) / 2;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int dx = x - centerX;
            int dy = y - centerY;
            return dx * dx + dy * dy <= radius * radius;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JogodoBotao::new);
    }
}
