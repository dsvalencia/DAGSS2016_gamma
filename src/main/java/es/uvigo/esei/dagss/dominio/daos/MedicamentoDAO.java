/*
 Proyecto Java EE, DAGSS-2014
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class MedicamentoDAO extends GenericoDAO<Medicamento> {

     public List<Medicamento> buscarPorNombre(String nombre) {
        TypedQuery<Medicamento> q = em.createQuery("SELECT p FROM Medicamento "
                                                  + "  WHERE p.nombre = :nombre", Medicamento.class);
        q.setParameter("nombre", nombre);
        
        return q.getResultList();
    }
     public List<Medicamento> buscarPorPrincipioActivo(String principioActivo) {
        TypedQuery<Medicamento> q = em.createQuery("SELECT p FROM Medicamento "
                                                  + "  WHERE p.principioactivo = :principioActivo", Medicamento.class);
        q.setParameter("principioActivo", principioActivo);
        
        return q.getResultList();
    }
       
      public List<Medicamento> buscarPorFabricante(String fabricante) {
        TypedQuery<Medicamento> q = em.createQuery("SELECT p FROM Medicamento "
                                                  + "  WHERE p.fabricante = :fabricante", Medicamento.class);
        q.setParameter("fabricante", fabricante);
        
        return q.getResultList();
    }
      
      public List<Medicamento> buscarPorFamilia(String familia) {
        TypedQuery<Medicamento> q = em.createQuery("SELECT p FROM Medicamento "
                                                  + "  WHERE p.familia = :familia", Medicamento.class);
        q.setParameter("familia", familia);
        
        return q.getResultList();
    }
    

}
