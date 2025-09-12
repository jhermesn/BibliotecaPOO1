package application.usecases;

import domain.entities.Livro;
import domain.repositories.CompraRepository;
import domain.repositories.LivroRepository;

import java.util.List;
import java.util.Optional;

public class LivroUseCases {

    private final LivroRepository livroRepository;
    private final CompraRepository compraRepository;

    public LivroUseCases(LivroRepository livroRepository, CompraRepository compraRepository) {
        this.livroRepository = livroRepository;
        this.compraRepository = compraRepository;
    }

    public Livro criarLivro(Livro livro) {
        if (livroRepository.findByIsbn(livro.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("ISBN já cadastrado.");
        }
        return livroRepository.save(livro);
    }

    public Optional<Livro> buscarLivroPorIsbn(String isbn) {
        return livroRepository.findByIsbn(isbn);
    }

    public List<Livro> listarTodosLivros() {
        return livroRepository.findAll();
    }

    public List<Livro> buscarLivroPorAssunto(String assunto) {
        return livroRepository.findByAssunto(assunto);
    }
    
    public List<Livro> buscarLivroPorAutor(String autor) {
        return livroRepository.findByNomeAutor(autor);
    }

    public void deletarLivro(String isbn) {
        if (!compraRepository.findByLivroIsbn(isbn).isEmpty()) {
            throw new IllegalStateException("Não é possível deletar o livro, pois ele possui um histórico de compras.");
        }
        livroRepository.deleteByIsbn(isbn);
    }

    public Livro atualizarLivro(Livro livro) {
        return livroRepository.update(livro);
    }
}
