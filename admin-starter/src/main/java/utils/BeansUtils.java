package utils;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class BeansUtils {

    public static void guardarUrlAnterior() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String urlActual = externalContext.getRequestHeaderMap().get("referer");
        externalContext.getSessionMap().put("urlAnterior", urlActual);
    }

    public static String redirectToPreviousPage() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String urlAnterior = (String) externalContext.getSessionMap().get("urlAnterior");
        if (urlAnterior != null) {
            try {
                externalContext.redirect(urlAnterior);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void recargarPagina() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        // Obtiene la URL actual
        String urlActual = externalContext.getRequestContextPath() + ((HttpServletRequest) externalContext.getRequest()).getRequestURI();

        try {
            // Redirige a la URL actual para recargar la página
            externalContext.redirect(urlActual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
