package RegistrarTramite.beans;

import RegistrarTramite.ControladorRegistrarTramite;
import RegistrarTramite.dtos.DTODocumentacion;
import RegistrarTramite.dtos.DTOFile;
import RegistrarTramite.dtos.DTOTramiteElegido;
import RegistrarTramite.exceptions.RegistrarTramiteException;
import entidades.TramiteDocumentacion;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.shaded.commons.io.IOUtils;
import utils.BeansUtils;
import utils.DTOCriterio;
import utils.FachadaPersistencia;

@Named("uiresumen")
@ViewScoped
public class UIResumen implements Serializable {

    private int nroTramite;
    private Timestamp fechaRecepcionTramite;
    private Timestamp fechaAnulacionTramite;
    private int plazoDocumentacion;
    private int codTipoTramite;
    private String nombreTipoTramite;
    private String nombreEstado;
    private double precioTramite;
    private int dniCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String mailCliente;
    private int legajoConsultor;
    private String nombreConsultor;
    private int codTD;
    private String nombreTD;
    private String nombreDocumentacion;
    private Timestamp fechaEntregaDoc;
    private List<DTODocumentacion> resumenDoc;

    @PostConstruct
    public void init() {
        // Obtener el parámetro de la URL
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String codigoTramite = params.get("nroTramite");

        if (codigoTramite != null) {
            try {
                this.nroTramite = Integer.parseInt(codigoTramite);

                // Llamar al controlador para obtener el tramite
                DTOTramiteElegido tramiteElegido = controladorRegistrarTramite.mostrarResumenTramite(nroTramite);

                // Asignar los datos del tramite a los atributos del bean
                if (tramiteElegido != null) {
                    this.nroTramite = tramiteElegido.getNroTramite();
                    this.fechaRecepcionTramite = tramiteElegido.getFechaRecepcionTramite();
                    this.fechaAnulacionTramite = tramiteElegido.getFechaAnulacionTramite();
                    this.plazoDocumentacion = tramiteElegido.getPlazoDocumentacion();
                    this.precioTramite = tramiteElegido.getPrecioTramite();
                    this.codTipoTramite = tramiteElegido.getCodTipoTramite();
                    this.nombreTipoTramite = tramiteElegido.getNombreTipoTramite();
                    this.nombreEstado = tramiteElegido.getNombreEstado();
                    this.dniCliente = tramiteElegido.getDniCliente();
                    this.nombreCliente = tramiteElegido.getNombreCliente();
                    this.apellidoCliente = tramiteElegido.getApellidoCliente();
                    this.mailCliente = tramiteElegido.getMailCliente();
                    this.nombreConsultor = tramiteElegido.getNombreConsultor();
                    this.legajoConsultor = tramiteElegido.getLegajoConsultor();

                    this.resumenDoc = tramiteElegido.getResumenDoc();

                    for (DTODocumentacion doc : resumenDoc) {
                        this.codTD = doc.getCodTD();
                        this.nombreTD = doc.getNombreTD();
                        this.nombreDocumentacion = doc.getNombreDocumentacion();
                        this.fechaEntregaDoc = doc.getFechaEntregaDoc();

                    }
                }
            } catch (NumberFormatException e) {
                // Manejar error de conversión de número
                e.printStackTrace();
            }
        }
    }

    public int getNroTramite() {
        return nroTramite;
    }

    public void setNroTramite(int nroTramite) {
        this.nroTramite = nroTramite;
    }

    public Timestamp getFechaRecepcionTramite() {
        return fechaRecepcionTramite;
    }

    public void setFechaRecepcionTramite(Timestamp fechaRecepcionTramite) {
        this.fechaRecepcionTramite = fechaRecepcionTramite;
    }

    public int getPlazoDocumentacion() {
        return plazoDocumentacion;
    }

    public void setPlazoDocumentacion(int plazoDocumentacion) {
        this.plazoDocumentacion = plazoDocumentacion;
    }

    public int getCodTipoTramite() {
        return codTipoTramite;
    }

    public void setCodTipoTramite(int codTipoTramite) {
        this.codTipoTramite = codTipoTramite;
    }

    public String getNombreTipoTramite() {
        return nombreTipoTramite;
    }

