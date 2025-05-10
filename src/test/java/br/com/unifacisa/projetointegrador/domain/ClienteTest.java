package br.com.unifacisa.projetointegrador.domain;

import static br.com.unifacisa.projetointegrador.domain.ClienteTestSamples.*;
import static br.com.unifacisa.projetointegrador.domain.EnderecoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.unifacisa.projetointegrador.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cliente.class);
        Cliente cliente1 = getClienteSample1();
        Cliente cliente2 = new Cliente();
        assertThat(cliente1).isNotEqualTo(cliente2);

        cliente2.setId(cliente1.getId());
        assertThat(cliente1).isEqualTo(cliente2);

        cliente2 = getClienteSample2();
        assertThat(cliente1).isNotEqualTo(cliente2);
    }

    @Test
    void enderecosTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Endereco enderecoBack = getEnderecoRandomSampleGenerator();

        cliente.addEnderecos(enderecoBack);
        assertThat(cliente.getEnderecos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getCliente()).isEqualTo(cliente);

        cliente.removeEnderecos(enderecoBack);
        assertThat(cliente.getEnderecos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getCliente()).isNull();

        cliente.enderecos(new HashSet<>(Set.of(enderecoBack)));
        assertThat(cliente.getEnderecos()).containsOnly(enderecoBack);
        assertThat(enderecoBack.getCliente()).isEqualTo(cliente);

        cliente.setEnderecos(new HashSet<>());
        assertThat(cliente.getEnderecos()).doesNotContain(enderecoBack);
        assertThat(enderecoBack.getCliente()).isNull();
    }
}
