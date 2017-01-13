/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.controladores.prescripcion;

import java.io.Serializable;
import javax.inject.Named;
import javax.inject.Singleton;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;


@Named(value="prescripcionControlador")
@SessionScoped
public class PrescripcionControlador implements Serializable{
    @EJB
    PrescripcionDAO prescripcionDAO; 
    
    @EJB 
    RecetaDAO recetaDAO;
    
    public PrescripcionControlador(){
    }

    /**
     *
     * @param medicamento
     * @param citaActual
     * @param fechaInicio
     * @param fechaFin
     * @param dosis
     * @param indicaciones
     */
    public void doPrescripcionNueva(Medicamento medicamento, Cita citaActual, Date fechaInicio, Date fechaFin, int dosis, String indicaciones){
        Prescripcion p = new Prescripcion(citaActual.getPaciente(), medicamento,citaActual.getMedico(),dosis,indicaciones, fechaInicio, fechaFin);
        
        
        LocalDate begin = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        Period time = Period.between(begin, end);
        
        int numRecetas=(time.getDays()*dosis) / medicamento.getNumeroDosis();
        System.out.println("Las recetas totales son" + numRecetas);
    }

   public Prescripcion getUltimaPrescripcion(long id) {
       return prescripcionDAO.buscarUltimaPrescripcionPorID( id);
       
    }

   public List<Prescripcion> getPrescripcionesPaciente(Paciente paciente){
       return prescripcionDAO.getPrescripcionesPaciente(paciente.getId());
   }
   
   public void eliminarPrescripcion(Prescripcion prescripcion){
       prescripcionDAO.eliminar(prescripcion);
   }
}
