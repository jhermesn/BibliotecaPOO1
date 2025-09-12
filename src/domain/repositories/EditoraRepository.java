package domain.repositories;

import domain.entities.Editora;
import java.util.List;
import java.util.Optional;

public interface EditoraRepository {
    Editora save(Editora editora);
    Optional<Editora> findById(Integer id);
    List<Editora> findAll();
    void deleteById(Integer id);
    Editora update(Editora editora);
}
