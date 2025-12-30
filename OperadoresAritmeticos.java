import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;  
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.lang.Math;
public class OperadoresAritmeticos extends JFrame {

    private JPanel contentPane;
    private JTextField txtNum1;
    private JTextField txtNum2;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OperadoresAritmeticos frame = new OperadoresAritmeticos();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * Create the frame.
     */
    public OperadoresAritmeticos() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblTitulo = new JLabel("Operadores Aritméticos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(10, 11, 414, 30);
        contentPane.add(lblTitulo);
        
        JLabel lblNum1 = new JLabel("Número 1:");
        lblNum1.setBounds(10, 60, 80, 14);
        contentPane.add(lblNum1);
        
        txtNum1 = new JTextField();
        txtNum1.setBounds(100, 57, 86, 20);
        contentPane.add(txtNum1);
        txtNum1.setColumns(10);
        
        JLabel lblNum2 = new JLabel("Número 2:");
        lblNum2.setBounds(10, 90, 80, 14);
        contentPane.add(lblNum2);
        
        txtNum2 = new JTextField();
        txtNum2.setBounds(100, 87, 86, 20);
        contentPane.add(txtNum2);
        txtNum2.setColumns(10);
        
        JButton btnCalcular = new JButton("Calcular");
        btnCalcular.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double num1 = Double.parseDouble(txtNum1.getText());
                double num2 = Double.parseDouble(txtNum2.getText());
                
                double soma = num1 + num2;
                double subtracao = num1 - num2;
                double multiplicacao = num1 * num2;
                double divisao = num1 / num2;
                double resto = num1 % num2;
                double potencia = Math.pow(num1, num2);
                double raizQuadradaNum1 = Math.sqrt(num1);
                double raizQuadradaNum2 = Math.sqrt(num2);
                
                    String mensagem = "Soma: " + soma +
                                      "\nSubtração: " + subtracao +
                                      "\nMultiplicação: " + multiplicacao +
                                      "\nDivisão: " + divisao +
                                      "\nResto: " + resto +
                                      "\nPotência: " + potencia +
                                      "\nRaiz Quadrada de " + num1 + ": " + raizQuadradaNum1 +
                                      "\nRaiz Quadrada de " + num2 + ": " + raizQuadradaNum2;
                    JOptionPane.showMessageDialog(null, mensagem);
                }
            });
            btnCalcular.setBounds(100, 130, 89, 23);
            contentPane.add(btnCalcular);
        }
    }