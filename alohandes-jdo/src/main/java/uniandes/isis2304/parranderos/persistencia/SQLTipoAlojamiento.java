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

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

import uniandes.isis2304.parranderos.negocio.TipoAlojamiento;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto TIPO DE BEBIDA de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLTipoAlojamiento 
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
	
	private static Logger log = Logger.getLogger(SQLTipoAlojamiento.class.getName());


	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLTipoAlojamiento (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	

	public long adicionarTipoAlojamiento(PersistenceManager pm, long ID, String NOMBRE) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaTipoAlojamiento() + "(ID, NOMBRE) values (?, ?)");
        log.info("se creo Query q en SQLTipoAlojamiento");
        q.setParameters(ID, NOMBRE);
        return (long) q.executeUnique();            
	}

	
	public long eliminarTipoAlojamientoPorNombre (PersistenceManager pm, String NOMBRE)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoAlojamiento() + " WHERE NOMBRE = ?");
        q.setParameters(NOMBRE);
        return (long) q.executeUnique();            
	}
	
	public long eliminarTipoAlojamientoPorId (PersistenceManager pm, long ID)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoAlojamiento() + " WHERE ID = ?");
        q.setParameters(ID);
        return (long) q.executeUnique();            
	}
	
	public TipoAlojamiento darTipoAlojamientoPorId (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoAlojamiento() + " WHERE ID = ?");
		q.setResultClass(TipoAlojamiento.class);
		q.setParameters(ID);
		return (TipoAlojamiento) q.executeUnique();
	}


	public List<TipoAlojamiento> darTiposAlojamientoPorNombre (PersistenceManager pm, String NOMBRE) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoAlojamiento() + " WHERE NOMBRE = ?");
		q.setResultClass(TipoAlojamiento.class);
		q.setParameters(NOMBRE);
		return (List<TipoAlojamiento>) q.executeList();
	}

	public List<TipoAlojamiento> darTiposAlojamiento(PersistenceManager pm)
	{
		log.info("in SQLTipoAlojamiento");
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoAlojamiento());
		q.setResultClass(TipoAlojamiento.class);
		log.info("before SQLTipoAlojamiento");
		return (List<TipoAlojamiento>) q.executeList();
	}

}
