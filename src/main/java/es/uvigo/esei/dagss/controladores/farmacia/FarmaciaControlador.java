/*
 Proyecto Java EE, DAGSS-2013
 */
package es.uvigo.esei.dagss.controladores.farmacia;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.FarmaciaDAO;

import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;

import es.uvigo.esei.dagss.dominio.entidades.EstadoReceta;
import es.uvigo.esei.dagss.dominio.entidades.Farmacia;

import es.uvigo.esei.dagss.dominio.entidades.Receta;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author ribadas
 */
@Named(value = "farmaciaControlador")
@SessionScoped
public class FarmaciaControlador implements Serializable {

    private Farmacia farmaciaActual;
    private Receta recetaActual;
    
    private String nif;
    private String password;
    
    private String tarjetaUsuario;
    
    private List<Receta> listaRecetasUsuario;

    @Inject
    private AutenticacionControlador autenticacionControlador;

    @EJB
    private FarmaciaDAO farmaciaDAO;
    
     @EJB
    private RecetaDAO recetaDAO;

    /**
     * Creates a new instance of AdministradorControlador
     */
    public FarmaciaControlador() {
    }

    public void setTarjetaUsuario(String tarjetaUsuario) {
        this.tarjetaUsuario = tarjetaUsuario;
    }

    public String getTarjetaUsuario() {
        return tarjetaUsuario;
    }
    
    public Receta getRecetaActual(){
    return this.recetaActual;
    }
    
    public String getNif() {
        return nif;
    }

    public void setRecetaActual(Receta receta){
        this.recetaActual=receta;
    }
    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Farmacia getFarmaciaActual() {
        return farmaciaActual;
    }

    public void setFarmaciaActual(Farmacia farmaciaActual) {
        this.farmaciaActual = farmaciaActual;
    }

    private boolean parametrosAccesoInvalidos() {
        return ((nif == null) || (password == null));
    }

    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un nif o una contraseña", ""));
        } else {
            Farmacia farmacia = farmaciaDAO.buscarPorNIF(nif);
            if (farmacia == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe una farmacia con el NIF " + nif, ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(farmacia.getId(), password,
                        TipoUsuario.FARMACIA.getEtiqueta().toLowerCase())) {
                    farmaciaActual = farmacia;
                    destino = "privado/index";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }

            }
        }
        return destino;
    }
    
    public void suministrarReceta(Receta receta){
        if( System.currentTimeMillis() >= receta.getInicioValidez().getTime() && System.currentTimeMillis() < receta.getFinValidez().getTime()){
            if(receta.getEstadoReceta().compareTo(EstadoReceta.GENERADA)==0){
            receta.setEstadoReceta(EstadoReceta.SERVIDA);
            receta.setFarmaciaDispensadora(farmaciaActual);
            recetaActual=recetaDAO.actualizar(receta);
            }else{
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al suministrar", ""));   
            }
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede suministrar fuera de la fecha", ""));
        }
     
    }
    
    public void buscarPorTarjeta(){
        if(this.tarjetaUsuario.equals("")){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "numero de tarjeta vacio", ""));
        }else{
            this.listaRecetasUsuario = this.recetaDAO.buscarPorTarjeta(this.tarjetaUsuario);
        }
    }

    public List<Receta> getListaRecetasUsuario() {
        return this.listaRecetasUsuario;
    }
}
