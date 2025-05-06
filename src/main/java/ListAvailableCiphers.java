import java.security.Security;
import java.util.Set;

public class ListAvailableCiphers {
    public static void main(String[] args) {
        System.out.println("Ciphers disponíveis:");
        Set<String> ciphers = Security.getAlgorithms("Cipher");
        ciphers.forEach(System.out::println);
    }
}

