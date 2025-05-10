package br.com.unifacisa.projetointegrador.web.rest;

import br.com.unifacisa.projetointegrador.domain.Funcionario;
import br.com.unifacisa.projetointegrador.repository.FuncionarioRepository;
import br.com.unifacisa.projetointegrador.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.unifacisa.projetointegrador.domain.Funcionario}.
 */
@RestController
@RequestMapping("/api/funcionarios")
@Transactional
public class FuncionarioResource {

    private static final Logger LOG = LoggerFactory.getLogger(FuncionarioResource.class);

    private static final String ENTITY_NAME = "funcionario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioResource(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    /**
     * {@code POST  /funcionarios} : Create a new funcionario.
     *
     * @param funcionario the funcionario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new funcionario, or with status {@code 400 (Bad Request)} if the funcionario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Funcionario> createFuncionario(@Valid @RequestBody Funcionario funcionario) throws URISyntaxException {
        LOG.debug("REST request to save Funcionario : {}", funcionario);
        if (funcionario.getId() != null) {
            throw new BadRequestAlertException("A new funcionario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        funcionario = funcionarioRepository.save(funcionario);
        return ResponseEntity.created(new URI("/api/funcionarios/" + funcionario.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, funcionario.getId().toString()))
            .body(funcionario);
    }

    /**
     * {@code PUT  /funcionarios/:id} : Updates an existing funcionario.
     *
     * @param id the id of the funcionario to save.
     * @param funcionario the funcionario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionario,
     * or with status {@code 400 (Bad Request)} if the funcionario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the funcionario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> updateFuncionario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Funcionario funcionario
    ) throws URISyntaxException {
        LOG.debug("REST request to update Funcionario : {}, {}", id, funcionario);
        if (funcionario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funcionarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        funcionario = funcionarioRepository.save(funcionario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, funcionario.getId().toString()))
            .body(funcionario);
    }

    /**
     * {@code PATCH  /funcionarios/:id} : Partial updates given fields of an existing funcionario, field will ignore if it is null
     *
     * @param id the id of the funcionario to save.
     * @param funcionario the funcionario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionario,
     * or with status {@code 400 (Bad Request)} if the funcionario is not valid,
     * or with status {@code 404 (Not Found)} if the funcionario is not found,
     * or with status {@code 500 (Internal Server Error)} if the funcionario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Funcionario> partialUpdateFuncionario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Funcionario funcionario
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Funcionario partially : {}, {}", id, funcionario);
        if (funcionario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funcionarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Funcionario> result = funcionarioRepository
            .findById(funcionario.getId())
            .map(existingFuncionario -> {
                if (funcionario.getNome() != null) {
                    existingFuncionario.setNome(funcionario.getNome());
                }
                if (funcionario.getCpf() != null) {
                    existingFuncionario.setCpf(funcionario.getCpf());
                }
                if (funcionario.getEmail() != null) {
                    existingFuncionario.setEmail(funcionario.getEmail());
                }
                if (funcionario.getTelefone() != null) {
                    existingFuncionario.setTelefone(funcionario.getTelefone());
                }
                if (funcionario.getCargo() != null) {
                    existingFuncionario.setCargo(funcionario.getCargo());
                }
                if (funcionario.getDataAdmissao() != null) {
                    existingFuncionario.setDataAdmissao(funcionario.getDataAdmissao());
                }
                if (funcionario.getDataDesligamento() != null) {
                    existingFuncionario.setDataDesligamento(funcionario.getDataDesligamento());
                }
                if (funcionario.getSalario() != null) {
                    existingFuncionario.setSalario(funcionario.getSalario());
                }
                if (funcionario.getAtivo() != null) {
                    existingFuncionario.setAtivo(funcionario.getAtivo());
                }

                return existingFuncionario;
            })
            .map(funcionarioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, funcionario.getId().toString())
        );
    }

    /**
     * {@code GET  /funcionarios} : get all the funcionarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of funcionarios in body.
     */
    @GetMapping("")
    public List<Funcionario> getAllFuncionarios() {
        LOG.debug("REST request to get all Funcionarios");
        return funcionarioRepository.findAll();
    }

    /**
     * {@code GET  /funcionarios/:id} : get the "id" funcionario.
     *
     * @param id the id of the funcionario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the funcionario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> getFuncionario(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Funcionario : {}", id);
        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(funcionario);
    }

    /**
     * {@code DELETE  /funcionarios/:id} : delete the "id" funcionario.
     *
     * @param id the id of the funcionario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuncionario(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Funcionario : {}", id);
        funcionarioRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