    public void setNombreTipoTramite(String nombreTipoTramite) {
        this.nombreTipoTramite = nombreTipoTramite;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public double getPrecioTramite() {
        return precioTramite;
    }

    public void setPrecioTramite(double precioTramite) {
        this.precioTramite = precioTramite;
    }

    public int getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(int dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getMailCliente() {
        return mailCliente;
    }

    public int getLegajoConsultor() {
        return legajoConsultor;
    }

    public void setLegajoConsultor(int legajoConsultor) {
        this.legajoConsultor = legajoConsultor;
    }

    public String getNombreConsultor() {
        return nombreConsultor;
    }

    public void setNombreConsultor(String nombreConsultor) {
        this.nombreConsultor = nombreConsultor;
    }

    public String getNombreDocumentacion() {
        return nombreDocumentacion;
    }

    public void setNombreDocumentacion(String nombreDocumentacion) {
        this.nombreDocumentacion = nombreDocumentacion;
    }

    public Timestamp getFechaEntregaDoc() {
        return fechaEntregaDoc;
    }

    public void setFechaEntregaDoc(Timestamp fechaEntregaDoc) {
        this.fechaEntregaDoc = fechaEntregaDoc;
    }

    public void setMailCliente(String mailCliente) {
        this.mailCliente = mailCliente;
    }

    public Timestamp getFechaAnulacionTramite() {
        return fechaAnulacionTramite;
    }

    public void setFechaAnulacionTramite(Timestamp fechaAnulacionTramite) {
        this.fechaAnulacionTramite = fechaAnulacionTramite;
    }

    public int getCodTD() {
        return codTD;
    }

    public void setCodTD(int codTD) {
        this.codTD = codTD;
    }

    public String getNombreTD() {
        return nombreTD;
    }

    public void setNombreTD(String nombreTD) {
        this.nombreTD = nombreTD;
    }
    
    

    public List<DTODocumentacion> getResumenDoc() {
        return resumenDoc;
    }

    public void setResumenDoc(List<DTODocumentacion> resumenDoc) {
        this.resumenDoc = resumenDoc;
    }

    public DefaultStreamedContent getFileD() {
        return fileD;
    }

    public void setFileD(DefaultStreamedContent fileD) {
        this.fileD = fileD;
    }

    ControladorRegistrarTramite controladorRegistrarTramite = new ControladorRegistrarTramite();

    // anularTramite()
    public void anularTramite(int nroTramite) throws RegistrarTramiteException {
        try {
            controladorRegistrarTramite.anularTramite(nroTramite);
            Messages.create("Trámite Anulado").add();
        } catch (RegistrarTramiteException e) {
            Messages.create("Error!").error().detail("AdminFaces Error message.").add();
        }
    }

    public String registrarDocumentacion(int codTD) {
        BeansUtils.guardarUrlAnterior();
        return "Tramite?faces-redirect=true&codTD=" + codTD + "&nroTramite=" + nroTramite;
    }

    // Manejar la subida del archivo
    public void handleFileUpload(FileUploadEvent event) {
        try {
            FacesMessage message = new FacesMessage("Exitoso", event.getFile().getFileName() + " subido.");
            FacesContext.getCurrentInstance().addMessage(null, message);

            //Convierto el archivo subido en Base64
            byte[] sourceBytes = IOUtils.toByteArray(event.getFile().getInputStream());
            String encodedString = Base64.getEncoder().encodeToString(sourceBytes);

            DTOFile fileU = new DTOFile();

            //Usar DTOFile para almacenar el archivo subido
            fileU.setNombre(event.getFile().getFileName());
            fileU.setContenidoB64(encodedString);

            //System.out.println("encriptado =" + fileEjemplo.getContenidoB64());
            //llamo a la funcion registrarDocumentacion una vez cargado el archivo
            controladorRegistrarTramite.registrarDocumentacion(codTD, fileU, nroTramite);

            this.file = fileU;

        } catch (IOException ex) {
            Logger.getLogger(UIResumen.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    // Manejar la descarga del archivo
    private DefaultStreamedContent fileD;
    private DTOFile file = new DTOFile();

    public DTOFile getFile() {
        return file;
    }

    public void setFile(DTOFile file) {
        this.file = file;
    }

    public StreamedContent getFileD(int codTD) {

        List<DTOCriterio> criterioList = new ArrayList<DTOCriterio>();

        DTOCriterio fileCriterio = new DTOCriterio();
        fileCriterio.setAtributo("codTD");
        fileCriterio.setOperacion("=");
        fileCriterio.setValor(codTD);

        criterioList.add(fileCriterio);

        TramiteDocumentacion td = (TramiteDocumentacion) FachadaPersistencia.getInstance().buscar("TramiteDocumentacion", criterioList).get(0);

        file.setContenidoB64(td.getArchivoTD());
        file.setNombre(td.getNombreTD());

        // Verifica si el archivo (fileEjemplo) no es nulo y tiene contenido en Base64
        if (file != null && file.getContenidoB64() != null) {
            try {
                // Decodifica el contenido Base64 a un arreglo de bytes
                byte[] data = Base64.getDecoder().decode(file.getContenidoB64());

                // Crea un InputStream a partir del arreglo de bytes decodificado
                InputStream inputStream = new ByteArrayInputStream(data);

                // Construye el StreamedContent, asignando nombre, tipo de contenido y flujo
                fileD = DefaultStreamedContent.builder()
                        .name(file.getNombre()) // Asigna el nombre del archivo descargado
                        .contentType("application/octet-stream") // Tipo genérico para archivos binarios
                        .stream(() -> inputStream) // Proporciona el flujo de datos del archivo
                        .build();
            } catch (Exception ex) {
                Logger.getLogger(UIResumen.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

        //Retorna el archivo listo para ser descargado
        return fileD;
    }

}
