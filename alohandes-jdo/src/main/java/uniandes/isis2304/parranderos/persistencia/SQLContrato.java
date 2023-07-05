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

import uniandes.isis2304.parranderos.negocio.Contrato;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto Contrato de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLContrato 
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
	public SQLContrato (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un Contrato a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param ID - El identificador del Contrato
	 * @param nombre - El nombre del Contrato
	 * @param ciudad - La ciudad del Contrato
	 * @param presupuesto - El presupuesto del Contrato (ALTO, MEDIO, BAJO)
	 * @param sedes - El número de sedes del Contrato
	 * @return El número de tuplas insertadas
	 */
	public long adicionarContrato (PersistenceManager pm, 
			long ID, long INTERNET, long ADMINISTRACION, 
			long SERVICIOSPUBLICOS, long TV) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaContrato () + "(ID, INTERNET, ADMINISTRACION, SERVICIOSPUBLICOS, TV) values (?, ?, ?, ?, ?)");
        q.setParameters(ID, INTERNET, ADMINISTRACION,SERVICIOSPUBLICOS, TV);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar Contratos de la base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombreContrato - El nombre del Contrato
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarContratosPorNombre (PersistenceManager pm, String nombreContrato)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaContrato () + " WHERE nombre = ?");
        q.setParameters(nombreContrato);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN Contrato de la base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idContrato - El identificador del Contrato
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarContratoPorId (PersistenceManager pm, long idContrato)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaContrato () + " WHERE id = ?");
        q.setParameters(idContrato);
        return (long) q.executeUnique();
	}
	
	
	
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN Contrato de la 
	 * base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idContrato - El identificador del Contrato
	 * @return El objeto Contrato que tiene el identificador dado
	 */
	public Contrato darContratoPorId (PersistenceManager pm, long idContrato) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaContrato () + " WHERE id = ?");
		q.setResultClass(Contrato.class);
		q.setParameters(idContrato);
		return (Contrato) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS Contratos de la 
	 * base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombreContrato - El nombre de Contrato buscado
	 * @return Una lista de objetos Contrato que tienen el nombre dado
	 */
	public List<Contrato> darContratosPorNombre (PersistenceManager pm, String nombreContrato) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaContrato () + " WHERE nombre = ?");
		q.setResultClass(Contrato.class);
		q.setParameters(nombreContrato);
		return (List<Contrato>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS Contratos de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Contrato
	 */
	public List<Contrato> darContratos (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaContrato ());
		q.setResultClass(Contrato.class);
		return (List<Contrato>) q.executeList();
	}

	
	public void eliminarContratoPorIdReserva (PersistenceManager pm, long ID)
	{	
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaContrato() + 
        		" WHERE ID = ( SELECT CONTRATO FROM " + pp.darTablaReserva()+ 
        		" WHERE ID = "+ ID + ")");
        q.setParameters(ID);
        q.executeUnique();
	}
	
}
