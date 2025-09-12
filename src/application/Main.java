package application;

import application.usecases.*;
import domain.repositories.*;
import infrastructure.repositories.*;
import presentation.controllers.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ClienteRepository clienteRepository = new ClientRepositoryImpl();
        LivroRepository livroRepository = new LivroRepositoryImpl();
        EditoraRepository editoraRepository = new EditoraRepositoryImpl();
        CompraRepository compraRepository = new CompraRepositoryImpl();

        ClienteUseCases clienteUseCases = new ClienteUseCases(clienteRepository, compraRepository);
        LivroUseCases livroUseCases = new LivroUseCases(livroRepository, compraRepository);
        EditoraUseCases editoraUseCases = new EditoraUseCases(editoraRepository, livroRepository);
        CompraUseCases compraUseCases = new CompraUseCases(compraRepository, livroRepository);

        Scanner scanner = new Scanner(System.in);
        ClienteController clienteController = new ClienteController(scanner, clienteUseCases);
        LivroController livroController = new LivroController(scanner, livroUseCases, editoraUseCases);
        EditoraController editoraController = new EditoraController(scanner, editoraUseCases);
        CompraController compraController = new CompraController(scanner, compraUseCases, clienteUseCases, livroUseCases);

        mainMenu(scanner, clienteController, livroController, editoraController, compraController);

        scanner.close();
    }

    private static void mainMenu(Scanner scanner, ClienteController clienteController, LivroController livroController, EditoraController editoraController, CompraController compraController) {
        while (true) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Livros");
            System.out.println("3. Gerenciar Editoras");
            System.out.println("4. Gerenciar Compras");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    clienteController.menu();
                    break;
                case 2:
                    livroController.menu();
                    break;
                case 3:
                    editoraController.menu();
                    break;
                case 4:
                    compraController.menu();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
