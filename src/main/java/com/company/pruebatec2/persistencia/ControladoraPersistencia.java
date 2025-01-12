package com.company.pruebatec2.persistencia;

import com.company.pruebatec2.logica.Ciudadano;
import com.company.pruebatec2.logica.Turno;
import com.company.pruebatec2.persistencia.exceptions.NonexistentEntityException;
import java.util.List;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ControladoraPersistencia {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TurnoPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    CiudadanoJpaController ciudadanoJPA = new CiudadanoJpaController(emf);
    TurnoJpaController turnoJPA = new TurnoJpaController(emf);
    private Logger logger = Logger.getLogger(ControladoraPersistencia.class.getName());
    
    /* PARA CIUDADANOS */
    // Método para crear ciudadano
    public void crearCiudadano(Ciudadano ciudadano) {
        try {
            ciudadanoJPA.create(ciudadano);
        } catch (Exception e) {
            manejarExcepcion(e, "Error al crear ciudadano");
        }
    }
    
    // Método para eliminar ciudadano
    public void eliminarCiudadano(Long id) {
        try {
            ciudadanoJPA.destroy(id);
        } catch (NonexistentEntityException ex) {
            manejarExcepcion(ex, "El ciudadano con id " + id + " no existe.");
        } catch (Exception e) {
            manejarExcepcion(e, "Error al eliminar ciudadano");
        }
    }
    
    // Método para traer ciudadanos
    public List<Ciudadano> traerCiudadanos() {
        try {
            return ciudadanoJPA.findCiudadanoEntities();
        } catch (Exception e) {
            manejarExcepcion(e, "Error al traer ciudadanos");
            return null;
        }
    }
    
    // Método para editar ciudadano
    public void editarCiudadano(Ciudadano ciudadano) {
        try {
            ciudadanoJPA.edit(ciudadano);
        } catch (NonexistentEntityException ex) {
            manejarExcepcion(ex, "El ciudadano con id " + ciudadano.getId() + " no existe.");
        } catch (Exception ex) {
            manejarExcepcion(ex, "Error al editar ciudadano");
        }
    }
    
    // Método para obtener ciudadano por id
    public Ciudadano obtenerCiudadano(Long id) {
        try {
            return ciudadanoJPA.findCiudadano(id);
        } catch (Exception e) {
            manejarExcepcion(e, "Error al obtener ciudadano");
            return null;
        }
    }
        
    /* PARA TURNOS */
    // Método para crear turno
    public void crearTurno(Turno turno) {
        try {
            turnoJPA.create(turno);
        } catch (Exception e) {
            manejarExcepcion(e, "Error al crear turno");
        }
    }
    
    // Método para eliminar turno
    public void eliminarTurno(Long id) {
        try {
            turnoJPA.destroy(id);
        } catch (NonexistentEntityException ex) {
            manejarExcepcion(ex, "El turno con id " + id + " no existe.");
        } catch (Exception e) {
            manejarExcepcion(e, "Error al eliminar turno");
        }
    }
    
    // Método para traer turnos
    public List<Turno> traerTurnos() {
        try {
            return turnoJPA.findTurnoEntities();
        } catch (Exception e) {
            manejarExcepcion(e, "Error al traer turnos");
            return null;
        }
    }
    
    // Método para editar turno
    public void editarTurno(Turno turno) {
        try {
            turnoJPA.edit(turno);
        } catch (NonexistentEntityException ex) {
            manejarExcepcion(ex, "El turno con id " + turno.getId() + " no existe.");
        } catch (Exception ex) {
            manejarExcepcion(ex, "Error al editar turno");
        }
    }    
    
    // Método para traer turnos filtrados
    public List<Turno> traerTurnosFiltrados(LocalDate fecha, String estado) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Turno> cq = cb.createQuery(Turno.class);
            Root<Turno> root = cq.from(Turno.class);

            Predicate fechaPredicate = cb.equal(root.get("fecha"), fecha);
            Predicate estadoPredicate = cb.equal(root.get("estado"), estado);
            cq.where(cb.and(fechaPredicate, estadoPredicate));

            Query query = em.createQuery(cq);
            return query.getResultList();
        } catch (Exception e) {
            manejarExcepcion(e, "Error al filtrar turnos");
            return null;
        } finally {
            em.close();
        }
    }

    // Método para manejar excepciones
    private void manejarExcepcion(Exception e, String mensaje) {
        logger.log(Level.SEVERE, mensaje + ": " + e.getMessage(), e);
    }
}
