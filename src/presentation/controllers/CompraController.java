package presentation.controllers;

import application.usecases.ClienteUseCases;
import application.usecases.CompraUseCases;
import application.usecases.LivroUseCases;
import domain.entities.Cliente;
import domain.entities.Compra;
import domain.entities.Livro;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

public class CompraController {
    private final Scanner scanner;
    private final CompraUseCases compraUseCases;
    private final ClienteUseCases clienteUseCases;
    private final LivroUseCases livroUseCases;

    public CompraController(Scanner scanner, CompraUseCases compraUseCases, ClienteUseCases clienteUseCases, LivroUseCases livroUseCases) {
        this.scanner = scanner;
        this.compraUseCases = compraUseCases;
        this.clienteUseCases = clienteUseCases;
        this.livroUseCases = livroUseCases;
    }

    public void menu() {
        while (true) {
            System.out.println("\n--- Gerenciar Compras ---");
            System.out.println("1. Realizar Compra");
            System.out.println("2. Buscar Compra por ID");
            System.out.println("3. Listar Todas as Compras");
            System.out.println("4. Listar Compras por Cliente");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    realizarCompra();
                    break;
                case 2:
                    buscarCompraPorId();
                    break;
                case 3:
                    listarTodasCompras();
                    break;
                case 4:
                    listarComprasPorCliente();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void realizarCompra() {
        if (clienteUseCases.listarTodosClientes().isEmpty()) {
            System.out.println("\nErro: Nenhum cliente cadastrado. Cadastre um cliente antes de realizar uma compra.");
            return;
        }

        if (livroUseCases.listarTodosLivros().isEmpty()) {
            System.out.println("\nErro: Nenhum livro cadastrado. Cadastre um livro antes de realizar uma compra.");
            return;
        }

        System.out.println("\n--- Clientes Disponíveis ---");
        clienteUseCases.listarTodosClientes().forEach(c -> System.out.println("ID: " + c.getId() + ", Nome: " + c.getNome()));
        System.out.print("Digite o ID do Cliente: ");
        int clienteId = scanner.nextInt();
        scanner.nextLine();
        Optional<Cliente> clienteOpt = clienteUseCases.buscarClientePorId(clienteId);
        if (clienteOpt.isEmpty()) {
            System.out.println("Erro: Cliente não encontrado.");
            return;
        }

        System.out.println("\n--- Livros Disponíveis ---");
        livroUseCases.listarTodosLivros().forEach(l -> System.out.println("ISBN: " + l.getIsbn() + ", Nome: " + l.getNome() + ", Estoque: " + l.getEstoque()));
        System.out.print("Digite o ISBN do Livro: ");
        String isbn = scanner.nextLine();
        Optional<Livro> livroOpt = livroUseCases.buscarLivroPorIsbn(isbn);
        if (livroOpt.isEmpty()) {
            System.out.println("Erro: Livro não encontrado.");
            return;
        }

        Livro livro = livroOpt.get();

        System.out.print("Quantidade desejada (Estoque atual: " + livro.getEstoque() + "): ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();
        if (quantidade <= 0) {
            System.out.println("Erro: A quantidade deve ser positiva.");
            return;
        }
        if (livro.getEstoque() < quantidade) {
            System.out.println("Erro: Estoque insuficiente.");
            return;
        }

        System.out.print("Preço Unitário: ");
        BigDecimal preco = scanner.nextBigDecimal();
        scanner.nextLine();

        try {
            Compra compra = new Compra(null, clienteOpt.get(), livro, new Date(), quantidade, preco);
            compraUseCases.realizarCompra(compra);
            System.out.println("Compra realizada com sucesso! ID da Compra: " + compra.getId());
            System.out.println("Novo estoque do livro '" + livro.getNome() + "': " + livro.getEstoque());
        } catch (Exception e) {
            System.out.println("Erro ao processar a compra: " + e.getMessage());
        }
    }

    private void buscarCompraPorId() {
        System.out.print("ID da Compra: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        compraUseCases.buscarCompraPorId(id).ifPresentOrElse(
                c -> {
                    System.out.println("--- Dados da Compra ---");
                    System.out.println("ID: " + c.getId());
                    System.out.println("Data: " + c.getData_compra());
                    System.out.println("Cliente: " + c.getCliente().getNome());
                    System.out.println("Livro: " + c.getLivro().getNome());
                    System.out.println("Quantidade: " + c.getQuantidade());
                    System.out.println("Valor Total: " + c.getValorTotal());
                },
                () -> System.out.println("Compra não encontrada.")
        );
    }

    private void listarTodasCompras() {
        System.out.println("\n--- Histórico de Compras ---");
        var compras = compraUseCases.listarTodasCompras();
        if (compras.isEmpty()) {
            System.out.println("Nenhuma compra foi realizada.");
        } else {
            compras.forEach(c -> System.out.println(
                    "ID: " + c.getId() + ", Data: " + c.getData_compra() + ", Cliente: " + c.getCliente().getNome() + ", Livro: " + c.getLivro().getNome()
            ));
        }
    }

    private void listarComprasPorCliente() {
        System.out.print("Digite o ID do Cliente: ");
        int clienteId = scanner.nextInt();
        scanner.nextLine();

        if (clienteUseCases.buscarClientePorId(clienteId).isEmpty()) {
            System.out.println("Erro: Cliente não encontrado.");
            return;
        }

        System.out.println("\n--- Compras do Cliente ID: " + clienteId + " ---");
        var compras = compraUseCases.listarComprasPorCliente(clienteId);
        if (compras.isEmpty()) {
            System.out.println("Nenhuma compra encontrada para este cliente.");
        } else {
            compras.forEach(c -> System.out.println(
                    "ID: " + c.getId() + ", Data: " + c.getData_compra() + ", Livro: " + c.getLivro().getNome() + ", Qtd: " + c.getQuantidade()
            ));
        }
    }
}
