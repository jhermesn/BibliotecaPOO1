package presentation.controllers;

import application.usecases.ClienteUseCases;
import application.usecases.CompraUseCases;
import application.usecases.LivroUseCases;
import domain.entities.Cliente;
import domain.entities.Compra;
import domain.entities.Livro;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CompraController {
    private final CompraUseCases compraUseCases;
    private final ClienteUseCases clienteUseCases;
    private final LivroUseCases livroUseCases;

    public CompraController(CompraUseCases compraUseCases, ClienteUseCases clienteUseCases, LivroUseCases livroUseCases) {
        this.compraUseCases = compraUseCases;
        this.clienteUseCases = clienteUseCases;
        this.livroUseCases = livroUseCases;
    }

    public void menu() {
        String[] options = {
                "Realizar Compra",
                "Buscar Compra por ID",
                "Listar Todas as Compras",
                "Listar Compras por Cliente",
                "Voltar ao Menu Principal"
        };

        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Selecione uma opção:",
                    "Gerenciar Compras",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == JOptionPane.CLOSED_OPTION || choice == 4) { // Voltar
                return;
            }

            switch (choice) {
                case 0 -> realizarCompra();
                case 1 -> buscarCompraPorId();
                case 2 -> listarTodasCompras();
                case 3 -> listarComprasPorCliente();
            }
        }
    }

    private void realizarCompra() {
        List<Cliente> clientes = clienteUseCases.listarTodosClientes();
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Erro: Nenhum cliente cadastrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Livro> livros = livroUseCases.listarTodosLivros();
        if (livros.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Erro: Nenhum livro cadastrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = selectClienteDialog(clientes);
        if (cliente == null) return;

        Livro livro = selectLivroDialog(livros);
        if (livro == null) return;

        int quantidade;
        try {
            String qtdStr = JOptionPane.showInputDialog("Quantidade desejada (Estoque atual: " + livro.getEstoque() + "):");
            if (qtdStr == null) return;
            quantidade = Integer.parseInt(qtdStr);
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(null, "Erro: A quantidade deve ser positiva.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (livro.getEstoque() < quantidade) {
                JOptionPane.showMessageDialog(null, "Erro: Estoque insuficiente.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal preco;
        try {
            String precoStr = JOptionPane.showInputDialog("Preço Unitário:");
            if (precoStr == null) return;
            preco = new BigDecimal(precoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Preço inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Compra compra = new Compra(null, cliente, livro, new Date(), quantidade, preco);
            compraUseCases.realizarCompra(compra);
            JOptionPane.showMessageDialog(null, "Compra realizada com sucesso! ID da Compra: " + compra.getId() +
                    "\nNovo estoque do livro '" + livro.getNome() + "': " + livro.getEstoque());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao processar a compra: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarCompraPorId() {
        String idStr = JOptionPane.showInputDialog("ID da Compra:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            compraUseCases.buscarCompraPorId(id).ifPresentOrElse(
                    c -> {
                        String info = """
                                --- Dados da Compra ---
                                ID: %d
                                Data: %s
                                Cliente: %s
                                Livro: %s
                                Quantidade: %d
                                Valor Total: %s
                                """.formatted(c.getId(), c.getData_compra(), c.getCliente().getNome(), c.getLivro().getNome(), c.getQuantidade(), c.getValorTotal());
                        JOptionPane.showMessageDialog(null, info, "Dados da Compra", JOptionPane.INFORMATION_MESSAGE);
                    },
                    () -> JOptionPane.showMessageDialog(null, "Compra não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE)
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarTodasCompras() {
        List<Compra> compras = compraUseCases.listarTodasCompras();
        if (compras.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma compra foi realizada.");
        } else {
            StringBuilder sb = new StringBuilder("--- Histórico de Compras ---\n");
            for (Compra c : compras) {
                sb.append("-------------------------\n");
                sb.append("ID: ").append(c.getId()).append("\n");
                sb.append("Data: ").append(c.getData_compra()).append("\n");
                sb.append("Cliente: ").append(c.getCliente().getNome()).append("\n");
                sb.append("Livro: ").append(c.getLivro().getNome()).append("\n");
                sb.append("Qtd: ").append(c.getQuantidade()).append("\n");
                sb.append("Valor Total: ").append(c.getValorTotal()).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(500, 500));
            JOptionPane.showMessageDialog(null, scrollPane, "Histórico de Compras", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void listarComprasPorCliente() {
        Cliente cliente = selectClienteDialog(clienteUseCases.listarTodosClientes());
        if (cliente == null) return;

        List<Compra> compras = compraUseCases.listarComprasPorCliente(cliente.getId());
        if (compras.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma compra encontrada para este cliente.");
        } else {
            StringBuilder sb = new StringBuilder("--- Compras do Cliente: " + cliente.getNome() + " ---\n");
            for (Compra c : compras) {
                sb.append("-------------------------\n");
                sb.append("ID Compra: ").append(c.getId()).append("\n");
                sb.append("Data: ").append(c.getData_compra()).append("\n");
                sb.append("Livro: ").append(c.getLivro().getNome()).append("\n");
                sb.append("Qtd: ").append(c.getQuantidade()).append("\n");
                sb.append("Valor Total: ").append(c.getValorTotal()).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(500, 500));
            JOptionPane.showMessageDialog(null, scrollPane, "Compras do Cliente", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Cliente selectClienteDialog(List<Cliente> clientes) {
        String[] clientesArray = clientes.stream()
                .map(c -> c.getId() + ": " + c.getNome())
                .toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(null, "Selecione o Cliente",
                "Clientes", JOptionPane.QUESTION_MESSAGE, null, clientesArray, clientesArray[0]);

        if (selected == null) {
            return null;
        }

        int clienteId = Integer.parseInt(selected.split(":")[0]);
        return clientes.stream().filter(c -> c.getId() == clienteId).findFirst().orElse(null);
    }

    private Livro selectLivroDialog(List<Livro> livros) {
        String[] livrosArray = livros.stream()
                .map(l -> l.getIsbn() + ": " + l.getNome() + " (Estoque: " + l.getEstoque() + ")")
                .toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(null, "Selecione o Livro",
                "Livros", JOptionPane.QUESTION_MESSAGE, null, livrosArray, livrosArray[0]);

        if (selected == null) {
            return null;
        }

        String isbn = selected.split(":")[0];
        return livros.stream().filter(l -> l.getIsbn().equals(isbn)).findFirst().orElse(null);
    }
}
