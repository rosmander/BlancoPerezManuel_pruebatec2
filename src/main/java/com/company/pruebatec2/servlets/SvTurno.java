package com.company.pruebatec2.servlets;

import com.company.pruebatec2.logica.Controladora;
import com.company.pruebatec2.logica.Turno;
import com.company.pruebatec2.logica.Ciudadano;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvTurno", urlPatterns = {"/SvTurno"})
public class SvTurno extends HttpServlet {
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
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");

            // Obtener datos desde el JSP
            String fechaBusqueda = request.getParameter("fecha_busqueda");
            String estadoBusqueda = request.getParameter("estado_busqueda");

            // Convertir la fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fecha = LocalDate.parse(fechaBusqueda, formatter);

            // Filtrar turnos por fecha y estado
            List<Turno> listTurnos = controlLogica.traerTurnosFiltrados(fecha, estadoBusqueda);

            // Establecer los resultados en la solicitud para que se muestren en el JSP
            if (listTurnos.isEmpty()) {
                request.setAttribute("mensajeSinResultados", "No existen turnos con los campos seleccionados.");
            } else {
                request.setAttribute("turnos", listTurnos);
            }

            // Redirigir de vuelta al formulario con un ancla para los resultados
            request.getRequestDispatcher("index.jsp#resultados").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMensaje", "Ocurrió un error al buscar los turnos: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
            Logger.getLogger(SvTurno.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");

            // Obtener datos desde el JSP
            String numeroTurno = request.getParameter("numero_turno");
            String fechaIngresada = request.getParameter("fecha_turno");
            String descripcionTramite = request.getParameter("descripcion_tramite");
            String estadoTurno = request.getParameter("estado_turno");
            String ciudadanoId = request.getParameter("ciudadano_id");

            // Validaciones del lado del servidor
            if (numeroTurno == null || numeroTurno.isEmpty() || !numeroTurno.matches("\\d+")) {
                request.setAttribute("errorMensaje", "El número de turno debe ser un valor numérico positivo.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            if (descripcionTramite == null || descripcionTramite.trim().isEmpty()) {
                request.setAttribute("errorMensaje", "La descripción del trámite no puede estar vacía.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            if (ciudadanoId == null || ciudadanoId.isEmpty() || !ciudadanoId.matches("\\d+")) {
                request.setAttribute("errorMensaje", "El ID del ciudadano debe ser un valor numérico positivo.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Convertir la fecha ingresada al formato correcto
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fecha = LocalDate.parse(fechaIngresada, formatter);

            // Obtener el ciudadano por ID
            Ciudadano ciudadano = controlLogica.obtenerCiudadano(Long.parseLong(ciudadanoId));

            // Crear nuevo TURNO
            Turno turno = new Turno();
            turno.setNumero(Integer.parseInt(numeroTurno));
            turno.setFecha(fecha);
            turno.setDescripcionDelTramite(descripcionTramite);
            turno.setEstado(estadoTurno);
            turno.setCiudadano(ciudadano);

            // Mandar a persistir el turno
            try {
                controlLogica.crearTurno(turno);
                // Establecer mensaje de éxito
                request.setAttribute("mensajeExitoTurno", "El turno ha sido agregado con éxito.");
            } catch (IllegalArgumentException e) {
                request.setAttribute("errorMensaje", "El ciudadano ya tiene un turno con el mismo número, descripción o estado en la misma fecha.");
            }

            // Redirigir de vuelta al formulario
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMensaje", "Ocurrió un error al agregar el turno: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
            Logger.getLogger(SvTurno.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
