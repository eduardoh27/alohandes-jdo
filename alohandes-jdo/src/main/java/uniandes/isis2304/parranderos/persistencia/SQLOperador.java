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

import uniandes.isis2304.parranderos.negocio.Operador;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto OPERADOR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLOperador 
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
	public SQLOperador (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un OPERADOR a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param idOperador - El identificador del bar
	 * @param NOMBRE - El nombre del bar
	 * @param CLAVE - La ciudad del bar
	 * @param presupuesto - El presupuesto del bar (ALTO, MEDIO, BAJO)
	 * @param sedes - El número de sedes del bar
	 * @return El número de tuplas insertadas
	 */
	public long adicionarOperador (PersistenceManager pm, long ID,long INGRESOPORANIOACTUAL,long INGRESOPORANIOCORRIDO,String NOMBRE, String CLAVE, String CORREO) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaOperador () + "(ID, INGRESOPORANIOACTUAL, INGRESOPORANIOCORRIDO, NOMBRE, CLAVE,CORREO) values (?, ?, ?, ?, ?,?)");
        q.setParameters(ID, INGRESOPORANIOACTUAL, INGRESOPORANIOCORRIDO, NOMBRE, CLAVE,CORREO);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar OPERADORES de la base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param NOMBRE - El nombre del bar
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarOperadoresPorNombre (PersistenceManager pm, String NOMBRE)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaOperador () + " WHERE NOMBRE = ?");
        q.setParameters(NOMBRE);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN OPERADOR de la base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param ID - El identificador del bar
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarOperadorPorId (PersistenceManager pm, long ID)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaOperador () + " WHERE ID = ?");
        q.setParameters(ID);
        return (long) q.executeUnique();
	}
	
	public List<Object> darOperadorMasSolicitado(PersistenceManager pm,String fECHAINICIO, String fECHAFINAL) {
		Query q = pm.newQuery(SQL, "select operador.* from operador where id in (select a.operador from( "
				+ "select reserva.*,alojamiento.operador as operador from reserva "
				+ "inner join alojamiento "
				+ "on reserva.alojamiento = alojamiento.id "
				+ "WHERE FECHAINICIO between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ "OR FECHAFINAL between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ "OR (FECHAINICIO <to_date(?,'yyyy-mm-dd') and FECHAFINAL > to_date(?,'yyyy-mm-dd')) "
				+ "AND FECHACANCELACION IS NULL) a "
				+ "GROUP BY a.operador "
				+ "HAVING COUNT(a.operador) >= 1 "
				+ "ORDER BY count(a.operador) desc "
				+ "FETCH FIRST 3 ROWS ONLY)");
		q.setParameters(fECHAINICIO,fECHAFINAL,fECHAINICIO,fECHAFINAL,fECHAINICIO,fECHAFINAL);
        //q.setResultClass(Object.class);
        return (List<Object>) q.executeList();
	}
	
	public List<Object> darOperadorMenosSolicitado(PersistenceManager pm,String fECHAINICIO, String fECHAFINAL) {
		Query q = pm.newQuery(SQL, "select operador.* from operador where id in (select a.operador from( "
				+ "select reserva.*,alojamiento.operador as operador from reserva "
				+ "inner join alojamiento "
				+ "on reserva.alojamiento = alojamiento.id "
				+ "WHERE FECHAINICIO between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ "OR FECHAFINAL between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ "OR (FECHAINICIO <to_date(?,'yyyy-mm-dd') and FECHAFINAL > to_date(?,'yyyy-mm-dd')) "
				+ "AND FECHACANCELACION IS NULL) a "
				+ "GROUP BY a.operador "
				+ "HAVING COUNT(a.operador) >= 1 "
				+ "ORDER BY count(a.operador) asc "
				+ "FETCH FIRST 3 ROWS ONLY)");
		q.setParameters(fECHAINICIO,fECHAFINAL,fECHAINICIO,fECHAFINAL,fECHAINICIO,fECHAFINAL);
        //q.setResultClass(Object.class);
        return (List<Object>) q.executeList();
	}

	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN OPERADOR de la 
	 * base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param ID - El identificador del bar
	 * @return El objeto OPERADOR que tiene el identificador dado
	 */
	public Operador darOperadorPorId (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOperador () + " WHERE ID = ?");
		q.setResultClass(Operador.class);
		q.setParameters(ID);
		return (Operador) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS OPERADORES de la 
	 * base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombreOperador - El nombre de bar buscado
	 * @return Una lista de objetos OPERADOR que tienen el nombre dado
	 */
	public List<Operador> darOperadoresPorNombre (PersistenceManager pm, String nombreOperador) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOperador () + " WHERE NOMBRE = ?");
		q.setResultClass(Operador.class);
		q.setParameters(nombreOperador);
		return (List<Operador>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS OPERADORES de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos OPERADOR
	 */
	public List<Operador> darOperadores (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOperador ()
						+" FETCH FIRST 1000 ROWS ONLY");
		q.setResultClass(Operador.class);
		return (List<Operador>) q.executeList();
	}
	
	/*
	public List<TipoBebida> darTiposBebida (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaTipoBebida  ());
		q.setResultClass(TipoBebida.class);
		return (List<TipoBebida>) q.executeList();
	}
  	*/


	public Operador verificarOperador(PersistenceManager pm, long iD_OPERADOR, String cLAVE_OPERADOR) {
		
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOperador () + " WHERE ID = ? AND CLAVE = ?");
		q.setResultClass(Operador.class);
		q.setParameters(iD_OPERADOR, cLAVE_OPERADOR);
		return (Operador) q.executeUnique();
		
	}

	public Object darSumaAnioCorrido(PersistenceManager pm, long idOperador, String fechaInicio) {
		Query q = pm.newQuery(SQL, "SELECT sum(RESERVA.PRECIO) FROM " + pp.darTablaReserva() 
		+ " INNER JOIN " + pp.darTablaAlojamiento() 
		+ " ON RESERVA.ALOJAMIENTO = ALOJAMIENTO.ID "
		+ " WHERE OPERADOR = " + idOperador +" AND FECHAINICIO >= TO_DATE(?, 'YYYY-MM-DD')");
		q.setParameters(fechaInicio);
		return (Object) q.executeUnique();
	}
	
	public Object darSumaAnioActual(PersistenceManager pm, long idOperador, String fechaInicio) {
		
		Query q = pm.newQuery(SQL, "SELECT sum(RESERVA.PRECIO) FROM " + pp.darTablaReserva() 
		+ " INNER JOIN " + pp.darTablaAlojamiento() 
		+ " ON RESERVA.ALOJAMIENTO = ALOJAMIENTO.ID "
		+ " WHERE OPERADOR = " + idOperador +" AND FECHAINICIO >= TO_DATE(?, 'YYYY-MM-DD')");
		q.setParameters(fechaInicio);
		return (Object) q.executeUnique();
	}
	
	
	public void actualizarIngresoAnioCorrido(PersistenceManager pm, long idOperador, long ingreso) {
		
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaOperador()
				+ " SET INGRESOPORANIOCORRIDO = " + ingreso 
				+ " WHERE ID = " + idOperador);
		//q.setParameters(ingreso, idOperador);
		q.executeUnique();
	}
	
	public void actualizarIngresoAnioActual(PersistenceManager pm, long idOperador, long ingreso) {
		
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaOperador()
				+ " SET INGRESOPORANIOACTUAL = " + ingreso 
				+ " WHERE ID = " + idOperador);
		//q.setParameters(ingreso, idOperador);
		q.executeUnique();
	}
	
}
