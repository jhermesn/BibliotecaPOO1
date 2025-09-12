package presentation.controllers;

import application.usecases.ClienteUseCases;
import domain.entities.Cliente;
import domain.utils.FormatUtils;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClienteController {
    private final Scanner scanner;
    private final ClienteUseCases clienteUseCases;

    public ClienteController(Scanner scanner, ClienteUseCases clienteUseCases) {
        this.scanner = scanner;
        this.clienteUseCases = clienteUseCases;
    }

    public void menu() {
        while (true) {
            System.out.println("\n--- Gerenciar Clientes ---");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Buscar Cliente por ID");
            System.out.println("3. Listar Todos os Clientes");
            System.out.println("4. Atualizar Cliente");
            System.out.println("5. Deletar Cliente");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    cadastrarCliente();
                    break;
                case 2:
                    buscarClientePorId();
                    break;
                case 3:
                    listarTodosClientes();
                    break;
                case 4:
                    atualizarCliente();
                    break;
                case 5:
                    deletarCliente();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void cadastrarCliente() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Pessoa Física (F) ou Jurídica (J)? ");
        String tipo = scanner.nextLine();
        String cpf = null;
        String cnpj = null;

        if (tipo.equalsIgnoreCase("F")) {
            System.out.print("CPF: ");
            cpf = scanner.nextLine();
            try {
                Cliente cliente = new Cliente(null, nome, endereco, telefone, cpf);
                clienteUseCases.criarCliente(cliente);
                System.out.println("Cliente cadastrado com sucesso! ID: " + cliente.getId());
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } else if (tipo.equalsIgnoreCase("J")) {
            System.out.print("CNPJ: ");
            cnpj = scanner.nextLine();
            try {
                Cliente cliente = new Cliente(nome, endereco, telefone, cnpj, null);
                clienteUseCases.criarCliente(cliente);
                System.out.println("Cliente cadastrado com sucesso! ID: " + cliente.getId());
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } else {
            System.out.println("Tipo inválido.");
        }
    }

    private void buscarClientePorId() {
        System.out.print("ID do Cliente: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        clienteUseCases.buscarClientePorId(id).ifPresentOrElse(
                c -> {
                    System.out.println("--- Dados do Cliente ---");
                    System.out.println("ID: " + c.getId());
                    System.out.println("Nome: " + c.getNome());
                    System.out.println("Endereço: " + c.getEndereco());
                    System.out.println("Telefone: " + FormatUtils.formatTelefone(c.getTelefone()));
                    if (c.isPessoaFisica()) {
                        System.out.println("CPF: " + FormatUtils.formatCPF(c.getCpf()));
                    } else {
                        System.out.println("CNPJ: " + FormatUtils.formatCNPJ(c.getCnpj()));
                    }
                },
                () -> System.out.println("Cliente não encontrado.")
        );
    }

    private void listarTodosClientes() {
        List<Cliente> clientes = clienteUseCases.buscarTodosClientes();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            System.out.println("--- Lista de Clientes ---");
            clientes.forEach(c -> {
                System.out.println("-------------------------");
                System.out.println("ID: " + c.getId());
                System.out.println("Nome: " + c.getNome());
                System.out.println("Telefone: " + FormatUtils.formatTelefone(c.getTelefone()));
                if (c.isPessoaFisica()) {
                    System.out.println("Documento: " + FormatUtils.formatCPF(c.getDocumento()));
                } else {
                    System.out.println("Documento: " + FormatUtils.formatCNPJ(c.getDocumento()));
                }
            });
        }
    }

    private void atualizarCliente() {
        System.out.print("ID do cliente a ser atualizado: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Cliente> clienteOpt = clienteUseCases.buscarClientePorId(id);
        if (clienteOpt.isEmpty()) {
            System.out.println("Erro: Cliente não encontrado.");
            return;
        }

        Cliente cliente = clienteOpt.get();
        System.out.print("Novo Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Novo Telefone: ");
        String telefone = scanner.nextLine();

        cliente.setNome(nome);
        cliente.setEndereco(endereco);
        cliente.setTelefone(telefone);

        try {
            clienteUseCases.atualizarCliente(cliente);
            System.out.println("Cliente atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    private void deletarCliente() {
        System.out.print("ID do cliente a ser deletado: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (clienteUseCases.buscarClientePorId(id).isEmpty()) {
            System.out.println("Erro: Cliente não encontrado.");
            return;
        }

        try {
            clienteUseCases.deletarCliente(id);
            System.out.println("Cliente deletado com sucesso.");
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado ao deletar o cliente.");
        }
    }
}
