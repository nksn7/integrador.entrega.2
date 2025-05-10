package br.com.unifacisa.projetointegrador.web.rest;

import static br.com.unifacisa.projetointegrador.domain.FuncionarioAsserts.*;
import static br.com.unifacisa.projetointegrador.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.unifacisa.projetointegrador.IntegrationTest;
import br.com.unifacisa.projetointegrador.domain.Funcionario;
import br.com.unifacisa.projetointegrador.domain.enumeration.Cargo;
import br.com.unifacisa.projetointegrador.repository.FuncionarioRepository;
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
 * Integration tests for the {@link FuncionarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FuncionarioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final Cargo DEFAULT_CARGO = Cargo.GERENTE;
    private static final Cargo UPDATED_CARGO = Cargo.VENDEDOR;

    private static final LocalDate DEFAULT_DATA_ADMISSAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_ADMISSAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_DESLIGAMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DESLIGAMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_SALARIO = 1F;
    private static final Float UPDATED_SALARIO = 2F;

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/funcionarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFuncionarioMockMvc;

    private Funcionario funcionario;

    private Funcionario insertedFuncionario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcionario createEntity() {
        return new Funcionario()
            .nome(DEFAULT_NOME)
            .cpf(DEFAULT_CPF)
            .email(DEFAULT_EMAIL)
            .telefone(DEFAULT_TELEFONE)
            .cargo(DEFAULT_CARGO)
            .dataAdmissao(DEFAULT_DATA_ADMISSAO)
            .dataDesligamento(DEFAULT_DATA_DESLIGAMENTO)
            .salario(DEFAULT_SALARIO)
            .ativo(DEFAULT_ATIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcionario createUpdatedEntity() {
        return new Funcionario()
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .cargo(UPDATED_CARGO)
            .dataAdmissao(UPDATED_DATA_ADMISSAO)
            .dataDesligamento(UPDATED_DATA_DESLIGAMENTO)
            .salario(UPDATED_SALARIO)
            .ativo(UPDATED_ATIVO);
    }

    @BeforeEach
    void initTest() {
        funcionario = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFuncionario != null) {
            funcionarioRepository.delete(insertedFuncionario);
            insertedFuncionario = null;
        }
    }

    @Test
    @Transactional
    void createFuncionario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Funcionario
        var returnedFuncionario = om.readValue(
            restFuncionarioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(funcionario)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Funcionario.class
        );

        // Validate the Funcionario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFuncionarioUpdatableFieldsEquals(returnedFuncionario, getPersistedFuncionario(returnedFuncionario));

        insertedFuncionario = returnedFuncionario;
    }

    @Test
    @Transactional
    void createFuncionarioWithExistingId() throws Exception {
        // Create the Funcionario with an existing ID
        funcionario.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFuncionarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(funcionario)))
            .andExpect(status().isBadRequest());

        // Validate the Funcionario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCpfIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        funcionario.setCpf(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(funcionario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        funcionario.setEmail(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(funcionario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        funcionario.setTelefone(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(funcionario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCargoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        funcionario.setCargo(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(funcionario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataAdmissaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        funcionario.setDataAdmissao(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(funcionario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSalarioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        funcionario.setSalario(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(funcionario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        funcionario.setAtivo(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(funcionario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFuncionarios() throws Exception {
        // Initialize the database
        insertedFuncionario = funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarioList
        restFuncionarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcionario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO.toString())))
            .andExpect(jsonPath("$.[*].dataAdmissao").value(hasItem(DEFAULT_DATA_ADMISSAO.toString())))
            .andExpect(jsonPath("$.[*].dataDesligamento").value(hasItem(DEFAULT_DATA_DESLIGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].salario").value(hasItem(DEFAULT_SALARIO.doubleValue())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));
    }

    @Test
    @Transactional
    void getFuncionario() throws Exception {
        // Initialize the database
        insertedFuncionario = funcionarioRepository.saveAndFlush(funcionario);

        // Get the funcionario
        restFuncionarioMockMvc
            .perform(get(ENTITY_API_URL_ID, funcionario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(funcionario.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.cargo").value(DEFAULT_CARGO.toString()))
            .andExpect(jsonPath("$.dataAdmissao").value(DEFAULT_DATA_ADMISSAO.toString()))
            .andExpect(jsonPath("$.dataDesligamento").value(DEFAULT_DATA_DESLIGAMENTO.toString()))
            .andExpect(jsonPath("$.salario").value(DEFAULT_SALARIO.doubleValue()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO));
    }

    @Test
    @Transactional
    void getNonExistingFuncionario() throws Exception {
        // Get the funcionario
        restFuncionarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFuncionario() throws Exception {
        // Initialize the database
        insertedFuncionario = funcionarioRepository.saveAndFlush(funcionario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the funcionario
        Funcionario updatedFuncionario = funcionarioRepository.findById(funcionario.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFuncionario are not directly saved in db
        em.detach(updatedFuncionario);
        updatedFuncionario
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .cargo(UPDATED_CARGO)
            .dataAdmissao(UPDATED_DATA_ADMISSAO)
            .dataDesligamento(UPDATED_DATA_DESLIGAMENTO)
            .salario(UPDATED_SALARIO)
            .ativo(UPDATED_ATIVO);

        restFuncionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFuncionario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFuncionario))
            )
            .andExpect(status().isOk());

        // Validate the Funcionario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFuncionarioToMatchAllProperties(updatedFuncionario);
    }

    @Test
    @Transactional
    void putNonExistingFuncionario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        funcionario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, funcionario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(funcionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFuncionario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        funcionario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(funcionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFuncionario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        funcionario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(funcionario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funcionario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFuncionarioWithPatch() throws Exception {
        // Initialize the database
        insertedFuncionario = funcionarioRepository.saveAndFlush(funcionario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the funcionario using partial update
        Funcionario partialUpdatedFuncionario = new Funcionario();
        partialUpdatedFuncionario.setId(funcionario.getId());

        partialUpdatedFuncionario
            .cargo(UPDATED_CARGO)
            .dataDesligamento(UPDATED_DATA_DESLIGAMENTO)
            .salario(UPDATED_SALARIO)
            .ativo(UPDATED_ATIVO);

        restFuncionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFuncionario))
            )
            .andExpect(status().isOk());

        // Validate the Funcionario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFuncionarioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFuncionario, funcionario),
            getPersistedFuncionario(funcionario)
        );
    }

    @Test
    @Transactional
    void fullUpdateFuncionarioWithPatch() throws Exception {
        // Initialize the database
        insertedFuncionario = funcionarioRepository.saveAndFlush(funcionario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the funcionario using partial update
        Funcionario partialUpdatedFuncionario = new Funcionario();
        partialUpdatedFuncionario.setId(funcionario.getId());

        partialUpdatedFuncionario
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE)
            .cargo(UPDATED_CARGO)
            .dataAdmissao(UPDATED_DATA_ADMISSAO)
            .dataDesligamento(UPDATED_DATA_DESLIGAMENTO)
            .salario(UPDATED_SALARIO)
            .ativo(UPDATED_ATIVO);

        restFuncionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFuncionario))
            )
            .andExpect(status().isOk());

        // Validate the Funcionario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFuncionarioUpdatableFieldsEquals(partialUpdatedFuncionario, getPersistedFuncionario(partialUpdatedFuncionario));
    }

    @Test
    @Transactional
    void patchNonExistingFuncionario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        funcionario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, funcionario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(funcionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFuncionario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        funcionario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(funcionario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Funcionario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFuncionario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        funcionario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncionarioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(funcionario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Funcionario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFuncionario() throws Exception {
        // Initialize the database
        insertedFuncionario = funcionarioRepository.saveAndFlush(funcionario);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the funcionario
        restFuncionarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, funcionario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return funcionarioRepository.count();
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

    protected Funcionario getPersistedFuncionario(Funcionario funcionario) {
        return funcionarioRepository.findById(funcionario.getId()).orElseThrow();
    }

    protected void assertPersistedFuncionarioToMatchAllProperties(Funcionario expectedFuncionario) {
        assertFuncionarioAllPropertiesEquals(expectedFuncionario, getPersistedFuncionario(expectedFuncionario));
    }

    protected void assertPersistedFuncionarioToMatchUpdatableProperties(Funcionario expectedFuncionario) {
        assertFuncionarioAllUpdatablePropertiesEquals(expectedFuncionario, getPersistedFuncionario(expectedFuncionario));
    }
}
