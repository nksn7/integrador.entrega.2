package br.com.unifacisa.projetointegrador.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VendaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Venda getVendaSample1() {
        return new Venda().id(1L).parcelas(1).observacoes("observacoes1");
    }

    public static Venda getVendaSample2() {
        return new Venda().id(2L).parcelas(2).observacoes("observacoes2");
    }

    public static Venda getVendaRandomSampleGenerator() {
        return new Venda().id(longCount.incrementAndGet()).parcelas(intCount.incrementAndGet()).observacoes(UUID.randomUUID().toString());
    }
}
