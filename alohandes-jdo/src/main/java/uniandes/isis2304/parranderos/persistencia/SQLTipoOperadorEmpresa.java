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

import uniandes.isis2304.parranderos.negocio.TipoOperadorEmpresa;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto TIPO DE BEBIDA de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLTipoOperadorEmpresa 
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
	public SQLTipoOperadorEmpresa (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un TipoOperadorEmpresa a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param ID - El identificador del tipo de bebida
	 * @param NOMBRE - El nombre del tipo de bebida
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarTipoOperadorEmpresa (PersistenceManager pm, long ID, String NOMBRE) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaTipoOperadorEmpresa  () + "(id, nombre) values (?, ?)");
        q.setParameters(ID, NOMBRE);
        return (long) q.executeUnique();            
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar TIPOS DE BEBIDA de la base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombreTipoOperadorEmpresa - El nombre del tipo de bebida
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarTipoOperadorEmpresaPorNombre (PersistenceManager pm, String nombreTipoOperadorEmpresa)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoOperadorEmpresa  () + " WHERE nombre = ?");
        q.setParameters(nombreTipoOperadorEmpresa);
        return (long) q.executeUnique();            
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar TIPOS DE BEBIDA de la base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idTipoOperadorEmpresa - El identificador del tipo de bebida
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarTipoOperadorEmpresaPorId (PersistenceManager pm, long idTipoOperadorEmpresa)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoOperadorEmpresa  () + " WHERE id = ?");
        q.setParameters(idTipoOperadorEmpresa);
        return (long) q.executeUnique();            
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN TIPO DE BEBIDA de la 
	 * base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idTipoOperadorEmpresa - El identificador del tipo de bebida
	 * @return El objeto TipoOperadorEmpresa que tiene el identificador dado
	 */
	public TipoOperadorEmpresa darTipoOperadorEmpresaPorId (PersistenceManager pm, long idTipoOperadorEmpresa) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoOperadorEmpresa  () + " WHERE id = ?");
		q.setResultClass(TipoOperadorEmpresa.class);
		q.setParameters(idTipoOperadorEmpresa);
		return (TipoOperadorEmpresa) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN TIPO DE BEBIDA de la 
	 * base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombreTipoOperadorEmpresa - El nombre del tipo de bebida
	 * @return El objeto TipoOperadorEmpresa que tiene el nombre dado
	 */
	public List<TipoOperadorEmpresa> darTiposMiembroComunidadPorNombre (PersistenceManager pm, String nombreTipoOperadorEmpresa) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoOperadorEmpresa  () + " WHERE nombre = ?");
		q.setResultClass(TipoOperadorEmpresa.class);
		q.setParameters(nombreTipoOperadorEmpresa);
		return (List<TipoOperadorEmpresa>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS TIPOS DE BEBIDA de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos TipoOperadorEmpresa
	 */
	public List<TipoOperadorEmpresa> darTiposMiembroComunidad (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoOperadorEmpresa  ());
		q.setResultClass(TipoOperadorEmpresa.class);
		return (List<TipoOperadorEmpresa>) q.executeList();
	}

}
