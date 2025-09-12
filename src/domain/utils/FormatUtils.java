package domain.utils;

public class FormatUtils {

    public static String formatCPF(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return cpf;
        }
        return cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String formatCNPJ(String cnpj) {
        if (cnpj == null || !cnpj.matches("\\d{14}")) {
            return cnpj;
        }
        return cnpj.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    public static String formatTelefone(String telefone) {
        if (telefone == null) {
            return telefone;
        }
        String digitsOnly = telefone.replaceAll("\\D", "");
        if (digitsOnly.length() == 11) {
            return digitsOnly.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        }
        if (digitsOnly.length() == 10) {
            return digitsOnly.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        return telefone;
    }
}

