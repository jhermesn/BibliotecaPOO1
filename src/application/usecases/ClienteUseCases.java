package application.usecases;

import domain.entities.Cliente;
import domain.repositories.ClienteRepository;
import domain.repositories.CompraRepository;

import java.util.List;
import java.util.Optional;

public class ClienteUseCases {

    private final ClienteRepository clienteRepository;
    private final CompraRepository compraRepository;

    public ClienteUseCases(ClienteRepository clienteRepository, CompraRepository compraRepository) {
        this.clienteRepository = clienteRepository;
        this.compraRepository = compraRepository;
    }

    public Cliente criarCliente(Cliente cliente) {
        if (cliente.isPessoaFisica()) {
            if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
                throw new IllegalArgumentException("CPF já cadastrado.");
            }
        } else {
            if (clienteRepository.findByCnpj(cliente.getCnpj()).isPresent()) {
                throw new IllegalArgumentException("CNPJ já cadastrado.");
            }
        }
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarClientePorId(Integer id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> listarTodosClientes() {
        return clienteRepository.findAll();
    }

    public void deletarCliente(Integer id) {
        if (!compraRepository.findByClienteId(id).isEmpty()) {
            throw new IllegalStateException("Não é possível deletar o cliente, pois ele possui um histórico de compras.");
        }
        clienteRepository.deleteById(id);
    }

    public Cliente atualizarCliente(Cliente cliente) {
        return clienteRepository.update(cliente);
    }
}
