package br.com.unifacisa.projetointegrador.domain;

import static br.com.unifacisa.projetointegrador.domain.EnderecoTestSamples.*;
import static br.com.unifacisa.projetointegrador.domain.FornecedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.unifacisa.projetointegrador.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FornecedorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fornecedor.class);
        Fornecedor fornecedor1 = getFornecedorSample1();
        Fornecedor fornecedor2 = new Fornecedor();
        assertThat(fornecedor1).isNotEqualTo(fornecedor2);

        fornecedor2.setId(fornecedor1.getId());
        assertThat(fornecedor1).isEqualTo(fornecedor2);

        fornecedor2 = getFornecedorSample2();
        assertThat(fornecedor1).isNotEqualTo(fornecedor2);
    }

    @Test
    void enderecosTest() {
        Fornecedor fornecedor = getFornecedorRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        fornecedor.addEnderecos(enderecoBack);
        assertThat(fornecedor.getEnderecos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getFornecedor()).isEqualTo(fornecedor);

        fornecedor.removeEnderecos(enderecoBack);
        assertThat(fornecedor.getEnderecos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getFornecedor()).isNull();

        fornecedor.enderecos(new HashSet<>(Set.of(enderecoBack)));
        assertThat(fornecedor.getEnderecos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getFornecedor()).isEqualTo(fornecedor);

        fornecedor.setEnderecos(new HashSet<>());
        assertThat(fornecedor.getEnderecos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getFornecedor()).isNull();
    }
}
