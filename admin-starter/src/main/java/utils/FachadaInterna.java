package utils;

import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import org.hibernate.criterion.Order;

public class FachadaInterna {

    private static FachadaInterna fachadaInterna;
    private final String paqueteEntidades = "entidades";

    public FachadaInterna() {
    }

    public static FachadaInterna getInstance() {
        if (fachadaInterna == null) {
            fachadaInterna = new FachadaInterna();
        }
        return fachadaInterna;
    }

    List<Object> buscar(String claseABuscar, List<DTOCriterio> criterioList) {
        if (HibernateUtil.getSession().getTransaction().isActive()) {
            HibernateUtil.getSession().getTransaction().commit();
        }
        HibernateUtil.getSession().beginTransaction();

        Criteria cr = null;
        try {
            cr = HibernateUtil.getSession().createCriteria(Class.forName(paqueteEntidades + "." + claseABuscar), claseABuscar.toLowerCase());
        } catch (ClassNotFoundException e) {
            System.out.println("Error creating criteria. " + e);
        }

        if (criterioList != null) {
            for (DTOCriterio criterio : criterioList) {
                String atributo = criterio.getAtributo();
                String operacion = criterio.getOperacion();
                Object valor = criterio.getValor();

                // Manejo especial para rango de fechas
                if (operacion.equals("range") && valor instanceof Date[]) {
                    Date[] rangoFechas = (Date[]) valor;
                    Date fechaInicio = rangoFechas[0];
                    Date fechaFin = rangoFechas[1];
                    cr.add(Restrictions.ge(atributo, fechaInicio));
                    cr.add(Restrictions.le(atributo, fechaFin));
                } else {
                    switch (operacion) {
                        case "=":
                            cr.add(Restrictions.conjunction(Restrictions.eqOrIsNull(atributo, valor)));
                            break;
                        case "<":
                            cr.add(Restrictions.conjunction(Restrictions.lt(atributo, valor)));
                            break;
                        case ">":
                            cr.add(Restrictions.conjunction(Restrictions.gt(atributo, valor)));
                            break;
                        case "<=":
                            cr.add(Restrictions.conjunction(Restrictions.le(atributo, valor)));
                            break;
                        case ">=":
                            cr.add(Restrictions.conjunction(Restrictions.ge(atributo, valor)));
                            break;
                        case "<>":
                            cr.add(Restrictions.conjunction(Restrictions.ne(atributo, valor)));
                            break;
                        case "like":
                            cr.add(Restrictions.conjunction(Restrictions.ilike(atributo, (String) valor, MatchMode.ANYWHERE)));
                            break;
                        case "between":
                            // Suponiendo que el valor es un array de dos fechas o números
                            if (valor instanceof Object[]) {
                                Object[] valoresBetween = (Object[]) valor;
                                cr.add(Restrictions.between(atributo, valoresBetween[0], valoresBetween[1]));
                            }
                            break;
                        case "desc":  // Para el orden descendente y obtener el nroTramite
                            cr.addOrder(Order.desc(atributo));
                            break;
                        case "asc":  // Para el orden asc y obtener el nroTramite
                            cr.addOrder(Order.asc(atributo));
                            break;
                        case "contains":
                            String property = String.format("%s.%s", claseABuscar.toLowerCase(), atributo);
                            cr.setFetchMode(property, FetchMode.JOIN);
                            cr.createAlias(property, "lista");
                            cr.setFetchMode("lista.OID" + claseABuscar, FetchMode.JOIN);
                            cr.add(Restrictions.eq("lista.OID", ((entidades.Entidad) valor).getOID()));
                            break;
                    }
                }
            }
        }

        return cr.list();
    }

    void guardar(Object objeto) {
        HibernateUtil.getSession().saveOrUpdate(objeto);
        HibernateUtil.getSession().flush();
    }

    void merge(Object objeto) {
        HibernateUtil.getSession().merge(objeto);
        HibernateUtil.getSession().flush();
    }

    void refrescar(Object objeto) {
        HibernateUtil.getSession().refresh(objeto);
        HibernateUtil.getSession().flush();
    }

    void iniciarTransaccion() {
        if (HibernateUtil.getSession().getTransaction().isActive()) {
            HibernateUtil.getSession().getTransaction().commit();
        }
        HibernateUtil.getSession().beginTransaction();
    }

    void finalizarTransaccion() {
        HibernateUtil.getSession().setFlushMode(FlushMode.NEVER);
        HibernateUtil.getSession().getTransaction().commit();
        HibernateUtil.getSession().close();

    }

}
