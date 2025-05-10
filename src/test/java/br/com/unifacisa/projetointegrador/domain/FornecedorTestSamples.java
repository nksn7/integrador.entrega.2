package br.com.unifacisa.projetointegrador.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FornecedorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Fornecedor getFornecedorSample1() {
        return new Fornecedor()
            .id(1L)
            .nome("nome1")
            .razaoSocial("razaoSocial1")
            .cpf("cpf1")
            .cnpj("cnpj1")
            .email("email1")
            .telefone("telefone1")
            .condicaoPagamento("condicaoPagamento1");
    }

    public static Fornecedor getFornecedorSample2() {
        return new Fornecedor()
            .id(2L)
            .nome("nome2")
            .razaoSocial("razaoSocial2")
            .cpf("cpf2")
            .cnpj("cnpj2")
            .email("email2")
            .telefone("telefone2")
            .condicaoPagamento("condicaoPagamento2");
    }

    public static Fornecedor getFornecedorRandomSampleGenerator() {
        return new Fornecedor()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .razaoSocial(UUID.randomUUID().toString())
            .cpf(UUID.randomUUID().toString())
            .cnpj(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString())
            .condicaoPagamento(UUID.randomUUID().toString());
    }
}
