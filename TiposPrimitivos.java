
import java.util.Scanner;

public class TiposPrimitivos {

    public static void main(String[] args) {

        Scanner nomeScanner = new Scanner(System.in);
        System.out.print("Digite o nome do aluno: ");
        String nome = nomeScanner.nextLine();  

        Scanner idadeScanner = new Scanner(System.in);
        System.out.print("Digite a idade do aluno: ");
        int idade = idadeScanner.nextInt();

        Scanner notScanner = new Scanner(System.in);
        System.out.print("Digite a nota do aluno: ");
        float nota = notScanner.nextFloat();

        System.out.println("\nO nome do aluno é: " + nome);
        System.out.println("A idade do aluno é: " + idade);
        System.out.println("A nota do aluno é: " + nota);

        if (nota < 70.0) {
            System.out.println("\nAluno Reprovado.");
            nomeScanner.close();
            idadeScanner.close();
            notScanner.close();
            return;
        } else {
            System.out.println("\nAluno Aprovado.");
        }
        nomeScanner.close();
        idadeScanner.close();
        notScanner.close(); 
    }
}