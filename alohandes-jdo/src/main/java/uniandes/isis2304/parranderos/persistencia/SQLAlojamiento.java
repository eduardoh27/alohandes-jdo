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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

import uniandes.isis2304.parranderos.negocio.Alojamiento;
import uniandes.isis2304.parranderos.negocio.Reserva;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto BAR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLAlojamiento 
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
	
	private static Logger log = Logger.getLogger(SQLAlojamiento.class.getName());


	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLAlojamiento (PersistenciaAlohandes pp)
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
	
	
	public long adicionarAlojamiento (PersistenceManager pm,
			long ID, long NUMHABITACIONES, String UBICACION, long PRECIO, long AMOBLADO,
			long CAPACIDAD, long COMPARTIDO, long INDICEOCUPACION, long TIPO, Long HORARIO,
			Long SEGURO, long OPERADOR,long ACTIVO) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaAlojamiento() + 
        		"(ID, NUMHABITACIONES, UBICACION, PRECIO, AMOBLADO, CAPACIDAD, COMPARTIDO, INDICEOCUPACION, TIPO, HORARIO, SEGURO, OPERADOR,ACTIVO) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
        log.info("se creo Query q en SQLAlojamiento");
        q.setParameters(ID, NUMHABITACIONES, UBICACION, PRECIO, AMOBLADO, CAPACIDAD, 
        		COMPARTIDO, INDICEOCUPACION, TIPO, HORARIO, SEGURO, OPERADOR,ACTIVO);
        log.info("right after setParameters");
        long a = (long) q.executeUnique();
        log.info("right after executeUnique");
        return a;
	}

	
	public long eliminarAlojamientoPorId (PersistenceManager pm, long ID)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaAlojamiento () + " WHERE ID = ?");
        q.setParameters(ID);
        return (long) q.executeUnique();
	}
	
	public long eliminarAlojamientoPorIdSimple (PersistenceManager pm, long ID)
	{
		Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaAlojamiento() 
		+ " WHERE ID = ? AND ID NOT IN (SELECT ALOJAMIENTO FROM " + pp.darTablaReserva() + ")");
		q.setParameters(ID);
        return (long) q.executeUnique();
	}

	
	public List<Alojamiento> darAlojamientoPorId (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaAlojamiento () + " WHERE ID = ?");
		q.setResultClass(Alojamiento.class);
		q.setParameters(ID);
		return (List<Alojamiento>) q.executeList();
	}
	
	public Alojamiento darAlojamientoPorId2 (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaAlojamiento () + " WHERE ID = ?");
		q.setResultClass(Alojamiento.class);
		q.setParameters(ID);
		return (Alojamiento) q.executeUnique();
	}


	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS BARES de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos BAR
	 */
	public List<Alojamiento> darAlojamientos (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaAlojamiento ()
								+" FETCH FIRST 1000 ROWS ONLY");
		q.setResultClass(Alojamiento.class);
		return (List<Alojamiento>) q.executeList();
	}
	
	public List<Object> darAlojamientoPorFechaYServicios(PersistenceManager pm, String fECHAINICIO,
			String fECHAFINAL, ArrayList<String> serviciosNumeros) {
				
		String listaEventos = "";
		for (int i = 0; i < serviciosNumeros.size()-1; i++) {
			listaEventos += serviciosNumeros.get(i);
			listaEventos += ", ";
		}
		listaEventos += serviciosNumeros.get(serviciosNumeros.size()-1);
		
		long tamEventos = (long) serviciosNumeros.size();

		String sql = "SELECT q2.id";
        sql += " FROM ";
        sql += "(";
        sql += "WITH servicios_deseados AS (SELECT * FROM " + 
        		pp.darTablaServicio() + " WHERE TIPO IN (" + listaEventos +")";
        sql += "), ";
        sql += "conteo_servicios_alojamiento AS (";
        sql += "SELECT ALOJAMIENTO, COUNT(*) as total_servicios_alojamiento"
        		+ " FROM servicios_deseados"
        		+ " GROUP BY ALOJAMIENTO)";
        sql += " SELECT * FROM " + pp.darTablaAlojamiento() 
        		+ " a JOIN conteo_servicios_alojamiento b ON a.ID = b.ALOJAMIENTO"
        		+ " WHERE b.total_servicios_alojamiento = ?) q1 ";
        sql += "JOIN (";
        sql += "SELECT ALOJAMIENTO.ID FROM " + pp.darTablaAlojamiento() 
        		+ " WHERE ID NOT IN (";
        sql += "SELECT reserva.alojamiento FROM " + pp.darTablaReserva() 
        		+ " WHERE FECHACANCELACION = NULL AND FECHAINICIO >= TO_DATE(?, 'YYYY-MM-DD')"
        		+ " AND FECHAFINAL <= TO_DATE(?, 'YYYY-MM-DD')"; 
        sql += " ) ORDER BY ALOJAMIENTO.ID " 
		
        + " ) q2 "
		+ " ON q1.ALOJAMIENTO = q2.ID";
		
        
		Query q = pm.newQuery(SQL, sql);
		
		log.info(listaEventos);
		log.info(tamEventos);
		log.info(fECHAINICIO);
		log.info(fECHAFINAL);

		//q.setParameters(serviciosNumeros, tamEventos, fECHAINICIO, fECHAFINAL);
		q.setParameters(tamEventos, fECHAINICIO, fECHAFINAL);
		return q.executeList();
		
	}
	
	public Alojamiento verificarAlojamiento(PersistenceManager pm, long iD) {
		
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaAlojamiento() + " WHERE ID = ?");
		q.setResultClass(Alojamiento.class);
		q.setParameters(iD);
		return (Alojamiento) q.executeUnique();
		
	}

	public Alojamiento verificarAlojamientoOperador(PersistenceManager pm, long idOper, long idAloj) {
		
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaAlojamiento() + " WHERE ID = ? AND OPERADOR = ?");
		q.setResultClass(Alojamiento.class);
		q.setParameters(idAloj, idOper);
		return (Alojamiento) q.executeUnique();
	}

	public Object darOperador(PersistenceManager pm, long idAloj) {
		
		Query q = pm.newQuery(SQL, "SELECT OPERADOR FROM " + pp.darTablaAlojamiento() + " WHERE ID = ?");
		q.setParameters(idAloj);
		return (Object) q.executeUnique();
	}

	public List<Object> darAlojamientoPorFechaServiciosYTipoAlojamiento(PersistenceManager pm,
			String fECHAINICIO, String fECHAFINAL, ArrayList<String> serviciosNumeros,Long tipoA) {
		String listaEventos = "";
		for (int i = 0; i < serviciosNumeros.size()-1; i++) {
			listaEventos += serviciosNumeros.get(i);
			listaEventos += ", ";
		}
		listaEventos += serviciosNumeros.get(serviciosNumeros.size()-1);
		
		long tamEventos = (long) serviciosNumeros.size();

		String sql = "SELECT q2.id";
        sql += " FROM ";
        sql += "(";
        sql += "WITH servicios_deseados AS (SELECT * FROM " + 
        		pp.darTablaServicio() + " WHERE TIPO IN (" + listaEventos +")";
        sql += "), ";
        sql += "conteo_servicios_alojamiento AS (";
        sql += "SELECT ALOJAMIENTO, COUNT(*) as total_servicios_alojamiento"
        		+ " FROM servicios_deseados"
        		+ " GROUP BY ALOJAMIENTO)";
        sql += " SELECT * FROM " + pp.darTablaAlojamiento() 
        		+ " a JOIN conteo_servicios_alojamiento b ON a.ID = b.ALOJAMIENTO"
        		+ " WHERE b.total_servicios_alojamiento = ?) q1 ";
        sql += "JOIN (";
        sql += "SELECT ALOJAMIENTO.ID FROM " + pp.darTablaAlojamiento() 
        		+ " WHERE ID NOT IN (";
        sql += "SELECT reserva.alojamiento FROM " + pp.darTablaReserva() 
        		+ " WHERE FECHACANCELACION = NULL AND FECHAINICIO >= TO_DATE(?, 'YYYY-MM-DD')"
        		+ " AND FECHAFINAL <= TO_DATE(?, 'YYYY-MM-DD')"; 
        sql += " ) ORDER BY ALOJAMIENTO.ID " 
		
        + " ) q2 "
		+ " ON q1.ALOJAMIENTO = q2.ID"
        + " WHERE TIPO = ?";
		    
		Query q = pm.newQuery(SQL, sql);
		
		log.info(listaEventos);
		log.info(tamEventos);
		log.info(fECHAINICIO);
		log.info(fECHAFINAL);

		//q.setParameters(serviciosNumeros, tamEventos, fECHAINICIO, fECHAFINAL);
		q.setParameters(tamEventos, fECHAINICIO, fECHAFINAL,tipoA);
		return q.executeList();

	}
	
	
	public List<Object> darConsultarFuncionamiento(PersistenceManager pm,String fECHAINICIO, String fECHAFINAL) {
		Query q = pm.newQuery(SQL, "select alojamiento.* from alojamiento where id=(select reserva.alojamiento "
				+ "from reserva "
				+ "WHERE FECHAINICIO between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ "OR FECHAFINAL between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ "AND FECHACANCELACION IS NULL "
				+ "GROUP BY reserva.alojamiento "
				+ "HAVING COUNT(reserva.alojamiento) >= 1 "
				+ "ORDER BY count(ALOJAMIENTO) desc "
				+ "FETCH FIRST 1 ROWS ONLY)");
		q.setParameters(fECHAINICIO,fECHAFINAL,fECHAINICIO,fECHAFINAL);
        //q.setResultClass(Object.class);
        return (List<Object>) q.executeList();
	}
	
	public List<Object> darConsultarFuncionamientoMenos(PersistenceManager pm,String fECHAINICIO, String fECHAFINAL) {
		Query q = pm.newQuery(SQL, "select alojamiento.* from alojamiento where id=(select reserva.alojamiento "
				+ "from reserva "
				+ "WHERE FECHAINICIO between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ "OR FECHAFINAL between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ "AND FECHACANCELACION IS NULL "
				+ "GROUP BY reserva.alojamiento "
				+ "HAVING COUNT(reserva.alojamiento) >= 1 "
				+ "ORDER BY count(ALOJAMIENTO) asc "
				+ "FETCH FIRST 1 ROWS ONLY)");
		q.setParameters(fECHAINICIO,fECHAFINAL,fECHAINICIO,fECHAFINAL);
        //q.setResultClass(Object.class);
        return (List<Object>) q.executeList();
	}


	public Object verificarCupoAlojamiento(PersistenceManager pm, long idAlojamiento, String fechaInicio, String fechaFinal) {
		
		Query q = pm.newQuery(SQL, "SELECT sum(RESERVA.CANTIDADPERSONAS) FROM " + pp.darTablaReserva() 
		+ " INNER JOIN " + pp.darTablaAlojamiento() 
		+ " ON RESERVA.ALOJAMIENTO = ALOJAMIENTO.ID"
		+ " WHERE ALOJAMIENTO.ID = " + idAlojamiento
		+ " AND FECHAINICIO >= TO_DATE(?, 'YYYY-MM-DD')"
		+ " AND FECHAFINAL <= TO_DATE(?, 'YYYY-MM-DD')");
		q.setParameters(fechaInicio, fechaFinal);
		return (Object) q.executeUnique();
	}

	public Object darCapacidadPorId (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT CAPACIDAD FROM " + pp.darTablaAlojamiento () + " WHERE ID = ?");
		q.setParameters(ID);
		return (Object) q.executeUnique();
	}


	public Object darTipoAlojamientoPorId(PersistenceManager pm, long idAlojamiento) {
		
		Query q = pm.newQuery(SQL, "SELECT TIPO FROM " + pp.darTablaAlojamiento() 
		+ " WHERE ID = ?");
		//q.setResultClass(Timestamp.class);
		q.setParameters(idAlojamiento);
		return (Object) q.executeUnique();
	}
	
	public void deshabilitarAlojamientoPorId(PersistenceManager pm, long idAlojamiento) {		
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaAlojamiento()
				+ " SET ACTIVO = 0" 
				+ " WHERE ID = " + idAlojamiento);
		//q.setParameters(id);
		q.executeUnique();
	}
	
	
	public List<Alojamiento> darAlojamientosActivosPorTipoAlojamiento (PersistenceManager pm, long tipoAloj)
	{
		Query q = pm.newQuery(SQL, "SELECT ALOJAMIENTO.* FROM " + pp.darTablaAlojamiento()
				+ " where tipo = ? AND ACTIVO = 1");
		q.setParameters(tipoAloj);
		q.setResultClass(Alojamiento.class);
		return (List<Alojamiento>) q.executeList();
	}

	public Object darActivoPorId(PersistenceManager pm, long idAlojamiento) {
		
		Query q = pm.newQuery(SQL, "SELECT ACTIVO FROM " + pp.darTablaAlojamiento() 
		+ " WHERE ID = ?");
		//q.setResultClass(Long.class);
		q.setParameters(idAlojamiento);
		return (Object) q.executeUnique();
	}

	public void rehabilitarAlojamientoPorId(PersistenceManager pm, long idAlojamiento) {		
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaAlojamiento()
				+ " SET ACTIVO = 1" 
				+ " WHERE ID = " + idAlojamiento);
		//q.setParameters(id);
		q.executeUnique();
	}
	
	


	
}
