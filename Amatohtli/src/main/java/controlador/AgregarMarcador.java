/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Marcador;
import modelo.MarcadorDAO;
import modelo.Tema;
import modelo.TemaDAO;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;


/**
 *
 * @author ailyn
 */

@ManagedBean
@ViewScoped
public class AgregarMarcador implements Serializable {
    private int idMarcador;
    private int idTema;
    private String tema;
    private Tema temaByIdColor;
    private Tema temaByIdTema;
    private double latitud;
    private double longitud;
    private String descripcion;
    private Set comentarios = new HashSet(0);
    private Marker marcador;
    private MapModel simpleModel;
    private List<Tema> temas;
    private ArrayList<String> lista_temas;
    
    
    
    @PostConstruct
    public void init(){
        simpleModel = new DefaultMapModel();
        marcador = new Marker(new LatLng(23.382390, -102.291477),"Arrastrame");
        marcador.setDraggable(true);
        marcador.setClickable(true);
        simpleModel.addOverlay(marcador);
        this.latitud = marcador.getLatlng().getLat();
        this.longitud = marcador.getLatlng().getLng();
        TemaDAO tdao = new TemaDAO();
        ControladorSesion.UserLogged us = (ControladorSesion.UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("informador");
        temas = tdao.buscaPorInformador(us.getCorreo());
        lista_temas = new ArrayList<String>();
        descripcion = "";
        for(Tema t : temas){
            lista_temas.add(t.getNombreTema());
        }
        
        
        
        
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
    
    public int getIdTema() {
        return idTema;
    }

    public void setIdTema(int idTema) {
        this.idTema = idTema;
    }
    public Marker getMarcador() {
        return marcador;
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public int getIdMarcador() {
        return idMarcador;
    }

    public void setIdMarcador(int idMarcador) {
        this.idMarcador = idMarcador;
    }

    public Tema getTemaByIdColor() {
        return temaByIdColor;
    }

    public void setTemaByIdColor(Tema temaByIdColor) {
        this.temaByIdColor = temaByIdColor;
    }

    public Tema getTemaByIdTema() {
        return temaByIdTema;
    }

    public void setTemaByIdTema(Tema temaByIdTema) {
        this.temaByIdTema = temaByIdTema;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set comentarios) {
        this.comentarios = comentarios;
    }

    public List<Tema> getTemas() {
        return temas;
    }

    public void setTemas(List<Tema> temas) {
        this.temas = temas;
    }

    public ArrayList<String> getLista_temas() {
        return lista_temas;
    }

    public void setLista_temas(ArrayList<String> lista_temas) {
        this.lista_temas = lista_temas;
    }
    
    

      public void onMarkerDrag(MarkerDragEvent event){
        marcador = event.getMarker();
        this.latitud = marcador.getLatlng().getLat();
        this.longitud = marcador.getLatlng().getLng();
    }
      
    public void onPointSelect(PointSelectEvent event) {
        LatLng latlng = event.getLatLng();
        marcador = simpleModel.getMarkers().get(0);
        marcador.setLatlng(latlng);
        this.latitud = latlng.getLat();
        this.longitud = latlng.getLng();
        
    }

    
    public String muestraVentana(){
        return "/informador/agregarMarcadores?faces-redirect=true";
    }


    
    public void agregaMarcador(){
        TemaDAO tdao = new TemaDAO();
        Tema tema_elegido = tdao.buscaPorNombre(tema);
        Marcador m = new Marcador();
        MarcadorDAO mdao = new MarcadorDAO();
        this.temaByIdTema = tema_elegido;
        this.temaByIdColor = tema_elegido;
        if(this.temaByIdTema == null || this.temaByIdColor == null){
            Mensajes.error("El tema que usted escogió no existe, escriba otro");
        }else if(this.temaByIdTema != null && this.temaByIdColor != null ){
            System.out.println("Detectó tema");
            m.setLatitud(latitud);
            m.setLongitud(longitud);
            m.setTemaByIdColor(temaByIdColor);
            m.setTemaByIdTema(temaByIdTema);
            m.setDescripcion(descripcion);
            Marcador marc = mdao.buscaMarcadorPorLatLng(latitud, longitud);
            if(marc != null){
                Mensajes.error("El marcador que desea agregar ya existe");
                
            }else if(descripcion.equals("")){
                Mensajes.error("Favor de ingresar una descripción");
                System.out.println("No hay descripción");
            }
            else{
                mdao.save(m);
                Mensajes.info("Se ha agregado correctamente su marcador");
            }
            this.descripcion = "";
            this.tema = "";
        }
    }
}
