/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendapf;



import java.util.ArrayList;

// --------------------------- PRODUCTO ---------------------------
class Producto {
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    public void reducirStock(int cantidad) {
        stock -= cantidad;
    }

    @Override
    public String toString() {
        return nombre + " | $" + precio + " | Stock: " + stock;
    }
}

// --------------------------- USUARIO ---------------------------
class Usuario {
    private String nombre;
    private String correo;

    public Usuario(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }
}

// --------------------------- ADMINISTRADOR ---------------------------
class Administrador {

    private ArrayList<Producto> inventario;

    public Administrador(ArrayList<Producto> inventario) {
        this.inventario = inventario;
    }

    public void agregarProducto(String nombre, double precio, int stock) {
        inventario.add(new Producto(nombre, precio, stock));
    }

    public ArrayList<Producto> getInventario() {
        return inventario;
    }
}

// --------------------------- PAGO ABSTRACTO ---------------------------
abstract class Pago {
    protected double monto;
    public Pago(double monto) { this.monto = monto; }
    public abstract void realizarPago();
}

// --------------------------- PAGOS ---------------------------
class PagoTarjeta extends Pago {
    public PagoTarjeta(double monto) { super(monto); }
    @Override
    public void realizarPago() {
        System.out.println("Pago con tarjeta procesado: $" + monto);
    }
}

class PagoPayPal extends Pago {
    public PagoPayPal(double monto) { super(monto); }
    @Override
    public void realizarPago() {
        System.out.println("Pago por PayPal procesado: $" + monto);
    }
}

class PagoEfectivo extends Pago {
    public PagoEfectivo(double monto) { super(monto); }
    @Override
    public void realizarPago() {
        System.out.println("Pago en efectivo procesado: $" + monto);
    }
}
