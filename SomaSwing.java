import javax.swing.*;
import javax.util.jOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;
import java.util.Random;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.DoubleStream;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.DoublePredicate;
import java.util.function.LongPredicate;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;
import java.util.function.BinaryOperator;



public class SomaSwing extends JFrame {
    private JTextField num1Field;
    private JTextField num2Field;
    private JButton sumButton;
    private JLabel resultLabel;

    public SomaSwing() {
        setTitle("Soma de Dois Números");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        num1Field = new JTextField(10);
        num2Field = new JTextField(10);
        sumButton = new JButton("Somar");
        resultLabel = new JLabel("Resultado: ");

        sumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double num1 = Double.parseDouble(num1Field.getText());
                    double num2 = Double.parseDouble(num2Field.getText());
                    double sum = num1 + num2;
                    resultLabel.setText("Resultado: " + sum);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SomaSwing.this, "Por favor, insira números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(new JLabel("Número 1:"));
        add(num1Field);
        add(new JLabel("Número 2:"));
        add(num2Field);
        add(sumButton);
        add(resultLabel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SomaSwing frame = new SomaSwing();
            frame.setVisible(true);
        });
    }
}

