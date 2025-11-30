/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tiendapf;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class TiendaPF extends JFrame {

    
    //  MODELO DE DATOS
    
    class Producto {
        String nombre;
        double precio;
        int stock;

        Producto(String n, double p, int s) {
            this.nombre = n;
            this.precio = p;
            this.stock = s;
        }
    }

    
    // DATOS GLOBALES
   
    ArrayList<Producto> productos = new ArrayList<>();
    
    // Inicializador de bloque para llenar productos por defecto
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
        setTitle("TiendaPF - Sistema de Ventas");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mostrarLogin();
    }

    
    // 1. LOGIN
    
    void mostrarLogin() {
        carrito.clear();
        modeloCarrito.clear();
        rol = "";

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Bienvenido a TiendaPF");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel l1 = new JLabel("Usuario:");
        JLabel l2 = new JLabel("Contraseña:");
        JTextField t1 = new JTextField(15);
        JPasswordField t2 = new JPasswordField(15);
        JButton entrar = new JButton("Iniciar Sesión");

        c.gridwidth = 2; c.gridx = 0; c.gridy = 0; panel.add(title, c);
        c.gridwidth = 1; c.gridy = 1; panel.add(new JLabel(" "), c); 

        c.gridx = 0; c.gridy = 2; panel.add(l1, c);
        c.gridx = 1; panel.add(t1, c);
        
        c.gridx = 0; c.gridy = 3; panel.add(l2, c);
        c.gridx = 1; panel.add(t2, c);
        
        c.gridwidth = 2; c.gridx = 0; c.gridy = 4; c.fill = GridBagConstraints.NONE;
        panel.add(entrar, c);
        
        c.gridy = 5; 
        panel.add(new JLabel("<html><center><font size='2' color='gray'>Admin: admin/admin<br>User: user/user</font></center></html>"), c);

        setContentPane(panel);
        revalidate(); repaint();

        entrar.addActionListener(e -> {
            String user = t1.getText();
            String pass = new String(t2.getPassword());

            if (user.equals("admin") && pass.equals("admin")) {
                rol = "admin";
                mostrarMenu();
            } else if (!user.isEmpty()) { 
                rol = "cliente";
                mostrarMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña");
            }
        });
    }

    
    // 2. MENÚ PRINCIPAL
    
    void mostrarMenu() {
        int filas = rol.equals("admin") ? 4 : 3;
        JPanel p = new JPanel(new GridLayout(filas, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblRol = new JLabel("Sesión: " + rol.toUpperCase());
        lblRol.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lblRol);

        JButton verProd = new JButton("Ver Productos / Comprar");
        JButton verCart = new JButton("Ver Carrito (" + carrito.size() + ")");
        JButton logout = new JButton("Cerrar Sesión");
        logout.setBackground(new Color(255, 200, 200));

        p.add(verProd);
        p.add(verCart);

        if (rol.equals("admin")) {
            JButton adminBtn = new JButton("ADMIN: Gestionar Tienda");
            p.add(adminBtn); 
            adminBtn.addActionListener(e -> ventanaAdmin());
        }

        p.add(logout);

        verProd.addActionListener(e -> ventanaProductos());
        verCart.addActionListener(e -> ventanaCarrito());
        
        logout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Logout", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION){
                mostrarLogin();
            }
        });

        setContentPane(p);
        revalidate(); repaint();
    }

   
    // 3. ADMIN (Pestañas: Stock y Nuevo Producto)
    
    void ventanaAdmin() {
        JDialog d = new JDialog(this, "Panel de Administración", true);
        d.setSize(450, 350);
        d.setLocationRelativeTo(this);

        JTabbedPane tabs = new JTabbedPane();

        // --- PESTAÑA 1: Actualizar Stock ---
        JPanel pStock = new JPanel(new GridLayout(4, 2, 5, 5));
        pStock.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> comboProductos = new JComboBox<>();
        actualizarComboAdmin(comboProductos); 

        JTextField tCantidad = new JTextField();
        JButton btnActualizar = new JButton("Añadir Stock");

        pStock.add(new JLabel("Producto:")); pStock.add(comboProductos);
        pStock.add(new JLabel("Cantidad a sumar:")); pStock.add(tCantidad);
        pStock.add(new JLabel("")); pStock.add(btnActualizar);

        btnActualizar.addActionListener(e -> {
            int index = comboProductos.getSelectedIndex();
            if (index >= 0) {
                try {
                    int cantidad = Integer.parseInt(tCantidad.getText());
                    if (cantidad > 0) {
                        Producto pr = productos.get(index);
                        pr.stock += cantidad;
                        JOptionPane.showMessageDialog(d, "Stock actualizado.\nProducto: " + pr.nombre + "\nNuevo total: " + pr.stock);
                        actualizarComboAdmin(comboProductos); 
                        tCantidad.setText("");
                    } else {
                        JOptionPane.showMessageDialog(d, "La cantidad debe ser positiva.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(d, "Error: Ingrese un número entero.");
                }
            }
        });

        // --- PESTAÑA 2: Crear Nuevo Producto ---
        JPanel pNuevo = new JPanel(new GridLayout(5, 2, 5, 5));
        pNuevo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField tNombreNew = new JTextField();
        JTextField tPrecioNew = new JTextField();
        JTextField tStockNew = new JTextField();
        JButton btnCrear = new JButton("Crear Producto");

        pNuevo.add(new JLabel("Nombre del Producto:")); pNuevo.add(tNombreNew);
        pNuevo.add(new JLabel("Precio (Ej: 1500.50):")); pNuevo.add(tPrecioNew);
        pNuevo.add(new JLabel("Stock Inicial (Ej: 10):")); pNuevo.add(tStockNew);
        pNuevo.add(new JLabel("")); pNuevo.add(btnCrear);
        pNuevo.add(new JLabel("")); pNuevo.add(new JLabel("")); // Relleno

        btnCrear.addActionListener(e -> {
            try {
                String nombre = tNombreNew.getText().trim();
                double precio = Double.parseDouble(tPrecioNew.getText());
                int stock = Integer.parseInt(tStockNew.getText());

                if (!nombre.isEmpty() && precio > 0 && stock >= 0) {
                    // Crear y añadir
                    productos.add(new Producto(nombre, precio, stock));
                    JOptionPane.showMessageDialog(d, "¡Producto '" + nombre + "' creado exitosamente!");
                    
                    // Limpiar campos
                    tNombreNew.setText("");
                    tPrecioNew.setText("");
                    tStockNew.setText("");

                    // Actualizar el combo de la otra pestaña por si el admin cambia de pestaña
                    actualizarComboAdmin(comboProductos);
                } else {
                    JOptionPane.showMessageDialog(d, "Datos inválidos. El precio debe ser > 0 y nombre no vacío.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(d, "Error en números. Verifique Precio y Stock.");
            }
        });

        tabs.addTab("Gestionar Stock", pStock);
        tabs.addTab("Nuevo Producto", pNuevo);

        d.add(tabs);
        d.setVisible(true);
    }

    void actualizarComboAdmin(JComboBox<String> combo) {
        combo.removeAllItems();
        for (Producto prod : productos) {
            combo.addItem(prod.nombre + " [Stock: " + prod.stock + "]");
        }
    }

    
    // 4. VENTANA PRODUCTOS
    
    void ventanaProductos() {
        JDialog d = new JDialog(this, "Catálogo de Productos", true);
        d.setSize(500, 400);
        d.setLocationRelativeTo(this);

        actualizarModeloProductos(); // Esto cargará los productos NUEVOS automáticamente

        JList<String> lista = new JList<>(modeloProductos);
        JScrollPane sp = new JScrollPane(lista);
        JButton add = new JButton("Agregar al carrito");

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("Seleccione un producto para comprar:"), BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        p.add(add, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            int i = lista.getSelectedIndex();
            if (i >= 0) {
                Producto pr = productos.get(i);
                
                // Lógica de stock seguro
                long enCarrito = carrito.stream().filter(c -> c.nombre.equals(pr.nombre)).count();
                int stockDisponibleReal = pr.stock - (int)enCarrito;

                if (stockDisponibleReal > 0) {
                    String input = JOptionPane.showInputDialog(d, 
                        "Stock disponible: " + stockDisponibleReal + "\n¿Cuántos desea llevar?");
                    try {
                        int cant = Integer.parseInt(input);
                        if (cant > 0 && cant <= stockDisponibleReal) {
                            for(int k=0; k<cant; k++){
                                carrito.add(new Producto(pr.nombre, pr.precio, 1));
                                modeloCarrito.addElement(pr.nombre + " - $" + pr.precio);
                            }
                            JOptionPane.showMessageDialog(d, "Agregado(s) " + cant + " artículo(s).");
                            mostrarMenu(); 
                        } else {
                            JOptionPane.showMessageDialog(d, "Cantidad no válida o excede el stock disponible.");
                        }
                    } catch (Exception ex) {}
                } else {
                    JOptionPane.showMessageDialog(d, "Producto agotado (o ya lo tienes todo en tu carrito).");
                }
            } else {
                JOptionPane.showMessageDialog(d, "Seleccione un producto primero.");
            }
        });

        d.add(p);
        d.setVisible(true);
    }

   
    // 5. VENTANA CARRITO
    
    void ventanaCarrito() {
        JDialog d = new JDialog(this, "Tu Carrito", true);
        d.setSize(400, 400);
        d.setLocationRelativeTo(this);

        JList<String> lista = new JList<>(modeloCarrito);
        JScrollPane sp = new JScrollPane(lista);

        double total = 0;
        for(Producto p : carrito) total += p.precio;
        
        JLabel lblTotal = new JLabel("Total a pagar: $" + String.format("%.2f", total));
        lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));

        JButton comprar = new JButton("Proceder al pago");
        JButton vaciar = new JButton("Vaciar Carrito");

        comprar.addActionListener(e -> {
            if (!carrito.isEmpty()) {
                d.dispose();
                ventanaPago();
            } else {
                JOptionPane.showMessageDialog(d, "El carrito está vacío.");
            }
        });

        vaciar.addActionListener(e -> {
            carrito.clear();
            modeloCarrito.clear();
            lblTotal.setText("Total a pagar: $0.00");
            mostrarMenu(); 
        });

        JPanel bottom = new JPanel(new GridLayout(3,1));
        bottom.add(lblTotal);
        bottom.add(vaciar);
        bottom.add(comprar);

        JPanel p = new JPanel(new BorderLayout());
        p.add(sp, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        d.add(p);
        d.setVisible(true);
    }

    
    // 6. VENTANA PAGO
    
    void ventanaPago() {
        JDialog d = new JDialog(this, "Pasarela de Pago", true);
        d.setSize(400, 450);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(9, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JComboBox<String> metodo = new JComboBox<>(new String[]{"Tarjeta de Crédito/Débito", "PayPal", "Efectivo"});
        JTextField tNum = new JTextField();
        JTextField tCVV = new JTextField();
        JTextField tMail = new JTextField();
        JButton pagar = new JButton("CONFIRMAR PAGO");
        pagar.setBackground(Color.GREEN);

        p.add(new JLabel("Método de Pago:")); p.add(metodo);
        p.add(new JLabel("Número de tarjeta:")); p.add(tNum);
        p.add(new JLabel("CVV:")); p.add(tCVV);
        p.add(new JLabel("Correo PayPal:")); p.add(tMail);
        p.add(new JLabel("")); 
        p.add(pagar);

        Runnable actualizarCampos = () -> {
            String m = metodo.getSelectedItem().toString();
            tNum.setEnabled(m.contains("Tarjeta"));
            tCVV.setEnabled(m.contains("Tarjeta"));
            tMail.setEnabled(m.contains("PayPal"));
            if(m.equals("Efectivo")) {
                tNum.setEnabled(false); tCVV.setEnabled(false); tMail.setEnabled(false);
            }
        };
        metodo.addActionListener(e -> actualizarCampos.run());
        actualizarCampos.run(); 

        pagar.addActionListener(e -> {
            String m = metodo.getSelectedItem().toString();
            if(m.contains("Tarjeta") && (tNum.getText().length() < 10 || tCVV.getText().length() < 3)){
                JOptionPane.showMessageDialog(d, "Datos de tarjeta inválidos.");
                return;
            }
            if(m.contains("PayPal") && !tMail.getText().contains("@")){
                JOptionPane.showMessageDialog(d, "Correo inválido.");
                return;
            }
            d.dispose();
            finalizarCompra();
        });

        d.add(p);
        d.setVisible(true);
    }

  
    // 7. PROCESAR COMPRA Y STOCK
   
    void finalizarCompra() {
        // Descontar del stock real GLOBAL
        for (Producto prodEnCarrito : carrito) {
            for (Producto prodEnTienda : productos) {
                if (prodEnCarrito.nombre.equals(prodEnTienda.nombre)) {
                    if (prodEnTienda.stock > 0) {
                        prodEnTienda.stock -= 1;
                    }
                }
            }
        }
        
        carrito.clear();
        modeloCarrito.clear();
        mostrarMenu(); 

        ventanaDireccion();
    }

    
    // 8. DIRECCIÓN
  
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
            if(!dir.getText().trim().isEmpty()){
                d.dispose();
                ventanaAgradecimiento(dir.getText());
            } else {
                JOptionPane.showMessageDialog(d, "Por favor ingrese una dirección.");
            }
        });

        d.add(p);
        d.setVisible(true);
    }

   
    // 9. AGRADECIMIENTO (Retorno al menú)
    
    void ventanaAgradecimiento(String direccion) {
        JDialog d = new JDialog(this, "¡Pedido Exitoso!", true);
        d.setSize(400, 300);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(5, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JLabel icon = new JLabel("✔");
        icon.setFont(new Font("Serif", Font.BOLD, 40));
        icon.setForeground(new Color(0, 150, 0));
        icon.setHorizontalAlignment(SwingConstants.CENTER);

        p.add(icon);
        p.add(new JLabel("¡Gracias por su compra!", SwingConstants.CENTER));
        p.add(new JLabel("Enviaremos su pedido a:", SwingConstants.CENTER));
        p.add(new JLabel(direccion, SwingConstants.CENTER));
        
        JButton volver = new JButton("Volver al Menú Principal");
        volver.addActionListener(e -> {
            d.dispose(); 
        });
        p.add(volver);

        d.add(p);
        d.setVisible(true);
    }

    
    // UTILIDADES
    
    void actualizarModeloProductos() {
        modeloProductos.clear();
        for (Producto p : productos) {
            String estado = (p.stock > 0) ? "[Disp: " + p.stock + "]" : "[AGOTADO]";
            modeloProductos.addElement(p.nombre + " - $" + p.precio + " " + estado);
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e){}
        
        SwingUtilities.invokeLater(() -> {
            TiendaPF t = new TiendaPF();
            t.setVisible(true);
        });
    }
}

