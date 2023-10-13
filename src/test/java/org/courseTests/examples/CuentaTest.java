package org.courseTests.examples;


import org.courseTests.examples.exceptions.DineroInsuficienteException;
import org.courseTests.examples.models.Banco;
import org.courseTests.examples.models.Cuenta;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


class CuentaTest {

    Cuenta cuenta1;
    Cuenta cuenta2;

    @BeforeEach
    void setUp() {
        this.cuenta1 = new Cuenta("Julia", new BigDecimal(39889.29));
        this.cuenta2 = new Cuenta("Joe", new BigDecimal(56733.99));
        System.out.println("iniciando la prueba");
    }

    @AfterEach
    void tearDown() {
        System.out.println("finalizando la prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Iniciando los tests");

    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalicando los tests");

    }

    @Test
    @DisplayName("probando el nombre de la cuenta corriente")
    void testNombreCuenta() {
        assertEquals("Julia", cuenta1.getPersona());
    }


    @Test
    @DisplayName("probando el saldo de la cuenta corriente")
    void testSaldoCuenta() {
        assertNotNull(cuenta1.getSaldo());
        assertEquals(39889.29, cuenta1.getSaldo().doubleValue());
        assertFalse(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) > 0);

    }

    @Test
    @DisplayName("probando que las cuentas son iguales")
    void testReferenciaCuentas() {
        cuenta2 = new Cuenta("Joe", new BigDecimal(56733.99));
        cuenta1 = new Cuenta("Joe", new BigDecimal(56733.99));

        assertEquals(cuenta2, cuenta1);
    }

    @Test
    @DisplayName("probando el metodo debitar")
    void TestDeditoCuenta() {

        cuenta1.debitar(new BigDecimal(1000.29));
        assertNotNull(cuenta1.getSaldo());
        assertEquals(38889, cuenta1.getSaldo().intValue());
        assertEquals("38889.00", cuenta1.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    @Test
    @DisplayName("probando el metodo acreditar")
    void TestCreditoCuenta() {

        cuenta1.acreditar(new BigDecimal(1000));
        assertNotNull(cuenta1.getSaldo());
        assertEquals(40889, cuenta1.getSaldo().intValue());
        assertEquals("40889.29", cuenta1.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    @Test
    @DisplayName("comprobando si el dinero es suficiente para debitar")
    void testDineroInsuficienteExceptionCuenta() {
        Exception exception = assertThrows(DineroInsuficienteException.class, () ->
                cuenta1.debitar(new BigDecimal(50000)));
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(actual, esperado);
    }

    @Test
    @DisplayName("probando el metodo de transferencia de cuenta a cuenta")
    void testTransferirDineroCuentas() {
        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta1, cuenta2, new BigDecimal(5000));
        assertEquals("61733.99", cuenta2.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString());
        assertEquals("34889.29", cuenta1.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    @Test
    //@Disabled - para saltar la prueba
    @DisplayName("probando de la relación entre el Banco y las cuentas")
    void testRealcionBancoCuentas() {

        String esperado = "Julia";
        String real = cuenta2.getPersona();

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);
        {
            banco.setNombre("Banco del Estado");
            banco.transferir(cuenta1, cuenta2, new BigDecimal(5000));
            assertAll(() -> assertEquals("61733.99", cuenta2.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString()),
                    () -> assertEquals("34889.29", cuenta1.getSaldo().setScale(2, RoundingMode.HALF_UP).toPlainString()),
                    () -> assertEquals(2, banco.getCuentas().size(), () -> "el banco no tiene las cuentas esperadas"),
                    () -> assertEquals("Banco del Estado", cuenta1.getBanco().getNombre(), () -> "el nombre del Banco no coicide"),
                    () -> assertEquals("Julia", banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Julia"))
                            .findFirst()
                            .get().getPersona(), () -> "el nombre esperado del cliente " + esperado + " no se encuentra entre los tutulares de las cuentas "),
                    () -> assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getPersona().equals("Joe")), () -> "el nombre esperado del cliente no se encuentra entre los tutulares de las cuentas "));

        }
    }

    @Test
    void imprimirSystemPropierties() {
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    void imprimirVariablesAmbiente() {
        Map<String, String> getenv = System.getenv();
        getenv.forEach((k, v) -> System.out.println(k + "=" + v));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void onlyWindow() {
        System.out.println("se ejecuta en Windows");
    }
    @Test
    @EnabledOnOs(OS.LINUX)
    void notLinux() {
        System.out.println("se ejecuta en Linux");
    }

    @Test
    @EnabledOnOs({OS.LINUX,OS.MAC})
    void notWindows() {
        System.out.println("se ejecuta en Linux o MacOs");
    }


    @Test
    @EnabledOnJre(JRE.JAVA_22)
    void onlyJRE22() {
        System.out.println("se ejecuta en version Java 22");
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = ".*20.*")
    void enabled_JavaUp20() {
        System.out.println("se ejecuta en version Java 22");
    }


    @Test
    @DisabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
    void only32x() {
        System.out.println("se ejecuta en sistemas de 32 bits");
    }

    @Test
    @EnabledIfSystemProperty(named = "user.name", matches = "varan")
    void userName() {
        System.out.println("Ejecutando para varan");

    }

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void ENV_dev() {
        System.out.println("Ejecutando para dev");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
    void testEnvironment_dev() {
        System.out.println("Ejecutando para ENVIEROMENT prod");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*openjdk-20.0.1*")
    void testJava_HOME() {
        System.out.println("Ejecutando en version openjdk-19.0.1 ");
    }
}

