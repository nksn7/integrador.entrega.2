package br.com.unifacisa.projetointegrador.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class UsuarioAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsuarioAllPropertiesEquals(Usuario expected, Usuario actual) {
        assertUsuarioAutoGeneratedPropertiesEquals(expected, actual);
        assertUsuarioAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsuarioAllUpdatablePropertiesEquals(Usuario expected, Usuario actual) {
        assertUsuarioUpdatableFieldsEquals(expected, actual);
        assertUsuarioUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsuarioAutoGeneratedPropertiesEquals(Usuario expected, Usuario actual) {
        assertThat(actual)
            .as("Verify Usuario auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsuarioUpdatableFieldsEquals(Usuario expected, Usuario actual) {
        assertThat(actual)
            .as("Verify Usuario relevant properties")
            .satisfies(a -> assertThat(a.getLogin()).as("check login").isEqualTo(expected.getLogin()))
            .satisfies(a -> assertThat(a.getSenhaHash()).as("check senhaHash").isEqualTo(expected.getSenhaHash()))
            .satisfies(a -> assertThat(a.getAtivo()).as("check ativo").isEqualTo(expected.getAtivo()))
            .satisfies(a -> assertThat(a.getUltimoAcesso()).as("check ultimoAcesso").isEqualTo(expected.getUltimoAcesso()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsuarioUpdatableRelationshipsEquals(Usuario expected, Usuario actual) {
        // empty method
    }
}
