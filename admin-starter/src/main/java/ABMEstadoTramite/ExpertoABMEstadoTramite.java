/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ABMEstadoTramite;

import ABMEstadoTramite.dtos.EstadoTramiteDTO;
import ABMEstadoTramite.dtos.ModificarEstadoTramiteDTO;
import ABMEstadoTramite.dtos.ModificarEstadoTramiteDTOIn;
import ABMEstadoTramite.dtos.NuevoEstadoTramiteDTO;
import ABMEstadoTramite.exceptions.EstadoTramiteException;
import entidades.ConfTipoTramiteEstadoTramite;
import entidades.EstadoTramite;
import entidades.Version;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import utils.DTOCriterio;
import utils.FachadaPersistencia;

/**
 *
 * @author matis
 */
public class ExpertoABMEstadoTramite {

    public List<EstadoTramiteDTO> buscarEstadosTramite(int codEstadoTramite, String nombreEstadoTramite, String descripcionEstadoTramite) {

        List<DTOCriterio> lCriterio = new ArrayList<DTOCriterio>();

        if (codEstadoTramite > 0) {
            DTOCriterio criterio1 = new DTOCriterio();
            criterio1.setAtributo("codEstadoTramite");
            criterio1.setOperacion("=");
            criterio1.setValor(codEstadoTramite);
            lCriterio.add(criterio1);
        }

        if (nombreEstadoTramite.trim().length() > 0) {
            DTOCriterio criterio2 = new DTOCriterio();
            criterio2.setAtributo("nombreEstadoTramite");
            criterio2.setOperacion("like");
            criterio2.setValor(nombreEstadoTramite);
            lCriterio.add(criterio2);
        }

        if (descripcionEstadoTramite.trim().length() > 0) {
            DTOCriterio criterio3 = new DTOCriterio();
            criterio3.setAtributo("descripcionEstadoTramite");
            criterio3.setOperacion("like");
            criterio3.setValor(descripcionEstadoTramite);
            lCriterio.add(criterio3);
        }

        List objetoList = FachadaPersistencia.getInstance().buscar("EstadoTramite", lCriterio);
        List<EstadoTramiteDTO> estadoTramiteResultados = new ArrayList<>();

        for (Object x : objetoList) {
            EstadoTramite estadoTramite = (EstadoTramite) x;
            EstadoTramiteDTO estadoTramiteDTO = new EstadoTramiteDTO();
            estadoTramiteDTO.setCodEstadoTramite(estadoTramite.getCodEstadoTramite());
            estadoTramiteDTO.setNombreEstadoTramite(estadoTramite.getNombreEstadoTramite());
            estadoTramiteDTO.setDescripcionEstadoTramite(estadoTramite.getDescripcionEstadoTramite());
            estadoTramiteDTO.setFechaHoraBajaEstadoTramite(estadoTramite.getFechaHoraBajaEstadoTramite());
            estadoTramiteDTO.setFechaHoraAltaEstadoTramite(estadoTramite.getFechaHoraAltaEstadoTramite());
            estadoTramiteResultados.add(estadoTramiteDTO);
        }

        return estadoTramiteResultados;

    }

    public void agregarEstadoTramite(NuevoEstadoTramiteDTO nuevoEstadoTramiteDTO) throws EstadoTramiteException {
        FachadaPersistencia.getInstance().iniciarTransaccion();

        List<DTOCriterio> criterioList = new ArrayList<>();
        DTOCriterio dto = new DTOCriterio();

        dto.setAtributo("codEstadoTramite");
        dto.setOperacion("=");
        dto.setValor(nuevoEstadoTramiteDTO.getCodEstadoTramite());

        criterioList.add(dto);

        List lEstadoTramite = FachadaPersistencia.getInstance().buscar("EstadoTramite", criterioList);

        if (lEstadoTramite.size() > 0) {
            throw new EstadoTramiteException("El codigo de EstadoTramite ya existe");
        } else {
            EstadoTramite estadoTramite = new EstadoTramite();
            estadoTramite.setCodEstadoTramite(nuevoEstadoTramiteDTO.getCodEstadoTramite());
            estadoTramite.setNombreEstadoTramite(nuevoEstadoTramiteDTO.getNombreEstadoTramite());
            estadoTramite.setDescripcionEstadoTramite(nuevoEstadoTramiteDTO.getDescripcionEstadoTramite());
            estadoTramite.setFechaHoraAltaEstadoTramite(new Timestamp(System.currentTimeMillis()));

            FachadaPersistencia.getInstance().guardar(estadoTramite);
            FachadaPersistencia.getInstance().finalizarTransaccion();
        }
    }

