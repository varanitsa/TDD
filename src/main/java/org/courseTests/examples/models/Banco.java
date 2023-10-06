package org.courseTests.examples.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {


    private List<Cuenta> cuentas;
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Banco() {
        cuentas = new ArrayList<>();
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
        cuenta.setBanco(this);
    }
    public void transferir(Cuenta origin, Cuenta destino, BigDecimal monto) {
     origin.debitar(monto);
     destino.acreditar(monto);
    }


}
