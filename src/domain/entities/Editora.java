package domain.entities;

import domain.utils.ValidationUtils;

public class Editora {
    private Integer id;
    private String nome;
    private String endereco;
    private String telefone;
    private String nome_gerente;

    public Editora(Integer id, String nome, String endereco, String telefone, String nome_gerente) {
        if (!ValidationUtils.isValidTelefone(telefone)) {
            throw new IllegalArgumentException("Telefone inválido.");
        }
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone.replaceAll("\\D", "");
        this.nome_gerente = nome_gerente;
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

    public String getNome_gerente() {
        return nome_gerente;
    }

    public void setNome_gerente(String nome_gerente) {
        this.nome_gerente = nome_gerente;
    }
}
