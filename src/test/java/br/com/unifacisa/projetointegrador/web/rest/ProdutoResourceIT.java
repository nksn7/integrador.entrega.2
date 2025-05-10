package br.com.unifacisa.projetointegrador.web.rest;

import static br.com.unifacisa.projetointegrador.domain.ProdutoAsserts.*;
import static br.com.unifacisa.projetointegrador.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.unifacisa.projetointegrador.IntegrationTest;
import br.com.unifacisa.projetointegrador.domain.Produto;
import br.com.unifacisa.projetointegrador.domain.enumeration.UnidadeMedida;
import br.com.unifacisa.projetointegrador.repository.ProdutoRepository;
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
 * Integration tests for the {@link ProdutoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProdutoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_BARRAS = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_BARRAS = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORIA = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIA = "BBBBBBBBBB";

    private static final Float DEFAULT_CUSTO_AQUISICAO = 1F;
    private static final Float UPDATED_CUSTO_AQUISICAO = 2F;

    private static final Float DEFAULT_PRECO_VENDA = 1F;
    private static final Float UPDATED_PRECO_VENDA = 2F;

    private static final Integer DEFAULT_QUANTIDADE_ESTOQUE = 1;
    private static final Integer UPDATED_QUANTIDADE_ESTOQUE = 2;

    private static final Integer DEFAULT_ESTOQUE_MINIMO = 1;
    private static final Integer UPDATED_ESTOQUE_MINIMO = 2;

    private static final UnidadeMedida DEFAULT_UNIDADE_MEDIDA = UnidadeMedida.UNIDADE;
    private static final UnidadeMedida UPDATED_UNIDADE_MEDIDA = UnidadeMedida.KG;

    private static final LocalDate DEFAULT_DATA_CADASTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CADASTRO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/produtos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProdutoMockMvc;

    private Produto produto;

    private Produto insertedProduto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produto createEntity() {
        return new Produto()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .codigoBarras(DEFAULT_CODIGO_BARRAS)
            .categoria(DEFAULT_CATEGORIA)
            .custoAquisicao(DEFAULT_CUSTO_AQUISICAO)
            .precoVenda(DEFAULT_PRECO_VENDA)
            .quantidadeEstoque(DEFAULT_QUANTIDADE_ESTOQUE)
            .estoqueMinimo(DEFAULT_ESTOQUE_MINIMO)
            .unidadeMedida(DEFAULT_UNIDADE_MEDIDA)
            .dataCadastro(DEFAULT_DATA_CADASTRO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produto createUpdatedEntity() {
        return new Produto()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .categoria(UPDATED_CATEGORIA)
            .custoAquisicao(UPDATED_CUSTO_AQUISICAO)
            .precoVenda(UPDATED_PRECO_VENDA)
            .quantidadeEstoque(UPDATED_QUANTIDADE_ESTOQUE)
            .estoqueMinimo(UPDATED_ESTOQUE_MINIMO)
            .unidadeMedida(UPDATED_UNIDADE_MEDIDA)
            .dataCadastro(UPDATED_DATA_CADASTRO);
    }

    @BeforeEach
    void initTest() {
        produto = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProduto != null) {
            produtoRepository.delete(insertedProduto);
            insertedProduto = null;
        }
    }

    @Test
    @Transactional
    void createProduto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Produto
        var returnedProduto = om.readValue(
            restProdutoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Produto.class
        );

        // Validate the Produto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProdutoUpdatableFieldsEquals(returnedProduto, getPersistedProduto(returnedProduto));

        insertedProduto = returnedProduto;
    }

    @Test
    @Transactional
    void createProdutoWithExistingId() throws Exception {
        // Create the Produto with an existing ID
        produto.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produto.setNome(null);

        // Create the Produto, which fails.

        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoBarrasIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produto.setCodigoBarras(null);

        // Create the Produto, which fails.

        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustoAquisicaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produto.setCustoAquisicao(null);

        // Create the Produto, which fails.

        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecoVendaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produto.setPrecoVenda(null);

        // Create the Produto, which fails.

        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnidadeMedidaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produto.setUnidadeMedida(null);

        // Create the Produto, which fails.

        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProdutos() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        // Get all the produtoList
        restProdutoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].codigoBarras").value(hasItem(DEFAULT_CODIGO_BARRAS)))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)))
            .andExpect(jsonPath("$.[*].custoAquisicao").value(hasItem(DEFAULT_CUSTO_AQUISICAO.doubleValue())))
            .andExpect(jsonPath("$.[*].precoVenda").value(hasItem(DEFAULT_PRECO_VENDA.doubleValue())))
            .andExpect(jsonPath("$.[*].quantidadeEstoque").value(hasItem(DEFAULT_QUANTIDADE_ESTOQUE)))
            .andExpect(jsonPath("$.[*].estoqueMinimo").value(hasItem(DEFAULT_ESTOQUE_MINIMO)))
            .andExpect(jsonPath("$.[*].unidadeMedida").value(hasItem(DEFAULT_UNIDADE_MEDIDA.toString())))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(DEFAULT_DATA_CADASTRO.toString())));
    }

    @Test
    @Transactional
    void getProduto() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        // Get the produto
        restProdutoMockMvc
            .perform(get(ENTITY_API_URL_ID, produto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(produto.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.codigoBarras").value(DEFAULT_CODIGO_BARRAS))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA))
            .andExpect(jsonPath("$.custoAquisicao").value(DEFAULT_CUSTO_AQUISICAO.doubleValue()))
            .andExpect(jsonPath("$.precoVenda").value(DEFAULT_PRECO_VENDA.doubleValue()))
            .andExpect(jsonPath("$.quantidadeEstoque").value(DEFAULT_QUANTIDADE_ESTOQUE))
            .andExpect(jsonPath("$.estoqueMinimo").value(DEFAULT_ESTOQUE_MINIMO))
            .andExpect(jsonPath("$.unidadeMedida").value(DEFAULT_UNIDADE_MEDIDA.toString()))
            .andExpect(jsonPath("$.dataCadastro").value(DEFAULT_DATA_CADASTRO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProduto() throws Exception {
        // Get the produto
        restProdutoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduto() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produto
        Produto updatedProduto = produtoRepository.findById(produto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduto are not directly saved in db
        em.detach(updatedProduto);
        updatedProduto
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .categoria(UPDATED_CATEGORIA)
            .custoAquisicao(UPDATED_CUSTO_AQUISICAO)
            .precoVenda(UPDATED_PRECO_VENDA)
            .quantidadeEstoque(UPDATED_QUANTIDADE_ESTOQUE)
            .estoqueMinimo(UPDATED_ESTOQUE_MINIMO)
            .unidadeMedida(UPDATED_UNIDADE_MEDIDA)
            .dataCadastro(UPDATED_DATA_CADASTRO);

        restProdutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProduto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProduto))
            )
            .andExpect(status().isOk());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProdutoToMatchAllProperties(updatedProduto);
    }

    @Test
    @Transactional
    void putNonExistingProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(put(ENTITY_API_URL_ID, produto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(produto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProdutoWithPatch() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produto using partial update
        Produto partialUpdatedProduto = new Produto();
        partialUpdatedProduto.setId(produto.getId());

        partialUpdatedProduto
            .categoria(UPDATED_CATEGORIA)
            .custoAquisicao(UPDATED_CUSTO_AQUISICAO)
            .quantidadeEstoque(UPDATED_QUANTIDADE_ESTOQUE)
            .unidadeMedida(UPDATED_UNIDADE_MEDIDA);

        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduto))
            )
            .andExpect(status().isOk());

        // Validate the Produto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProdutoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduto, produto), getPersistedProduto(produto));
    }

    @Test
    @Transactional
    void fullUpdateProdutoWithPatch() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produto using partial update
        Produto partialUpdatedProduto = new Produto();
        partialUpdatedProduto.setId(produto.getId());

        partialUpdatedProduto
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .categoria(UPDATED_CATEGORIA)
            .custoAquisicao(UPDATED_CUSTO_AQUISICAO)
            .precoVenda(UPDATED_PRECO_VENDA)
            .quantidadeEstoque(UPDATED_QUANTIDADE_ESTOQUE)
            .estoqueMinimo(UPDATED_ESTOQUE_MINIMO)
            .unidadeMedida(UPDATED_UNIDADE_MEDIDA)
            .dataCadastro(UPDATED_DATA_CADASTRO);

        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduto))
            )
            .andExpect(status().isOk());

        // Validate the Produto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProdutoUpdatableFieldsEquals(partialUpdatedProduto, getPersistedProduto(partialUpdatedProduto));
    }

    @Test
    @Transactional
    void patchNonExistingProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, produto.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(produto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(produto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(produto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduto() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the produto
        restProdutoMockMvc
            .perform(delete(ENTITY_API_URL_ID, produto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return produtoRepository.count();
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

    protected Produto getPersistedProduto(Produto produto) {
        return produtoRepository.findById(produto.getId()).orElseThrow();
    }

    protected void assertPersistedProdutoToMatchAllProperties(Produto expectedProduto) {
        assertProdutoAllPropertiesEquals(expectedProduto, getPersistedProduto(expectedProduto));
    }

    protected void assertPersistedProdutoToMatchUpdatableProperties(Produto expectedProduto) {
        assertProdutoAllUpdatablePropertiesEquals(expectedProduto, getPersistedProduto(expectedProduto));
    }
}
