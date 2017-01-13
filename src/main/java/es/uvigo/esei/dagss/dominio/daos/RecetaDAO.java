/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Receta;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class RecetaDAO extends GenericoDAO<Receta>{
 
    public List<Receta> buscarPorTarjeta(String numTarjeta){
      TypedQuery <Receta> q = em.createQuery("SELECT receta FROM Receta AS receta "
                + "  WHERE (receta.prescripcion.paciente.numeroTarjetaSanitaria = :numTarjetaSanitaria)", Receta.class);
    
        q.setParameter("numTarjetaSanitaria",numTarjeta);
                
        return q.getResultList();
    }
}
