package domain.repositories;

import domain.entities.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente save(Cliente cliente);
    Optional<Cliente> findById(Integer id);
    Optional<Cliente> findByCpf(String cpf);
    Optional<Cliente> findByCnpj(String cnpj);
    List<Cliente> findAll();
    void deleteById(Integer id);
    Cliente update(Cliente cliente);
}
