package infrastructure.repositories;

import domain.entities.Cliente;
import domain.repositories.ClienteRepository;
import infrastructure.db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepositoryImpl implements ClienteRepository {

    @Override
    public Cliente save(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, endereco, telefone, cpf, cnpj) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEndereco());
            stmt.setString(3, cliente.getTelefone());

            if (cliente.isPessoaFisica()) {
                stmt.setString(4, cliente.getCpf());
                stmt.setNull(5, Types.VARCHAR);
            } else {
                stmt.setNull(4, Types.VARCHAR);
                stmt.setString(5, cliente.getCnpj());
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cliente.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return cliente;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cliente> findById(Integer id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCliente(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clientes.add(mapRowToCliente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Cliente update(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, endereco = ?, telefone = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEndereco());
            stmt.setString(3, cliente.getTelefone());
            stmt.setInt(4, cliente.getId());
            stmt.executeUpdate();
            return cliente;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) {
        String sql = "SELECT * FROM cliente WHERE cpf = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCliente(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por CPF: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Cliente> findByCnpj(String cnpj) {
        String sql = "SELECT * FROM cliente WHERE cnpj = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cnpj);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCliente(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por CNPJ: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    private Cliente mapRowToCliente(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String endereco = rs.getString("endereco");
        String telefone = rs.getString("telefone");
        String cpf = rs.getString("cpf");
        String cnpj = rs.getString("cnpj");

        if (cpf != null) {
            return new Cliente(id, nome, endereco, telefone, cpf);
        } else {
            return new Cliente(nome, endereco, telefone, cnpj, id);
        }
    }
}