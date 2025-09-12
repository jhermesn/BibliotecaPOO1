package domain.repositories;

import domain.entities.Compra;
import java.util.List;
import java.util.Optional;

public interface CompraRepository {
    Compra save(Compra compra);
    Optional<Compra> findById(Integer id);
    List<Compra> findAll();
    List<Compra> findByClienteId(Integer clienteId);
    void deleteById(Integer id);
    List<Compra> findByLivroIsbn(String isbn);
}
