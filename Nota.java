import java.util.Scanner;
public class Nota {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Digite a nota do estudante (0-100): ");
        int nota = scanner.nextInt();
        
        if (nota >= 70) {
            System.out.println("Aprovado: Parabéns!");
        } else { 
            System.out.println("Reprovado: Tente novamente.");
        }
            scanner.close();
    }
}