/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Marcador;
import modelo.MarcadorDAO;
import modelo.Tema;
import modelo.TemaDAO;
import org.primefaces.event.map.OverlaySelectEvent;
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
public class VerMarcadoresTema implements Serializable{
    private String tema;
    private MapModel simpleModel;
    private Marker marker;
    
    @PostConstruct
    public void VerMarcadoresTemas(){
        System.out.println(tema);
        simpleModel = new DefaultMapModel();
        TemaDAO tdao = new TemaDAO();
        MarcadorDAO mdao = new MarcadorDAO();
        Tema t = tdao.buscaPorNombre(tema);
        if(t == null){
            Mensajes.error("Éste tema no existe, por lo que no hay marcadores");
        }else{
            List<Marcador> marcadores = mdao.buscaPorTema(tema);
            for(Marcador m :marcadores){
                LatLng cord = new LatLng(m.getLatitud(),m.getLongitud());
                Marker marcador = new Marker(cord,m.getDescripcion());
                simpleModel.addOverlay(marcador);
            }
        }
       
    }
    
     public MapModel getSimpleModel() {
        return simpleModel;
    }
    
    public void onMarkerSelect(OverlaySelectEvent event) {
       marker =(Marker) event.getOverlay();
       
    }

    public Marker getMarker() {
        return marker;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
    
    public String muestraVentana(){
        return "/verMarcadoresTema?faces-redirect=true";
    }
}
