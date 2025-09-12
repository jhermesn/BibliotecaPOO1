package infrastructure.repositories;

import domain.entities.Editora;
import domain.repositories.EditoraRepository;
import infrastructure.db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditoraRepositoryImpl implements EditoraRepository {

    @Override
    public Editora save(Editora editora) {
        String sql = "INSERT INTO editora (nome, endereco, telefone, nome_gerente) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, editora.getNome());
            stmt.setString(2, editora.getEndereco());
            stmt.setString(3, editora.getTelefone());
            stmt.setString(4, editora.getNome_gerente());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        editora.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return editora;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar editora: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Editora> findById(Integer id) {
        String sql = "SELECT * FROM editora WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToEditora(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar editora por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Editora> findAll() {
        String sql = "SELECT * FROM editora";
        List<Editora> editoras = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                editoras.add(mapRowToEditora(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todas as editoras: " + e.getMessage(), e);
        }
        return editoras;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM editora WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar editora: " + e.getMessage(), e);
        }
    }

    @Override
    public Editora update(Editora editora) {
        String sql = "UPDATE editora SET nome = ?, endereco = ?, telefone = ?, nome_gerente = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, editora.getNome());
            stmt.setString(2, editora.getEndereco());
            stmt.setString(3, editora.getTelefone());
            stmt.setString(4, editora.getNome_gerente());
            stmt.setInt(5, editora.getId());
            stmt.executeUpdate();
            return editora;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar editora: " + e.getMessage(), e);
        }
    }

    private Editora mapRowToEditora(ResultSet rs) throws SQLException {
        return new Editora(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("endereco"),
                rs.getString("telefone"),
                rs.getString("nome_gerente")
        );
    }
}
