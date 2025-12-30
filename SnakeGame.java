import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Comparator;

public class SnakeGame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String playerName = JOptionPane.showInputDialog(null, "Seu nome para o placar:", "Jogador");
            if (playerName == null || playerName.isBlank()) playerName = "Jogador";

            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            ScoreBoard scoreBoard = new ScoreBoard();
            scoreBoard.registerPlayer(playerName);

            GamePanel gamePanel = new GamePanel(playerName, scoreBoard);

            frame.add(gamePanel, BorderLayout.CENTER);
            frame.add(scoreBoard, BorderLayout.EAST);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            SwingUtilities.invokeLater(gamePanel::requestFocusInWindow);
        });
    }

    // -------------------- Painel do jogo --------------------
    static class GamePanel extends JPanel implements ActionListener, KeyListener {
        private static final int GAME_WIDTH  = 600;
        private static final int GAME_HEIGHT = 600;
        private static final int UNIT = 20;

        // Velocidade
        private static final int START_DELAY_MS = 220;
        private static final int MIN_DELAY_MS   = 60;
        // Ajuste para atingir velocidade máxima só em 8000 pts (80 alimentos):
        // (220 - 60) / 80 = 2 ms por alimento
        private static final int MS_PER_FOOD    = 2;

        private static final int MAX_SCORE      = 1_000_000;
        private static final int POINTS_PER_OBSTACLE = 5_000;

        // Alimentos (muda a cada 1000 pontos)
        private enum FoodType { APPLE, CHERRY, BANANA, GRAPE, DIAMOND }
        private FoodType currentFoodType = FoodType.APPLE;

        private String playerName;                // pode mudar a cada novo jogo
        private final ScoreBoard scoreBoard;

        private final List<Point> snake = new LinkedList<>();
        private final List<Point> obstacles = new ArrayList<>();
        private Point food;
        private char dir = 'R';
        private boolean running = false;
        private boolean gameOver = false;
        private boolean win = false;
        private boolean paused = false;
        private boolean wrap = false; // mapa toroidal

        // countdown
        private boolean starting = false;
        private long startAtMillis = 0;
        private static final int COUNTDOWN_MS = 3000;

        private int score = 0;
        private final Random random = new Random();
        private Timer timer;

        // Cores
        private final Color bgGrid     = new Color(30, 30, 30);
        private final Color gridLine   = new Color(45, 45, 45);
        private final Color snakeHead  = new Color(46, 204, 113);
        private final Color snakeBodyA = new Color(39, 174, 96);
        private final Color snakeBodyB = new Color(33, 160, 86);
        private final Color obstacleColor = new Color(120, 120, 120);

        GamePanel(String playerName, ScoreBoard scoreBoard) {
            this.playerName = playerName;
            this.scoreBoard = scoreBoard;

            setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
            setBackground(bgGrid);
            setFocusable(true);
            addKeyListener(this);

            startGame();
        }

        private void startGame() {
            snake.clear();
            obstacles.clear();
            snake.add(new Point(UNIT * 5, UNIT * 5));
            snake.add(new Point(UNIT * 4, UNIT * 5));
            snake.add(new Point(UNIT * 3, UNIT * 5));
            dir = 'R';
            score = 0;
            win = false;
            paused = false;
            wrap = false; // começa sem wrap; o jogador pode alternar com 'W'

            scoreBoard.registerPlayer(playerName);
            scoreBoard.setScore(playerName, score);

            updateFoodType();
            spawnFood();
            running = false;     // aguardando countdown
            gameOver = false;
            starting = true;
            startAtMillis = System.currentTimeMillis();

            if (timer != null) timer.stop();
            timer = new Timer(START_DELAY_MS, this);
            timer.start();
        }

        private void ensureObstaclesForScore() {
            int shouldHave = Math.max(0, score / POINTS_PER_OBSTACLE);
            while (obstacles.size() < shouldHave) {
                Point p = spawnFreeCellAvoidingHUD();
                obstacles.add(p);
            }
        }

        private Point spawnFreeCellAvoidingHUD() {
            int cols = GAME_WIDTH / UNIT;
            int rows = GAME_HEIGHT / UNIT;
            while (true) {
                int x = random.nextInt(cols) * UNIT;
                int yIndex = 1 + random.nextInt(rows - 1); // [1 .. rows-1] evita HUD
                int y = yIndex * UNIT;
                Point p = new Point(x, y);
                if (!snake.contains(p) && (food == null || !food.equals(p)) && !obstacles.contains(p)) {
                    return p;
                }
            }
        }

        private void spawnFood() {
            food = spawnFreeCellAvoidingHUD();
        }

        private void updateFoodType() {
            int step = (score / 1000) % 5;
            currentFoodType = FoodType.values()[step];
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawGrid(g2);
            drawObstacles(g2);
            if (running || starting) {
                if (food != null) drawFood(g2);
                drawSnake(g2);
                drawHUD(g2);
                if (starting) drawCountdown(g2);
                if (paused && !starting) drawPaused(g2);
            } else {
                drawSnake(g2);
                if (food != null) drawFood(g2);
                drawHUD(g2);
                drawGameOver(g2);
            }
            g2.dispose();
        }

        private void drawGrid(Graphics2D g2) {
            g2.setColor(gridLine);
            for (int x = 0; x <= GAME_WIDTH; x += UNIT) g2.drawLine(x, 0, x, GAME_HEIGHT);
            for (int y = 0; y <= GAME_HEIGHT; y += UNIT) g2.drawLine(0, y, GAME_WIDTH, y);
        }

        private void drawObstacles(Graphics2D g2) {
            g2.setColor(obstacleColor);
            for (Point p : obstacles) {
                g2.fillRoundRect(p.x + 2, p.y + 2, UNIT - 4, UNIT - 4, 6, 6);
            }
        }

        private void drawFood(Graphics2D g2) {
            switch (currentFoodType) {
                case APPLE -> {
                    g2.setColor(new Color(231, 76, 60));
                    g2.fillOval(food.x + 2, food.y + 2, UNIT - 4, UNIT - 4);
                    g2.setColor(new Color(101, 67, 33)); // cabo
                    g2.fillRect(food.x + UNIT / 2 - 2, food.y - 2, 4, 8);
                    g2.setColor(new Color(46, 204, 113)); // folha
                    int[] lx = {food.x + UNIT / 2 + 2, food.x + UNIT / 2 + 12, food.x + UNIT / 2 + 2};
                    int[] ly = {food.y, food.y + 6, food.y + 10};
                    g2.fillPolygon(lx, ly, 3);
                }
                case CHERRY -> {
                    g2.setColor(new Color(192, 57, 43));
                    g2.fillOval(food.x + 3, food.y + 6, UNIT - 10, UNIT - 10);
                    g2.fillOval(food.x + 8, food.y + 2, UNIT - 10, UNIT - 10);
                    g2.setColor(new Color(39, 174, 96));
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawLine(food.x + 8, food.y + 2, food.x + 12, food.y - 4);
                }
                case BANANA -> {
                    g2.setColor(new Color(241, 196, 15));
                    g2.setStroke(new BasicStroke(4f));
                    g2.drawArc(food.x + 2, food.y + 2, UNIT - 6, UNIT - 6, 20, 140);
                }
                case GRAPE -> {
                    g2.setColor(new Color(155, 89, 182));
                    for (int i = 0; i < 5; i++) {
                        int dx = (i % 3) * 6;
                        int dy = (i / 3) * 6;
                        g2.fillOval(food.x + 5 + dx, food.y + 5 + dy, 6, 6);
                    }
                }
                case DIAMOND -> {
                    g2.setColor(new Color(52, 152, 219));
                    int cx = food.x + UNIT / 2;
                    int cy = food.y + UNIT / 2;
                    int[] px = {cx, cx + 8, cx, cx - 8};
                    int[] py = {cy - 8, cy, cy + 8, cy};
                    g2.fillPolygon(px, py, 4);
                }
            }
        }

        private void drawSnake(Graphics2D g2) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
            for (int i = snake.size() - 1; i >= 0; i--) {
                Point p = snake.get(i);
                g2.setColor(Color.BLACK);
                g2.fillRoundRect(p.x + 3, p.y + 3, UNIT - 4, UNIT - 4, 10, 10);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            for (int i = snake.size() - 1; i >= 0; i--) {
                Point p = snake.get(i);
                if (i == 0) {
                    g2.setPaint(new GradientPaint(p.x, p.y, snakeHead.brighter(), p.x, p.y + UNIT, snakeHead.darker()));
                    g2.fillRoundRect(p.x + 1, p.y + 1, UNIT - 2, UNIT - 2, 12, 12);

                    g2.setColor(new Color(0, 0, 0, 140));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(p.x + 1, p.y + 1, UNIT - 2, UNIT - 2, 12, 12);

                    g2.setColor(Color.WHITE);
                    int eye = 4;
                    int ex1 = p.x + (dir == 'L' ? 4 : dir == 'R' ? UNIT - 8 : UNIT / 2 - 6);
                    int ex2 = p.x + (dir == 'L' ? 4 : dir == 'R' ? UNIT - 8 : UNIT / 2 + 2);
                    int ey  = p.y + (dir == 'U' ? 4 : dir == 'D' ? UNIT - 8 : UNIT / 2 - 6);
                    g2.fillOval(ex1, ey, eye, eye);
                    g2.fillOval(ex2, ey, eye, eye);
                    g2.setColor(Color.BLACK);
                    g2.fillOval(ex1 + 1, ey + 1, eye - 2, eye - 2);
                    g2.fillOval(ex2 + 1, ey + 1, eye - 2, eye - 2);
                } else {
                    Color a = (i % 2 == 0) ? snakeBodyA : snakeBodyB;
                    Color b = (i % 2 == 0) ? snakeBodyA.darker() : snakeBodyB.darker();
                    g2.setPaint(new GradientPaint(p.x, p.y, a, p.x + UNIT, p.y + UNIT, b));
                    g2.fillRoundRect(p.x + 2, p.y + 2, UNIT - 4, UNIT - 4, 10, 10);

                    g2.setColor(new Color(0, 0, 0, 120));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(p.x + 2, p.y + 2, UNIT - 4, UNIT - 4, 10, 10);
                }
            }
        }

        private void drawHUD(Graphics2D g2) {
            g2.setColor(new Color(0, 0, 0, 120));
            g2.fillRect(0, 0, GAME_WIDTH, 28);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Jogador: " + playerName, 10, 18);
            g2.drawString("Pontuação: " + score, GAME_WIDTH - 140, 18);

            String status = (wrap ? "WRAP ON" : "WRAP OFF") + (paused ? " • PAUSADO" : "");
            g2.drawString(status, GAME_WIDTH/2 - 40, 18);
        }

        private void drawCountdown(Graphics2D g2) {
            long elapsed = System.currentTimeMillis() - startAtMillis;
            long remaining = Math.max(0, COUNTDOWN_MS - elapsed);
            int sec = (int)Math.ceil(remaining / 1000.0);

            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRoundRect(GAME_WIDTH/2 - 80, GAME_HEIGHT/2 - 90, 160, 120, 20, 20);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            String s = (sec <= 0) ? "GO!" : String.valueOf(sec);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(s, (GAME_WIDTH - fm.stringWidth(s)) / 2, GAME_HEIGHT / 2);
        }

        private void drawPaused(Graphics2D g2) {
            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRoundRect(GAME_WIDTH/2 - 120, GAME_HEIGHT/2 - 80, 240, 120, 20, 20);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            String s = "PAUSADO";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(s, (GAME_WIDTH - fm.stringWidth(s)) / 2, GAME_HEIGHT / 2 - 10);

            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            String s2 = "P/Espaço: Retomar • W: Wrap";
            FontMetrics fm2 = g2.getFontMetrics();
            g2.drawString(s2, (GAME_WIDTH - fm2.stringWidth(s2)) / 2, GAME_HEIGHT / 2 + 20);
        }

        private void drawGameOver(Graphics2D g2) {
            String msg = win ? "VOCÊ VENCEU!" : "GAME OVER";
            String sub = "ENTER: novo jogo  •  P: pausar/retomar  •  W: wrap";

            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRoundRect(80, GAME_HEIGHT / 2 - 80, GAME_WIDTH - 160, 150, 20, 20);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 38));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(msg, (GAME_WIDTH - fm.stringWidth(msg)) / 2, GAME_HEIGHT / 2 - 20);

            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            FontMetrics fm2 = g2.getFontMetrics();
            g2.drawString(sub, (GAME_WIDTH - fm2.stringWidth(sub)) / 2, GAME_HEIGHT / 2 + 20);
        }

        // ------------ Loop ------------
        @Override
        public void actionPerformed(ActionEvent e) {
            // countdown
            if (starting) {
                long elapsed = System.currentTimeMillis() - startAtMillis;
                if (elapsed >= COUNTDOWN_MS) {
                    starting = false;
                    running = true;
                }
                repaint();
                return;
            }

            if (!running || paused) { repaint(); return; }

            Point head = new Point(snake.get(0));
            switch (dir) {
                case 'U' -> head.y -= UNIT;
                case 'D' -> head.y += UNIT;
                case 'L' -> head.x -= UNIT;
                case 'R' -> head.x += UNIT;
            }

            // wrap ou colisão com parede
            if (wrap) {
                int cols = GAME_WIDTH / UNIT;
                int rows = GAME_HEIGHT / UNIT;
                if (head.x < 0) head.x = (cols - 1) * UNIT;
                else if (head.x >= GAME_WIDTH) head.x = 0;
                if (head.y < 0) head.y = (rows - 1) * UNIT;
                else if (head.y >= GAME_HEIGHT) head.y = 0;
            } else {
                if (head.x < 0 || head.x >= GAME_WIDTH || head.y < 0 || head.y >= GAME_HEIGHT) {
                    endGame(); repaint(); return;
                }
            }

            // colisões
            if (snake.contains(head)) { endGame(); repaint(); return; }
            if (obstacles.contains(head)) { endGame(); repaint(); return; }

            snake.add(0, head);

            if (head.equals(food)) {
                score += 100;
                if (score >= MAX_SCORE) {
                    score = MAX_SCORE;
                    scoreBoard.setScore(playerName, score);
                    winGame(); return;
                }
                scoreBoard.setScore(playerName, score);

                int foodsEaten = score / 100;
                int newDelay = Math.max(MIN_DELAY_MS, START_DELAY_MS - foodsEaten * MS_PER_FOOD);
                if (timer.getDelay() != newDelay) timer.setDelay(newDelay);

                updateFoodType();
                ensureObstaclesForScore();
                spawnFood();
            } else {
                snake.remove(snake.size() - 1);
            }

            repaint();
        }

        private void endGame() {
            running = false;
            gameOver = true;
            win = false;
            if (timer != null) timer.stop();
            scoreBoard.finalizeScore(playerName, score);
        }

        private void winGame() {
            running = false;
            gameOver = true;
            win = true;
            if (timer != null) timer.stop();
            scoreBoard.finalizeScore(playerName, score);
            repaint();
        }

        @Override public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();

            // controles globais
            if (code == KeyEvent.VK_P || code == KeyEvent.VK_SPACE) {
                if (!gameOver && !starting) {
                    paused = !paused;
                }
            } else if (code == KeyEvent.VK_W) {
                wrap = !wrap;
            }

            if (running && !paused) {
                if (code == KeyEvent.VK_LEFT  && dir != 'R') dir = 'L';
                else if (code == KeyEvent.VK_RIGHT && dir != 'L') dir = 'R';
                else if (code == KeyEvent.VK_UP    && dir != 'D') dir = 'U';
                else if (code == KeyEvent.VK_DOWN  && dir != 'U') dir = 'D';
            } else if (gameOver && code == KeyEvent.VK_ENTER) {
                String name = JOptionPane.showInputDialog(this, "Seu nome para o placar:", "Jogador");
                if (name == null || name.isBlank()) name = "Jogador";
                this.playerName = name;
                scoreBoard.registerPlayer(playerName);
                startGame();
            }

            // ESC fecha a janela
            if (code == KeyEvent.VK_ESCAPE) {
                Window w = SwingUtilities.getWindowAncestor(this);
                if (w != null) w.dispose();
            }

            repaint();
        }

        @Override public void keyReleased(KeyEvent e) {}
    }

    // -------------------- Placar (Top-10 por pontuação) com persistência --------------------
    static class ScoreBoard extends JPanel {
        private static final int MAX_PLAYERS = 10;
        private static final String MAGIC = "#SNAKE_SCORES v2"; // ranking por score

        private final Path savePath = Paths.get(System.getProperty("user.home"), ".snake_scores.txt");

        private final DefaultListModel<String> model = new DefaultListModel<>();
        private final JList<String> list = new JList<>(model);

        // Melhor pontuação por jogador (todos os já vistos)
        private final Map<String, Integer> bestScores = new LinkedHashMap<>();

        ScoreBoard() {
            setPreferredSize(new Dimension(240, 600));
            setLayout(new BorderLayout());
            setBackground(new Color(20, 20, 20));

            var title = new javax.swing.JLabel(" Ranking Top-10 (maiores pontuações)");
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Arial", Font.BOLD, 14));
            add(title, BorderLayout.NORTH);

            list.setBackground(new Color(25, 25, 25));
            list.setForeground(Color.WHITE);
            list.setFont(new Font("Consolas", Font.PLAIN, 14));
            add(new JScrollPane(list), BorderLayout.CENTER);

            var hint = new javax.swing.JLabel(" P/Espaço: Pausa • W: Wrap • ENTER: Reinicia");
            hint.setForeground(new Color(200, 200, 200));
            hint.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
            add(hint, BorderLayout.SOUTH);

            loadFromDisk();
            refresh();
        }

        void registerPlayer(String player) {
            if (player == null || player.isBlank()) return;
            bestScores.putIfAbsent(player, 0);
            saveToDisk();
            refresh();
        }

        void setScore(String player, int score) {
            updateBest(player, score);
            saveToDisk();
            refresh();
        }

        void finalizeScore(String player, int score) {
            updateBest(player, score);
            saveToDisk();
            refresh();
        }

        private void updateBest(String player, int score) {
            bestScores.putIfAbsent(player, 0);
            if (score > bestScores.get(player)) {
                bestScores.put(player, score);
            }
        }

        private List<Map.Entry<String, Integer>> top10() {
            List<Map.Entry<String, Integer>> list = new ArrayList<>(bestScores.entrySet());
            list.sort(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                    .reversed()
                    .thenComparing(Map.Entry::getKey));
            if (list.size() > MAX_PLAYERS) {
                list = new ArrayList<>(list.subList(0, MAX_PLAYERS));
            }
            return list;
        }

        private void refresh() {
            model.clear();
            int pos = 1;
            for (Map.Entry<String, Integer> e : top10()) {
                model.addElement(String.format("%2d) %-16s %7d", pos++, e.getKey(), e.getValue()));
            }
        }

        private void loadFromDisk() {
            if (!Files.exists(savePath)) return;
            try {
                List<String> lines = Files.readAllLines(savePath);
                if (lines.isEmpty() || !MAGIC.equals(lines.get(0))) return;

                bestScores.clear();
                for (int i = 1; i < lines.size(); i++) {
                    String line = lines.get(i).trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    String[] kv = line.split("=", 2);
                    if (kv.length == 2) {
                        String name = kv[0];
                        try {
                            int sc = Integer.parseInt(kv[1]);
                            bestScores.put(name, sc);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            } catch (IOException ignored) {}
        }

        private void saveToDisk() {
            List<String> out = new ArrayList<>();
            out.add(MAGIC);
            for (Map.Entry<String, Integer> e : top10()) {
                out.add(e.getKey() + "=" + e.getValue());
            }
            try {
                Files.write(savePath, out);
            } catch (IOException ignored) {}
        }
    }
}
