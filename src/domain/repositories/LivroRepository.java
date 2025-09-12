package domain.repositories;

import domain.entities.Livro;
import java.util.List;
import java.util.Optional;

public interface LivroRepository {
    Livro save(Livro livro);
    Optional<Livro> findByIsbn(String isbn);
    List<Livro> findAll();
    List<Livro> findByAssunto(String assunto);
    List<Livro> findByNomeAutor(String nomeAutor);
    void deleteByIsbn(String isbn);
    Livro update(Livro livro);
    List<Livro> findByEditoraId(Integer editoraId);
}
