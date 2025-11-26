/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tiendapf;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class TiendaPF extends JFrame {

    // ---------------------------
    //  MODELO DE DATOS
    // ---------------------------
    class Producto {
        String nombre;
        double precio;
        int stock;

        Producto(String n, double p, int s) {
            nombre = n;
            precio = p;
            stock = s;
        }
    }

    // ---------------------------
    // DATOS GLOBALES
    // ---------------------------
    ArrayList<Producto> productos = new ArrayList<>();
    // Inicializador de bloque para llenar productos
    {
        productos.add(new Producto("Laptop Gamer", 18999.99, 5));
        productos.add(new Producto("Smartphone Pro", 12999.49, 8));
        productos.add(new Producto("Audífonos Inalámbricos", 1299.00, 15));
        productos.add(new Producto("Teclado Mecánico RGB", 899.99, 10));
        productos.add(new Producto("Monitor 144Hz", 4599.50, 6));
    }
    
    ArrayList<Producto> carrito = new ArrayList<>();
    DefaultListModel<String> modeloProductos = new DefaultListModel<>();
    DefaultListModel<String> modeloCarrito = new DefaultListModel<>();
    String rol = "cliente";

    public TiendaPF() {
        setTitle("TiendaPF - Login");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mostrarLogin();
    }

    // ---------------------------
    // 1. LOGIN
    // ---------------------------
    void mostrarLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JLabel l1 = new JLabel("Usuario (admin / user):");
        JLabel l2 = new JLabel("Contraseña (admin / user):");
        JTextField t1 = new JTextField(12);
        JPasswordField t2 = new JPasswordField(12);
        JButton entrar = new JButton("Entrar");

        c.gridx = 0; c.gridy = 0; panel.add(l1, c);
        c.gridx = 1; panel.add(t1, c);
        c.gridx = 0; c.gridy = 1; panel.add(l2, c);
        c.gridx = 1; panel.add(t2, c);
        c.gridx = 1; c.gridy = 2; panel.add(entrar, c);

        setContentPane(panel);
        revalidate(); repaint();

        entrar.addActionListener(e -> {
            String user = t1.getText();
            String pass = new String(t2.getPassword());

            if (user.equals("admin") && pass.equals("admin")) {
                rol = "admin";
            } else {
                rol = "cliente";
            }
            mostrarMenu();
        });
    }

    // ---------------------------
    // 2. MENÚ PRINCIPAL
    // ---------------------------
    void mostrarMenu() {
        JPanel p = new JPanel(new GridLayout(rol.equals("admin") ? 3 : 2, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton verProd = new JButton("Ver productos / Comprar");
        JButton verCart = new JButton("Ver Carrito (" + carrito.size() + ")");

        p.add(verProd);
        p.add(verCart);

        if (rol.equals("admin")) {
            JButton adminBtn = new JButton("ADMIN: Aumentar stock");
            p.add(adminBtn);
            adminBtn.addActionListener(e -> ventanaAdmin());
        }

        verProd.addActionListener(e -> ventanaProductos());
        verCart.addActionListener(e -> ventanaCarrito());

        setContentPane(p);
        revalidate(); repaint();
    }

    // ---------------------------
    // 3. ADMIN (Corregido: ahora usa un ComboBox)
    // ---------------------------
    void ventanaAdmin() {
        JDialog d = new JDialog(this, "Administración de Stock", true);
        d.setSize(400, 250);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(4, 2, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Selector de productos existente
        JComboBox<String> comboProductos = new JComboBox<>();
        for (Producto prod : productos) {
            comboProductos.addItem(prod.nombre + " (Stock actual: " + prod.stock + ")");
        }

        JTextField tCantidad = new JTextField();
        JButton btnActualizar = new JButton("Actualizar Stock");

        p.add(new JLabel("Producto:")); p.add(comboProductos);
        p.add(new JLabel("Cantidad a agregar:")); p.add(tCantidad);
        p.add(new JLabel("")); p.add(btnActualizar);

        btnActualizar.addActionListener(e -> {
            int index = comboProductos.getSelectedIndex();
            if (index >= 0) {
                try {
                    int cantidad = Integer.parseInt(tCantidad.getText());
                    if (cantidad > 0) {
                        Producto pr = productos.get(index);
                        pr.stock += cantidad;
                        JOptionPane.showMessageDialog(d, "Stock actualizado. Nuevo stock: " + pr.stock);
                        d.dispose(); // Cerrar ventana al terminar
                    } else {
                        JOptionPane.showMessageDialog(d, "La cantidad debe ser mayor a 0.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(d, "Por favor ingrese un número válido.");
                }
            }
        });

        d.add(p);
        d.setVisible(true);
    }

    // ---------------------------
    // 4. VENTANA PRODUCTOS
    // ---------------------------
    void ventanaProductos() {
        JDialog d = new JDialog(this, "Catálogo de Productos", true);
        d.setSize(500, 400);
        d.setLocationRelativeTo(this);

        actualizarModeloProductos();

        JList<String> lista = new JList<>(modeloProductos);
        JScrollPane sp = new JScrollPane(lista);
        JButton add = new JButton("Agregar al carrito");

        JPanel p = new JPanel(new BorderLayout());
        p.add(sp, BorderLayout.CENTER);
        p.add(add, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            int i = lista.getSelectedIndex();
            if (i >= 0) {
                Producto pr = productos.get(i);
                if (pr.stock > 0) {
                    // Preguntar cantidad
                    String input = JOptionPane.showInputDialog(d, "¿Cuántos desea llevar? (Max: " + pr.stock + ")");
                    try {
                        int cant = Integer.parseInt(input);
                        if (cant > 0 && cant <= pr.stock) {
                            for(int k=0; k<cant; k++){
                                carrito.add(new Producto(pr.nombre, pr.precio, 1));
                                modeloCarrito.addElement(pr.nombre + " - $" + pr.precio);
                            }
                            JOptionPane.showMessageDialog(d, "Agregado(s) " + cant + " artículo(s) al carrito.");
                            mostrarMenu(); // Actualizar contador del menú principal
                        } else {
                            JOptionPane.showMessageDialog(d, "Cantidad no válida o stock insuficiente.");
                        }
                    } catch (Exception ex) {
                       // Usuario canceló o no puso número
                    }
                } else {
                    JOptionPane.showMessageDialog(d, "Producto agotado.");
                }
            } else {
                JOptionPane.showMessageDialog(d, "Seleccione un producto primero.");
            }
        });

        d.add(p);
        d.setVisible(true);
    }

    // ---------------------------
    // 5. VENTANA CARRITO
    // ---------------------------
    void ventanaCarrito() {
        JDialog d = new JDialog(this, "Tu Carrito", true);
        d.setSize(400, 400);
        d.setLocationRelativeTo(this);

        JList<String> lista = new JList<>(modeloCarrito);
        JScrollPane sp = new JScrollPane(lista);

        // Calcular total
        double total = 0;
        for(Producto p : carrito) total += p.precio;
        
        JLabel lblTotal = new JLabel("Total a pagar: $" + String.format("%.2f", total));
        lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));

        JButton comprar = new JButton("Proceder al pago");

        comprar.addActionListener(e -> {
            if (!carrito.isEmpty()) {
                d.dispose();
                ventanaPago();
            } else {
                JOptionPane.showMessageDialog(d, "El carrito está vacío.");
            }
        });

        JPanel p = new JPanel(new BorderLayout());
        p.add(sp, BorderLayout.CENTER);
        
        JPanel bottom = new JPanel(new GridLayout(2,1));
        bottom.add(lblTotal);
        bottom.add(comprar);
        p.add(bottom, BorderLayout.SOUTH);

        d.add(p);
        d.setVisible(true);
    }

    // ---------------------------
    // 6. VENTANA PAGO
    // ---------------------------
    void ventanaPago() {
        JDialog d = new JDialog(this, "Pasarela de Pago", true);
        d.setSize(400, 400);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(8, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JComboBox<String> metodo = new JComboBox<>(new String[]{"Tarjeta", "PayPal", "Efectivo"});
        JTextField tNum = new JTextField();
        JTextField tCVV = new JTextField();
        JTextField tMail = new JTextField();
        JButton pagar = new JButton("CONFIRMAR PAGO");

        p.add(new JLabel("Seleccione Método:")); p.add(metodo);
        p.add(new JLabel("Número de tarjeta (Si aplica):")); p.add(tNum);
        p.add(new JLabel("CVV (Si aplica):")); p.add(tCVV);
        p.add(new JLabel("Correo PayPal (Si aplica):")); p.add(tMail);
        p.add(pagar);

        // Lógica simple para habilitar/deshabilitar campos visualmente
        metodo.addActionListener(e -> {
            String m = metodo.getSelectedItem().toString();
            tNum.setEnabled(m.equals("Tarjeta"));
            tCVV.setEnabled(m.equals("Tarjeta"));
            tMail.setEnabled(m.equals("PayPal"));
        });

        pagar.addActionListener(e -> {
            // Validaciones básicas
            String m = metodo.getSelectedItem().toString();
            if(m.equals("Tarjeta") && (tNum.getText().isEmpty() || tCVV.getText().isEmpty())){
                JOptionPane.showMessageDialog(d, "Faltan datos de la tarjeta.");
                return;
            }
            if(m.equals("PayPal") && tMail.getText().isEmpty()){
                JOptionPane.showMessageDialog(d, "Falta el correo de PayPal.");
                return;
            }

            d.dispose();
            finalizarCompra();
        });

        d.add(p);
        d.setVisible(true);
    }

    // ---------------------------
    // 7. PROCESAR COMPRA Y STOCK
    // ---------------------------
    void finalizarCompra() {
        // Descontar del stock real
        for (Producto pc : carrito) {
            for (Producto pr : productos) {
                if (pc.nombre.equals(pr.nombre)) {
                    pr.stock -= 1;
                }
            }
        }
        
        // Limpiar carrito visual
        carrito.clear();
        modeloCarrito.clear();
        mostrarMenu(); // Refrescar menú principal

        ventanaDireccion();
    }

    // ---------------------------
    // 8. DIRECCIÓN
    // ---------------------------
    void ventanaDireccion() {
        JDialog d = new JDialog(this, "Datos de Envío", true);
        d.setSize(400, 200);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(3, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JTextField dir = new JTextField();
        JButton fin = new JButton("Finalizar Pedido");

        p.add(new JLabel("Ingrese dirección de entrega:"));
        p.add(dir);
        p.add(fin);

        fin.addActionListener(e -> {
            if(!dir.getText().isEmpty()){
                d.dispose();
                ventanaAgradecimiento(dir.getText());
            } else {
                JOptionPane.showMessageDialog(d, "Por favor ingrese una dirección.");
            }
        });

        d.add(p);
        d.setVisible(true);
    }

    // ---------------------------
    // 9. AGRADECIMIENTO
    // ---------------------------
    void ventanaAgradecimiento(String direccion) {
        JDialog d = new JDialog(this, "¡Pedido Exitoso!", true);
        d.setSize(400, 300);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(5, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JLabel icon = new JLabel("✔");
        icon.setFont(new Font("Serif", Font.BOLD, 40));
        icon.setForeground(Color.GREEN);
        icon.setHorizontalAlignment(SwingConstants.CENTER);

        p.add(icon);
        p.add(new JLabel("¡Gracias por su compra!", SwingConstants.CENTER));
        p.add(new JLabel("Enviaremos su pedido a: " + direccion, SwingConstants.CENTER));
        
        JButton salir = new JButton("Cerrar Tienda");
        salir.addActionListener(e -> System.exit(0));
        p.add(salir);

        d.add(p);
        d.setVisible(true);
    }

    // ---------------------------
    // UTILIDADES
    // ---------------------------
    void actualizarModeloProductos() {
        modeloProductos.clear();
        for (Producto p : productos) {
            modeloProductos.addElement(p.nombre + " - $" + p.precio + " [Disp: " + p.stock + "]");
        }
    }

    public static void main(String[] args) {
        // Estilo visual del sistema operativo
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e){}
        
        SwingUtilities.invokeLater(() -> {
            TiendaPF t = new TiendaPF();
            t.setVisible(true);
        });
    }
}