import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class BlocoDeNotas extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;

    public BlocoDeNotas() {
        setTitle("Bloco de Notas");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Arquivo");

        JMenuItem openItem = new JMenuItem("Abrir");
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirArquivo();
            }
        });

        JMenuItem saveItem = new JMenuItem("Salvar");
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarArquivo();
            }
        });

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        fileChooser = new JFileChooser();
    }

    private void abrirArquivo() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvarArquivo() {
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                textArea.write(writer);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BlocoDeNotas blocoDeNotas = new BlocoDeNotas();
            blocoDeNotas.setVisible(true);
        });
    }
}