    public ModificarEstadoTramiteDTO buscarEstadoTramiteAModificar(int codEstadoTramite) throws EstadoTramiteException {
        List<DTOCriterio> criterioList = new ArrayList<>();
        DTOCriterio dto = new DTOCriterio();

        dto.setAtributo("codEstadoTramite");
        dto.setOperacion("=");
        dto.setValor(codEstadoTramite);

        criterioList.add(dto);

        EstadoTramite estadoTramiteEncontrado = (EstadoTramite) FachadaPersistencia.getInstance().buscar("EstadoTramite", criterioList).get(0);

        ModificarEstadoTramiteDTO modificarEstadoTramiteDTO = new ModificarEstadoTramiteDTO();
        modificarEstadoTramiteDTO.setNombreEstadoTramite(estadoTramiteEncontrado.getNombreEstadoTramite());
        modificarEstadoTramiteDTO.setCodEstadoTramite(estadoTramiteEncontrado.getCodEstadoTramite());
        modificarEstadoTramiteDTO.setDescripcionEstadoTramite(estadoTramiteEncontrado.getDescripcionEstadoTramite());

        return modificarEstadoTramiteDTO;
    }

    public void modificarEstadoTramite(ModificarEstadoTramiteDTOIn modificarEstadoTramiteDTOIn) throws EstadoTramiteException {
        FachadaPersistencia.getInstance().iniciarTransaccion();

        int codEstado = modificarEstadoTramiteDTOIn.getCodEstadoTramite();

        List<DTOCriterio> criterioList = new ArrayList<>();
        DTOCriterio dto = new DTOCriterio();

        dto.setAtributo("codEstadoTramite");
        dto.setOperacion("=");
        dto.setValor(codEstado);
        criterioList.add(dto);

        EstadoTramite estadoTramiteEncontrado = (EstadoTramite) FachadaPersistencia.getInstance().buscar("EstadoTramite", criterioList).get(0);

        criterioList.clear();

        // Buscar versiones activas (fecha desde menor a ahora y sin fecha de baja)
        DTOCriterio dto2 = new DTOCriterio();

        dto2.setAtributo("fechaDesdeVersion");
        dto2.setOperacion("<");
        dto2.setValor(Timestamp.from(Instant.now()));

        criterioList.add(dto2);

        DTOCriterio dto4 = new DTOCriterio();

        dto4.setAtributo("fechaBajaVersion");
        dto4.setOperacion("=");
        dto4.setValor(null);

        criterioList.add(dto4);

        List<Object> objetoList = FachadaPersistencia.getInstance().buscar("Version", criterioList);

        // Verificar en cada versión si el EstadoTramite está presente en origen o destino
        for (Object o : objetoList) {

            Version version = (Version) o;
            List<ConfTipoTramiteEstadoTramite> confTTETList = version.getConfTipoTramiteEstadoTramite();

            for (ConfTipoTramiteEstadoTramite confTTET : confTTETList) {
                verificarEstadoTramiteEnOrigenODestinoM(confTTET.getEstadoTramiteOrigen(), confTTET.getEstadoTramiteDestino(), codEstado);
            }
        }

        // Modificar el EstadoTramite si no hay problema
        estadoTramiteEncontrado.setCodEstadoTramite(modificarEstadoTramiteDTOIn.getCodEstadoTramite());
        estadoTramiteEncontrado.setNombreEstadoTramite(modificarEstadoTramiteDTOIn.getNombreEstadoTramite());
        estadoTramiteEncontrado.setDescripcionEstadoTramite(modificarEstadoTramiteDTOIn.getDescripcionEstadoTramite());

        FachadaPersistencia.getInstance().guardar(estadoTramiteEncontrado);
        FachadaPersistencia.getInstance().finalizarTransaccion();
    }

