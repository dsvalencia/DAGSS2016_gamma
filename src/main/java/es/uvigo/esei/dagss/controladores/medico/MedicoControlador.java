/*
 Proyecto Java EE, DAGSS-2013
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.controladores.prescripcion.PrescripcionControlador;
import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoCita;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author ribadas
 */

@Named(value = "medicoControlador")
@SessionScoped
public class MedicoControlador implements Serializable {

    private Medico medicoActual;
    private String dni;
    private String numeroColegiado;
    private String password;
    private PrescripcionControlador prescripcionControlador;
    private Cita citaActual;
    private String estadoCita;
    private Prescripcion prescripcionActual;

    public String getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(String estadoCita) {
        this.estadoCita = estadoCita;
    }
    

    @Inject
    private AutenticacionControlador autenticacionControlador;
    

    @EJB
    private MedicoDAO medicoDAO;
    
    
     @EJB
    private CitaDAO citaDAO;
     
    /**
     * Creates a new instance of AdministradorControlador
     */
    public MedicoControlador() {
    }
   
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Medico getMedicoActual() {
        return medicoActual;
    }

    public void setMedicoActual(Medico medicoActual) {
        this.medicoActual = medicoActual;
    }

    private boolean parametrosAccesoInvalidos() {
        return (((dni == null) && (numeroColegiado == null)) || (password == null));
    }

    private Medico recuperarDatosMedico() {
        Medico medico = null;
        if (dni != null) {
            medico = medicoDAO.buscarPorDNI(dni);
        }
        if ((medico == null) && (numeroColegiado != null)) {
            medico = medicoDAO.buscarPorNumeroColegiado(numeroColegiado);
        }
        return medico;
    }
     public Cita getCitaActual(){return this.citaActual;}
         
     public void setCitaActual(Cita citaActual){this.citaActual=citaActual;}

     
     public List<Cita> consultarCitasHoy(){ 
        
        Date today = Calendar.getInstance().getTime();
        String DATE_FORMAT = "yyyyMMdd";
        SimpleDateFormat sdf =new SimpleDateFormat(DATE_FORMAT);
        
        return  citaDAO.buscarCitasMedico(medicoActual.getId(),today);
      }
     
    public void doAddPrescripcion(Medicamento medicamento){
        prescripcionControlador.doPrescripcionNueva(medicamento, null, null, null, 0, null);
    }
    
    public void doFinalizarCitaRealizada(){
        citaActual.setEstado(EstadoCita.COMPLETADA);
        this.citaActual= this.citaDAO.actualizar(citaActual);
    }
    public void doFinalizarCitaNoRealizada(){
        citaActual.setEstado(EstadoCita.AUSENTE);
        this.citaActual=citaDAO.actualizar(citaActual);
        
    }
    
    public String doShowPrescripciones(){
       this.prescripcionActual= prescripcionControlador.getUltimaPrescripcion(citaActual.getPaciente().getId());
       String destino= "verTramiento";
       return destino;
    }
    
    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un DNI ó un número de colegiado o una contraseña", ""));
        } else {
            Medico medico = recuperarDatosMedico();
            if (medico == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe ningún médico con los datos indicados", ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(medico.getId(), password,
                        TipoUsuario.MEDICO.getEtiqueta().toLowerCase())) {
                    medicoActual = medico;
                    destino = "privado/index";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }
            }
        }
        return destino;
    }
    //Acciones
    public String doShowCita() {
        return "detallesCita";
    }
}
