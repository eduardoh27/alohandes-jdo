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

import uniandes.isis2304.parranderos.negocio.TipoServicio;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto TIPO DE BEBIDA de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLTipoServicio 
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
	public SQLTipoServicio (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	

	public long adicionarTipoServicio(PersistenceManager pm, long ID, String NOMBRE) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaTipoServicio() + "(ID, NOMBRE) values (?, ?)");
        q.setParameters(ID, NOMBRE);
        return (long) q.executeUnique();            
	}

	
	public long eliminarTipoServicioPorNombre (PersistenceManager pm, String NOMBRE)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoServicio() + " WHERE NOMBRE = ?");
        q.setParameters(NOMBRE);
        return (long) q.executeUnique();            
	}
	
	public long eliminarTipoServicioPorId (PersistenceManager pm, long ID)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTipoServicio() + " WHERE ID = ?");
        q.setParameters(ID);
        return (long) q.executeUnique();            
	}
	
	public TipoServicio darTipoServicioPorId (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoServicio() + " WHERE ID = ?");
		q.setResultClass(TipoServicio.class);
		q.setParameters(ID);
		return (TipoServicio) q.executeUnique();
	}


	public List<TipoServicio> darTiposServicioPorNombre (PersistenceManager pm, String NOMBRE) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoServicio() + " WHERE NOMBRE = ?");
		q.setResultClass(TipoServicio.class);
		q.setParameters(NOMBRE);
		return (List<TipoServicio>) q.executeList();
	}

	public List<TipoServicio> darTiposServicio(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoServicio());
		q.setResultClass(TipoServicio.class);
		return (List<TipoServicio>) q.executeList();
	}

}
