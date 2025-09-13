package presentation.controllers;

import application.usecases.EditoraUseCases;
import application.usecases.LivroUseCases;
import domain.entities.Editora;
import domain.entities.Livro;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class LivroController {
    private final LivroUseCases livroUseCases;
    private final EditoraUseCases editoraUseCases;

    public LivroController(LivroUseCases livroUseCases, EditoraUseCases editoraUseCases) {
        this.livroUseCases = livroUseCases;
        this.editoraUseCases = editoraUseCases;
    }

    public void menu() {
        String[] options = {
                "Cadastrar Livro",
                "Buscar Livro por ISBN",
                "Listar Todos os Livros",
                "Atualizar Livro",
                "Deletar Livro",
                "Voltar ao Menu Principal"
        };

        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Selecione uma opção:",
                    "Gerenciar Livros",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == JOptionPane.CLOSED_OPTION || choice == 5) { // Voltar
                return;
            }

            switch (choice) {
                case 0 -> cadastrarLivro();
                case 1 -> buscarLivroPorIsbn();
                case 2 -> listarTodosLivros();
                case 3 -> atualizarLivro();
                case 4 -> deletarLivro();
            }
        }
    }

    private void cadastrarLivro() {
        List<Editora> editoras = editoraUseCases.listarTodasEditoras();
        if (editoras.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Erro: Nenhuma editora cadastrada. Cadastre uma editora antes de criar um livro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String isbn = JOptionPane.showInputDialog("ISBN:");
        if (isbn == null) return;
        if (livroUseCases.buscarLivroPorIsbn(isbn).isPresent()) {
            JOptionPane.showMessageDialog(null, "Erro: Já existe um livro com este ISBN.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nome = JOptionPane.showInputDialog("Nome:");
        if (nome == null) return;
        String autor = JOptionPane.showInputDialog("Autor:");
        if (autor == null) return;
        String assunto = JOptionPane.showInputDialog("Assunto:");
        if (assunto == null) return;

        int estoque;
        try {
            String estoqueStr = JOptionPane.showInputDialog("Estoque:");
            if (estoqueStr == null) return;
            estoque = Integer.parseInt(estoqueStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Estoque inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Editora editoraSelecionada = selectEditoraDialog(editoras);
        if (editoraSelecionada == null) {
            return;
        }

        try {
            Livro livro = new Livro(isbn, editoraSelecionada, nome, autor, assunto, estoque);
            livroUseCases.criarLivro(livro);
            JOptionPane.showMessageDialog(null, "Livro cadastrado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar livro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarLivroPorIsbn() {
        String isbn = JOptionPane.showInputDialog("ISBN do Livro:");
        if (isbn == null) return;

        livroUseCases.buscarLivroPorIsbn(isbn).ifPresentOrElse(
                l -> {
                    String info = """
                            --- Dados do Livro ---
                            ISBN: %s
                            Nome: %s
                            Autor: %s
                            Assunto: %s
                            Estoque: %d
                            Editora: %s
                            """.formatted(l.getIsbn(), l.getNome(), l.getNome_autor(), l.getAssunto(), l.getEstoque(), l.getEditora().getNome());
                    JOptionPane.showMessageDialog(null, info, "Dados do Livro", JOptionPane.INFORMATION_MESSAGE);
                },
                () -> JOptionPane.showMessageDialog(null, "Livro não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE)
        );
    }

    private void listarTodosLivros() {
        List<Livro> livros = livroUseCases.listarTodosLivros();
        if (livros.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum livro cadastrado.");
        } else {
            StringBuilder sb = new StringBuilder("--- Lista de Livros ---\n");
            for (Livro l : livros) {
                sb.append("-------------------------\n");
                sb.append("ISBN: ").append(l.getIsbn()).append("\n");
                sb.append("Nome: ").append(l.getNome()).append("\n");
                sb.append("Editora: ").append(l.getEditora().getNome()).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(500, 500));
            JOptionPane.showMessageDialog(null, scrollPane, "Lista de Livros", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void atualizarLivro() {
        String isbn = JOptionPane.showInputDialog("ISBN do livro a ser atualizado:");
        if (isbn == null) return;

        Optional<Livro> livroOpt = livroUseCases.buscarLivroPorIsbn(isbn);
        if (livroOpt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Livro não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Livro livro = livroOpt.get();

        String nome = JOptionPane.showInputDialog("Novo Nome:", livro.getNome());
        if (nome == null) return;
        livro.setNome(nome);

        String autor = JOptionPane.showInputDialog("Novo Autor:", livro.getNome_autor());
        if (autor == null) return;
        livro.setNome_autor(autor);

        String assunto = JOptionPane.showInputDialog("Novo Assunto:", livro.getAssunto());
        if (assunto == null) return;
        livro.setAssunto(assunto);

        try {
            String estoqueStr = JOptionPane.showInputDialog("Novo Estoque:", livro.getEstoque());
            if (estoqueStr == null) return;
            livro.setEstoque(Integer.parseInt(estoqueStr));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Estoque inválido. Mantendo o valor anterior.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        List<Editora> editoras = editoraUseCases.listarTodasEditoras();
        Editora novaEditora = selectEditoraDialog(editoras, livro.getEditora());
        if (novaEditora != null) {
            livro.setEditora(novaEditora);
        }

        try {
            livroUseCases.atualizarLivro(livro);
            JOptionPane.showMessageDialog(null, "Livro atualizado com sucesso.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar livro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarLivro() {
        String isbn = JOptionPane.showInputDialog("ISBN do livro a ser deletado:");
        if (isbn == null) return;

        if (livroUseCases.buscarLivroPorIsbn(isbn).isEmpty()) {
            JOptionPane.showMessageDialog(null, "Erro: Livro não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar este livro?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            livroUseCases.deletarLivro(isbn);
            JOptionPane.showMessageDialog(null, "Livro deletado com sucesso.");
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro de Deleção", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro inesperado ao deletar o livro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Editora selectEditoraDialog(List<Editora> editoras) {
        return selectEditoraDialog(editoras, null);
    }

    private Editora selectEditoraDialog(List<Editora> editoras, Editora preselecionada) {
        String[] editorasArray = editoras.stream()
                .map(e -> e.getId() + ": " + e.getNome())
                .toArray(String[]::new);

        int preselectIndex = -1;
        if (preselecionada != null) {
            for (int i = 0; i < editoras.size(); i++) {
                if (editoras.get(i).getId().equals(preselecionada.getId())) {
                    preselectIndex = i;
                    break;
                }
            }
        }

        String selected = (String) JOptionPane.showInputDialog(null, "Selecione a Editora",
                "Editoras", JOptionPane.QUESTION_MESSAGE, null, editorasArray, preselectIndex != -1 ? editorasArray[preselectIndex] : editorasArray[0]);

        if (selected == null) {
            return null;
        }

        int editoraId = Integer.parseInt(selected.split(":")[0]);
        return editoras.stream().filter(e -> e.getId() == editoraId).findFirst().orElse(null);
    }
}
