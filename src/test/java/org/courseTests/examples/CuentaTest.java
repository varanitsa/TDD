package org.courseTests.examples;


import org.courseTests.examples.exceptions.DineroInsuficienteException;
import org.courseTests.examples.models.Banco;
import org.courseTests.examples.models.Cuenta;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;


class CuentaTest {

    Cuenta cuenta1;
    Cuenta cuenta2;

    @BeforeEach
    void setUp() {
        this.cuenta1 = new Cuenta("Julia", new BigDecimal("39889.299"));
        this.cuenta2 = new Cuenta("Joe", new BigDecimal("56733.999"));
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

    @Nested
    class SaldoCuentaTest {

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
            cuenta2 = new Cuenta("Joe", new BigDecimal("56733.99"));
            cuenta1 = new Cuenta("Joe", new BigDecimal("56733.99"));

            assertEquals(cuenta2, cuenta1);
        }

        @Test
        @DisplayName("probando el metodo debitar")
        void TestDeditoCuenta() {

            cuenta1.debitar(new BigDecimal("1000.29"));
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
    }

    @Nested
    class BancoCuentasTest {
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

    }

    @Nested
    class SystemPropertiesTest {
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
        @EnabledOnOs({OS.LINUX, OS.MAC})
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

        @Test
        @DisplayName("envieronment dev test")
        void testSaldoCuentaDev() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumeTrue(esDev);
            assertNotNull(cuenta1.getSaldo());
            assertEquals(39889.29, cuenta1.getSaldo().doubleValue());
            assertFalse(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("envieronment dev test variant")
        void testSaldoCuentaDev2() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumingThat(esDev, () -> {
                System.out.println("No se ejecuta");
            });
            assertEquals(39889.29, cuenta1.getSaldo().doubleValue());
            assertNotNull(cuenta1.getSaldo());
            assertEquals(39889.29, cuenta1.getSaldo().doubleValue());
            assertFalse(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) > 0);

        }
    }

    @DisplayName("probando el nombre de la cuenta corriente")
    @RepeatedTest(3)
    void testNombreCuentaRepetir(RepetitionInfo info) {
        if (info.getCurrentRepetition() == 3) {
            System.out.println("Estamos en la repiticion  " + info.getCurrentRepetition());
        }
        assertEquals("Julia", cuenta1.getPersona());
    }


    @Nested
    @DisplayName("probando el metodo debitar")
    class ParametrizadosTests {
        @ParameterizedTest
        @ValueSource(strings = {"1000", "2000", "39889.29"})
        @DisplayName("probando con valueSource")
        void TestDeditoCuenta(String montoString) {
            BigDecimal monto = new BigDecimal(montoString);
            cuenta1.debitar(monto);
            assertNotNull(cuenta1.getSaldo());
            assertTrue(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
        }

        @ParameterizedTest
        @CsvSource({"1,1000", "2,2000", "3,39889.29"})
        @DisplayName("probando con csvSource")
        void TestDeditoCuentaCSVsource(String index, String montoString) {
            BigDecimal monto = new BigDecimal(montoString);
            cuenta1.debitar(monto);
            assertNotNull(cuenta1.getSaldo());
            assertTrue(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/data.csv")
        @DisplayName("probando con csvFile")
        void TestDeditoCuentaCSVfileSource(String montoString) {
            BigDecimal monto = new BigDecimal(montoString);
            cuenta1.debitar(monto);
            assertNotNull(cuenta1.getSaldo());
            assertTrue(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest
        @MethodSource("montoList")
        @DisplayName("probando con csvFile")
        void TestDeditoCuentaMethodSource(String montoString) {
            BigDecimal monto = new BigDecimal(montoString);
            cuenta1.debitar(monto);
            assertNotNull(cuenta1.getSaldo());
            assertTrue(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        static List<String> montoList() {
            return Arrays.asList("1000", "2000", "39199.299");

        }
        @ParameterizedTest
        @CsvSource({"1200,1000, Julia,Julia", "2300,2000, Marta,Marta", "39889.29,39889.29, Joe,Joe"})
        @DisplayName("probando con csvSource2")
        void TestDeditoCuentaCSVsource2(String saldo, String montoString, String esperado, String actual) {
            BigDecimal monto = new BigDecimal(montoString);
            cuenta1.setSaldo(new BigDecimal(saldo));
            cuenta1.debitar(monto);
            cuenta1.setPersona(actual);
            assertNotNull(cuenta1.getSaldo());
            assertNotNull(cuenta1.getPersona());
            assertEquals(esperado,actual);
            assertTrue(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/data2.csv")
        @DisplayName("probando con csvFile2")
        void TestDeditoCuentaCSVfileSource2(String saldo, String montoString, String esperado, String actual) {
            BigDecimal monto = new BigDecimal(montoString);
            cuenta1.setSaldo(new BigDecimal(saldo));
            cuenta1.debitar(monto);
            cuenta1.setPersona(actual);
            assertNotNull(cuenta1.getSaldo());
            assertNotNull(cuenta1.getPersona());
            assertEquals(esperado,actual);
            assertTrue(cuenta1.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
        }

    }
}

