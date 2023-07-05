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
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

import uniandes.isis2304.parranderos.negocio.Cliente;
import uniandes.isis2304.parranderos.negocio.Reserva;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto BAR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLReserva 
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
	
	private static Logger log = Logger.getLogger(SQLReserva.class.getName());


	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLReserva (PersistenciaAlohandes pp)
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
	public long adicionarReserva(PersistenceManager pm, 
			long ID, 
			String FECHAINICIO, String FECHAFINAL, 
			Long ALOJAMIENTO, long CLIENTE, Long CONTRATO, long PRECIO, long CANTIDADPERSONAS,Long RESERVAMADRE)
	{	
		
		log.info("CONTRATO: " + CONTRATO);
		

		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaReserva() + 
        		"(ID, FECHAINICIO, FECHAFINAL, ALOJAMIENTO, CLIENTE, CONTRATO,PRECIO,CANTIDADPERSONAS,RESERVAMADRE) values (?, to_date(?,'yyyy-mm-dd'), to_date(?,'yyyy-mm-dd'), ?, ?, ?, ?,?,?)");
        q.setParameters( ID, 
    			FECHAINICIO, FECHAFINAL, ALOJAMIENTO, 
    			CLIENTE, CONTRATO, PRECIO,CANTIDADPERSONAS,RESERVAMADRE);
        return (long) q.executeUnique();
	
	}

	


	public long eliminarReservaPorId (PersistenceManager pm, long ID)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReserva() + " WHERE ID = ?");
        q.setParameters(ID);
        return (long) q.executeUnique();
	}
	
	public long cancelarReservaPorId (PersistenceManager pm, long ID, String fechaCancelacion)
	{
        Query q = pm.newQuery(SQL, //"SET TRANSACTION ISOLATION LEVEL READ COMMITTED"

        " UPDATE " + pp.darTablaReserva() 
        + " SET FECHACANCELACION = TO_DATE(?, 'YYYY-MM-DD')"
        + " WHERE ID = ?");
        q.setParameters(fechaCancelacion, ID);
        return (long) q.executeUnique();
	}
	
	public long cancelarReservasPorIdReservaColectiva(PersistenceManager pm, long idReservaColectiva, String fechaCancelacion)
	{
        Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaReserva() 
        + " SET FECHACANCELACION = TO_DATE(?, 'YYYY-MM-DD')"
        + " WHERE RESERVAMADRE = ?");
        q.setParameters(fechaCancelacion, idReservaColectiva);
        return (long) q.executeUnique();
	}
	

	public Object darAlojamiento(PersistenceManager pm, long idReserva) {
		
		Query q = pm.newQuery(SQL, "SELECT ALOJAMIENTO FROM " + pp.darTablaReserva() + " WHERE ID = ?");
		q.setParameters(idReserva);
		log.info("dentro de darAlojamiento");
		return (Object) q.executeUnique();
	}
	
	
	public Object[] consultarPrecioFinal(PersistenceManager pm, long ID_ALOJAMIENTO) {
		Query q = pm.newQuery(SQL, "select PRECIO,TIPOPAGO from " + pp.darTablaAlojamiento() 
				+ " INNER JOIN "+ pp.darTablaTipoAlojamiento() +" ON ALOJAMIENTO.tipo = TIPOALOJAMIENTO.id "
				+ " WHERE ALOJAMIENTO.ID = ?");
        q.setParameters(ID_ALOJAMIENTO);
        return (Object[]) q.executeUnique();
	}

	
	public Reserva darReservaPorId (PersistenceManager pm, long ID) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva() + " WHERE ID = ?");
		q.setResultClass(Reserva.class);
		q.setParameters(ID);
		return (Reserva) q.executeUnique();
	}

	
	
	public List<Reserva> darReservas (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva());
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.executeList();
	}
	
	public List<Reserva> darReservasMiembroComunidad (PersistenceManager pm, String vinculacion)
	{
		Query q = pm.newQuery(SQL, "SELECT RESERVA.* FROM " + pp.darTablaReserva()+
				" inner join " + pp.darTablaCliente() +
				" on reserva.cliente = cliente.cedula "
				+ " where vinculacion = ?"
				+ " FETCH FIRST 50 ROWS ONLY");
		q.setParameters(vinculacion);
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.executeList();
	}
	
	public List<Reserva> listarConsumoUsuario (PersistenceManager pm,String fECHAINICIO,String fECHAFINAL,String cRITERIO,long aLOJAMIENTO,long iD_CLIENTE)
	{
		Query q = pm.newQuery(SQL, "SELECT RESERVA.* FROM " + pp.darTablaReserva()+
				" WHERE CLIENTE = ? "
				+ "AND FECHAINICIO between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ "AND FECHAFINAL between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ "AND FECHACANCELACION IS NULL "
				+ "AND ALOJAMIENTO = ? "
				+ "ORDER BY ? ");
		q.setParameters(iD_CLIENTE,fECHAINICIO, fECHAFINAL,fECHAINICIO, fECHAFINAL, aLOJAMIENTO,cRITERIO);
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.executeList();
	}
	
	public List<Cliente> listarConsumoUsuario2 (PersistenceManager pm,String fECHAINICIO,String fECHAFINAL,String cRITERIO,long aLOJAMIENTO,long iD_CLIENTE)
	{
		Query q = pm.newQuery(SQL, 
				"SELECT CLIENTE.*"
				+" FROM CLIENTE "
				+" WHERE NOT EXISTS ("
				+" SELECT 1"
				+" FROM RESERVA"
				+" WHERE RESERVA.CLIENTE = ? "
				+ "AND FECHAINICIO between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ "AND ALOJAMIENTO = ? )"
				+ " AND CLIENTE.CEDULA = ?");
				//+ "ORDER BY ? ");
		q.setParameters(iD_CLIENTE,fECHAINICIO, fECHAFINAL, aLOJAMIENTO,iD_CLIENTE);
		q.setResultClass(Cliente.class);
		return (List<Cliente>) q.executeList();
	}
	
	public List<Cliente> listarBuenosClientes1(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, 
				
				" SELECT Cliente.* "
				+ " FROM Cliente"
				+ " WHERE Cedula IN ("
				+ " SELECT Cliente"
				+ " FROM Reserva"
				+ " GROUP BY Cliente"
				+ " HAVING COUNT(DISTINCT(EXTRACT(MONTH FROM FechaInicio))) >= 1"
				+ " )"
				+ " FETCH FIRST 100 ROWS ONLY");
		q.setResultClass(Cliente.class);
		return (List<Cliente>) q.executeList();
	}
	
	public List<Cliente> listarBuenosClientes2(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, 
				
				" SELECT Cliente.*"
				+ " FROM Cliente"
				+ " WHERE Cedula NOT IN ("
				+ " SELECT Cliente"
				+ "    FROM Reserva"
				+ "    WHERE Precio <= 1500000" // 1.5M
				+ ")"
				+ " FETCH FIRST 100 ROWS ONLY");
		q.setResultClass(Cliente.class);
		return (List<Cliente>) q.executeList();
	}
	
	
	public List<Cliente> listarBuenosClientes3(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, 
				
				" SELECT Cliente.*"
				+ " FROM Cliente"
				+ " WHERE Cedula NOT IN ("
				+ "    SELECT Reserva.Cliente"
				+ "    FROM Reserva"
				+ "    JOIN Alojamiento ON Reserva.Alojamiento = Alojamiento.ID"
				+ "    WHERE Alojamiento.Tipo != '1'"
				+ ")"
				+ " FETCH FIRST 100 ROWS ONLY");
		q.setResultClass(Cliente.class);
		return (List<Cliente>) q.executeList();
	}
	
	

	public List<Object> darTop20Ofertas(PersistenceManager pm) {
		Query q = pm.newQuery(SQL, "SELECT ALOJAMIENTO, count(ALOJAMIENTO) FROM " + pp.darTablaReserva() + 
	    " GROUP BY ALOJAMIENTO ORDER BY count(ALOJAMIENTO) desc FETCH FIRST 20 ROWS ONLY");
		
        //q.setResultClass(Object.class);
        return (List<Object>) q.executeList();
	}
	
	public List<Object> darUsoUsuario(PersistenceManager pm, long idCLIENTE) {
		Query q = pm.newQuery(SQL, "SELECT reserva.alojamiento,alojamiento.ubicacion,alojamiento.amoblado, reserva.precio, "
				+ "reserva.cantidadpersonas,reserva.fechainicio, reserva.fechafinal,"
				+ "fechafinal - fechainicio as totaldias from reserva "
				+ "inner join alojamiento on "
				+ "reserva.alojamiento = alojamiento.id "
				+ "where fechacancelacion IS NULL "
				+ "and cliente = ?");
		q.setParameters(idCLIENTE);
        //q.setResultClass(Object.class);
        return (List<Object>) q.executeList();
	}
	
	public List<Object> darConsumoUsuario(PersistenceManager pm, long idCLIENTE,String fECHAINICIO,String fECHAFINAL,String cRITERIO) {
		Query q = pm.newQuery(SQL, "SELECT reserva.alojamiento,alojamiento.ubicacion,alojamiento.amoblado, reserva.precio, "
				+ "reserva.cantidadpersonas,reserva.fechainicio, reserva.fechafinal,"
				+ "fechafinal - fechainicio as totaldias from reserva "
				+ "inner join alojamiento on "
				+ "reserva.alojamiento = alojamiento.id "
				+ "where fechacancelacion IS NULL "
				+ "and cliente = ?");
		q.setParameters(idCLIENTE,fECHAINICIO, fECHAFINAL,cRITERIO);
        //q.setResultClass(Object.class);
        return (List<Object>) q.executeList();
	}
	
	public List<Object> darConsumoUsuarios(PersistenceManager pm,String fECHAINICIO,String fECHAFINAL,String cRITERIO, long aLOJAMIENTO) {
		Query q = pm.newQuery(SQL, "SELECT reserva.cliente AS CEDULA, "
				+ "COUNT(reserva.cliente) as NUMRESERVAS, "
				+ "CLIENTE.NOMBRE,CLIENTE.VINCULACION, CLIENTE.CORREO "
				+ "FROM reserva "
				+ "INNER JOIN CLIENTE ON reserva.cliente = cliente.cedula "
				+ "WHERE FECHAINICIO between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				//+ "AND FECHACANCELACION IS NULL  "
				+ "AND ALOJAMIENTO = " + aLOJAMIENTO 
				+ " GROUP BY reserva.cliente, CLIENTE.NOMBRE,CLIENTE.VINCULACION, CLIENTE.CORREO "
				+ "HAVING COUNT(reserva.cliente) >= 1 "
				+ "ORDER BY ? ");
		q.setParameters(fECHAINICIO, fECHAFINAL, cRITERIO); 
        //q.setResultClass(Object.class);
        return (List<Object>) q.executeList();
	}
	
	public List<Object> darConsumoUsuarios2(PersistenceManager pm,String fECHAINICIO,String fECHAFINAL,String cRITERIO, long aLOJAMIENTO) {
		Query q = pm.newQuery(SQL, 
				
				"SELECT cliente.CEDULA AS CEDULA, 0 AS NUMRESERVAS, "
				+ "CLIENTE.NOMBRE, CLIENTE.VINCULACION, CLIENTE.CORREO "
				+ " FROM CLIENTE "
				+ " WHERE CLIENTE.CEDULA NOT IN ("
				+ " SELECT RESERVA.CLIENTE "
				+ " FROM reserva "
				+ " INNER JOIN CLIENTE ON reserva.cliente = cliente.cedula "
				+ " WHERE FECHAINICIO between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				+ " AND ALOJAMIENTO = " + aLOJAMIENTO 
				+ " GROUP BY reserva.cliente, CLIENTE.NOMBRE,CLIENTE.VINCULACION, CLIENTE.CORREO "
				+ " HAVING COUNT(reserva.cliente) >= 1)"
				+ " ORDER BY ? "
				+ " FETCH FIRST 1397 ROWS ONLY");
		q.setParameters(fECHAINICIO, fECHAFINAL, cRITERIO); 
        //q.setResultClass(Object.class);
        return (List<Object>) q.executeList();
	}
	
	public List<Object> darConsumoUsuarios22(PersistenceManager pm,String fECHAINICIO,String fECHAFINAL,String cRITERIO, long aLOJAMIENTO) {
		Query q = pm.newQuery(SQL, "SELECT reserva.cliente AS CEDULA, "
				+ "COUNT(reserva.cliente) as NUMRESERVAS, "
				+ "CLIENTE.NOMBRE,CLIENTE.VINCULACION, CLIENTE.CORREO "
				+ "FROM reserva "
				+ "INNER JOIN CLIENTE ON reserva.cliente = cliente.cedula "
				+ "WHERE FECHAINICIO between to_date(?,'yyyy-mm-dd') and to_date(?,'yyyy-mm-dd') "
				//+ "AND FECHACANCELACION IS NULL  "
				+ " AND ALOJAMIENTO = " + aLOJAMIENTO 
				+ " GROUP BY reserva.cliente, CLIENTE.NOMBRE,CLIENTE.VINCULACION, CLIENTE.CORREO "
				+ "HAVING COUNT(reserva.cliente) = 0 "
				+ "ORDER BY ? "
				+ " FETCH FIRST 1000 ROWS ONLY");
		q.setParameters(fECHAINICIO, fECHAFINAL, cRITERIO); 
        //q.setResultClass(Object.class);
        return (List<Object>) q.executeList();
	}
	
	public Reserva verificarReservaCliente(PersistenceManager pm, long idCLIENTE, long idReserva) {
			
			Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva() + " WHERE ID = ? AND CLIENTE = ?");
			q.setResultClass(Reserva.class);
			q.setParameters(idReserva, idCLIENTE);
			return (Reserva) q.executeUnique();
		}

	public Timestamp darFechaInicio(PersistenceManager pm, long idReserva) {
		
		Query q = pm.newQuery(SQL, "SELECT FECHAINICIO FROM " + pp.darTablaReserva() + " WHERE ID = ?");
		//q.setResultClass(Timestamp.class);
		q.setParameters(idReserva);
		return (Timestamp) q.executeUnique();
	}
	
	public Timestamp darFechaFinal(PersistenceManager pm, long idReserva) {
		
		Query q = pm.newQuery(SQL, "SELECT FECHAFINAL FROM " + pp.darTablaReserva() + " WHERE ID = ?");
		//q.setResultClass(Timestamp.class);
		q.setParameters(idReserva);
		return (Timestamp) q.executeUnique();
	}

	public Object darTipoPago(PersistenceManager pm, long idAlojamiento) {
		Query q = pm.newQuery(SQL, "select TIPOPAGO from " + pp.darTablaAlojamiento() 
		+ " INNER JOIN "+ pp.darTablaTipoAlojamiento() +" ON ALOJAMIENTO.tipo = TIPOALOJAMIENTO.id "
		+ " WHERE ALOJAMIENTO.ID = ?");
		q.setParameters(idAlojamiento);
		return (Object) q.executeUnique();
	}

	public void actualizarPrecio(PersistenceManager pm, double porcentaje, long idReserva) {
		
        Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaReserva() 
        + " SET PRECIO = PRECIO*?"
        + " WHERE ID = ?");
        q.setParameters(porcentaje, idReserva);
        q.executeUnique();
	}
	
	public void actualizarPrecioReservaColectiva(PersistenceManager pm, long idReservaColectiva, long sumaColectiva) {
		
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaReserva()
				+ " SET PRECIO = " + sumaColectiva
				+ " WHERE ID = " + idReservaColectiva);
		//q.setParameters(ingreso, idOperador);
		q.executeUnique();
	}
	
	public Object darSumaReservaColectiva(PersistenceManager pm, long idReservaColectiva) {
		Query q = pm.newQuery(SQL, "SELECT sum(RESERVA.PRECIO) FROM " + pp.darTablaReserva() 
		+ " WHERE RESERVAMADRE = " + idReservaColectiva +" AND ID != " + idReservaColectiva);
		//q.setParameters(fechaInicio);
		return (Object) q.executeUnique();
	}
	
	public List<Reserva> darReservasPorReservaColectiva (PersistenceManager pm, long idReservaColectiva)
	{
		Query q = pm.newQuery(SQL, "SELECT RESERVA.* FROM " + pp.darTablaReserva()
				+ " where RESERVAMADRE = ? AND ID != ?");
		q.setParameters(idReservaColectiva, idReservaColectiva);
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.executeList();
	}
	
	public List<Reserva> darReservasIndividualesYColectivasPorTipoAlojamiento(PersistenceManager pm, long tipoAlojamiento) 
	{
		Query q = pm.newQuery(SQL, "SELECT RESERVA.* FROM " + pp.darTablaReserva()+
				" inner join " + pp.darTablaAlojamiento() +
				" on reserva.alojamiento = alojamiento.id "
				+ " where alojamiento.tipo = ? and (reserva.reservamadre is null or reserva.id = reserva.reservamadre)");
		q.setParameters(tipoAlojamiento);
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.executeList();
	}
	
	public List<Reserva> darReservasPorAlojamiento(PersistenceManager pm, long alojamiento) 
	{
		Query q = pm.newQuery(SQL, "SELECT RESERVA.* FROM " + pp.darTablaReserva()
				+ " where reserva.alojamiento = ?"
				+ " and reserva.fechacancelacion is null"
				+ " order by fechainicio");
		q.setParameters(alojamiento);
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.executeList();
	}
	
	public List<Reserva> darReservasVigentesPorAlojamiento(PersistenceManager pm, long alojamiento, String fechaActual) 
	{
		Query q = pm.newQuery(SQL, "SELECT RESERVA.* FROM " + pp.darTablaReserva()
				+ " where reserva.alojamiento = ?"
				+ " and reserva.fechacancelacion is null"
				+ " and reservamadre is null"
				+ " and fechafinal > TO_DATE(?, 'YYYY-MM-DD')"
				+ " and fechainicio <= TO_DATE(?, 'YYYY-MM-DD')"
				+ " order by fechafinal");
		q.setParameters(alojamiento, fechaActual, fechaActual);
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.executeList();
	}
	
	public List<Reserva> darReservasProximasPorAlojamiento(PersistenceManager pm, long alojamiento, String fechaActual) 
	{
		Query q = pm.newQuery(SQL, "SELECT RESERVA.* FROM " + pp.darTablaReserva()
				+ " where reserva.alojamiento = ?"
				+ " and reserva.fechacancelacion is null"
				+ " and reservamadre is null"
				+ " and fechafinal > TO_DATE(?, 'YYYY-MM-DD')"
				+ " and fechainicio > TO_DATE(?, 'YYYY-MM-DD')"
				+ " order by id");
		q.setParameters(alojamiento, fechaActual, fechaActual);
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.executeList();
	}
	
	public List<Reserva> darReservasIndividualesYSubreservasPorTipoAlojamiento(PersistenceManager pm, long tipoAlojamiento) 
	{
		Query q = pm.newQuery(SQL, "SELECT RESERVA.* FROM " + pp.darTablaReserva()+
				" inner join " + pp.darTablaAlojamiento() +
				" on reserva.alojamiento = alojamiento.id "
				+ " where alojamiento.tipo = ? and (reserva.reservamadre is null or reserva.id != reserva.reservamadre)");
		q.setParameters(tipoAlojamiento);
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.executeList();
	}

	public Timestamp darUltimaFechaReservasPorAlojamiento(PersistenceManager pm, long alojamiento) {
		Query q = pm.newQuery(SQL, "select max(fechafinal) from " + pp.darTablaReserva() 
		+ " WHERE FECHACANCELACION IS NULL AND reserva.alojamiento = ?");
		q.setParameters(alojamiento);
		Object result = q.executeUnique();
		if (result == null) {
		    return null;
		} else {
		    return (Timestamp) result;
		}
	}
	
	public void cancelarReservasColectivasPorAlojamiento(PersistenceManager pm, long idAlojamiento, String fechaActual) {		
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaReserva()
				+ " SET FECHACANCELACION = ?" 
				+ " WHERE RESERVAMADRE = ID"
				+ " AND ID IN (" 
				+ " SELECT RESERVA.ID FROM Reserva"
				+ " where reserva.alojamiento = ?"
				+ " and fechafinal > TO_DATE(?, 'YYYY-MM-DD'))");
		q.setParameters(fechaActual, idAlojamiento, fechaActual);
		q.executeUnique();
	}
	
	public void desagregarSubreservasPorAlojamiento(PersistenceManager pm, long idAlojamiento, String fechaActual) {		
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaReserva()
				+ " SET RESERVAMADRE = NULL" 
				+ " WHERE RESERVAMADRE != ID"
				+ " AND ID IN (" 
				+ " SELECT RESERVA.ID FROM Reserva"
				+ " where reserva.alojamiento = ?"
				+ " and fechafinal > TO_DATE(?, 'YYYY-MM-DD'))");
		q.setParameters(idAlojamiento, fechaActual);
		q.executeUnique();
	}
	
	
}
