package org.courseTests.examples;


import org.courseTests.examples.exceptions.DineroInsuficienteException;
import org.courseTests.examples.models.Cuenta;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta =new Cuenta("Julia",new BigDecimal(39889.29));
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
        Cuenta cuenta = new Cuenta("Joe",new BigDecimal(56733.99));
        Cuenta cuenta2 = new Cuenta("Joe",new BigDecimal(56733.99));

   assertEquals(cuenta, cuenta2);
    }

    @Test
    void TestDeditoCuenta() {
        Cuenta cuenta = new Cuenta("Julia", new BigDecimal(39889.29));
        cuenta.debitar(new BigDecimal(1000.29));
        assertNotNull(cuenta.getSaldo());
        assertEquals(38889,cuenta.getSaldo().intValue());
        assertEquals("38889.00",cuenta.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    @Test
    void TestCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Julia", new BigDecimal(39889.29));
        cuenta.acreditar(new BigDecimal(1000));
        assertNotNull(cuenta.getSaldo());
        assertEquals(40889,cuenta.getSaldo().intValue());
        assertEquals("40889.29",cuenta.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString());
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
}
