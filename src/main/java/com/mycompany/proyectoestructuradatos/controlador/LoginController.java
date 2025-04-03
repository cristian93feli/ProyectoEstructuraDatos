package com.mycompany.proyectoestructuradatos.controlador;



import com.mycompany.proyectoestructuradatos.controlador.gestorDeVistas.GestorDeVistas;
import com.mycompany.proyectoestructuradatos.vista.JFrmLogin;
import com.mycompany.proyectoestructuradatos.modelo.dao.UsuarioDao;
import com.mycompany.proyectoestructuradatos.modelo.entidad.Usuario;
import com.mycompany.proyectoestructuradatos.sesion.SesionUsuario;
import com.mycompany.proyectoestructuradatos.vista.JFmCrearUsuario;
import com.mycompany.proyectoestructuradatos.vista.JFmMenu;
import javax.swing.JOptionPane;


public class LoginController {

    private final UsuarioDao usuarioDAO;
    private final JFrmLogin vista;

    public LoginController(UsuarioDao usuarioDAO, JFrmLogin vista) {
        this.usuarioDAO = usuarioDAO;
        this.vista = vista;

        this.vista.getBtnAceptar().addActionListener(e -> autenticarUsuario());
        this.vista.getBtnRegistrar().addActionListener(e -> crearUsuario());
    }

    public LoginController(JFrmLogin vista) {
        this.usuarioDAO = new UsuarioDao();
        this.vista = vista;

        this.vista.getBtnAceptar().addActionListener(e -> autenticarUsuario());
    }

    private void autenticarUsuario() {
        try {
            String username = vista.getUsuario();
            String password = vista.getPassword();

            Usuario usuario = usuarioDAO.obtenerUsuario(username, password);

            // Validar el rol obtenido desde la tabla empleado
            if (usuario != null) {
                vista.limpiarcampos();
//                // Redirigir según el rol
                switch (usuario.getIdRol()) {
                    case 1:
                        JOptionPane.showMessageDialog(vista, "Ingreso exitoso");
                        SesionUsuario.getInstancia().setUsuario(usuario);
                        JFmMenu mantenimientoVista = new JFmMenu();
                        MenuController maneteminetoController = new MenuController(mantenimientoVista, vista); // Instancia del controlador
                        GestorDeVistas.mostrarVista(vista, mantenimientoVista);
                        

                        break;
//                    case 2:
//                        break;
                    default:
                        JOptionPane.showMessageDialog(vista, "¡Rol no reconocido!");
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Usuario o contraseña incorrectos");
                vista.limpiarcampos();
            }

        } catch (Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Ocurrio un error al intentar ingresar a la opcion seleccionada. "
                    + e.getMessage(), "Error login", JOptionPane.ERROR_MESSAGE);
            
        }
    }
    
    public void crearUsuario() {
        JFmCrearUsuario crearUsuario = new JFmCrearUsuario();
        CrearUsuarioController administradorController = new CrearUsuarioController(crearUsuario, vista);
        vista.limpiarcampos();
        GestorDeVistas.mostrarVista(vista, crearUsuario);
    }
}
