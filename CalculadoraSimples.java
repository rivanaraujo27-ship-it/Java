import java.util.Scanner;

public class CalculadoraSimples {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo à Calculadora Simples!");
        System.out.print("Digite o primeiro número: ");
        double num1 = scanner.nextDouble();

        System.out.print("Digite o segundo número: ");
        double num2 = scanner.nextDouble();

        int escolha = -1;

        while (escolha !=0) {
            System.out.println("1 - Adição");
            System.out.println("2 - Subtração");
            System.out.println("3 - Multiplicação");
            System.out.println("4 - Divisão");
            System.out.println("0 - Sair");
            escolha = scanner.nextInt();
            if (escolha == 0) {
                System.out.println("Encerrando a calculadora. Até mais!");
                break;
            }
            double num1Atual = num1;
            double num2Atual = num2;
            switch (escolha) {
                case 1:
                    System.out.println("Resultado: " + (num1Atual + num2Atual));
                    break;
                case 2:
                    System.out.println("Resultado: " + (num1Atual - num2Atual));
                    break;
                case 3:
                    System.out.println("Resultado: " + (num1Atual * num2Atual));
                    break;
                case 4:
                    if (num2Atual != 0) {
                        System.out.println("Resultado: " + (num1Atual / num2Atual));
                    } else {
                        System.out.println("Erro: Divisão por zero não é permitida.");
                    }
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.print("Digite o próximo número (ou o mesmo para continuar): ");
            num1 = scanner.nextDouble();
            System.out.print("Digite o próximo número (ou o mesmo para continuar): ");
            num2 = scanner.nextDouble();
        }
        scanner.close();
    }
}
