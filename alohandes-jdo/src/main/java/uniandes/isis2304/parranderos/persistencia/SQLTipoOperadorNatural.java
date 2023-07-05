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

import uniandes.isis2304.parranderos.negocio.TipoOperadorNatural;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto TIPO DE BEBIDA de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLTipoOperadorNatural 
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

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLTipoOperadorNatural (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un TipoOperadorNatural a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param ID - El identificador del tipo de bebida
	 * @param NOMBRE - El nombre del tipo de bebida
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarTipoOperadorNatural (PersistenceManager pm, long ID, String NOMBRE) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaTipoOperadorNatural  () + "(id, nombre) values (?, ?)");
        q.setParameters(ID, NOMBRE);
        return (long) q.executeUnique();            
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar TIPOS DE BEBIDA de la base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombreTipoOperadorNatural - El nombre del tipo de bebida
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarTipoOperadorNaturalPorNombre (PersistenceManager pm, String nombreTipoOperadorNatural)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoOperadorNatural  () + " WHERE nombre = ?");
        q.setParameters(nombreTipoOperadorNatural);
        return (long) q.executeUnique();            
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar TIPOS DE BEBIDA de la base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idTipoOperadorNatural - El identificador del tipo de bebida
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarTipoOperadorNaturalPorId (PersistenceManager pm, long idTipoOperadorNatural)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoOperadorNatural  () + " WHERE id = ?");
        q.setParameters(idTipoOperadorNatural);
        return (long) q.executeUnique();            
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN TIPO DE BEBIDA de la 
	 * base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idTipoOperadorNatural - El identificador del tipo de bebida
	 * @return El objeto TipoOperadorNatural que tiene el identificador dado
	 */
	public TipoOperadorNatural darTipoOperadorNaturalPorId (PersistenceManager pm, long idTipoOperadorNatural) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoOperadorNatural  () + " WHERE id = ?");
		q.setResultClass(TipoOperadorNatural.class);
		q.setParameters(idTipoOperadorNatural);
		return (TipoOperadorNatural) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN TIPO DE BEBIDA de la 
	 * base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombreTipoOperadorNatural - El nombre del tipo de bebida
	 * @return El objeto TipoOperadorNatural que tiene el nombre dado
	 */
	public List<TipoOperadorNatural> darTiposMiembroComunidadPorNombre (PersistenceManager pm, String nombreTipoOperadorNatural) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoOperadorNatural  () + " WHERE nombre = ?");
		q.setResultClass(TipoOperadorNatural.class);
		q.setParameters(nombreTipoOperadorNatural);
		return (List<TipoOperadorNatural>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS TIPOS DE BEBIDA de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos TipoOperadorNatural
	 */
	public List<TipoOperadorNatural> darTiposMiembroComunidad (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoOperadorNatural  ());
		q.setResultClass(TipoOperadorNatural.class);
		return (List<TipoOperadorNatural>) q.executeList();
	}

}
