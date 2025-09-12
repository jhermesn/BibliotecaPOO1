package presentation.controllers;

import application.usecases.EditoraUseCases;
import application.usecases.LivroUseCases;
import domain.entities.Editora;
import domain.entities.Livro;

import java.util.Optional;
import java.util.Scanner;

public class LivroController {
    private final Scanner scanner;
    private final LivroUseCases livroUseCases;
    private final EditoraUseCases editoraUseCases;

    public LivroController(Scanner scanner, LivroUseCases livroUseCases, EditoraUseCases editoraUseCases) {
        this.scanner = scanner;
        this.livroUseCases = livroUseCases;
        this.editoraUseCases = editoraUseCases;
    }

    public void menu() {
        while (true) {
            System.out.println("\n--- Gerenciar Livros ---");
            System.out.println("1. Cadastrar Livro");
            System.out.println("2. Buscar Livro por ISBN");
            System.out.println("3. Listar Todos os Livros");
            System.out.println("4. Atualizar Livro");
            System.out.println("5. Deletar Livro");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    cadastrarLivro();
                    break;
                case 2:
                    buscarLivroPorIsbn();
                    break;
                case 3:
                    listarTodosLivros();
                    break;
                case 4:
                    atualizarLivro();
                    break;
                case 5:
                    deletarLivro();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void cadastrarLivro() {
        if (editoraUseCases.listarTodasEditoras().isEmpty()) {
            System.out.println("\nErro: Nenhuma editora cadastrada. Cadastre uma editora antes de criar um livro.");
            return;
        }

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        if (livroUseCases.buscarLivroPorIsbn(isbn).isPresent()) {
            System.out.println("Erro: Já existe um livro com este ISBN.");
            return;
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Assunto: ");
        String assunto = scanner.nextLine();
        System.out.print("Estoque: ");
        int estoque = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\n--- Editoras Disponíveis ---");
        editoraUseCases.listarTodasEditoras().forEach(e -> System.out.println("ID: " + e.getId() + ", Nome: " + e.getNome()));
        System.out.print("ID da Editora: ");
        int editoraId = scanner.nextInt();
        scanner.nextLine();

        Optional<Editora> editoraOpt = editoraUseCases.buscarEditoraPorId(editoraId);
        if (editoraOpt.isEmpty()) {
            System.out.println("Erro: Editora com ID " + editoraId + " não encontrada.");
            return;
        }

        try {
            Livro livro = new Livro(isbn, editoraOpt.get(), nome, autor, assunto, estoque);
            livroUseCases.criarLivro(livro);
            System.out.println("Livro cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar livro: " + e.getMessage());
        }
    }

    private void buscarLivroPorIsbn() {
        System.out.print("ISBN do Livro: ");
        String isbn = scanner.nextLine();

        livroUseCases.buscarLivroPorIsbn(isbn).ifPresentOrElse(
                l -> {
                    System.out.println("\n--- Dados do Livro ---");
                    System.out.println("ISBN: " + l.getIsbn());
                    System.out.println("Nome: " + l.getNome());
                    System.out.println("Autor: " + l.getNome_autor());
                    System.out.println("Assunto: " + l.getAssunto());
                    System.out.println("Estoque: " + l.getEstoque());
                    System.out.println("Editora: " + l.getEditora().getNome());
                },
                () -> System.out.println("Livro não encontrado.")
        );
    }

    private void listarTodosLivros() {
        System.out.println("\n--- Lista de Livros ---");
        var livros = livroUseCases.listarTodosLivros();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            livros.forEach(l -> System.out.println(
                    "ISBN: " + l.getIsbn() + ", Nome: " + l.getNome() + ", Editora: " + l.getEditora().getNome()
            ));
        }
    }

    private void atualizarLivro() {
        System.out.print("ISBN do livro a ser atualizado: ");
        String isbn = scanner.nextLine();

        Optional<Livro> livroOpt = livroUseCases.buscarLivroPorIsbn(isbn);
        if (livroOpt.isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }

        Livro livro = livroOpt.get();

        System.out.print("Novo Nome (Deixe em branco para manter: " + livro.getNome() + "): ");
        String nome = scanner.nextLine();
        if (!nome.isBlank()) {
            livro.setNome(nome);
        }

        System.out.print("Novo Autor (Deixe em branco para manter: " + livro.getNome_autor() + "): ");
        String autor = scanner.nextLine();
        if (!autor.isBlank()) {
            livro.setNome_autor(autor);
        }

        System.out.print("Novo Assunto (Deixe em branco para manter: " + livro.getAssunto() + "): ");
        String assunto = scanner.nextLine();
        if (!assunto.isBlank()) {
            livro.setAssunto(assunto);
        }

        System.out.print("Novo Estoque (Deixe em branco para manter: " + livro.getEstoque() + "): ");
        String estoqueStr = scanner.nextLine();
        if (!estoqueStr.isBlank()) {
            try {
                livro.setEstoque(Integer.parseInt(estoqueStr));
            } catch (NumberFormatException e) {
                System.out.println("Estoque inválido. Mantendo o valor anterior.");
            }
        }

        System.out.println("\n--- Editoras Disponíveis ---");
        editoraUseCases.listarTodasEditoras().forEach(e -> System.out.println("ID: " + e.getId() + ", Nome: " + e.getNome()));
        System.out.print("Novo ID da Editora (Deixe em branco para manter: " + livro.getEditora().getNome() + "): ");
        String editoraIdStr = scanner.nextLine();
        if (!editoraIdStr.isBlank()) {
            try {
                int editoraId = Integer.parseInt(editoraIdStr);
                Optional<Editora> editoraOpt = editoraUseCases.buscarEditoraPorId(editoraId);
                if (editoraOpt.isPresent()) {
                    livro.setEditora(editoraOpt.get());
                } else {
                    System.out.println("Editora não encontrada. Mantendo a anterior.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID da editora inválido. Mantendo a anterior.");
            }
        }

        try {
            livroUseCases.atualizarLivro(livro);
            System.out.println("Livro atualizado com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar livro: " + e.getMessage());
        }
    }

    private void deletarLivro() {
        System.out.print("ISBN do livro a ser deletado: ");
        String isbn = scanner.nextLine();

        if (livroUseCases.buscarLivroPorIsbn(isbn).isEmpty()) {
            System.out.println("Erro: Livro não encontrado.");
            return;
        }

        try {
            livroUseCases.deletarLivro(isbn);
            System.out.println("Livro deletado com sucesso.");
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado ao deletar o livro.");
        }
    }
}
