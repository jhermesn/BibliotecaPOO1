package infrastructure.repositories;

import domain.entities.Editora;
import domain.entities.Livro;
import domain.repositories.LivroRepository;
import infrastructure.db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LivroRepositoryImpl implements LivroRepository {

    private static final String SELECT_LIVRO_SQL = "SELECT l.ISBN, l.nome, l.nome_autor, l.assunto, l.estoque, " +
            "e.id as editora_id, e.nome as editora_nome, e.endereco as editora_endereco, " +
            "e.telefone as editora_telefone, e.nome_gerente as editora_gerente " +
            "FROM livro l JOIN editora e ON l.editora_id = e.id ";

    @Override
    public Livro save(Livro livro) {
        String sql = "INSERT INTO livro (ISBN, editora_id, nome, nome_autor, assunto, estoque) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livro.getIsbn());
            stmt.setInt(2, livro.getEditora().getId());
            stmt.setString(3, livro.getNome());
            stmt.setString(4, livro.getNome_autor());
            stmt.setString(5, livro.getAssunto());
            stmt.setInt(6, livro.getEstoque());
            stmt.executeUpdate();
            return livro;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar livro: " + e.getMessage(), e);
        }
    }

    @Override
    public Livro update(Livro livro) {
        String sql = "UPDATE livro SET editora_id = ?, nome = ?, nome_autor = ?, assunto = ?, estoque = ? WHERE ISBN = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livro.getEditora().getId());
            stmt.setString(2, livro.getNome());
            stmt.setString(3, livro.getNome_autor());
            stmt.setString(4, livro.getAssunto());
            stmt.setInt(5, livro.getEstoque());
            stmt.setString(6, livro.getIsbn());
            stmt.executeUpdate();
            return livro;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar livro: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Livro> findByIsbn(String isbn) {
        String sql = SELECT_LIVRO_SQL + " WHERE l.ISBN = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToLivro(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por ISBN: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Livro> findAll() {
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_LIVRO_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                livros.add(mapRowToLivro(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os livros: " + e.getMessage(), e);
        }
        return livros;
    }

    @Override
    public List<Livro> findByAssunto(String assunto) {
        List<Livro> livros = new ArrayList<>();
        String sql = SELECT_LIVRO_SQL + " WHERE l.assunto LIKE ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + assunto + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livros.add(mapRowToLivro(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livros por assunto: " + e.getMessage(), e);
        }
        return livros;
    }

    @Override
    public List<Livro> findByNomeAutor(String nomeAutor) {
        List<Livro> livros = new ArrayList<>();
        String sql = SELECT_LIVRO_SQL + " WHERE l.nome_autor LIKE ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeAutor + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livros.add(mapRowToLivro(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livros por autor: " + e.getMessage(), e);
        }
        return livros;
    }

    @Override
    public void deleteByIsbn(String isbn) {
        String sql = "DELETE FROM livro WHERE ISBN = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar livro: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Livro> findByEditoraId(Integer editoraId) {
        List<Livro> livros = new ArrayList<>();
        String sql = SELECT_LIVRO_SQL + " WHERE l.editora_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, editoraId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livros.add(mapRowToLivro(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livros por editora: " + e.getMessage(), e);
        }
        return livros;
    }

    private Livro mapRowToLivro(ResultSet rs) throws SQLException {
        Editora editora = new Editora(
                rs.getInt("editora_id"),
                rs.getString("editora_nome"),
                rs.getString("editora_endereco"),
                rs.getString("editora_telefone"),
                rs.getString("editora_gerente")
        );

        return new Livro(
                rs.getString("ISBN"),
                editora,
                rs.getString("nome"),
                rs.getString("nome_autor"),
                rs.getString("assunto"),
                rs.getInt("estoque")
        );
    }
}
