package presentation.controllers;

import application.usecases.EditoraUseCases;
import domain.entities.Editora;
import domain.utils.FormatUtils;

import java.util.Scanner;

public class EditoraController {
    private final Scanner scanner;
    private final EditoraUseCases editoraUseCases;

    public EditoraController(Scanner scanner, EditoraUseCases editoraUseCases) {
        this.scanner = scanner;
        this.editoraUseCases = editoraUseCases;
    }

    public void menu() {
        while (true) {
            System.out.println("\n--- Gerenciar Editoras ---");
            System.out.println("1. Cadastrar Editora");
            System.out.println("2. Buscar Editora por ID");
            System.out.println("3. Listar Todas as Editoras");
            System.out.println("4. Atualizar Editora");
            System.out.println("5. Deletar Editora");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    cadastrarEditora();
                    break;
                case 2:
                    buscarEditoraPorId();
                    break;
                case 3:
                    listarTodasEditoras();
                    break;
                case 4:
                    atualizarEditora();
                    break;
                case 5:
                    deletarEditora();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void cadastrarEditora() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Nome do Gerente: ");
        String gerente = scanner.nextLine();

        try {
            Editora editora = new Editora(null, nome, endereco, telefone, gerente);
            editoraUseCases.criarEditora(editora);
            System.out.println("Editora cadastrada com sucesso! ID: " + editora.getId());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscarEditoraPorId() {
        System.out.print("ID da Editora: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        editoraUseCases.buscarEditoraPorId(id).ifPresentOrElse(
                e -> {
                    System.out.println("\n--- Dados da Editora ---");
                    System.out.println("ID: " + e.getId());
                    System.out.println("Nome: " + e.getNome());
                    System.out.println("Endereço: " + e.getEndereco());
                    System.out.println("Telefone: " + FormatUtils.formatTelefone(e.getTelefone()));
                    System.out.println("Gerente: " + e.getNome_gerente());
                },
                () -> System.out.println("Editora não encontrada.")
        );
    }

    private void listarTodasEditoras() {
        System.out.println("\n--- Lista de Editoras ---");
        var editoras = editoraUseCases.listarTodasEditoras();
        if (editoras.isEmpty()) {
            System.out.println("Nenhuma editora cadastrada.");
        } else {
            editoras.forEach(e -> {
                System.out.println("--------------------");
                System.out.println("ID: " + e.getId());
                System.out.println("Nome: " + e.getNome());
                System.out.println("Telefone: " + FormatUtils.formatTelefone(e.getTelefone()));
            });
        }
    }

    private void atualizarEditora() {
        System.out.print("ID da editora a ser atualizada: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        editoraUseCases.buscarEditoraPorId(id).ifPresentOrElse(
                editora -> {
                    System.out.println("\n--- Dados Atuais da Editora ---");
                    System.out.println("ID: " + editora.getId());
                    System.out.println("Nome: " + editora.getNome());
                    System.out.println("Endereço: " + editora.getEndereco());
                    System.out.println("Telefone: " + FormatUtils.formatTelefone(editora.getTelefone()));
                    System.out.println("Gerente: " + editora.getNome_gerente());

                    System.out.println("\nDigite os novos dados (deixe em branco para não alterar):");

                    System.out.print("Novo Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Novo Endereço: ");
                    String endereco = scanner.nextLine();
                    System.out.print("Novo Telefone: ");
                    String telefone = scanner.nextLine();
                    System.out.print("Novo Nome do Gerente: ");
                    String gerente = scanner.nextLine();

                    try {
                        if (!nome.isBlank()) editora.setNome(nome);
                        if (!endereco.isBlank()) editora.setEndereco(endereco);
                        if (!telefone.isBlank()) editora.setTelefone(telefone);
                        if (!gerente.isBlank()) editora.setNome_gerente(gerente);

                        editoraUseCases.atualizarEditora(editora);
                        System.out.println("Editora atualizada com sucesso!");
                    } catch (Exception e) {
                        System.out.println("Erro ao atualizar editora: " + e.getMessage());
                    }
                },
                () -> System.out.println("Editora não encontrada.")
        );
    }

    private void deletarEditora() {
        System.out.print("ID da editora a ser deletada: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (editoraUseCases.buscarEditoraPorId(id).isEmpty()) {
            System.out.println("Erro: Editora não encontrada.");
            return;
        }

        try {
            editoraUseCases.deletarEditora(id);
            System.out.println("Editora deletada com sucesso.");
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado ao deletar a editora.");
        }
    }
}
