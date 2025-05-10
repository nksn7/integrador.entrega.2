package br.com.unifacisa.projetointegrador.domain;

import br.com.unifacisa.projetointegrador.domain.enumeration.UnidadeMedida;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Produto.
 */
@Entity
@Table(name = "produto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotNull
    @Size(max = 50)
    @Column(name = "codigo_barras", length = 50, nullable = false)
    private String codigoBarras;

    @Size(max = 50)
    @Column(name = "categoria", length = 50)
    private String categoria;

    @NotNull
    @Column(name = "custo_aquisicao", nullable = false)
    private Float custoAquisicao;

    @NotNull
    @Column(name = "preco_venda", nullable = false)
    private Float precoVenda;

    @Column(name = "quantidade_estoque")
    private Integer quantidadeEstoque;

    @Column(name = "estoque_minimo")
    private Integer estoqueMinimo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unidade_medida", nullable = false)
    private UnidadeMedida unidadeMedida;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Produto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Produto nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Produto descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoBarras() {
        return this.codigoBarras;
    }

    public Produto codigoBarras(String codigoBarras) {
        this.setCodigoBarras(codigoBarras);
        return this;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public Produto categoria(String categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Float getCustoAquisicao() {
        return this.custoAquisicao;
    }

    public Produto custoAquisicao(Float custoAquisicao) {
        this.setCustoAquisicao(custoAquisicao);
        return this;
    }

    public void setCustoAquisicao(Float custoAquisicao) {
        this.custoAquisicao = custoAquisicao;
    }

    public Float getPrecoVenda() {
        return this.precoVenda;
    }

    public Produto precoVenda(Float precoVenda) {
        this.setPrecoVenda(precoVenda);
        return this;
    }

    public void setPrecoVenda(Float precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Integer getQuantidadeEstoque() {
        return this.quantidadeEstoque;
    }

    public Produto quantidadeEstoque(Integer quantidadeEstoque) {
        this.setQuantidadeEstoque(quantidadeEstoque);
        return this;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Integer getEstoqueMinimo() {
        return this.estoqueMinimo;
    }

    public Produto estoqueMinimo(Integer estoqueMinimo) {
        this.setEstoqueMinimo(estoqueMinimo);
        return this;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public UnidadeMedida getUnidadeMedida() {
        return this.unidadeMedida;
    }

    public Produto unidadeMedida(UnidadeMedida unidadeMedida) {
        this.setUnidadeMedida(unidadeMedida);
        return this;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public LocalDate getDataCadastro() {
        return this.dataCadastro;
    }

    public Produto dataCadastro(LocalDate dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Produto)) {
            return false;
        }
        return getId() != null && getId().equals(((Produto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Produto{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", codigoBarras='" + getCodigoBarras() + "'" +
            ", categoria='" + getCategoria() + "'" +
            ", custoAquisicao=" + getCustoAquisicao() +
            ", precoVenda=" + getPrecoVenda() +
            ", quantidadeEstoque=" + getQuantidadeEstoque() +
            ", estoqueMinimo=" + getEstoqueMinimo() +
            ", unidadeMedida='" + getUnidadeMedida() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            "}";
    }
}
