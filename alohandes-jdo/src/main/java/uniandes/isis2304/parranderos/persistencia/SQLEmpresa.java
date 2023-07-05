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

import uniandes.isis2304.parranderos.negocio.Empresa;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto BAR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLEmpresa 
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
	public SQLEmpresa (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un BAR a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param NIT - El identificador del empresa
	 * @param UBICACION - El nombre del empresa
	 * @param TIPO - La ciudad del empresa
	 * @param presupuesto - El presupuesto del empresa (ALTO, MEDIO, BAJO)
	 * @param sedes - El número de sedes del empresa
	 * @return El número de tuplas insertadas
	 */
	public long adicionarEmpresa (PersistenceManager pm, long NIT, String UBICACION,long RECEPCION,long REGISTROTURISMO,long REGISTROCAMARA,long NUMEROCONTACTO, long TIPO) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaEmpresa () + "(NIT, UBICACION, RECEPCION, REGISTROTURISMO, REGISTROCAMARA, NUMEROCONTACTO, TIPO) values (?, ?, ?, ?, ?, ?, ?)");
        q.setParameters(NIT, UBICACION,RECEPCION,REGISTROTURISMO,REGISTROCAMARA,NUMEROCONTACTO, TIPO);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar EMPRESAS de la base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombreEmpresa - El nombre del empresa
	 * @return EL número de tuplas eliminadas
	 */
	
	// OJO: NOMBRE ES EN OPERADOR
	public long eliminarEmpresaesPorNombre (PersistenceManager pm, String nombreEmpresa)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEmpresa () + " WHERE nombre = ?");
        q.setParameters(nombreEmpresa);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN BAR de la base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idEmpresa - El identificador del empresa
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarEmpresaPorId (PersistenceManager pm, long idEmpresa)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEmpresa () + " WHERE id = ?");
        q.setParameters(idEmpresa);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN BAR de la 
	 * base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idEmpresa - El identificador del empresa
	 * @return El objeto BAR que tiene el identificador dado
	 */
	public Empresa darEmpresaPorId (PersistenceManager pm, long idEmpresa) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaEmpresa () + " WHERE id = ?");
		q.setResultClass(Empresa.class);
		q.setParameters(idEmpresa);
		return (Empresa) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS EMPRESAS de la 
	 * base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombreEmpresa - El nombre de empresa buscado
	 * @return Una lista de objetos BAR que tienen el nombre dado
	 */
	public List<Empresa> darEmpresasPorNombre (PersistenceManager pm, String nombreEmpresa) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaEmpresa () + " WHERE nombre = ?");
		q.setResultClass(Empresa.class);
		q.setParameters(nombreEmpresa);
		return (List<Empresa>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS EMPRESAS de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos BAR
	 */
	public List<Empresa> darEmpresas (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaEmpresa ());
		q.setResultClass(Empresa.class);
		return (List<Empresa>) q.executeList();
	}
	
}
