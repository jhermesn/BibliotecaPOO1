package application.usecases;

import domain.entities.Editora;
import domain.repositories.EditoraRepository;
import domain.repositories.LivroRepository;

import java.util.List;
import java.util.Optional;

public class EditoraUseCases {

    private final EditoraRepository editoraRepository;
    private final LivroRepository livroRepository;

    public EditoraUseCases(EditoraRepository editoraRepository, LivroRepository livroRepository) {
        this.editoraRepository = editoraRepository;
        this.livroRepository = livroRepository;
    }

    public Editora criarEditora(Editora editora) {
        return editoraRepository.save(editora);
    }

    public Optional<Editora> buscarEditoraPorId(Integer id) {
        return editoraRepository.findById(id);
    }

    public List<Editora> listarTodasEditoras() {
        return editoraRepository.findAll();
    }

    public void deletarEditora(Integer id) {
        if (!livroRepository.findByEditoraId(id).isEmpty()) {
            throw new IllegalStateException("Não é possível deletar a editora, pois ela possui livros cadastrados.");
        }
        editoraRepository.deleteById(id);
    }

    public Editora atualizarEditora(Editora editora) {
        return editoraRepository.update(editora);
    }
}
