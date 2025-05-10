package br.com.unifacisa.projetointegrador.domain;

import static br.com.unifacisa.projetointegrador.domain.ClienteTestSamples.*;
import static br.com.unifacisa.projetointegrador.domain.FuncionarioTestSamples.*;
import static br.com.unifacisa.projetointegrador.domain.VendaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.unifacisa.projetointegrador.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VendaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venda.class);
        Venda venda1 = getVendaSample1();
        Venda venda2 = new Venda();
        assertThat(venda1).isNotEqualTo(venda2);

        venda2.setId(venda1.getId());
        assertThat(venda1).isEqualTo(venda2);

        venda2 = getVendaSample2();
        assertThat(venda1).isNotEqualTo(venda2);
    }

    @Test
    void clienteTest() {
        Venda venda = getVendaRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        venda.setCliente(clienteBack);
        assertThat(venda.getCliente()).isEqualTo(clienteBack);

        venda.cliente(null);
        assertThat(venda.getCliente()).isNull();
    }

    @Test
    void funcionarioTest() {
        Venda venda = getVendaRandomSampleGenerator();
        Funcionario funcionarioBack = getFuncionarioRandomSampleGenerator();

        venda.setFuncionario(funcionarioBack);
        assertThat(venda.getFuncionario()).isEqualTo(funcionarioBack);

        venda.funcionario(null);
        assertThat(venda.getFuncionario()).isNull();
    }
}
