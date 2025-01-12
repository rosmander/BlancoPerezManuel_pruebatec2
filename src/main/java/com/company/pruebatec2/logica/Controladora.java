package com.company.pruebatec2.logica;

import com.company.pruebatec2.persistencia.ControladoraPersistencia;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controladora {
    ControladoraPersistencia controlPersis = new ControladoraPersistencia();
    private Logger logger = Logger.getLogger(Controladora.class.getName());

    /* CIUDADANOS */
    // Método para crear ciudadano
    public void crearCiudadano(Ciudadano ciudadano) {
        try {
            if (ciudadanoDuplicado(ciudadano.getNombre(), ciudadano.getApellido())) {
                throw new IllegalArgumentException("Ya existe un ciudadano con el mismo nombre y apellido.");
            }
            controlPersis.crearCiudadano(ciudadano);
        } catch (IllegalArgumentException e) {
            manejarExcepcion(e, "Error al crear ciudadano duplicado");
        } catch (Exception e) {
            manejarExcepcion(e, "Error inesperado al crear ciudadano");
        }
    }

    // Método para verificar si un ciudadano está duplicado
    public boolean ciudadanoDuplicado(String nombre, String apellido) {
        List<Ciudadano> ciudadanosExistentes = controlPersis.traerCiudadanos().stream()
            .filter(c -> c.getNombre().equalsIgnoreCase(nombre) && c.getApellido().equalsIgnoreCase(apellido))
            .collect(Collectors.toList());
        return !ciudadanosExistentes.isEmpty();
    }
    
    // Método para eliminar ciudadano
    public void eliminarCiudadano(Long id) {
        try {
            controlPersis.eliminarCiudadano(id);
        } catch (Exception e) {
            manejarExcepcion(e, "Error al eliminar ciudadano");
        }
    }
    
    // Método para traer ciudadanos
    public List<Ciudadano> traerCiudadanos() {
        try {
            return controlPersis.traerCiudadanos();
        } catch (Exception e) {
            manejarExcepcion(e, "Error al traer ciudadanos");
            return null;
        }
    }
    
    // Método para editar ciudadano
    public void editarCiudadano(Ciudadano ciudadano) {
        try {
            controlPersis.editarCiudadano(ciudadano);
        } catch (Exception e) {
            manejarExcepcion(e, "Error al editar ciudadano");
        }
    }
    
    // Método para obtener ciudadano por id
    public Ciudadano obtenerCiudadano(Long id) {
        try {
            return controlPersis.obtenerCiudadano(id);
        } catch (Exception e) {
            manejarExcepcion(e, "Error al obtener ciudadano");
            return null;
        }
    }

    /* TURNOS */
    // Método para crear turno
    public void crearTurno(Turno turno) {
        try {
            List<Turno> turnosExistentes = controlPersis.traerTurnos().stream()
                .filter(t -> t.getFecha().equals(turno.getFecha()) &&
                             t.getCiudadano().getId().equals(turno.getCiudadano().getId()) &&
                             t.getNumero() == turno.getNumero() &&
                             t.getDescripcionDelTramite().equals(turno.getDescripcionDelTramite()) &&
                             t.getEstado().equals(turno.getEstado()))
                .collect(Collectors.toList());

            if (turnosExistentes.isEmpty()) {
                controlPersis.crearTurno(turno);
            } else {
                throw new IllegalArgumentException("El ciudadano ya tiene un turno con el mismo número, descripción y estado en la misma fecha.");
            }
        } catch (IllegalArgumentException e) {
            manejarExcepcion(e, "Error al crear turno duplicado");
        } catch (Exception e) {
            manejarExcepcion(e, "Error inesperado al crear turno");
        }
    }    
    
    // Método para eliminar turno
    public void eliminarTurno(Long id) {
        try {
            controlPersis.eliminarTurno(id);
        } catch (Exception e) {
            manejarExcepcion(e, "Error al eliminar turno");
        }
    }
    
    // Método para traer turnos
    public List<Turno> traerTurnos() {
        try {
            return controlPersis.traerTurnos();
        } catch (Exception e) {
            manejarExcepcion(e, "Error al traer turnos");
            return null;
        }
    }
    
    // Método para editar turno
    public void editarTurno(Turno turno) {
        try {
            controlPersis.editarTurno(turno);
        } catch (Exception e) {
            manejarExcepcion(e, "Error al editar turno");
        }
    }  

    // Método para traer turnos filtrados
    public List<Turno> traerTurnosFiltrados(LocalDate fecha, String estado) {
        try {
            return controlPersis.traerTurnosFiltrados(fecha, estado);
        } catch (Exception e) {
            manejarExcepcion(e, "Error al filtrar turnos");
            return null;
        }
    }
    
    // Método para manejar excepciones
    private void manejarExcepcion(Exception e, String mensaje) {
        logger.log(Level.SEVERE, mensaje + ": " + e.getMessage(), e);
    }
}
