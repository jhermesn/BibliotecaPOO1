package application.usecases;

import domain.entities.Compra;
import domain.repositories.CompraRepository;
import domain.repositories.LivroRepository;
import domain.entities.Livro;

import java.util.List;
import java.util.Optional;

public class CompraUseCases {

    private final CompraRepository compraRepository;
    private final LivroRepository livroRepository;

    public CompraUseCases(CompraRepository compraRepository, LivroRepository livroRepository) {
        this.compraRepository = compraRepository;
        this.livroRepository = livroRepository;
    }

    public Compra realizarCompra(Compra compra) {
        Livro livro = compra.getLivro();
        if (livro.getEstoque() >= compra.getQuantidade()) {
            livro.setEstoque(livro.getEstoque() - compra.getQuantidade());
            livroRepository.update(livro);
            return compraRepository.save(compra);
        } else {
            throw new IllegalStateException("Estoque insuficiente para o livro '" + livro.getNome() + "'. " +
                    "Disponível: " + livro.getEstoque() + ", Solicitado: " + compra.getQuantidade());
        }
    }

    public Optional<Compra> buscarCompraPorId(Integer id) {
        return compraRepository.findById(id);
    }

    public List<Compra> listarTodasCompras() {
        return compraRepository.findAll();
    }

    public List<Compra> listarComprasPorCliente(Integer clienteId) {
        return compraRepository.findByClienteId(clienteId);
    }
}
