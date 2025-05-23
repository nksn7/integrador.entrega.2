package br.com.unifacisa.projetointegrador.web.rest;

import br.com.unifacisa.projetointegrador.domain.Fornecedor;
import br.com.unifacisa.projetointegrador.repository.FornecedorRepository;
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
 * REST controller for managing {@link br.com.unifacisa.projetointegrador.domain.Fornecedor}.
 */
@RestController
@RequestMapping("/api/fornecedors")
@Transactional
public class FornecedorResource {

    private static final Logger LOG = LoggerFactory.getLogger(FornecedorResource.class);

    private static final String ENTITY_NAME = "fornecedor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FornecedorRepository fornecedorRepository;

    public FornecedorResource(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    /**
     * {@code POST  /fornecedors} : Create a new fornecedor.
     *
     * @param fornecedor the fornecedor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fornecedor, or with status {@code 400 (Bad Request)} if the fornecedor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Fornecedor> createFornecedor(@Valid @RequestBody Fornecedor fornecedor) throws URISyntaxException {
        LOG.debug("REST request to save Fornecedor : {}", fornecedor);
        if (fornecedor.getId() != null) {
            throw new BadRequestAlertException("A new fornecedor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fornecedor = fornecedorRepository.save(fornecedor);
        return ResponseEntity.created(new URI("/api/fornecedors/" + fornecedor.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, fornecedor.getId().toString()))
            .body(fornecedor);
    }

    /**
     * {@code PUT  /fornecedors/:id} : Updates an existing fornecedor.
     *
     * @param id the id of the fornecedor to save.
     * @param fornecedor the fornecedor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fornecedor,
     * or with status {@code 400 (Bad Request)} if the fornecedor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fornecedor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> updateFornecedor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Fornecedor fornecedor
    ) throws URISyntaxException {
        LOG.debug("REST request to update Fornecedor : {}, {}", id, fornecedor);
        if (fornecedor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fornecedor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fornecedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fornecedor = fornecedorRepository.save(fornecedor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fornecedor.getId().toString()))
            .body(fornecedor);
    }

    /**
     * {@code PATCH  /fornecedors/:id} : Partial updates given fields of an existing fornecedor, field will ignore if it is null
     *
     * @param id the id of the fornecedor to save.
     * @param fornecedor the fornecedor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fornecedor,
     * or with status {@code 400 (Bad Request)} if the fornecedor is not valid,
     * or with status {@code 404 (Not Found)} if the fornecedor is not found,
     * or with status {@code 500 (Internal Server Error)} if the fornecedor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Fornecedor> partialUpdateFornecedor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Fornecedor fornecedor
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Fornecedor partially : {}, {}", id, fornecedor);
        if (fornecedor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fornecedor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fornecedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fornecedor> result = fornecedorRepository
            .findById(fornecedor.getId())
            .map(existingFornecedor -> {
                if (fornecedor.getNome() != null) {
                    existingFornecedor.setNome(fornecedor.getNome());
                }
                if (fornecedor.getRazaoSocial() != null) {
                    existingFornecedor.setRazaoSocial(fornecedor.getRazaoSocial());
                }
                if (fornecedor.getCpf() != null) {
                    existingFornecedor.setCpf(fornecedor.getCpf());
                }
                if (fornecedor.getCnpj() != null) {
                    existingFornecedor.setCnpj(fornecedor.getCnpj());
                }
                if (fornecedor.getEmail() != null) {
                    existingFornecedor.setEmail(fornecedor.getEmail());
                }
                if (fornecedor.getTelefone() != null) {
                    existingFornecedor.setTelefone(fornecedor.getTelefone());
                }
                if (fornecedor.getCondicaoPagamento() != null) {
                    existingFornecedor.setCondicaoPagamento(fornecedor.getCondicaoPagamento());
                }
                if (fornecedor.getAtivo() != null) {
                    existingFornecedor.setAtivo(fornecedor.getAtivo());
                }
                if (fornecedor.getDataCadastro() != null) {
                    existingFornecedor.setDataCadastro(fornecedor.getDataCadastro());
                }

                return existingFornecedor;
            })
            .map(fornecedorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fornecedor.getId().toString())
        );
    }

    /**
     * {@code GET  /fornecedors} : get all the fornecedors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fornecedors in body.
     */
    @GetMapping("")
    public List<Fornecedor> getAllFornecedors() {
        LOG.debug("REST request to get all Fornecedors");
        return fornecedorRepository.findAll();
    }

    /**
     * {@code GET  /fornecedors/:id} : get the "id" fornecedor.
     *
     * @param id the id of the fornecedor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fornecedor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> getFornecedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Fornecedor : {}", id);
        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fornecedor);
    }

    /**
     * {@code DELETE  /fornecedors/:id} : delete the "id" fornecedor.
     *
     * @param id the id of the fornecedor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFornecedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Fornecedor : {}", id);
        fornecedorRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
