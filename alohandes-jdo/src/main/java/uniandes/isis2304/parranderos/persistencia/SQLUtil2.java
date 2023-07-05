/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.parranderos.persistencia;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto BAR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLUtil2
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaAlohandes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaAlohandes pp;
	
	private static Logger log = Logger.getLogger(SQLUtil2.class.getName());


	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLUtil2 (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para obtener un nuevo número de secuencia
	 * @param pm - El manejador de persistencia
	 * @return El número de secuencia generado
	 */
	public long nextval (PersistenceManager pm)
	{
        Query q = pm.newQuery(SQL, "SELECT "+ pp.darSeqAlohandes() + ".nextval FROM DUAL");
        log.info("se creo Query q en SQLUtil2");
        q.setResultClass(Long.class);
        long resp = (long) q.executeUnique();
        return resp;
	}
	
	public static Timestamp convertStringToTimestamp(String strDate) {
	    try {
	    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
	    	Date date = (Date) dateFormat.parse(strDate);
	    	Timestamp timestamp = new Timestamp(date.getTime());
	      return timestamp;
	    } catch (ParseException e) {
	      System.out.println("Exception :" + e);
	      return null;
	    }
	  }
	public static Timestamp stringToTimestamp(String str) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        java.util.Date date = dateFormat.parse(str);
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @param pm - El manejador de persistencia
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
	 * TIPOBEBIDA, BEBEDOR y BAR, respectivamente
	 */
	public long [] limpiarParranderos (PersistenceManager pm)
	{
        Query qTipoMiembroComunidad = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoMiembroComunidad ());          
        Query qCliente = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCliente ());
        Query qOperador = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaOperador ());
        Query qTipoOperadorEmpresa = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoOperadorEmpresa ());
        Query qEmpresa = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEmpresa ());
        Query qTipoOperadorNatural = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoOperadorNatural ());
        Query qPersonaNatural = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPersonaNatural ());
        Query qContrato = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaContrato ());
        Query qSeguro = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaSeguro ());
        Query qHorario = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaHorario ());
        Query qTipoAlojamiento = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoAlojamiento ());
        Query qAlojamiento = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaAlojamiento ());
        Query qReserva = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReserva ());
        Query qTipoServicioPublico = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoServicioPublico ());
        Query qServicioPublico = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaServicioPublico ());
        Query qTipoServicio = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoServicio ());
        Query qServicio = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaServicio ());
        
        
        
        long tipoMiembroComunidadEliminados = (long) qTipoMiembroComunidad.executeUnique ();
        long clienteEliminados = (long) qCliente.executeUnique ();
        long operadorEliminados = (long) qOperador.executeUnique ();
        long tipoOperadorEmpresaEliminados = (long) qTipoOperadorEmpresa.executeUnique ();
        long empresaEliminados = (long) qEmpresa.executeUnique ();
        long tipoOperadorNaturalEliminados = (long) qTipoOperadorNatural.executeUnique ();
        long personaNaturalEliminados = (long) qPersonaNatural.executeUnique ();
        long contratoEliminados = (long) qContrato.executeUnique ();
        long seguroEliminados = (long) qSeguro.executeUnique ();
        long horarioEliminados = (long) qHorario.executeUnique ();
        long tipoAlojamientoEliminados = (long) qTipoAlojamiento.executeUnique ();
        long alojamientoEliminados = (long) qAlojamiento.executeUnique ();
        long reservaEliminados = (long) qReserva.executeUnique ();
        long tipoServicioPublicoEliminados = (long) qTipoServicioPublico.executeUnique ();
        long servicioPublicoEliminados = (long) qServicioPublico.executeUnique ();
        long tipoServicioEliminados = (long) qTipoServicio.executeUnique ();
        long servicioEliminados = (long) qServicio.executeUnique ();
        
        return new long[] {tipoMiembroComunidadEliminados, clienteEliminados, operadorEliminados, tipoOperadorEmpresaEliminados, 
        		empresaEliminados, tipoOperadorNaturalEliminados, personaNaturalEliminados,contratoEliminados,seguroEliminados,
        		horarioEliminados,tipoAlojamientoEliminados, alojamientoEliminados,reservaEliminados,tipoServicioPublicoEliminados,
        		servicioPublicoEliminados,tipoServicioEliminados,servicioEliminados};
	}

}
