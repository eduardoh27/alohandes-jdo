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

import uniandes.isis2304.parranderos.negocio.Cliente;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto CLIENTE de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLCliente 
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
	
	private static Logger log = Logger.getLogger(PersistenciaAlohandes.class.getName());

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLCliente (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	
	public long adicionarCliente (PersistenceManager pm, long CEDULA, String CORREO, String NOMBRE,long CELULAR, long VINCULACION, String CLAVE) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCliente () + "(CEDULA, CORREO, NOMBRE,CELULAR,VINCULACION, CLAVE) values (?, ?, ?, ?, ?, ?)");
        q.setParameters(CEDULA, CORREO, NOMBRE,CELULAR,VINCULACION, CLAVE);
        log.info("Se setearon los params los parámetros");
        return (long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar CLIENTES de la base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param NOMBRE - El nombre del Cliente
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarClientesPorNombre (PersistenceManager pm, String NOMBRE)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCliente () + " WHERE NOMBRE = ?");
        q.setParameters(NOMBRE);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN CLIENTE de la base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param ID - El identificador del bar
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarClientePorId (PersistenceManager pm, long ID)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCliente () + " WHERE ID = ?");
        q.setParameters(ID);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN CLIENTE de la 
	 * base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param ID - El identificador del bar
	 * @return El objeto CLIENTE que tiene el identificador dado
	 */
	public Cliente darClientePorId (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCliente () + " WHERE ID = ?");
		q.setResultClass(Cliente.class);
		q.setParameters(ID);
		return (Cliente) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS CLIENTEES de la 
	 * base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombreCliente - El nombre de bar buscado
	 * @return Una lista de objetos CLIENTE que tienen el nombre dado
	 */
	public List<Cliente> darClientesPorNombre (PersistenceManager pm, String nombreCliente) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCliente () + " WHERE NOMBRE = ?");
		q.setResultClass(Cliente.class);
		q.setParameters(nombreCliente);
		return (List<Cliente>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS CLIENTEES de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos CLIENTE
	 */
	public List<Cliente> darClientes (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCliente ());
		q.setResultClass(Cliente.class);
		return (List<Cliente>) q.executeList();
	}
	

	public Cliente verificarCliente(PersistenceManager pm, long iD_CLIENTE, String cLAVE_CLIENTE) {
		
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCliente () + " WHERE CEDULA = ? AND CLAVE = ?");
		q.setResultClass(Cliente.class);
		q.setParameters(iD_CLIENTE, cLAVE_CLIENTE);
		return (Cliente) q.executeUnique();
		
	}


	public List<Cliente> listarClienteFrecuente (PersistenceManager pm, long alojamiento)
	{
		Query q = pm.newQuery(SQL, "SELECT DISTINCT cliente.* "
				+ " FROM ("
				+ " SELECT reserva.cliente "
				+ "    FROM reserva "
				+ "    WHERE fechafinal - fechainicio > 15"
				+ "    AND fechacancelacion IS NULL "
				+ "    AND reserva.alojamiento = ?"
				+ "    UNION"
				+ "    SELECT reserva.cliente "
				+ "    FROM reserva "
				+ "    WHERE reserva.alojamiento = ?"
				+ "    GROUP BY reserva.cliente "
				+ "    HAVING COUNT(reserva.cliente) > 3"
				+ ") subquery"
				+ " INNER JOIN CLIENTE "
				+ " ON subquery.cliente= cliente.cedula");
		q.setParameters(alojamiento, alojamiento);
		q.setResultClass(Cliente.class);
		return (List<Cliente>) q.executeList();
	}

	
}
