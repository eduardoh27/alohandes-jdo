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

import uniandes.isis2304.parranderos.negocio.Horario;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto BAR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLHorario 
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
	public SQLHorario (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	

	public long adicionarHorario (PersistenceManager pm, Long ID, String HORAAPERTURA, String HORACIERRE) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaHorario() + "(ID, HORAAPERTURA, HORACIERRE) values (?, to_date(?, 'yyyy-mm-dd hh24-mi-ss'), to_date(?, 'yyyy-mm-dd hh24-mi-ss'))");
        q.setParameters(ID, HORAAPERTURA, HORACIERRE);
        return (long) q.executeUnique();
	}
	
	public long eliminarHorarioPorId (PersistenceManager pm, long ID)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaHorario () + " WHERE ID = ?");
        q.setParameters(ID);
        return (long) q.executeUnique();
	}
	
	
	public Horario darHorarioPorId (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHorario () + " WHERE ID = ?");
		q.setResultClass(Horario.class);
		q.setParameters(ID);
		return (Horario) q.executeUnique();
	}




	
	public List<Horario> darHorarios (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHorario ());
		q.setResultClass(Horario.class);
		return (List<Horario>) q.executeList();
	}
	
	

	public Horario verificarHorario(PersistenceManager pm, String HORA_A, String HORA_C) {
		
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHorario() + " WHERE HORAAPERTURA = to_date(?, 'yyyy-mm-dd hh24-mi-ss') AND HORACIERRE = to_date(?, 'yyyy-mm-dd hh24-mi-ss')");
		q.setResultClass(Horario.class);
		q.setParameters(HORA_A, HORA_C);
		return (Horario) q.executeUnique();
		
	}
	
}
