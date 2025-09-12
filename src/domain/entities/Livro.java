package domain.entities;

public class Livro {
    private String isbn;
    private Editora editora;
    private String nome;
    private String nome_autor;
    private String assunto;
    private Integer estoque;

    public Livro(String isbn, Editora editora, String nome, String nome_autor, String assunto, Integer estoque) {
        this.isbn = isbn;
        this.editora = editora;
        this.nome = nome;
        this.nome_autor = nome_autor;
        this.assunto = assunto;
        this.estoque = estoque;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Editora getEditora() {
        return editora;
    }

    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome_autor() {
        return nome_autor;
    }

    public void setNome_autor(String nome_autor) {
        this.nome_autor = nome_autor;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }
}
