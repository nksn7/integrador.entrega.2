package br.com.unifacisa.projetointegrador.web.rest;

import static br.com.unifacisa.projetointegrador.domain.FornecedorAsserts.*;
import static br.com.unifacisa.projetointegrador.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.unifacisa.projetointegrador.IntegrationTest;
import br.com.unifacisa.projetointegrador.domain.Fornecedor;
import br.com.unifacisa.projetointegrador.repository.FornecedorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FornecedorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FornecedorResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_RAZAO_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZAO_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_CONDICAO_PAGAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_CONDICAO_PAGAMENTO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final LocalDate DEFAULT_DATA_CADASTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CADASTRO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/fornecedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFornecedorMockMvc;

    private Fornecedor fornecedor;

    private Fornecedor insertedFornecedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fornecedor createEntity() {
        return new Fornecedor()
            .nome(DEFAULT_NOME)
            .razaoSocial(DEFAULT_RAZAO_SOCIAL)
            .cpf(DEFAULT_CPF)
            .cnpj(DEFAULT_CNPJ)
            .email(DEFAULT_EMAIL)
            .telefone(DEFAULT_TELEFONE)
            .condicaoPagamento(DEFAULT_CONDICAO_PAGAMENTO)
            .ativo(DEFAULT_ATIVO)
            .dataCadastro(DEFAULT_DATA_CADASTRO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fornecedor createUpdatedEntity() {
        return new Fornecedor()
            .nome(UPDATED_NOME)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .cpf(UPDATED_CPF)
            .cnpj(UPDATED_CNPJ)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .condicaoPagamento(UPDATED_CONDICAO_PAGAMENTO)
            .ativo(UPDATED_ATIVO)
            .dataCadastro(UPDATED_DATA_CADASTRO);
    }

    @BeforeEach
    void initTest() {
        fornecedor = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFornecedor != null) {
            fornecedorRepository.delete(insertedFornecedor);
            insertedFornecedor = null;
        }
    }

    @Test
    @Transactional
    void createFornecedor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Fornecedor
        var returnedFornecedor = om.readValue(
            restFornecedorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Fornecedor.class
        );

        // Validate the Fornecedor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFornecedorUpdatableFieldsEquals(returnedFornecedor, getPersistedFornecedor(returnedFornecedor));

        insertedFornecedor = returnedFornecedor;
    }

    @Test
    @Transactional
    void createFornecedorWithExistingId() throws Exception {
        // Create the Fornecedor with an existing ID
        fornecedor.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFornecedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor)))
            .andExpect(status().isBadRequest());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fornecedor.setEmail(null);

        // Create the Fornecedor, which fails.

        restFornecedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fornecedor.setTelefone(null);

        // Create the Fornecedor, which fails.

        restFornecedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fornecedor.setAtivo(null);

        // Create the Fornecedor, which fails.

        restFornecedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFornecedors() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.saveAndFlush(fornecedor);

        // Get all the fornecedorList
        restFornecedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fornecedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].condicaoPagamento").value(hasItem(DEFAULT_CONDICAO_PAGAMENTO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(DEFAULT_DATA_CADASTRO.toString())));
    }

    @Test
    @Transactional
    void getFornecedor() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.saveAndFlush(fornecedor);

        // Get the fornecedor
        restFornecedorMockMvc
            .perform(get(ENTITY_API_URL_ID, fornecedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fornecedor.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.razaoSocial").value(DEFAULT_RAZAO_SOCIAL))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.condicaoPagamento").value(DEFAULT_CONDICAO_PAGAMENTO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO))
            .andExpect(jsonPath("$.dataCadastro").value(DEFAULT_DATA_CADASTRO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFornecedor() throws Exception {
        // Get the fornecedor
        restFornecedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFornecedor() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.saveAndFlush(fornecedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fornecedor
        Fornecedor updatedFornecedor = fornecedorRepository.findById(fornecedor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFornecedor are not directly saved in db
        em.detach(updatedFornecedor);
        updatedFornecedor
            .nome(UPDATED_NOME)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .cpf(UPDATED_CPF)
            .cnpj(UPDATED_CNPJ)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .condicaoPagamento(UPDATED_CONDICAO_PAGAMENTO)
            .ativo(UPDATED_ATIVO)
            .dataCadastro(UPDATED_DATA_CADASTRO);

        restFornecedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFornecedor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFornecedor))
            )
            .andExpect(status().isOk());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFornecedorToMatchAllProperties(updatedFornecedor);
    }

    @Test
    @Transactional
    void putNonExistingFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fornecedor.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fornecedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFornecedorWithPatch() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.saveAndFlush(fornecedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fornecedor using partial update
        Fornecedor partialUpdatedFornecedor = new Fornecedor();
        partialUpdatedFornecedor.setId(fornecedor.getId());

        partialUpdatedFornecedor
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .condicaoPagamento(UPDATED_CONDICAO_PAGAMENTO)
            .dataCadastro(UPDATED_DATA_CADASTRO);

        restFornecedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFornecedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFornecedor))
            )
            .andExpect(status().isOk());

        // Validate the Fornecedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFornecedorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFornecedor, fornecedor),
            getPersistedFornecedor(fornecedor)
        );
    }

    @Test
    @Transactional
    void fullUpdateFornecedorWithPatch() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.saveAndFlush(fornecedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fornecedor using partial update
        Fornecedor partialUpdatedFornecedor = new Fornecedor();
        partialUpdatedFornecedor.setId(fornecedor.getId());

        partialUpdatedFornecedor
            .nome(UPDATED_NOME)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .cpf(UPDATED_CPF)
            .cnpj(UPDATED_CNPJ)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .condicaoPagamento(UPDATED_CONDICAO_PAGAMENTO)
            .ativo(UPDATED_ATIVO)
            .dataCadastro(UPDATED_DATA_CADASTRO);

        restFornecedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFornecedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFornecedor))
            )
            .andExpect(status().isOk());

        // Validate the Fornecedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFornecedorUpdatableFieldsEquals(partialUpdatedFornecedor, getPersistedFornecedor(partialUpdatedFornecedor));
    }

    @Test
    @Transactional
    void patchNonExistingFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fornecedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fornecedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fornecedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fornecedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFornecedor() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.saveAndFlush(fornecedor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fornecedor
        restFornecedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, fornecedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fornecedorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Fornecedor getPersistedFornecedor(Fornecedor fornecedor) {
        return fornecedorRepository.findById(fornecedor.getId()).orElseThrow();
    }

    protected void assertPersistedFornecedorToMatchAllProperties(Fornecedor expectedFornecedor) {
        assertFornecedorAllPropertiesEquals(expectedFornecedor, getPersistedFornecedor(expectedFornecedor));
    }

    protected void assertPersistedFornecedorToMatchUpdatableProperties(Fornecedor expectedFornecedor) {
        assertFornecedorAllUpdatablePropertiesEquals(expectedFornecedor, getPersistedFornecedor(expectedFornecedor));
    }
}
