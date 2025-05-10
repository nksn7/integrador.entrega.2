package br.com.unifacisa.projetointegrador;

import br.com.unifacisa.projetointegrador.config.AsyncSyncConfiguration;
import br.com.unifacisa.projetointegrador.config.EmbeddedSQL;
import br.com.unifacisa.projetointegrador.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { ProjetoIntegradorApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
