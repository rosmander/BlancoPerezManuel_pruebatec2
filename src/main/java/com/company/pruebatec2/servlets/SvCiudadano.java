package com.company.pruebatec2.servlets;

import com.company.pruebatec2.logica.Ciudadano;
import com.company.pruebatec2.logica.Controladora;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvCiudadano", urlPatterns = {"/SvCiudadano"})
public class SvCiudadano extends HttpServlet {
    Controladora controlLogica = new Controladora();
    
    // Process request
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtener los ciudadanos desde la BD
            List<Ciudadano> listCiudadanos = controlLogica.traerCiudadanos();
            
            // Establecer los resultados en la solicitud para que se muestren en el JSP
            request.setAttribute("ciudadanos", listCiudadanos);
            
            // Redirigir de vuelta al formulario
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMensaje", "Ocurrió un error al obtener los ciudadanos: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");

            // Tomar datos del JSP
            String nombre = request.getParameter("nombre_ciudadano");
            String apellido = request.getParameter("apellido_ciudadano");

            // Validaciones del lado del servidor
            if (nombre == null || nombre.trim().isEmpty()) {
                request.setAttribute("errorMensaje", "El nombre del ciudadano no puede estar vacío.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            if (apellido == null || apellido.trim().isEmpty()) {
                request.setAttribute("errorMensaje", "El apellido del ciudadano no puede estar vacío.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Crear un nuevo objeto Ciudadano
            Ciudadano ciudadano = new Ciudadano();
            ciudadano.setNombre(nombre);
            ciudadano.setApellido(apellido);

            // Persistir el ciudadano en la BD
            try {
                controlLogica.crearCiudadano(ciudadano);
                // Establecer mensaje de éxito
                request.setAttribute("mensajeExito", "El ciudadano ha sido agregado con éxito.");
            } catch (IllegalArgumentException e) {
                request.setAttribute("errorMensaje", e.getMessage());
            }

            // Redirigir de vuelta al formulario
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMensaje", "Ocurrió un error al agregar el ciudadano: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
            Logger.getLogger(SvCiudadano.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
