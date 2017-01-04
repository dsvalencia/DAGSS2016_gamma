/*
 Proyecto Java EE, DAGSS-2016
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class PrescripcionDAO extends GenericoDAO<Prescripcion> {

    public Prescripcion buscarPorIdConRecetas(Long id) {
        TypedQuery<Prescripcion> q = em.createQuery("SELECT p FROM Prescripcion AS p JOIN FETCH p.recetas"
                                                  + "  WHERE p.id = :id", Prescripcion.class);
        q.setParameter("id", id);
        
        return q.getSingleResult();
    }
    
    // Completar aqui  

    public Prescripcion buscarUltimaPrescripcionPorID(long id) {
       TypedQuery<Prescripcion> q = em.createQuery("SELECT p FROM Prescripcion AS p JOIN FETCH p.recetas"
                                                  + "  WHERE p.pacienteid = :id", Prescripcion.class);
        q.setParameter("id", id);
        List<Prescripcion> listaPrescripciones=  q.getResultList();
        return listaPrescripciones.get(listaPrescripciones.size());
    }
    
    public List<Prescripcion> getPrescripcionesPaciente(long idPaciente){
        TypedQuery<Prescripcion> q = em.createQuery("SELECT prescripcion FROM Prescripcion AS prescripcion"
                                                  + "  WHERE prescripcion.paciente.id = :idPaciente", Prescripcion.class);
        q.setParameter("idPaciente", idPaciente);
        List<Prescripcion> listaPrescripciones=  q.getResultList();
        return listaPrescripciones;
    }
}
