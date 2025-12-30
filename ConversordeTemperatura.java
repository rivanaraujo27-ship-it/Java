import javax.swing.JOptionPane;

public class ConversordeTemperatura {
    public static void main(String[] args) {
        String[] options = {
            "Celsius a Fahrenheit",
            "Fahrenheit a Celsius",
            "Celsius a Kelvin",
            "Kelvin a Celsius",
            "Fahrenheit a Kelvin",
            "Kelvin a Fahrenheit"
        };

        String choice = (String) JOptionPane.showInputDialog(
            null,
            "Selecione a conversão de Temperatura:",
            "Conversor de Temperatura",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice != null) {
            String input = JOptionPane.showInputDialog("Digite a temperatura a converter:");
            try {
                double temperature = Double.parseDouble(input);
                double result = 0;
                String unidadeFinal = "";

                switch (choice) {
                    case "Celsius a Fahrenheit":
                        result = (temperature * 9/5) + 32;
                        unidadeFinal = "°F";
                        break;
                    case "Fahrenheit a Celsius":
                        result = (temperature - 32) * 5/9;
                        unidadeFinal = "°C";
                        break;
                    case "Celsius a Kelvin":
                        result = temperature + 273.15;
                        unidadeFinal = "K";
                        break;
                    case "Kelvin a Celsius":
                        result = temperature - 273.15;
                        unidadeFinal = "°C";
                        break;
                    case "Fahrenheit a Kelvin":
                        result = (temperature - 32) * 5/9 + 273.15;
                        unidadeFinal = "K";
                        break;
                    case "Kelvin a Fahrenheit":
                        result = (temperature - 273.15) * 9/5 + 32;
                        unidadeFinal = "°F";    
                        break;
                }

                JOptionPane.showMessageDialog(null, "O resultado é: " + result + " " + unidadeFinal);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingresse un número válido.");
            }
        }
    }
}