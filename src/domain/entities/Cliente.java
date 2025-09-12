package domain.entities;

import domain.utils.ValidationUtils;

public class Cliente {
    private Integer id;
    private String nome;
    private String endereco;
    private String telefone;
    private final String cpf;
    private final String cnpj;

    public Cliente(Integer id, String nome, String endereco, String telefone, String cpf) {
        if (!ValidationUtils.isValidCPF(cpf)) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        if (!ValidationUtils.isValidTelefone(telefone)) {
            throw new IllegalArgumentException("Telefone inválido.");
        }
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone.replaceAll("\\D", "");
        this.cpf = cpf.replaceAll("\\D", "");
        this.cnpj = null;
    }

    public Cliente(String nome, String endereco, String telefone, String cnpj, Integer id) {
        if (!ValidationUtils.isValidCNPJ(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido.");
        }
        if (!ValidationUtils.isValidTelefone(telefone)) {
            throw new IllegalArgumentException("Telefone inválido.");
        }
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone.replaceAll("\\D", "");
        this.cnpj = cnpj.replaceAll("\\D", "");
        this.cpf = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (!ValidationUtils.isValidTelefone(telefone)) {
            throw new IllegalArgumentException("Telefone inválido.");
        }
        this.telefone = telefone.replaceAll("\\D", "");
    }

    public String getCpf() {
        return cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public boolean isPessoaFisica() {
        return cpf != null;
    }

    public boolean isPessoaJuridica() {
        return cnpj != null;
    }

    public String getDocumento() {
        return isPessoaFisica() ? cpf : cnpj;
    }
}
