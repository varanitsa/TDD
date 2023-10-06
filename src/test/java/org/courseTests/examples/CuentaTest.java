package org.courseTests.examples;


import org.courseTests.examples.exceptions.DineroInsuficienteException;
import org.courseTests.examples.models.Banco;
import org.courseTests.examples.models.Cuenta;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Julia", new BigDecimal(39889.29));
        assertEquals("Julia", cuenta.getPersona());
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal(36444.33));
        assertNotNull(cuenta.getSaldo());
        assertEquals(36444.33, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

    }

    @Test
    void testReferenciaCuentas() {
        Cuenta cuenta = new Cuenta("Joe", new BigDecimal(56733.99));
        Cuenta cuenta2 = new Cuenta("Joe", new BigDecimal(56733.99));

        assertEquals(cuenta, cuenta2);
    }

    @Test
    void TestDeditoCuenta() {
        Cuenta cuenta = new Cuenta("Julia", new BigDecimal(39889.29));
        cuenta.debitar(new BigDecimal(1000.29));
        assertNotNull(cuenta.getSaldo());
        assertEquals(38889, cuenta.getSaldo().intValue());
        assertEquals("38889.00", cuenta.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    @Test
    void TestCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Julia", new BigDecimal(39889.29));
        cuenta.acreditar(new BigDecimal(1000));
        assertNotNull(cuenta.getSaldo());
        assertEquals(40889, cuenta.getSaldo().intValue());
        assertEquals("40889.29", cuenta.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Cuenta cuenta = new Cuenta("Julia", new BigDecimal(39889.29));
        Exception exception = assertThrows(DineroInsuficienteException.class, () ->
                cuenta.debitar(new BigDecimal(50000)));
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(actual, esperado);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("Julia", new BigDecimal(39889.29));
        Cuenta cuenta2 = new Cuenta("Joe", new BigDecimal(56733.99));
        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta1, cuenta2, new BigDecimal(5000));
        assertEquals("61733.99", cuenta2.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString());
        assertEquals("34889.29", cuenta1.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    @Test
    void testRealcionBancoCuentas() {
        Cuenta cuenta1 = new Cuenta("Julia", new BigDecimal(39889.29));
        Cuenta cuenta2 = new Cuenta("Joe", new BigDecimal(56733.99));

        String esperado = "Julia";
        String real = cuenta2.getPersona();

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta1, cuenta2, new BigDecimal(5000));
        assertAll(() -> assertEquals("61733.99", cuenta2.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString()),
                () -> assertEquals("34889.29", cuenta1.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString()),
                () -> assertEquals(2, banco.getCuentas().size(), ()-> "el banco no tiene las cuentas esperadas"),
                () -> assertEquals("Banco del Estado", cuenta1.getBanco().getNombre(), ()-> "el nombre del Banco no coicide"),
                () -> assertEquals("JuliaV", banco.getCuentas().stream()
                        .filter(c -> c.getPersona().equals("Julia"))
                        .findFirst()
                        .get().getPersona(), () -> "el nombre esperado del cliente " + esperado + " no se encuentra entre los tutulares de las cuentas "),
                () -> assertTrue(banco.getCuentas().stream()
                        .anyMatch(c -> c.getPersona().equals("Joe")), () -> "el nombre esperado del cliente no se encuentra entre los tutulares de las cuentas "));

    }
}

