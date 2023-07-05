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

import uniandes.isis2304.parranderos.negocio.Seguro;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto BAR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLSeguro 
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
	public SQLSeguro (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	

	public long adicionarSeguro (PersistenceManager pm, Long ID, String NOMBREEMPRESA, 
			String MODALIDAD, Long CODEUDOR, Long VALORARRIENDO, Long DURACION) {
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaSeguro () +
        		"(ID, NOMBREEMPRESA, MODALIDAD, CODEUDOR, VALORARRIENDO, DURACION) values (?, ?, ?, ?, ?, ?)");
        q.setParameters(ID, NOMBREEMPRESA, MODALIDAD, CODEUDOR, VALORARRIENDO, DURACION);
        return (long) q.executeUnique();
	}


	public long eliminarSeguroPorId (PersistenceManager pm, long ID)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaSeguro () + " WHERE ID = ?");
        q.setParameters(ID);
        return (long) q.executeUnique();
	}

	public Seguro darSeguroPorId (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaSeguro() + " WHERE ID = ?");
		q.setResultClass(Seguro.class);
		q.setParameters(ID);
		return (Seguro) q.executeUnique();
	}

	public List<Seguro> darSeguros (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaSeguro());
		q.setResultClass(Seguro.class);
		return (List<Seguro>) q.executeList();
	}

	
}
