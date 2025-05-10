package br.com.unifacisa.projetointegrador.domain;

import static br.com.unifacisa.projetointegrador.domain.ClienteTestSamples.*;
import static br.com.unifacisa.projetointegrador.domain.EnderecoTestSamples.*;
import static br.com.unifacisa.projetointegrador.domain.FornecedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.unifacisa.projetointegrador.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnderecoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Endereco.class);
        Endereco endereco1 = getEnderecoSample1();
        Endereco endereco2 = new Endereco();
        assertThat(endereco1).isNotEqualTo(endereco2);

        endereco2.setId(endereco1.getId());
        assertThat(endereco1).isEqualTo(endereco2);

        endereco2 = getEnderecoSample2();
        assertThat(endereco1).isNotEqualTo(endereco2);
    }

    @Test
    void clienteTest() {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        endereco.setCliente(clienteBack);
        assertThat(endereco.getCliente()).isEqualTo(clienteBack);

        endereco.cliente(null);
        assertThat(endereco.getCliente()).isNull();
    }

    @Test
    void fornecedorTest() {
        Endereco endereco = getEnderecoRandomSampleGenerator();
        Fornecedor fornecedorBack = getFornecedorRandomSampleGenerator();

        endereco.setFornecedor(fornecedorBack);
        assertThat(endereco.getFornecedor()).isEqualTo(fornecedorBack);

        endereco.fornecedor(null);
        assertThat(endereco.getFornecedor()).isNull();
    }
}
