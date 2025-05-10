package br.com.unifacisa.projetointegrador.domain;

import static br.com.unifacisa.projetointegrador.domain.FuncionarioTestSamples.*;
import static br.com.unifacisa.projetointegrador.domain.UsuarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.unifacisa.projetointegrador.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FuncionarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Funcionario.class);
        Funcionario funcionario1 = getFuncionarioSample1();
        Funcionario funcionario2 = new Funcionario();
        assertThat(funcionario1).isNotEqualTo(funcionario2);

        funcionario2.setId(funcionario1.getId());
        assertThat(funcionario1).isEqualTo(funcionario2);

        funcionario2 = getFuncionarioSample2();
        assertThat(funcionario1).isNotEqualTo(funcionario2);
    }

    @Test
    void usuarioTest() {
        Funcionario funcionario = getFuncionarioRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        funcionario.setUsuario(usuarioBack);
        assertThat(funcionario.getUsuario()).isEqualTo(usuarioBack);

        funcionario.usuario(null);
        assertThat(funcionario.getUsuario()).isNull();
    }
}
