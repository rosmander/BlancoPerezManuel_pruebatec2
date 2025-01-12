package com.company.pruebatec2.persistencia;

import com.company.pruebatec2.logica.Turno;
import com.company.pruebatec2.persistencia.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public class TurnoJpaController implements Serializable {

    public TurnoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("TurnoPU");
    }    
    
    public TurnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Crear turno
    public void create(Turno turno) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(turno);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(TurnoJpaController.class.getName()).log(Level.SEVERE, "Error al crear turno", e);
        } finally {
            em.close();
        }
    }

    // Editar turno
    public void edit(Turno turno) throws NonexistentEntityException, Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            turno = em.merge(turno);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTurno(turno.getId()) == null) {
                throw new NonexistentEntityException("El turno con id " + turno.getId() + " no existe.");
            }
            Logger.getLogger(TurnoJpaController.class.getName()).log(Level.SEVERE, "Error al editar turno", ex);
            throw ex;
        } finally {
            em.close();
        }
    }

    // Eliminar turno
    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Turno turno;
            try {
                turno = em.getReference(Turno.class, id);
                turno.getId();
            } catch (Exception ex) {
                throw new NonexistentEntityException("El turno con id " + id + " no existe.", ex);
            }
            em.remove(turno);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(TurnoJpaController.class.getName()).log(Level.SEVERE, "Error al eliminar turno", e);
        } finally {
            em.close();
        }
    }

    // Encontrar lista de turnos
    public List<Turno> findTurnoEntities() {
        return findTurnoEntities(true, -1, -1);
    }

    // Encontrar lista de turnos con límites de resultados
    public List<Turno> findTurnoEntities(int maxResults, int firstResult) {
        return findTurnoEntities(false, maxResults, firstResult);
    }

    // Método auxiliar para encontrar lista de turnos
    private List<Turno> findTurnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Turno.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(TurnoJpaController.class.getName()).log(Level.SEVERE, "Error al traer turnos", e);
            return null;
        } finally {
            em.close();
        }
    }

    // Encontrar turno por id
    public Turno findTurno(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Turno.class, id);
        } catch (Exception e) {
            Logger.getLogger(TurnoJpaController.class.getName()).log(Level.SEVERE, "Error al encontrar turno", e);
            return null;
        } finally {
            em.close();
        }
    }
}
