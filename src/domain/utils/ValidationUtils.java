package domain.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}");
    private static final Pattern CNPJ_PATTERN = Pattern.compile("\\d{14}");
    private static final Pattern TELEFONE_PATTERN = Pattern.compile("\\d{10,11}");

    public static boolean isValidCPF(String cpf) {
        if (cpf == null) {
            return false;
        }
        String cpfDigits = cpf.replaceAll("\\D", "");
        if (!CPF_PATTERN.matcher(cpfDigits).matches()) {
            return false;
        }
        if (cpfDigits.chars().distinct().count() == 1) {
            return false;
        }

        try {
            int[] numbers = cpfDigits.chars().map(Character::getNumericValue).toArray();
            int sum1 = 0;
            for (int i = 0; i < 9; i++) {
                sum1 += numbers[i] * (10 - i);
            }
            int d1 = 11 - (sum1 % 11);
            if (d1 >= 10) {
                d1 = 0;
            }

            if (numbers[9] != d1) {
                return false;
            }

            int sum2 = 0;
            for (int i = 0; i < 10; i++) {
                sum2 += numbers[i] * (11 - i);
            }
            int d2 = 11 - (sum2 % 11);
            if (d2 >= 10) {
                d2 = 0;
            }

            return numbers[10] == d2;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidCNPJ(String cnpj) {
        if (cnpj == null) {
            return false;
        }
        String cnpjDigits = cnpj.replaceAll("\\D", "");
        if (!CNPJ_PATTERN.matcher(cnpjDigits).matches()) {
            return false;
        }
        if (cnpjDigits.chars().distinct().count() == 1) {
            return false;
        }

        try {
            int[] numbers = cnpjDigits.chars().map(Character::getNumericValue).toArray();
            int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int sum1 = 0;
            for (int i = 0; i < 12; i++) {
                sum1 += numbers[i] * weights1[i];
            }
            int d1 = 11 - (sum1 % 11);
            if (d1 >= 10) {
                d1 = 0;
            }

            if (numbers[12] != d1) {
                return false;
            }

            int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int sum2 = 0;
            for (int i = 0; i < 13; i++) {
                sum2 += numbers[i] * weights2[i];
            }
            int d2 = 11 - (sum2 % 11);
            if (d2 >= 10) {
                d2 = 0;
            }

            return numbers[13] == d2;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidTelefone(String telefone) {
        if (telefone == null) {
            return false;
        }
        String telefoneDigits = telefone.replaceAll("\\D", "");
        return TELEFONE_PATTERN.matcher(telefoneDigits).matches();
    }
}
