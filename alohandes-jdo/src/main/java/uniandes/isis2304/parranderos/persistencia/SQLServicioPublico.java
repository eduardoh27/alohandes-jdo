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

import uniandes.isis2304.parranderos.negocio.ServicioPublico;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto BAR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLServicioPublico 
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
	public SQLServicioPublico (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un BAR a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param idBar - El identificador del bar
	 * @param nombre - El nombre del bar
	 * @param ciudad - La ciudad del bar
	 * @param presupuesto - El presupuesto del bar (ALTO, MEDIO, BAJO)
	 * @param sedes - El número de sedes del bar
	 * @return El número de tuplas insertadas
	 */
	public long adicionarServicioPublico(PersistenceManager pm, 
			long ID, long COSTO, long TIPO, long ALOJAMIENTO)
	
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaServicioPublico() + 
        		"(ID, COSTO, TIPO, ALOJAMIENTO) values (?, ?, ?, ?)");
        q.setParameters( ID, COSTO, TIPO, ALOJAMIENTO);
        return (long) q.executeUnique();
	}

	


	public long eliminarServicioPublicoPorId (PersistenceManager pm, long ID)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaServicioPublico() + " WHERE ID = ?");
        q.setParameters(ID);
        return (long) q.executeUnique();
	}

	
	public ServicioPublico darServicioPublicoPorId (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaServicioPublico() + " WHERE ID = ?");
		q.setResultClass(ServicioPublico.class);
		q.setParameters(ID);
		return (ServicioPublico) q.executeUnique();
	}


	public List<ServicioPublico> darServiciosPublicos (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaServicioPublico ());
		q.setResultClass(ServicioPublico.class);
		return (List<ServicioPublico>) q.executeList();
	}

	public void eliminarServicioPublicoPorAlojamientoSimple(PersistenceManager pm, long idAlojamiento) {
		Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaServicioPublico() 
		+  " WHERE alojamiento = " + idAlojamiento + " AND alojamiento NOT IN (select alojamiento from " + pp.darTablaReserva() + ")");
		//q.setParameters(idAlojamiento);
		q.executeUnique();
	}
	


	
}
