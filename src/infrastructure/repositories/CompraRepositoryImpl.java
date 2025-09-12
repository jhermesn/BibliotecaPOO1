package infrastructure.repositories;

import domain.entities.*;
import domain.repositories.CompraRepository;
import infrastructure.db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompraRepositoryImpl implements CompraRepository {

    private static final String SELECT_COMPRA_SQL =
            "SELECT " +
            "c.id as compra_id, c.data_compra, c.quantidade, c.preco_unitario, " +
            "cl.id as cliente_id, cl.nome as cliente_nome, cl.endereco as cliente_endereco, cl.telefone as cliente_telefone, cl.cpf, cl.cnpj, " +
            "l.ISBN as livro_isbn, l.nome as livro_nome, l.nome_autor as livro_autor, l.assunto as livro_assunto, l.estoque as livro_estoque, " +
            "e.id as editora_id, e.nome as editora_nome, e.endereco as editora_endereco, e.telefone as editora_telefone, e.nome_gerente as editora_gerente " +
            "FROM compra c " +
            "JOIN cliente cl ON c.cliente_id = cl.id " +
            "JOIN livro l ON c.livro_isbn = l.ISBN " +
            "JOIN editora e ON l.editora_id = e.id ";

    @Override
    public Compra save(Compra compra) {
        String sql = "INSERT INTO compra (cliente_id, livro_isbn, data_compra, quantidade, preco_unitario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, compra.getCliente().getId());
            stmt.setString(2, compra.getLivro().getIsbn());
            stmt.setDate(3, new java.sql.Date(compra.getData_compra().getTime()));
            stmt.setInt(4, compra.getQuantidade());
            stmt.setBigDecimal(5, compra.getPreco_unitario());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        compra.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return compra;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar compra: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Compra> findById(Integer id) {
        String sql = SELECT_COMPRA_SQL + " WHERE c.id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCompra(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar compra por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Compra> findAll() {
        List<Compra> compras = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_COMPRA_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                compras.add(mapRowToCompra(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todas as compras: " + e.getMessage(), e);
        }
        return compras;
    }

    @Override
    public List<Compra> findByClienteId(Integer clienteId) {
        List<Compra> compras = new ArrayList<>();
        String sql = SELECT_COMPRA_SQL + " WHERE c.cliente_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    compras.add(mapRowToCompra(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar compras por cliente: " + e.getMessage(), e);
        }
        return compras;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM compra WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar compra: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Compra> findByLivroIsbn(String isbn) {
        List<Compra> compras = new ArrayList<>();
        String sql = SELECT_COMPRA_SQL + " WHERE c.livro_isbn = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    compras.add(mapRowToCompra(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar compras por livro: " + e.getMessage(), e);
        }
        return compras;
    }

    private Compra mapRowToCompra(ResultSet rs) throws SQLException {
        Cliente cliente;
        if (rs.getString("cpf") != null) {
            cliente = new Cliente(
                    rs.getInt("cliente_id"),
                    rs.getString("cliente_nome"),
                    rs.getString("cliente_endereco"),
                    rs.getString("cliente_telefone"),
                    rs.getString("cpf")
            );
        } else {
            cliente = new Cliente(
                    rs.getString("cliente_nome"),
                    rs.getString("cliente_endereco"),
                    rs.getString("cliente_telefone"),
                    rs.getString("cnpj"),
                    rs.getInt("cliente_id")
            );
        }

        Editora editora = new Editora(
                rs.getInt("editora_id"),
                rs.getString("editora_nome"),
                rs.getString("editora_endereco"),
                rs.getString("editora_telefone"),
                rs.getString("editora_gerente")
        );

        Livro livro = new Livro(
                rs.getString("livro_isbn"),
                editora,
                rs.getString("livro_nome"),
                rs.getString("livro_autor"),
                rs.getString("livro_assunto"),
                rs.getInt("livro_estoque")
        );

        return new Compra(
                rs.getInt("compra_id"),
                cliente,
                livro,
                rs.getDate("data_compra"),
                rs.getInt("quantidade"),
                rs.getBigDecimal("preco_unitario")
        );
    }
}
