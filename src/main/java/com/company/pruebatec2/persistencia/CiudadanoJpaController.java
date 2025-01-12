package com.company.pruebatec2.persistencia;

import com.company.pruebatec2.logica.Ciudadano;
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

public class CiudadanoJpaController implements Serializable {

    public CiudadanoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("TurnoPU");
    }    
    
    public CiudadanoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Crear ciudadano
    public void create(Ciudadano ciudadano) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(ciudadano);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(CiudadanoJpaController.class.getName()).log(Level.SEVERE, "Error al crear ciudadano", e);
        } finally {
            em.close();
        }
    }

    // Editar ciudadano
    public void edit(Ciudadano ciudadano) throws NonexistentEntityException, Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            ciudadano = em.merge(ciudadano);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCiudadano(ciudadano.getId()) == null) {
                throw new NonexistentEntityException("El ciudadano con id " + ciudadano.getId() + " no existe.");
            }
            Logger.getLogger(CiudadanoJpaController.class.getName()).log(Level.SEVERE, "Error al editar ciudadano", ex);
            throw ex;
        } finally {
            em.close();
        }
    }

    // Eliminar ciudadano
    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Ciudadano ciudadano;
            try {
                ciudadano = em.getReference(Ciudadano.class, id);
                ciudadano.getId();
            } catch (Exception ex) {
                throw new NonexistentEntityException("El ciudadano con id " + id + " no existe.", ex);
            }
            em.remove(ciudadano);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(CiudadanoJpaController.class.getName()).log(Level.SEVERE, "Error al eliminar ciudadano", e);
        } finally {
            em.close();
        }
    }

    // Encontrar lista de ciudadanos
    public List<Ciudadano> findCiudadanoEntities() {
        return findCiudadanoEntities(true, -1, -1);
    }

    // Encontrar lista de ciudadanos con límites de resultados
    public List<Ciudadano> findCiudadanoEntities(int maxResults, int firstResult) {
        return findCiudadanoEntities(false, maxResults, firstResult);
    }

    // Método auxiliar para encontrar lista de ciudadanos
    private List<Ciudadano> findCiudadanoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudadano.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(CiudadanoJpaController.class.getName()).log(Level.SEVERE, "Error al traer ciudadanos", e);
            return null;
        } finally {
            em.close();
        }
    }

    // Encontrar ciudadano por id
    public Ciudadano findCiudadano(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudadano.class, id);
        } catch (Exception e) {
            Logger.getLogger(CiudadanoJpaController.class.getName()).log(Level.SEVERE, "Error al encontrar ciudadano", e);
            return null;
        } finally {
            em.close();
        }
    }
}
