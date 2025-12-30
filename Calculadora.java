import java.util.Scanner;

public class Calculadora {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        Calculadora calc = new Calculadora();

        System.out.println("Calculadora Básica");

        System.out.println("Soma:");
        System.out.print("Digite dois números para somar: ");
        int num1 = entrada.nextInt();
        int num2 = entrada.nextInt();
        System.out.println("Resultado: " + calc.somar(num1, num2));
        
        System.out.println("Subtração:");
        System.out.print("Digite dois números para subtrair: ");
        num1 = entrada.nextInt();
        num2 = entrada.nextInt();
        System.out.println("Resultado: " + calc.subtrair(num1, num2));
        
        System.out.println("Multiplicação:");
        System.out.print("Digite dois números para multiplicar: ");
        num1 = entrada.nextInt();
        num2 = entrada.nextInt();
        System.out.println("Resultado: " + calc.multiplicar(num1, num2));
    

        System.out.println("Divisão:");
        System.out.print("Digite dois números para dividir: ");
        num1 = entrada.nextInt();
        num2 = entrada.nextInt();
        System.out.println("Resultado: " + calc.dividir(num1, num2));
    
        entrada.close();
    }

    public int somar(int a, int b) {
        return a + b;
    }
    public int subtrair(int a, int b) {
        return a - b;
    }
    public int multiplicar(int a, int b) {
        return a * b;
    }
    public double dividir(int a, int b) {
        if (b == 0) {
            System.out.println("Erro: Divisão por zero não é permitida.");
            return 0;
        }
        return (double) a / b;
        }
}   