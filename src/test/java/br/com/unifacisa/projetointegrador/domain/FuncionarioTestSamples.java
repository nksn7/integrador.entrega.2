package br.com.unifacisa.projetointegrador.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FuncionarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Funcionario getFuncionarioSample1() {
        return new Funcionario().id(1L).nome("nome1").cpf("cpf1").email("email1").telefone("telefone1");
    }

    public static Funcionario getFuncionarioSample2() {
        return new Funcionario().id(2L).nome("nome2").cpf("cpf2").email("email2").telefone("telefone2");
    }

    public static Funcionario getFuncionarioRandomSampleGenerator() {
        return new Funcionario()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .cpf(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString());
    }
}
