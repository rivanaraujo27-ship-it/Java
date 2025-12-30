import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

public class Calculadora3 extends JFrame implements ActionListener {
    private JTextField display;
    private JPanel panel;
    private JLabel indicador; // <- mostra operador ativo (ex.: "12 +")
    private String operador;
    private double num1, num2, resultado;
    private boolean aguardandoSegundoNumero = false;

    public Calculadora3() {
        setTitle("Calculadora Simples");
        setSize(320, 440);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Cabeçalho com indicador (linha superior, menor) + display (linha inferior, maior)
        indicador = new JLabel(" ");
        indicador.setHorizontalAlignment(SwingConstants.RIGHT);
        indicador.setForeground(new Color(120, 120, 120));
        indicador.setFont(new Font("Arial", Font.PLAIN, 12));

        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);

        JPanel topo = new JPanel(new GridLayout(2, 1));
        topo.add(indicador);
        topo.add(display);
        add(topo, BorderLayout.NORTH);

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5));

        String[] botoes = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ",", "=", "+",
            "C", "⌫", "±", "%"
        };

        for (String texto : botoes) {
            JButton botao = new JButton(texto);
            botao.setFont(new Font("Arial", Font.PLAIN, 20));
            botao.addActionListener(this);
            panel.add(botao);
        }

        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null);

        instalarKeyBindings();
    }

    private void instalarKeyBindings() {
        JRootPane root = getRootPane();
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();

        class PressAction extends AbstractAction {
            private final String cmd;
            PressAction(String c) { this.cmd = c; }
            @Override public void actionPerformed(ActionEvent e) { pressionar(cmd); }
        }

        for (char c = '0'; c <= '9'; c++) {
            im.put(KeyStroke.getKeyStroke(c), "DIGIT_" + c);
            am.put("DIGIT_" + c, new PressAction(String.valueOf(c)));
        }
        for (int k = KeyEvent.VK_NUMPAD0; k <= KeyEvent.VK_NUMPAD9; k++) {
            int digit = k - KeyEvent.VK_NUMPAD0;
            im.put(KeyStroke.getKeyStroke(k, 0), "NDIGIT_" + digit);
            am.put("NDIGIT_" + digit, new PressAction(String.valueOf(digit)));
        }

        im.put(KeyStroke.getKeyStroke('+'), "PLUS");    am.put("PLUS",    new PressAction("+"));
        im.put(KeyStroke.getKeyStroke('-'), "MINUS");   am.put("MINUS",   new PressAction("-"));
        im.put(KeyStroke.getKeyStroke('*'), "MUL");     am.put("MUL",     new PressAction("*"));
        im.put(KeyStroke.getKeyStroke('/'), "DIV");     am.put("DIV",     new PressAction("/"));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "NPLUS");       am.put("NPLUS",   new PressAction("+"));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "NMINUS"); am.put("NMINUS",  new PressAction("-"));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_MULTIPLY, 0), "NMUL");   am.put("NMUL",    new PressAction("*"));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DIVIDE, 0), "NDIV");     am.put("NDIV",    new PressAction("/"));

        im.put(KeyStroke.getKeyStroke(','), "COMMA");   am.put("COMMA",   new PressAction(","));
        im.put(KeyStroke.getKeyStroke('.'), "DOT");     am.put("DOT",     new PressAction(","));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DECIMAL, 0), "NDEC"); am.put("NDEC", new PressAction(","));

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "EQUALS"); am.put("EQUALS", new PressAction("="));
        im.put(KeyStroke.getKeyStroke('='), "EQUALS2"); am.put("EQUALS2", new PressAction("="));

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CLEAR");  am.put("CLEAR",  new PressAction("C"));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "CLEAR2"); am.put("CLEAR2", new PressAction("C"));
        im.put(KeyStroke.getKeyStroke('c'), "CLEAR3");                   am.put("CLEAR3", new PressAction("C"));
        im.put(KeyStroke.getKeyStroke('C'), "CLEAR4");                   am.put("CLEAR4", new PressAction("C"));

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "BACK");
        am.put("BACK", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { backspace(); }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), "NEGATE");
        am.put("NEGATE", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { toggleSign(); }
        });
        im.put(KeyStroke.getKeyStroke('n'), "NEGATE2"); am.put("NEGATE2", am.get("NEGATE"));
        im.put(KeyStroke.getKeyStroke('N'), "NEGATE3"); am.put("NEGATE3", am.get("NEGATE"));

        im.put(KeyStroke.getKeyStroke('%'), "PERCENT");
        am.put("PERCENT", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { aplicarPercentual(); }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "COPY");
        am.put("COPY", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { copiarParaClipboard(); }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "PASTE");
        am.put("PASTE", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { colarDoClipboard(); }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pressionar(e.getActionCommand());
    }

    private void pressionar(String comando) {
        if ("0123456789,".contains(comando)) {
            if (aguardandoSegundoNumero) {
                display.setText("");
                aguardandoSegundoNumero = false;
            }
            if (",".equals(comando) && display.getText().contains(",")) return;
            display.setText(display.getText() + comando);
            return;
        }

        if ("⌫".equals(comando)) { backspace(); return; }
        if ("±".equals(comando)) { toggleSign(); return; }
        if ("%".equals(comando)) { aplicarPercentual(); return; }

        if ("C".equals(comando)) { limparTudo(); return; }

        if ("=".equals(comando)) { calcularSePossivel(); return; }

        if ("/-*+".contains(comando)) {
            if (!display.getText().isEmpty() && !"Erro".equals(display.getText())) {
                if (operador != null && !aguardandoSegundoNumero) {
                    calcularSePossivel();
                }
                num1 = parseNumero(display.getText());
                operador = comando;
                aguardandoSegundoNumero = true;
                // atualiza indicador (exibe "num1 operador")
                indicador.setText(formatarResultado(num1) + " " + operador);
            }
        }
    }

    private void calcularSePossivel() {
        if (operador != null && !display.getText().isEmpty() && !"Erro".equals(display.getText())) {
            num2 = parseNumero(display.getText());
            switch (operador) {
                case "+": resultado = num1 + num2; break;
                case "-": resultado = num1 - num2; break;
                case "*": resultado = num1 * num2; break;
                case "/":
                    if (num2 == 0) {
                        display.setText("Erro");
                        operador = null;
                        indicador.setText(" ");
                        return;
                    } else {
                        resultado = num1 / num2;
                    }
                    break;
            }
            // mostra expressão completa momentaneamente
            indicador.setText(formatarResultado(num1) + " " + operador + " " + formatarResultado(num2) + " =");
            display.setText(formatarResultado(resultado));
            operador = null;
            aguardandoSegundoNumero = false;
            num1 = resultado; // encadeamento
        }
    }

    private void limparTudo() {
        display.setText("");
        operador = null;
        num1 = num2 = resultado = 0;
        aguardandoSegundoNumero = false;
        indicador.setText(" ");
    }

    private void backspace() {
        String t = display.getText();
        if (t == null || t.isEmpty() || "Erro".equals(t)) return;
        display.setText(t.substring(0, t.length() - 1));
    }

    private void toggleSign() {
        String t = display.getText();
        if (t == null || t.isEmpty() || "Erro".equals(t)) return;
        if (t.startsWith("-")) {
            display.setText(t.substring(1));
        } else {
            display.setText("-" + t);
        }
    }

    private void aplicarPercentual() {
        if (operador == null || display.getText().isEmpty() || "Erro".equals(display.getText())) return;
        double atual = parseNumero(display.getText());
        double convertido;
        if ("+".equals(operador) || "-".equals(operador)) {
            convertido = num1 * (atual / 100.0);
        } else {
            convertido = atual / 100.0;
        }
        display.setText(formatarResultado(convertido));
        calcularSePossivel();
    }

    private void copiarParaClipboard() {
        String t = display.getText();
        if (t == null || t.isEmpty()) return;
        StringNormalizedSelection sel = new StringNormalizedSelection(t);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
    }

    private void colarDoClipboard() {
        try {
            Transferable tr = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (tr != null && tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String s = (String) tr.getTransferData(DataFlavor.stringFlavor);
                if (s != null) {
                    s = s.trim().replace('.', ',');
                    if (s.matches("-?[0-9]+(,[0-9]+)?")) {
                        if (aguardandoSegundoNumero) {
                            display.setText("");
                            aguardandoSegundoNumero = false;
                        }
                        display.setText(s);
                    }
                }
            }
        } catch (Exception ignored) { }
    }

    private static class StringNormalizedSelection implements Transferable {
        private final String data;
        public StringNormalizedSelection(String data) { this.data = data; }
        @Override public DataFlavor[] getTransferDataFlavors() { return new DataFlavor[]{DataFlavor.stringFlavor}; }
        @Override public boolean isDataFlavorSupported(DataFlavor flavor) { return DataFlavor.stringFlavor.equals(flavor); }
        @Override public Object getTransferData(DataFlavor flavor) { return data; }
    }

    private double parseNumero(String texto) {
        return Double.parseDouble(texto.replace(',', '.'));
    }

    private String formatarResultado(double valor) {
        if (valor == (long) valor) {
            return String.valueOf((long) valor);
        } else {
            String r = String.valueOf(valor);
            return r.replace('.', ',');
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculadora3 calc = new Calculadora3();
            calc.setVisible(true);
        });
    }
}
