package br.com.unifacisa.projetointegrador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "login", length = 100, nullable = false, unique = true)
    private String login;

    @NotNull
    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "ultimo_acesso")
    private LocalDate ultimoAcesso;

    @JsonIgnoreProperties(value = { "usuario" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "usuario")
    private Funcionario funcionario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public Usuario login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenhaHash() {
        return this.senhaHash;
    }

    public Usuario senhaHash(String senhaHash) {
        this.setSenhaHash(senhaHash);
        return this;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public Usuario ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getUltimoAcesso() {
        return this.ultimoAcesso;
    }

    public Usuario ultimoAcesso(LocalDate ultimoAcesso) {
        this.setUltimoAcesso(ultimoAcesso);
        return this;
    }

    public void setUltimoAcesso(LocalDate ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public Funcionario getFuncionario() {
        return this.funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        if (this.funcionario != null) {
            this.funcionario.setUsuario(null);
        }
        if (funcionario != null) {
            funcionario.setUsuario(this);
        }
        this.funcionario = funcionario;
    }

    public Usuario funcionario(Funcionario funcionario) {
        this.setFuncionario(funcionario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return getId() != null && getId().equals(((Usuario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", senhaHash='" + getSenhaHash() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", ultimoAcesso='" + getUltimoAcesso() + "'" +
            "}";
    }
}
