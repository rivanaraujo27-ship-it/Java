import javax.swing.JOptionPane;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DataHoraTemperatura {
    public static void main(String[] args) {

        // 1) Lista de locais do mundo (fuso horários)
        String[] locais = {
            "Europe/Lisbon",
            "America/Sao_Paulo",
            "America/New_York",
            "Europe/London",
            "Asia/Tokyo",
            "Australia/Sydney"
        };

        // 2) Solicitar ao utilizador que escolha um local
        String localEscolhido = (String) JOptionPane.showInputDialog(
            null,
            "Selecione o local no mundo:",
            "Escolha o Local",
            JOptionPane.QUESTION_MESSAGE,
            null,
            locais,
            locais[0] // valor padrão
        );

        // Se o utilizador cancelar
        if (localEscolhido == null) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return;
        }

        // 3) Obter data e hora atual no local escolhido
        ZonedDateTime dataHoraLocal = ZonedDateTime.now(ZoneId.of(localEscolhido));
        DateTimeFormatter formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHoraFormatada = dataHoraLocal.format(formatoDataHora);

        // 4) Solicitar ao utilizador a temperatura atual em Celsius para aquele local
        String entradaTemperatura = JOptionPane.showInputDialog(
            null,
            "Digite a temperatura atual em Celsius para o local selecionado:"
        );

        if (entradaTemperatura == null) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return;
        }

        double temperaturaCelsius;

        try {
            temperaturaCelsius = Double.parseDouble(entradaTemperatura);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor inválido. Digite um número.");
            return;
        }

        // 5) Converter para Fahrenheit
        double temperaturaFahrenheit = (temperaturaCelsius * 9 / 5) + 32;

        // 6) Montar mensagem final com local + data/hora + temperaturas
        String mensagem = String.format(
            "Local: %s\nData e Hora Local: %s\n\nTemperatura Atual:\n%.2f °C\n%.2f °F",
            localEscolhido,
            dataHoraFormatada,
            temperaturaCelsius,
            temperaturaFahrenheit
        );

        JOptionPane.showMessageDialog(null, mensagem);
    }
}
