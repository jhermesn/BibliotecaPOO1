package application;

import application.usecases.*;
import domain.repositories.*;
import infrastructure.repositories.*;
import presentation.controllers.*;

import javax.swing.*;

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

        ClienteController clienteController = new ClienteController(clienteUseCases);
        LivroController livroController = new LivroController(livroUseCases, editoraUseCases);
        EditoraController editoraController = new EditoraController(editoraUseCases);
        CompraController compraController = new CompraController(compraUseCases, clienteUseCases, livroUseCases);

        mainMenu(clienteController, livroController, editoraController, compraController);
    }

    private static void mainMenu(ClienteController clienteController, LivroController livroController, EditoraController editoraController, CompraController compraController) {
        String[] options = {"Gerenciar Clientes", "Gerenciar Livros", "Gerenciar Editoras", "Gerenciar Compras", "Sair"};
        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Selecione uma opção:",
                    "Menu Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == JOptionPane.CLOSED_OPTION || choice == 4) { // Sair
                JOptionPane.showMessageDialog(null, "Saindo...");
                return;
            }

            switch (choice) {
                case 0: // Gerenciar Clientes
                    clienteController.menu();
                    break;
                case 1: // Gerenciar Livros
                    livroController.menu();
                    break;
                case 2: // Gerenciar Editoras
                    editoraController.menu();
                    break;
                case 3: // Gerenciar Compras
                    compraController.menu();
                    break;
            }
        }
    }
}
