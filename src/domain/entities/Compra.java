package domain.entities;

import java.math.BigDecimal;
import java.util.Date;

public class Compra {
    private Integer id;
    private Cliente cliente;
    private Livro livro;
    private Date data_compra;
    private Integer quantidade;
    private BigDecimal preco_unitario;

    public Compra(Integer id, Cliente cliente, Livro livro, Date data_compra, Integer quantidade, BigDecimal preco_unitario) {
        this.id = id;
        this.cliente = cliente;
        this.livro = livro;
        this.data_compra = data_compra;
        this.quantidade = quantidade;
        this.preco_unitario = preco_unitario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Date getData_compra() {
        return data_compra;
    }

    public void setData_compra(Date data_compra) {
        this.data_compra = data_compra;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPreco_unitario() {
        return preco_unitario;
    }

    public void setPreco_unitario(BigDecimal preco_unitario) {
        this.preco_unitario = preco_unitario;
    }

    public BigDecimal getValorTotal() {
        if (preco_unitario == null || quantidade == null) {
            return BigDecimal.ZERO;
        }
        return preco_unitario.multiply(new BigDecimal(quantidade));
    }
}
