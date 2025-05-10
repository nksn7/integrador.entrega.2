package br.com.unifacisa.projetointegrador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fornecedor.
 */
@Entity
@Table(name = "fornecedor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fornecedor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @Column(name = "nome", length = 100)
    private String nome;

    @Size(max = 100)
    @Column(name = "razao_social", length = 100)
    private String razaoSocial;

    @Size(max = 11)
    @Column(name = "cpf", length = 11, unique = true)
    private String cpf;

    @Size(max = 14)
    @Column(name = "cnpj", length = 14, unique = true)
    private String cnpj;

    @NotNull
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @NotNull
    @Size(max = 13)
    @Column(name = "telefone", length = 13, nullable = false)
    private String telefone;

    @Size(max = 100)
    @Column(name = "condicao_pagamento", length = 100)
    private String condicaoPagamento;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fornecedor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cliente", "fornecedor" }, allowSetters = true)
    private Set<Endereco> enderecos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fornecedor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Fornecedor nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazaoSocial() {
        return this.razaoSocial;
    }

    public Fornecedor razaoSocial(String razaoSocial) {
        this.setRazaoSocial(razaoSocial);
        return this;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Fornecedor cpf(String cpf) {
        this.setCpf(cpf);
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public Fornecedor cnpj(String cnpj) {
        this.setCnpj(cnpj);
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return this.email;
    }

    public Fornecedor email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Fornecedor telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCondicaoPagamento() {
        return this.condicaoPagamento;
    }

    public Fornecedor condicaoPagamento(String condicaoPagamento) {
        this.setCondicaoPagamento(condicaoPagamento);
        return this;
    }

    public void setCondicaoPagamento(String condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public Fornecedor ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataCadastro() {
        return this.dataCadastro;
    }

    public Fornecedor dataCadastro(LocalDate dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Set<Endereco> getEnderecos() {
        return this.enderecos;
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        if (this.enderecos != null) {
            this.enderecos.forEach(i -> i.setFornecedor(null));
        }
        if (enderecos != null) {
            enderecos.forEach(i -> i.setFornecedor(this));
        }
        this.enderecos = enderecos;
    }

    public Fornecedor enderecos(Set<Endereco> enderecos) {
        this.setEnderecos(enderecos);
        return this;
    }

    public Fornecedor addEnderecos(Endereco endereco) {
        this.enderecos.add(endereco);
        endereco.setFornecedor(this);
        return this;
    }

    public Fornecedor removeEnderecos(Endereco endereco) {
        this.enderecos.remove(endereco);
        endereco.setFornecedor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fornecedor)) {
            return false;
        }
        return getId() != null && getId().equals(((Fornecedor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fornecedor{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", razaoSocial='" + getRazaoSocial() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", condicaoPagamento='" + getCondicaoPagamento() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            "}";
    }
}
