import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Senha extends JFrame {
    private JTextField lengthField;
    private JCheckBox upperCaseCheck;
    private JCheckBox lowerCaseCheck;
    private JCheckBox numbersCheck;
    private JCheckBox specialCharsCheck;
    private JButton generateButton;
    private JTextArea resultArea;

    public Senha() {
        setTitle("Gerador de Senhas");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Comprimento da Senha:"));
        lengthField = new JTextField(5);
        add(lengthField);

        upperCaseCheck = new JCheckBox("Letras Maiúsculas (A-Z)");
        add(upperCaseCheck);

        lowerCaseCheck = new JCheckBox("Letras Minúsculas (a-z)");
        add(lowerCaseCheck);

        numbersCheck = new JCheckBox("Números (0-9)");
        add(numbersCheck);

        specialCharsCheck = new JCheckBox("Caracteres Especiais (!@#$...)");
        add(specialCharsCheck);

        generateButton = new JButton("Gerar Senha");
        add(generateButton);

        resultArea = new JTextArea(5, 30);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea));

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePassword();
            }
        });
    }
    private void generatePassword() {
        String lengthText = lengthField.getText();
        int length;

        try {
            length = Integer.parseInt(lengthText);
            if (length <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um número válido para o comprimento da senha.");
            return;
        }

        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[]{}|;:',.<>?/`~";

        StringBuilder characterPool = new StringBuilder();
        List<Character> passwordChars = new ArrayList<>();

        if (upperCaseCheck.isSelected()) {
            characterPool.append(upperCase);
            passwordChars.add(upperCase.charAt(new Random().nextInt(upperCase.length())));
        }
        if (lowerCaseCheck.isSelected()) {
            characterPool.append(lowerCase);
            passwordChars.add(lowerCase.charAt(new Random().nextInt(lowerCase.length())));
        }
        if (numbersCheck.isSelected()) {
            characterPool.append(numbers);
            passwordChars.add(numbers.charAt(new Random().nextInt(numbers.length())));
        }
        if (specialCharsCheck.isSelected()) {
            characterPool.append(specialChars);
            passwordChars.add(specialChars.charAt(new Random().nextInt(specialChars.length())));
        }

        if (characterPool.length() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione pelo menos um tipo de caractere.");
            return;
        }

        Random random = new Random();
        while (passwordChars.size() < length) {
            passwordChars.add(characterPool.charAt(random.nextInt(characterPool.length())));
        }

        Collections.shuffle(passwordChars);
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        resultArea.setText(password.toString());
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Senha frame = new Senha();
            frame.setVisible(true);
        });
    }
}