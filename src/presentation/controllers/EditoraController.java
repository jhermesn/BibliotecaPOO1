package presentation.controllers;

import application.usecases.EditoraUseCases;
import domain.entities.Editora;
import domain.utils.FormatUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EditoraController {
    private final EditoraUseCases editoraUseCases;

    public EditoraController(EditoraUseCases editoraUseCases) {
        this.editoraUseCases = editoraUseCases;
    }

    public void menu() {
        String[] options = {
                "Cadastrar Editora",
                "Buscar Editora por ID",
                "Listar Todas as Editoras",
                "Atualizar Editora",
                "Deletar Editora",
                "Voltar ao Menu Principal"
        };

        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Selecione uma opção:",
                    "Gerenciar Editoras",
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
                case 0 -> cadastrarEditora();
                case 1 -> buscarEditoraPorId();
                case 2 -> listarTodasEditoras();
                case 3 -> atualizarEditora();
                case 4 -> deletarEditora();
            }
        }
    }

    private void cadastrarEditora() {
        String nome = JOptionPane.showInputDialog("Nome:");
        if (nome == null) return;
        String endereco = JOptionPane.showInputDialog("Endereço:");
        if (endereco == null) return;
        String telefone = JOptionPane.showInputDialog("Telefone:");
        if (telefone == null) return;
        String gerente = JOptionPane.showInputDialog("Nome do Gerente:");
        if (gerente == null) return;

        try {
            Editora editora = new Editora(null, nome, endereco, telefone, gerente);
            editoraUseCases.criarEditora(editora);
            JOptionPane.showMessageDialog(null, "Editora cadastrada com sucesso! ID: " + editora.getId());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarEditoraPorId() {
        String idStr = JOptionPane.showInputDialog("ID da Editora:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            editoraUseCases.buscarEditoraPorId(id).ifPresentOrElse(
                    e -> {
                        String info = """
                                --- Dados da Editora ---
                                ID: %d
                                Nome: %s
                                Endereço: %s
                                Telefone: %s
                                Gerente: %s
                                """.formatted(e.getId(), e.getNome(), e.getEndereco(), FormatUtils.formatTelefone(e.getTelefone()), e.getNome_gerente());
                        JOptionPane.showMessageDialog(null, info, "Dados da Editora", JOptionPane.INFORMATION_MESSAGE);
                    },
                    () -> JOptionPane.showMessageDialog(null, "Editora não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE)
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarTodasEditoras() {
        List<Editora> editoras = editoraUseCases.listarTodasEditoras();
        if (editoras.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma editora cadastrada.");
        } else {
            StringBuilder sb = new StringBuilder("--- Lista de Editoras ---\n");
            for (Editora e : editoras) {
                sb.append("--------------------\n");
                sb.append("ID: ").append(e.getId()).append("\n");
                sb.append("Nome: ").append(e.getNome()).append("\n");
                sb.append("Telefone: ").append(FormatUtils.formatTelefone(e.getTelefone())).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(500, 500));
            JOptionPane.showMessageDialog(null, scrollPane, "Lista de Editoras", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void atualizarEditora() {
        String idStr = JOptionPane.showInputDialog("ID da editora a ser atualizada:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            editoraUseCases.buscarEditoraPorId(id).ifPresentOrElse(
                    editora -> {
                        String nome = JOptionPane.showInputDialog("Novo Nome:", editora.getNome());
                        if (nome == null) return;
                        String endereco = JOptionPane.showInputDialog("Novo Endereço:", editora.getEndereco());
                        if (endereco == null) return;
                        String telefone = JOptionPane.showInputDialog("Novo Telefone:", editora.getTelefone());
                        if (telefone == null) return;
                        String gerente = JOptionPane.showInputDialog("Novo Nome do Gerente:", editora.getNome_gerente());
                        if (gerente == null) return;

                        try {
                            editora.setNome(nome);
                            editora.setEndereco(endereco);
                            editora.setTelefone(telefone);
                            editora.setNome_gerente(gerente);

                            editoraUseCases.atualizarEditora(editora);
                            JOptionPane.showMessageDialog(null, "Editora atualizada com sucesso!");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Erro ao atualizar editora: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    },
                    () -> JOptionPane.showMessageDialog(null, "Editora não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE)
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarEditora() {
        String idStr = JOptionPane.showInputDialog("ID da editora a ser deletada:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            if (editoraUseCases.buscarEditoraPorId(id).isEmpty()) {
                JOptionPane.showMessageDialog(null, "Erro: Editora não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar esta editora?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            editoraUseCases.deletarEditora(id);
            JOptionPane.showMessageDialog(null, "Editora deletada com sucesso.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro de Deleção", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro inesperado ao deletar a editora.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
