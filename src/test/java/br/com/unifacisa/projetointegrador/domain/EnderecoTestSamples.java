package br.com.unifacisa.projetointegrador.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnderecoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Endereco getEnderecoSample1() {
        return new Endereco().id(1L).rua("rua1").cidade("cidade1").estado("estado1").codigoPostal("codigoPostal1");
    }

    public static Endereco getEnderecoSample2() {
        return new Endereco().id(2L).rua("rua2").cidade("cidade2").estado("estado2").codigoPostal("codigoPostal2");
    }

    public static Endereco getEnderecoRandomSampleGenerator() {
        return new Endereco()
            .id(longCount.incrementAndGet())
            .rua(UUID.randomUUID().toString())
            .cidade(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString())
            .codigoPostal(UUID.randomUUID().toString());
    }
}