    public void darDeBajaEstadoTramite(int codEstadoTramite) throws EstadoTramiteException {
        FachadaPersistencia.getInstance().iniciarTransaccion();

        List<DTOCriterio> criterioList = new ArrayList<>();
        DTOCriterio dto = new DTOCriterio();

        dto.setAtributo("codEstadoTramite");
        dto.setOperacion("=");
        dto.setValor(codEstadoTramite);

        criterioList.add(dto);

        EstadoTramite estadoTramiteEncontrado = (EstadoTramite) FachadaPersistencia.getInstance().buscar("EstadoTramite", criterioList).get(0);
        criterioList.clear();

        // Buscar versiones con fecha actual y sin fecha de baja
        DTOCriterio dto2 = new DTOCriterio();
        dto2.setAtributo("fechaDesdeVersion");
        dto2.setOperacion("<");
        dto2.setValor(Timestamp.from(Instant.now()));
        criterioList.add(dto2);

        DTOCriterio dto3 = new DTOCriterio();
        dto3.setAtributo("fechaBajaVersion");
        dto3.setOperacion("=");
        dto3.setValor(null);
        criterioList.add(dto3);
        List<Object> objetoList = FachadaPersistencia.getInstance().buscar("Version", criterioList);

        // Verificar cada versión
        for (Object o : objetoList) {
            Version version = (Version) o;
            List<ConfTipoTramiteEstadoTramite> confTTETList = version.getConfTipoTramiteEstadoTramite();
            // Verificar si el EstadoTramite está en origen o en destino
            for (ConfTipoTramiteEstadoTramite confTTET : confTTETList) {
                verificarEstadoTramiteEnOrigenODestino(confTTET.getEstadoTramiteOrigen(), confTTET.getEstadoTramiteDestino(), codEstadoTramite);
            }
        }
        // Dar de baja el EstadoTramite
        estadoTramiteEncontrado.setFechaHoraBajaEstadoTramite(new Timestamp(System.currentTimeMillis()));
        FachadaPersistencia.getInstance().guardar(estadoTramiteEncontrado);
        FachadaPersistencia.getInstance().finalizarTransaccion();
    }


        private void verificarEstadoTramiteEnOrigenODestino(List<EstadoTramite> estadoTramiteOrigen, List<EstadoTramite> estadoTramiteDestino, int codEstadoTramite) throws EstadoTramiteException {
        // Verificar si el estado está en la lista de origen
        for (EstadoTramite estado : estadoTramiteOrigen) {
            if (estado.getCodEstadoTramite() == codEstadoTramite) {
                throw new EstadoTramiteException("No se pudo eliminar el Estado porque está presente en el origen de una versión actual/posterior");
            }
        }
        // Verificar si el estado está en la lista de destino
        for (EstadoTramite estado : estadoTramiteDestino) {
            if (estado.getCodEstadoTramite() == codEstadoTramite) {
                throw new EstadoTramiteException("No se pudo eliminar el Estado porque está presente en el destino de una versión actual/posterior");
            }
        }
    }
        private void verificarEstadoTramiteEnOrigenODestinoM(List<EstadoTramite> estadoTramiteOrigen, List<EstadoTramite> estadoTramiteDestino, int codEstadoTramite) throws EstadoTramiteException {
        // Verificar si el estado está en la lista de origen
        for (EstadoTramite estado : estadoTramiteOrigen) {
            if (estado.getCodEstadoTramite() == codEstadoTramite) {
                throw new EstadoTramiteException("No se pudo modficar el Estado porque está presente en el origen de una versión actual/posterior");
            }
        }
        // Verificar si el estado está en la lista de destino
        for (EstadoTramite estado : estadoTramiteDestino) {
            if (estado.getCodEstadoTramite() == codEstadoTramite) {
                throw new EstadoTramiteException("No se pudo modificar el Estado porque está presente en el destino de una versión actual/posterior");
            }
        }
    }
}
