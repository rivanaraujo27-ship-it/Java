public class OperadorTernario {
    public static void main(String[] args) {
        int n1 = -50;
        int n2 = 20;
        int maximo = (n1 > n2) ? n1+n2 : n2-n1;
        System.out.println("El número máximo es: " + maximo);
    }
}
