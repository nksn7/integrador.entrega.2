package br.com.unifacisa.projetointegrador.domain;

import br.com.unifacisa.projetointegrador.domain.enumeration.EstatusVenda;
import br.com.unifacisa.projetointegrador.domain.enumeration.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Venda.
 */
@Entity
@Table(name = "venda")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Venda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_venda", nullable = false)
    private LocalDate dataVenda;

    @NotNull
    @Column(name = "valor_total", nullable = false)
    private Float valorTotal;

    @NotNull
    @Column(name = "desconto_total", nullable = false)
    private Float descontoTotal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @NotNull
    @Column(name = "parcelas", nullable = false)
    private Integer parcelas;

    @NotNull
    @Column(name = "nota_fiscal_emitida", nullable = false)
    private Boolean notaFiscalEmitida;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = false)
    private EstatusVenda estatus;

    @Size(max = 500)
    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "enderecos" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "usuario" }, allowSetters = true)
    private Funcionario funcionario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venda id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataVenda() {
        return this.dataVenda;
    }

    public Venda dataVenda(LocalDate dataVenda) {
        this.setDataVenda(dataVenda);
        return this;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Float getValorTotal() {
        return this.valorTotal;
    }

    public Venda valorTotal(Float valorTotal) {
        this.setValorTotal(valorTotal);
        return this;
    }

    public void setValorTotal(Float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Float getDescontoTotal() {
        return this.descontoTotal;
    }

    public Venda descontoTotal(Float descontoTotal) {
        this.setDescontoTotal(descontoTotal);
        return this;
    }

    public void setDescontoTotal(Float descontoTotal) {
        this.descontoTotal = descontoTotal;
    }

    public FormaPagamento getFormaPagamento() {
        return this.formaPagamento;
    }

    public Venda formaPagamento(FormaPagamento formaPagamento) {
        this.setFormaPagamento(formaPagamento);
        return this;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Integer getParcelas() {
        return this.parcelas;
    }

    public Venda parcelas(Integer parcelas) {
        this.setParcelas(parcelas);
        return this;
    }

    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }

    public Boolean getNotaFiscalEmitida() {
        return this.notaFiscalEmitida;
    }

    public Venda notaFiscalEmitida(Boolean notaFiscalEmitida) {
        this.setNotaFiscalEmitida(notaFiscalEmitida);
        return this;
    }

    public void setNotaFiscalEmitida(Boolean notaFiscalEmitida) {
        this.notaFiscalEmitida = notaFiscalEmitida;
    }

    public EstatusVenda getEstatus() {
        return this.estatus;
    }

    public Venda estatus(EstatusVenda estatus) {
        this.setEstatus(estatus);
        return this;
    }

    public void setEstatus(EstatusVenda estatus) {
        this.estatus = estatus;
    }

    public String getObservacoes() {
        return this.observacoes;
    }

    public Venda observacoes(String observacoes) {
        this.setObservacoes(observacoes);
        return this;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Venda cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Funcionario getFuncionario() {
        return this.funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Venda funcionario(Funcionario funcionario) {
        this.setFuncionario(funcionario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venda)) {
            return false;
        }
        return getId() != null && getId().equals(((Venda) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venda{" +
            "id=" + getId() +
            ", dataVenda='" + getDataVenda() + "'" +
            ", valorTotal=" + getValorTotal() +
            ", descontoTotal=" + getDescontoTotal() +
            ", formaPagamento='" + getFormaPagamento() + "'" +
            ", parcelas=" + getParcelas() +
            ", notaFiscalEmitida='" + getNotaFiscalEmitida() + "'" +
            ", estatus='" + getEstatus() + "'" +
            ", observacoes='" + getObservacoes() + "'" +
            "}";
    }
}
