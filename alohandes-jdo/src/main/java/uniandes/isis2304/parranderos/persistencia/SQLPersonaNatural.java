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

import uniandes.isis2304.parranderos.negocio.PersonaNatural;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto PersonaNatural de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLPersonaNatural 
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
	public SQLPersonaNatural (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	
	public long adicionarPersonaNatural (PersistenceManager pm, long CEDULA, String CORREO, long CELULAR, long TIPO) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPersonaNatural () + "(CEDULA, CELULAR, TIPO) values (?, ?, ?)");
        q.setParameters(CEDULA, CORREO, CELULAR, TIPO);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar PersonaNaturalS de la base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param NOMBRE - El nombre del PersonaNatural
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarPersonaNaturalsPorNombre (PersistenceManager pm, String NOMBRE)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPersonaNatural () + " WHERE NOMBRE = ?");
        q.setParameters(NOMBRE);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN PersonaNatural de la base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param ID - El identificador del bar
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarPersonaNaturalPorId (PersistenceManager pm, long ID)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPersonaNatural () + " WHERE ID = ?");
        q.setParameters(ID);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN PersonaNatural de la 
	 * base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param ID - El identificador del bar
	 * @return El objeto PersonaNatural que tiene el identificador dado
	 */
	public PersonaNatural darPersonaNaturalPorId (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPersonaNatural () + " WHERE ID = ?");
		q.setResultClass(PersonaNatural.class);
		q.setParameters(ID);
		return (PersonaNatural) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS PersonaNaturalES de la 
	 * base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombrePersonaNatural - El nombre de bar buscado
	 * @return Una lista de objetos PersonaNatural que tienen el nombre dado
	 */
	public List<PersonaNatural> darPersonaNaturalsPorNombre (PersistenceManager pm, String nombrePersonaNatural) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPersonaNatural () + " WHERE NOMBRE = ?");
		q.setResultClass(PersonaNatural.class);
		q.setParameters(nombrePersonaNatural);
		return (List<PersonaNatural>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS PersonaNaturalES de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos PersonaNatural
	 */
	public List<PersonaNatural> darPersonaNaturals (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPersonaNatural ());
		q.setResultClass(PersonaNatural.class);
		return (List<PersonaNatural>) q.executeList();
	}
	
}
