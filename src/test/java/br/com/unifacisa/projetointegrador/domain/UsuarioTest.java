package br.com.unifacisa.projetointegrador.domain;

import static br.com.unifacisa.projetointegrador.domain.FuncionarioTestSamples.*;
import static br.com.unifacisa.projetointegrador.domain.UsuarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.unifacisa.projetointegrador.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Usuario.class);
        Usuario usuario1 = getUsuarioSample1();
        Usuario usuario2 = new Usuario();
        assertThat(usuario1).isNotEqualTo(usuario2);

        usuario2.setId(usuario1.getId());
        assertThat(usuario1).isEqualTo(usuario2);

        usuario2 = getUsuarioSample2();
        assertThat(usuario1).isNotEqualTo(usuario2);
    }

    @Test
    void funcionarioTest() {
        Usuario usuario = getUsuarioRandomSampleGenerator();
        Funcionario funcionarioBack = getFuncionarioRandomSampleGenerator();

        usuario.setFuncionario(funcionarioBack);
        assertThat(usuario.getFuncionario()).isEqualTo(funcionarioBack);
        assertThat(funcionarioBack.getUsuario()).isEqualTo(usuario);

        usuario.funcionario(null);
        assertThat(usuario.getFuncionario()).isNull();
        assertThat(funcionarioBack.getUsuario()).isNull();
    }
}
