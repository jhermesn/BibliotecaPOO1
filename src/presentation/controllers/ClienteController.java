package presentation.controllers;

import application.usecases.ClienteUseCases;
import domain.entities.Cliente;
import domain.utils.FormatUtils;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class ClienteController {
    private final ClienteUseCases clienteUseCases;

    public ClienteController(ClienteUseCases clienteUseCases) {
        this.clienteUseCases = clienteUseCases;
    }

    public void menu() {
        String[] options = {
                "Cadastrar Cliente",
                "Buscar Cliente por ID",
                "Listar Todos os Clientes",
                "Atualizar Cliente",
                "Deletar Cliente",
                "Voltar ao Menu Principal"
        };

        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Selecione uma opção:",
                    "Gerenciar Clientes",
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
                case 0 -> cadastrarCliente();
                case 1 -> buscarClientePorId();
                case 2 -> listarTodosClientes();
                case 3 -> atualizarCliente();
                case 4 -> deletarCliente();
            }
        }
    }

    private void cadastrarCliente() {
        String nome = JOptionPane.showInputDialog("Nome:");
        if (nome == null) return;
        String endereco = JOptionPane.showInputDialog("Endereço:");
        if (endereco == null) return;
        String telefone = JOptionPane.showInputDialog("Telefone:");
        if (telefone == null) return;

        String[] options = {"Pessoa Física", "Pessoa Jurídica"};
        int tipoChoice = JOptionPane.showOptionDialog(null, "Pessoa Física (F) ou Jurídica (J)?",
                "Tipo de Cliente", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (tipoChoice == -1) return;

        String tipo = (tipoChoice == 0) ? "F" : "J";
        String cpf = null;
        String cnpj = null;

        try {
            if (tipo.equalsIgnoreCase("F")) {
                cpf = JOptionPane.showInputDialog("CPF:");
                if (cpf == null) return;
                Cliente cliente = new Cliente(null, nome, endereco, telefone, cpf);
                clienteUseCases.criarCliente(cliente);
                JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso! ID: " + cliente.getId());
            } else if (tipo.equalsIgnoreCase("J")) {
                cnpj = JOptionPane.showInputDialog("CNPJ:");
                if (cnpj == null) return;
                Cliente cliente = new Cliente(nome, endereco, telefone, cnpj, null);
                clienteUseCases.criarCliente(cliente);
                JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso! ID: " + cliente.getId());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarClientePorId() {
        String idStr = JOptionPane.showInputDialog("ID do Cliente:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            clienteUseCases.buscarClientePorId(id).ifPresentOrElse(
                    c -> {
                        String info = """
                                --- Dados do Cliente ---
                                ID: %d
                                Nome: %s
                                Endereço: %s
                                Telefone: %s
                                Documento: %s
                                """.formatted(c.getId(), c.getNome(), c.getEndereco(),
                                FormatUtils.formatTelefone(c.getTelefone()),
                                c.isPessoaFisica() ? FormatUtils.formatCPF(c.getCpf()) : FormatUtils.formatCNPJ(c.getCnpj()));
                        JOptionPane.showMessageDialog(null, info);
                    },
                    () -> JOptionPane.showMessageDialog(null, "Cliente não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE)
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarTodosClientes() {
        List<Cliente> clientes = clienteUseCases.listarTodosClientes();
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum cliente cadastrado.");
        } else {
            StringBuilder sb = new StringBuilder("--- Lista de Clientes ---\n");
            for (Cliente c : clientes) {
                sb.append("-------------------------\n");
                sb.append("ID: ").append(c.getId()).append("\n");
                sb.append("Nome: ").append(c.getNome()).append("\n");
                sb.append("Telefone: ").append(FormatUtils.formatTelefone(c.getTelefone())).append("\n");
                sb.append("Documento: ").append(c.isPessoaFisica() ? FormatUtils.formatCPF(c.getDocumento()) : FormatUtils.formatCNPJ(c.getDocumento())).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 500));
            JOptionPane.showMessageDialog(null, scrollPane, "Lista de Clientes", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void atualizarCliente() {
        String idStr = JOptionPane.showInputDialog("ID do cliente a ser atualizado:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            Optional<Cliente> clienteOpt = clienteUseCases.buscarClientePorId(id);
            if (clienteOpt.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Erro: Cliente não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente cliente = clienteOpt.get();
            String nome = JOptionPane.showInputDialog("Novo Nome:", cliente.getNome());
            if (nome == null) return;
            String endereco = JOptionPane.showInputDialog("Novo Endereço:", cliente.getEndereco());
            if (endereco == null) return;
            String telefone = JOptionPane.showInputDialog("Novo Telefone:", cliente.getTelefone());
            if (telefone == null) return;

            cliente.setNome(nome);
            cliente.setEndereco(endereco);
            cliente.setTelefone(telefone);

            clienteUseCases.atualizarCliente(cliente);
            JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar cliente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarCliente() {
        String idStr = JOptionPane.showInputDialog("ID do cliente a ser deletado:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            if (clienteUseCases.buscarClientePorId(id).isEmpty()) {
                JOptionPane.showMessageDialog(null, "Erro: Cliente não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar este cliente?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            clienteUseCases.deletarCliente(id);
            JOptionPane.showMessageDialog(null, "Cliente deletado com sucesso.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro de Deleção", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro inesperado ao deletar o cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
