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


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uniandes.isis2304.parranderos.negocio.Alojamiento;
import uniandes.isis2304.parranderos.negocio.Cliente;
import uniandes.isis2304.parranderos.negocio.Horario;
import uniandes.isis2304.parranderos.negocio.Operador;
import uniandes.isis2304.parranderos.negocio.Reserva;
import uniandes.isis2304.parranderos.negocio.TipoAlojamiento;
import uniandes.isis2304.parranderos.negocio.VOAlojamiento;

/**
 * Clase para el manejador de persistencia del proyecto Parranderos
 * Traduce la información entre objetos Java y tuplas de la base de datos, en ambos sentidos
 * Sigue un patrón SINGLETON (Sólo puede haber UN objeto de esta clase) para comunicarse de manera correcta
 * con la base de datos
 * Se apoya en las clases SQLBar, SQLBebedor, SQLBebida, SQLGustan, SQLSirven, SQLTipoBebida y SQLVisitan, que son 
 * las que realizan el acceso a la base de datos
 * 
 * @author Germán Bravo
 */
public class PersistenciaAlohandes 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PersistenciaAlohandes.class.getName());
	
	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el único objeto de la clase - Patrón SINGLETON
	 */
	private static PersistenciaAlohandes instance;
	
	/**
	 * Fábrica de Manejadores de persistencia, para el manejo correcto de las transacciones
	 */
	private PersistenceManagerFactory pmf;
	
	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden:
	 * Secuenciador, tipoBebida, bebida, bar, bebedor, gustan, sirven y visitan
	 */
	private List <String> tablas;
	
	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaParranderos
	 */
	private SQLUtil2 sqlUtil2;
	

	private SQLTipoMiembroComunidad sqlTipoMiembroComunidad;

	private SQLCliente sqlCliente;

	private SQLOperador sqlOperador;

	private SQLTipoOperadorEmpresa sqlTipoOperadorEmpresa;

	private SQLEmpresa sqlEmpresa;

	private SQLTipoOperadorNatural sqlTipoOperadorNatural;

	private SQLPersonaNatural sqlPersonaNatural;

	private SQLContrato sqlContrato;

	private SQLSeguro sqlSeguro;

	private SQLHorario sqlHorario;

	private SQLTipoAlojamiento sqlTipoAlojamiento;

	private SQLAlojamiento sqlAlojamiento;

	private SQLReserva sqlReserva;

	private SQLTipoServicioPublico sqlTipoServicioPublico;

	private SQLServicioPublico sqlServicioPublico;

	private SQLTipoServicio sqlTipoServicio;

	private SQLServicio sqlServicio;
	
	/* ****************************************************************
	 * 			Métodos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */
	private PersistenciaAlohandes ()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("Alohandes");		
		crearClasesSQL ();
		
		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("ALOHANDES_sequence");
		tablas.add ("TIPOMIEMBROCOMUNIDAD");
		tablas.add ("CLIENTE");
		tablas.add ("OPERADOR");
		tablas.add ("TIPOOPERADOREMPRESA");
		tablas.add ("EMPRESA");
		tablas.add ("TIPOOPERADORNATURAL");
		tablas.add ("PERSONANATURAL");
		tablas.add ("CONTRATO");
		tablas.add ("SEGURO");
		tablas.add ("HORARIO");
		tablas.add ("TIPOALOJAMIENTO");
		tablas.add ("ALOJAMIENTO");
		tablas.add ("RESERVA");
		tablas.add ("TIPOSERVICIOPUBLICO");
		tablas.add ("SERVICIOPUBLICO");
		tablas.add ("TIPOSERVICIO");
		tablas.add ("SERVICIO");
		
	}

	/**
	 * Constructor privado, que recibe los nombres de las tablas en un objeto Json - Patrón SINGLETON
	 * @param tableConfig - Objeto Json que contiene los nombres de las tablas y de la unidad de persistencia a manejar
	 */
	private PersistenciaAlohandes (JsonObject tableConfig)
	{
		crearClasesSQL ();
		tablas = leerNombresTablas (tableConfig);
		
		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);
	}

	/**
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaAlohandes getInstance ()
	{
		if (instance == null)
		{
			instance = new PersistenciaAlohandes ();
		}
		return instance;
	}
	
	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del objeto tableConfig
	 * @param tableConfig - El objeto JSON con los nombres de las tablas
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaAlohandes getInstance (JsonObject tableConfig)
	{
		if (instance == null)
		{
			instance = new PersistenciaAlohandes (tableConfig);
		}
		return instance;
	}

	/**
	 * Cierra la conexión con la base de datos
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}
	
	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList <String> ();
		for (JsonElement nom : nombres)
		{
			resp.add (nom.getAsString ());
		}
		
		return resp;
	}
	
	/**
	 * Crea los atributos de clases de apoyo SQL
	 */
	private void crearClasesSQL ()
	{
		sqlTipoMiembroComunidad = new SQLTipoMiembroComunidad(this);
		sqlCliente = new SQLCliente(this);
		sqlOperador = new SQLOperador(this);
		sqlTipoOperadorEmpresa = new SQLTipoOperadorEmpresa(this);
		sqlEmpresa = new SQLEmpresa(this);
		sqlTipoOperadorNatural = new SQLTipoOperadorNatural (this);
		sqlPersonaNatural= new SQLPersonaNatural(this);		
		sqlContrato = new SQLContrato(this);
		sqlSeguro = new SQLSeguro(this);
		sqlHorario = new SQLHorario(this);
		sqlTipoAlojamiento = new SQLTipoAlojamiento(this);
		sqlAlojamiento = new SQLAlojamiento(this);
		sqlReserva = new SQLReserva(this);
		sqlTipoServicioPublico = new SQLTipoServicioPublico(this);
		sqlServicioPublico = new SQLServicioPublico(this);
		sqlTipoServicio = new SQLTipoServicio(this);
		sqlServicio = new SQLServicio(this);
		sqlUtil2 = new SQLUtil2(this);
		
	}

	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de parranderos
	 */
	public String darSeqAlohandes()
	{
		return tablas.get (0);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de TipoBebida de parranderos
	 */
	
	public String darTablaTipoMiembroComunidad ()
	{
		return tablas.get (1); //si no es 1
	}
	
	public String darTablaCliente() {
		return tablas.get (2); // O puede ser 2
	}
	
	public String darTablaOperador() {
		return tablas.get (3); // O puede ser 3
	}
	
	public String darTablaTipoOperadorEmpresa() {
		return tablas.get (4); // O puede ser 4
	}
	
	public String darTablaEmpresa() {
		return tablas.get (5); // O puede ser 5
	}
	
	public String darTablaTipoOperadorNatural() {
		return tablas.get (6); //si no es 6
	}
	

	public String darTablaPersonaNatural() {
		return tablas.get (7); //si no es 7
	}	
	
	public String darTablaContrato() {
		return tablas.get (8); //si no es 8
	}
	
	public String darTablaSeguro()
	{
		return tablas.get (9);
	}
	
	public String darTablaHorario()
	{
		return tablas.get (10);
	}
	
	public String darTablaTipoAlojamiento()
	{
		return tablas.get (11);
	}
	
	public String darTablaAlojamiento ()
	{
		return tablas.get(12); // o 12 con secuenciador
	}
	
	public String darTablaReserva()
	{
		return tablas.get (13);
	}
	
	public String darTablaTipoServicioPublico()
	{
		return tablas.get (14);
	}

	public String darTablaServicioPublico()
	{
		return tablas.get (15);
	}
	
	public String darTablaTipoServicio()
	{
		return tablas.get (16);
	}
	
	public String darTablaServicio()
	{
		return tablas.get (17);
	}
	
	

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Bebedor de parranderos
	 */
	public String darTablaBebedor ()
	{
		return tablas.get (4);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Gustan de parranderos
	 */
	public String darTablaGustan ()
	{
		return tablas.get (5);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Sirven de parranderos
	 */
	public String darTablaSirven ()
	{
		return tablas.get (6);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Visitan de parranderos
	 */
	public String darTablaVisitan ()
	{
		return tablas.get (7);
	}
	
	/**
	 * Transacción para el generador de secuencia de Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return El siguiente número del secuenciador de Parranderos
	 */
	private long nextval ()
	{
        long resp = sqlUtil2.nextval (pmf.getPersistenceManager());
        log.trace ("Generando secuencia: " + resp);
        return resp;
    }
	
	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la Exception e, que da el detalle específico del problema encontrado
	 * @param e - La excepción que ocurrio
	 * @return El mensaje de la excepción JDO
	 */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}
	
	
	
	public boolean verificarOperador(String iD_OPERADOR, String cLAVE_OPERADOR) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();

            Operador operadorEncontrado = sqlOperador.verificarOperador(pm, Long.parseLong(iD_OPERADOR), cLAVE_OPERADOR);
            
            log.info(operadorEncontrado);
            
            tx.commit();
            
            //log.trace ("Inserción de tipo de Alojamiento: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");
            
            if (operadorEncontrado != null) return true;
            else return false;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return false;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

	}


	public boolean verificarCliente(String iD_CLIENTE, String cLAVE_CLIENTE) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();

            Cliente clienteEncontrado = sqlCliente.verificarCliente(pm, Long.parseLong(iD_CLIENTE), cLAVE_CLIENTE);
            
            log.info(clienteEncontrado);
            
            tx.commit();
               
            if (clienteEncontrado != null) return true;
            else return false;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return false;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

	}


	public boolean verificarAlojamiento(String iD) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();

            Alojamiento alojamientoEncontrado = sqlAlojamiento.verificarAlojamiento(pm, Long.parseLong(iD));
            
            log.info(alojamientoEncontrado);
            
            tx.commit();
               
            if (alojamientoEncontrado != null) return true;
            else return false;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return false;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

	}
	


	public boolean verificarCupoAlojamiento(String iD,  String FECHAINICIO, String FECHAFINAL, String CANTIDADPERSONAS) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            
            
            Object cupoObject = sqlAlojamiento.verificarCupoAlojamiento(pm, Long.parseLong(iD), FECHAINICIO, FECHAFINAL);
            BigDecimal cupoEncontradoBD = cupoObject != null ? (BigDecimal) cupoObject : null;
            int cupoEncontrado = 0;
            if (cupoEncontradoBD != null) cupoEncontrado = cupoEncontradoBD.intValue();
            
            Object capacidadAlojObject = sqlAlojamiento.darCapacidadPorId(pm, Long.parseLong(iD));
            BigDecimal capacidadAlojBD = capacidadAlojObject != null ? (BigDecimal) capacidadAlojObject : null;
            int capacidadAloj = capacidadAlojBD.intValue();
            
            //int capacidadAloj = (Integer) sqlAlojamiento.darCapacidadPorId(pm, Long.parseLong(iD));
            
            tx.commit();
            
            if ( (cupoEncontrado + Integer.parseInt(CANTIDADPERSONAS)) <= capacidadAloj ) return true;
            else return false;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return false;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

	}

	
	public boolean verificarAlojamientoOperador(String iD_OPERADOR, long idAloj) {

		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();

            Alojamiento alojamientoEncontrado = sqlAlojamiento.verificarAlojamientoOperador(pm, Long.parseLong(iD_OPERADOR), idAloj);
            
            log.info(alojamientoEncontrado);
            
            tx.commit();
               
            if (alojamientoEncontrado != null) return true;
            else return false;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return false;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public boolean verificarReservaCliente(String iD_CLIENTE, long idReserva) {

		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
        	
            tx.begin();

            Reserva reservaEncontrada = sqlReserva.verificarReservaCliente(pm, Long.parseLong(iD_CLIENTE), idReserva);
            
            log.info(reservaEncontrada);
            
            tx.commit();
               
            if (reservaEncontrada != null) return true;
            else return false;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return false;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	
	
	/* ****************************************************************
	 * 			Métodos para manejar los TIPOS DE Alojamiento
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla TipoAlojamiento
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del tipo de Alojamiento
	 * @return El objeto TipoAlojamiento adicionado. null si ocurre alguna Excepción
	 */
	public TipoAlojamiento adicionarTipoAlojamiento(String nombre)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idTipoAlojamiento = nextval ();
            long tuplasInsertadas = sqlTipoAlojamiento.adicionarTipoAlojamiento(pm, idTipoAlojamiento, nombre);
            tx.commit();
            
            log.trace ("Inserción de tipo de Alojamiento: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new TipoAlojamiento (idTipoAlojamiento, nombre,"dia");
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla TipoAlojamiento, dado el nombre del tipo de Alojamiento
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del tipo de Alojamiento
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarTipoAlojamientoPorNombre (String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlTipoAlojamiento.eliminarTipoAlojamientoPorNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla TipoAlojamiento, dado el identificador del tipo de Alojamiento
	 * Adiciona entradas al log de la aplicación
	 * @param idTipoAlojamiento - El identificador del tipo de Alojamiento
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarTipoAlojamientoPorId (long idTipoAlojamiento) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlTipoAlojamiento.eliminarTipoAlojamientoPorId(pm, idTipoAlojamiento);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Método que consulta todas las tuplas en la tabla TipoAlojamiento
	 * @return La lista de objetos TipoAlojamiento, construidos con base en las tuplas de la tabla TIPOAlojamiento
	 */
	public List<TipoAlojamiento> darTiposAlojamiento ()
	{
		log.info("darTiposAlojamiento en pp");
		return sqlTipoAlojamiento.darTiposAlojamiento (pmf.getPersistenceManager());
	}
	
	public List<Reserva> listarReservas ()
	{
		return sqlReserva.darReservas (pmf.getPersistenceManager());
	}
	
	public List<Reserva> listarReservasTipoMiembroComunidad (String vinculacion)
	{
		return sqlReserva.darReservasMiembroComunidad (pmf.getPersistenceManager(),cambiarVinculacionPorNumero(vinculacion));
	}
	
	public List<Reserva> listarConsumoUsuario (String fECHAINICIO,String fECHAFINAL,String cRITERIO,String aLOJAMIENTO,String iD_CLIENTE)
	{
		return sqlReserva.listarConsumoUsuario (pmf.getPersistenceManager(),fECHAINICIO, fECHAFINAL, cRITERIO, Long.valueOf(aLOJAMIENTO),Long.valueOf(iD_CLIENTE));
	}
	
	public List<Cliente> listarConsumoUsuario2 (String fECHAINICIO,String fECHAFINAL,String cRITERIO,String aLOJAMIENTO,String iD_CLIENTE)
	{
		return sqlReserva.listarConsumoUsuario2 (pmf.getPersistenceManager(),fECHAINICIO, fECHAFINAL, cRITERIO, Long.valueOf(aLOJAMIENTO),Long.valueOf(iD_CLIENTE));
	}
	
	public List<Cliente> listarBuenosClientes1 ()
	{
		return sqlReserva.listarBuenosClientes1 (pmf.getPersistenceManager());
	}
	
	public List<Cliente> listarBuenosClientes2 ()
	{
		return sqlReserva.listarBuenosClientes2 (pmf.getPersistenceManager());
	}
	
	public List<Cliente> listarBuenosClientes3 ()
	{
		return sqlReserva.listarBuenosClientes3 (pmf.getPersistenceManager());
	}
	
	public List<Cliente> listarClienteFrecuente (String alojamiento)
	{
		return sqlCliente.listarClienteFrecuente (pmf.getPersistenceManager(),Long.parseLong(alojamiento));
	}
	
	/**
	 * Método que consulta todas las tuplas en la tabla TipoAlojamiento que tienen el nombre dado
	 * @param nombre - El nombre del tipo de Alojamiento
	 * @return La lista de objetos TipoAlojamiento, construidos con base en las tuplas de la tabla TIPOAlojamiento
	 */
	public List<TipoAlojamiento> darTipoAlojamientoPorNombre (String nombre)
	{
		return sqlTipoAlojamiento.darTiposAlojamientoPorNombre (pmf.getPersistenceManager(), nombre);
	}
 
	/**
	 * Método que consulta todas las tuplas en la tabla TipoAlojamiento con un identificador dado
	 * @param idTipoAlojamiento - El identificador del tipo de Alojamiento
	 * @return El objeto TipoAlojamiento, construido con base en las tuplas de la tabla TIPOAlojamiento con el identificador dado
	 */
	public TipoAlojamiento darTipoAlojamientoPorId (long idTipoAlojamiento)
	{
		return sqlTipoAlojamiento.darTipoAlojamientoPorId (pmf.getPersistenceManager(), idTipoAlojamiento);
	}



	
	/* ****************************************************************
	 * 			Métodos para manejar los BARES
	 *****************************************************************/
	

	public Reserva adicionarReserva(String fECHAINICIO, String fECHAFINAL, String aLOJAMIENTO,
			String cLIENTE, ArrayList<String> iNFO_CONTRATO, String cANTIDADPERSONAS, String reservaMadre) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idReserva = nextval ();
            
            
            Long idContrato = null;
            
            if (!iNFO_CONTRATO.isEmpty()) {
            	// crear contrato
            	idContrato= nextval();
            	sqlContrato.adicionarContrato(pm, idContrato, Long.parseLong(cambiarSiNoPorNumero(iNFO_CONTRATO.get(0))), Long.parseLong(cambiarSiNoPorNumero(iNFO_CONTRATO.get(1))), Long.parseLong(cambiarSiNoPorNumero(iNFO_CONTRATO.get(2))), Long.parseLong(cambiarSiNoPorNumero(iNFO_CONTRATO.get(3))) );
            }
            
            long tuplasInsertadas;
            long precio = 0;
            
            // caso de reserva original o subreserva
            if (aLOJAMIENTO != null) {
            	
                Object[] pRECIO = sqlReserva.consultarPrecioFinal(pm, Long.parseLong(aLOJAMIENTO));
                

    			precio = ((BigDecimal) pRECIO [0]).longValue ();
    			String pagofecha = ((String) pRECIO [1]);
    			
    			precio = obtenerPrecio(fECHAINICIO,fECHAFINAL,pagofecha,precio);
    			
    			// reserva original 
    			if (reservaMadre == null) {
                    tuplasInsertadas = sqlReserva.adicionarReserva(pm, idReserva,
                    		fECHAINICIO, fECHAFINAL, 
                    		Long.parseLong(aLOJAMIENTO), Long.parseLong(cLIENTE), 
                    		idContrato, precio, Long.parseLong(cANTIDADPERSONAS), null);
    			}
    			// subreserva
    			else {
                    tuplasInsertadas = sqlReserva.adicionarReserva(pm, idReserva,
                    		fECHAINICIO, fECHAFINAL, 
                    		Long.parseLong(aLOJAMIENTO), Long.parseLong(cLIENTE), 
                    		idContrato, precio, Long.parseLong(cANTIDADPERSONAS), 
                    		Long.parseLong(reservaMadre));
    			}
                
                long idOperador = ((BigDecimal) sqlAlojamiento.darOperador(pm, Long.parseLong(aLOJAMIENTO))).longValue();
                
                
                log.info("idOperador: " + idOperador);
                
              //after.date(1 year ago) 2022/04/03
                actualizarIngresosOperador(idOperador);
            }
            
            // caso reserva colectica
            else {
                tuplasInsertadas = sqlReserva.adicionarReserva(pm, idReserva,
                		fECHAINICIO, fECHAFINAL, null, Long.parseLong(cLIENTE), 
                		idContrato, precio, Long.parseLong(cANTIDADPERSONAS), idReserva);
            }


            tx.commit();

            log.trace ("Inserción de Reserva para el cliente con ID: " + cLIENTE + ": " + tuplasInsertadas + " tuplas insertadas");
            
            Reserva reserva;
            
            if (aLOJAMIENTO != null ) {
            	
    			// reserva original 
    			if (reservaMadre == null) {
	                reserva = new Reserva (idReserva, null, fECHAINICIO, fECHAFINAL, 
	                		Long.parseLong(aLOJAMIENTO),Long.parseLong(cLIENTE), 
	                		idContrato, precio,Long.parseLong(cANTIDADPERSONAS),null);
    			}
    			else {
	                reserva = new Reserva (idReserva, null, fECHAINICIO, fECHAFINAL, 
	                		Long.parseLong(aLOJAMIENTO),Long.parseLong(cLIENTE), 
	                		idContrato, precio,Long.parseLong(cANTIDADPERSONAS),Long.parseLong(reservaMadre));
    			}
            }
            
            else {	
        	   reserva = new Reserva (idReserva, null, fECHAINICIO, fECHAFINAL, 
               		null,Long.parseLong(cLIENTE), 
               		idContrato, precio,Long.parseLong(cANTIDADPERSONAS),idReserva);
           }

            return reserva;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public long obtenerPrecio(String fechainicio, String fechafinal, String tipopago,long precio) throws Exception {
		

		Timestamp fechainiStamp = cambiarATimestamp(fechainicio);
		Timestamp fechafinStamp = cambiarATimestamp(fechafinal);
			
		LocalDate fechaini = fechainiStamp.toLocalDateTime().toLocalDate();
		LocalDate fechafin = fechafinStamp.toLocalDateTime().toLocalDate();

		Period periodo = Period.between(fechaini,fechafin);
		int anios = periodo.getYears();
		int meses = periodo.getMonths();
		int days = periodo.getDays();
		
		long pagoTotal = 0; 
		
		if (tipopago.equals("mes")) {
			pagoTotal += anios * 12 * precio;
			pagoTotal += meses * precio;
			pagoTotal += days* precio/30;
		}
		else if (tipopago.equals("dia")){

			pagoTotal += anios * 365 * precio;
			pagoTotal += meses * 30 * precio;
			pagoTotal += days * precio;
		}
		else {
			throw new Exception("NO FUNCIONO");
		}
		return pagoTotal;
        
	}

	public static Timestamp cambiarATimestamp(String timestampString) throws ParseException {
		
		if (timestampString != null) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        
	        java.util.Date date = dateFormat.parse(timestampString);

	        return new Timestamp(date.getTime());
		}
		return null;
    }
	
	public void actualizarIngresosOperador(long idOperador) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
		
        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        LocalDateTime fechaCorrido = fechaActual.minusYears(1);
        String fechaCorridoStr = fechaCorrido.format(formatter);

        Object result = sqlOperador.darSumaAnioCorrido(pm, idOperador, fechaCorridoStr);
        BigDecimal sumaAnioCorrido = null;
        if (result != null) {
            sumaAnioCorrido = (BigDecimal) result;
        } else {
            sumaAnioCorrido = BigDecimal.ZERO; 
        }
        long ingresoAnioCorrido = sumaAnioCorrido.longValue();
        sqlOperador.actualizarIngresoAnioCorrido(pm, idOperador, ingresoAnioCorrido);

        
        String fechaAnioActualStr = fechaActual.getYear() + "-01-01";
        Object result1 = sqlOperador.darSumaAnioActual(pm, idOperador, fechaAnioActualStr);
        BigDecimal sumaAnioActual = null;
        if (result1 != null) {
            sumaAnioActual = (BigDecimal) result;
        } else {
            sumaAnioActual = BigDecimal.ZERO; 
        }
        long ingresoAnioActual = sumaAnioActual.longValue();
        sqlOperador.actualizarIngresoAnioActual(pm, idOperador, ingresoAnioActual);
	}
	
	public void actualizarPrecioReservaColectiva(long idReservaColectiva) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
        
		tx.begin();
		
        Object result = sqlReserva.darSumaReservaColectiva(pm, idReservaColectiva);
        BigDecimal sumaColectivaBD = null;
        if (result != null) {
            sumaColectivaBD = (BigDecimal) result;
        } else {
            sumaColectivaBD = BigDecimal.ZERO; 
        }
        long sumaColectiva = sumaColectivaBD.longValue();
        
        log.info("SUMA COLECTIVA: " + sumaColectiva);
        sqlReserva.actualizarPrecioReservaColectiva(pm, idReservaColectiva, sumaColectiva);
        
        tx.commit();
	}


	public Cliente adicionarCliente(long cEDULA, String cORREO, String nOMBRE, long cELULAR, String vINCULACION,
			String cLAVE) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            
            long vinculacion = Long.parseLong(cambiarVinculacionPorNumero(vINCULACION));
            long tuplasInsertadas = sqlCliente.adicionarCliente(pm, cEDULA, cORREO,nOMBRE, cELULAR, vinculacion,
        			cLAVE);
            log.info("Tuplas insertadas ahora commit de: CEDULA, CORREO, NOMBRE,CELULAR,VINCULACION, CLAVE");
            tx.commit();
            
            log.trace ("Inserción de nombre: " + nOMBRE + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Cliente (cEDULA, cORREO,nOMBRE, cELULAR, vinculacion,
        			cLAVE);
        }
        catch (Exception e)
        {
//          e.printStackTrace();
            log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public Operador adicionarOperador(String ID, String iNGRESOPORANIOACTUAL, String iNGRESOPORANIOCORRIDO, String nOMBRE,
			String cLAVE, String cORREO) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            //long idOperador = nextval ();
            long tuplasInsertadas = sqlOperador.adicionarOperador(pm, Long.parseLong(ID) ,Long.parseLong(iNGRESOPORANIOCORRIDO), Long.parseLong(iNGRESOPORANIOCORRIDO), nOMBRE, cLAVE,cORREO);
            tx.commit();

            log.trace ("Inserción de Operador: " + nOMBRE + ": " + tuplasInsertadas + " tuplas insertadas");

            return new Operador (Long.parseLong(ID) ,Long.parseLong(iNGRESOPORANIOCORRIDO), Long.parseLong(iNGRESOPORANIOCORRIDO), nOMBRE, cLAVE,cORREO);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public Operador adicionarOperadorEmpresa(String ID, String iNGRESOPORANIOACTUAL, String iNGRESOPORANIOCORRIDO, String nOMBRE,
			String cLAVE,String cORREO, String uBICACION, String rECEPCION, String rEGISTROTURISMO, String rEGISTROCAMARA, String nUMEROCONTACTO, String tIPOEMPRESA) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            //long idOperador = nextval ();
            long tuplasInsertadasOperador= sqlOperador.adicionarOperador(pm, Long.parseLong(ID), Long.parseLong(iNGRESOPORANIOCORRIDO), Long.parseLong(iNGRESOPORANIOCORRIDO), nOMBRE, cLAVE,cORREO);
            long tuplasInsertadasEmpresa = sqlEmpresa.adicionarEmpresa(pm, 
            		Long.parseLong(ID), uBICACION, Long.parseLong(rECEPCION),
            		Long.parseLong(rEGISTROTURISMO), 
            		Long.parseLong(rEGISTROCAMARA), 
            		Long.parseLong(nUMEROCONTACTO), 
            		Long.parseLong(tIPOEMPRESA));
            tx.commit();

            log.trace ("Inserción de Operador: " + nOMBRE + ": " + tuplasInsertadasOperador + " tuplas insertadas");
            log.trace ("Inserción de Empresa: " + ID + ": " + tuplasInsertadasEmpresa + " tuplas insertadas");

            return new Operador (Long.parseLong(ID) ,Long.parseLong(iNGRESOPORANIOCORRIDO), Long.parseLong(iNGRESOPORANIOCORRIDO), nOMBRE, cLAVE,cORREO);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	

	public Operador adicionarOperadorNatural(String ID, String iNGRESOPORANIOACTUAL, String iNGRESOPORANIOCORRIDO, String nOMBRE,
			String cLAVE, String cORREO, String cELULAR, String tIPOPERSONANATURAL) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            //long idOperador = nextval ();
            long tuplasInsertadasOperador= sqlOperador.adicionarOperador(pm, Long.parseLong(ID), Long.parseLong(iNGRESOPORANIOCORRIDO), Long.parseLong(iNGRESOPORANIOCORRIDO), nOMBRE, cLAVE,cORREO);
            long tuplasInsertadasNatural = sqlPersonaNatural.adicionarPersonaNatural(pm, Long.parseLong(ID), cORREO, Long.parseLong(cELULAR), Long.parseLong(tIPOPERSONANATURAL));
            tx.commit();

            log.trace ("Inserción de Operador: " + nOMBRE + ": " + tuplasInsertadasOperador + " tuplas insertadas");
            log.trace ("Inserción de Persona Natural: " + ID + ": " + tuplasInsertadasNatural + " tuplas insertadas");

            return new Operador (Long.parseLong(ID) ,Long.parseLong(iNGRESOPORANIOCORRIDO), Long.parseLong(iNGRESOPORANIOCORRIDO), nOMBRE, cLAVE,cORREO);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	
	


	
	
    public String cambiarSiNoPorNumero (String booleano) {
		if (booleano == "SI") return "1";
		else return "0"; // NO
    }
	
	
    public String cambiarVinculacionPorNumero (String vinculacion) {
		if (vinculacion == "Profesor") {
			return "1";
		}
		else if (vinculacion == "Empleado") {
			return "2";
		}
		else if (vinculacion == "Egresado") {
			return "3";
		}
		else if (vinculacion == "Estudiante") {
			return "4";
		}
		else return "5"; // padreEstudiante
    }
    
    public String cambiarTipoAlojamientoPorNumero (String tipoAlojamiento) {
		if (tipoAlojamiento == "hotelSuite") {
			return "1";
		}
		else if (tipoAlojamiento == "hotelSemisuit") {
			return "2";
		}
		else if (tipoAlojamiento == "hotelEstandar") {
			return "3";
		}
		else if (tipoAlojamiento == "habitacionHostal") {
			return "4";
		}
		else if (tipoAlojamiento == "habitacionVivienda") {
			return "5";
		}
		else if (tipoAlojamiento == "apartamento") {
			return "6";
		}
		else if (tipoAlojamiento == "vivienda") {
			return "7";
		}
		else return "8"; // habitacionResidencia
    }
    
    public String cambiarTipoServicioPublicoPorNumero (String tipoServicioPublico) {
		if (tipoServicioPublico == "agua") {
			return "1";
		}
		else if (tipoServicioPublico == "luz") {
			return "2";
		}
		else if (tipoServicioPublico == "telefono") {
			return "3";
		}
		else if (tipoServicioPublico == "tvCable") {
			return "4";
		}

		else return "5"; // internet
    }
    
    
    public String cambiarTipoServicioPorNumero (String tipoServicio) {
		
    	if (tipoServicio == "restaurante") {
			return "1";
		}
		else if (tipoServicio == "piscina") {
			return "2";
		}
		else if (tipoServicio == "parqueadero") {
			return "3";
		}
		else if (tipoServicio == "wifi") {
			return "4";
		}
		else if (tipoServicio == "recepcion") {
			return "5";
		}
    	
		else if (tipoServicio == "comida") {
			return "6";
		}
		else if (tipoServicio == "accesoCocina") {
			return "7";
		}
		else if (tipoServicio == "banioPrivado") {
			return "8";
		}
		else if (tipoServicio == "banioCompartido") {
			return "9";
		}
		else if (tipoServicio == "habitacionIndividual") {
			return "10";
		}
		else if (tipoServicio == "habitacionCompartida") {
			return "11";
		}
		else if (tipoServicio == "baniera") {
			return "12";
		}
		else if (tipoServicio == "yacuzzi") {
			return "13";
		}
		else if (tipoServicio == "sala") {
			return "14";
		}
		else if (tipoServicio == "cocineta") {
			return "15";
		}
		else if (tipoServicio == "servicioRestaurante") {
			return "16";
		}
		else if (tipoServicio == "salaEstudio") {
			return "17";
		}
		else if (tipoServicio == "salaEsparcimiento") {
			return "18";
		}
		
		else return "19"; // gimnasio
    }
    
	
	public Alojamiento adicionarAlojamiento(String nUMHABITACIONES, String uBICACION, String pRECIO, String aMOBLADO,
			String cAPACIDAD, String cOMPARTIDO, String iNDICEOCUPACION, String tIPO_ALOJAMIENTO,
			ArrayList<String> iNFO_HORARIO, ArrayList<String> iNFO_SEGURO, String iD_OPERADOR,
			ArrayList<String> iNFO_SERVICIOS, ArrayList<String> iNFO_SERVICIOS_PUBLICOS) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            
            log.info("right after begin");
            
            Long idHorario = null;
            
            if (iNFO_HORARIO.size() > 0) {
            	// crear horario
            	
            	Horario horarioEncontrado = sqlHorario.verificarHorario(pm, iNFO_HORARIO.get(0), iNFO_HORARIO.get(1));
            	
            	if (horarioEncontrado == null) {
            		idHorario = nextval();
                	sqlHorario.adicionarHorario(pm, idHorario, iNFO_HORARIO.get(0), iNFO_HORARIO.get(1));
            	}
            	
            	else {
            		idHorario = horarioEncontrado.getID();
            	}
            }
            
            log.info("SEGURO");
            
            Long idSeguro = null;
            
            if (iNFO_SEGURO.size() > 0) {
            	// crear seguro
            	idSeguro = nextval();
            	sqlSeguro.adicionarSeguro(pm, idSeguro, iNFO_SEGURO.get(0), iNFO_SEGURO.get(1), Long.parseLong(cambiarSiNoPorNumero(iNFO_SEGURO.get(2))), Long.parseLong(iNFO_SEGURO.get(3)), Long.parseLong(iNFO_SEGURO.get(4)));
            }
            
            log.info("ALOJAMIENTO");
            
            // crear alojamiento
            long idAlojamiento = nextval ();
            
            long tuplasInsertadas = sqlAlojamiento.adicionarAlojamiento(pm, idAlojamiento, 
            		Long.parseLong(nUMHABITACIONES), uBICACION,
            		Long.parseLong(pRECIO), Long.parseLong(cambiarSiNoPorNumero(aMOBLADO)),
            		Long.parseLong(cAPACIDAD), Long.parseLong(cambiarSiNoPorNumero(cOMPARTIDO)),
            		Long.parseLong(iNDICEOCUPACION), Long.parseLong(cambiarTipoAlojamientoPorNumero(tIPO_ALOJAMIENTO)),
            		idHorario, idSeguro, Long.parseLong(iD_OPERADOR),1);

            // adicionar servicio
            log.info("SERVICIO");
            
            
            if (!iNFO_SERVICIOS.isEmpty()) {
            	for(int i = 0; i < iNFO_SERVICIOS.size(); i++) {
            		
            		long idServicio = nextval ();
            		long idTipoServicio = Long.parseLong(cambiarTipoServicioPorNumero(iNFO_SERVICIOS.get(i)));
            		sqlServicio.adicionarServicio(pm, idServicio, idTipoServicio, idAlojamiento);
            	}
            }
            log.info("SERV PUBLICO");
            
            // adicionar serviciospublicos
            
            if (!iNFO_SERVICIOS_PUBLICOS.isEmpty()) {
            	for(int i = 0; i < iNFO_SERVICIOS_PUBLICOS.size(); i++) {
            		
            		long idServicioPublico = nextval ();
            		long idTipoServicioPublico = Long.parseLong(cambiarTipoServicioPublicoPorNumero(iNFO_SERVICIOS_PUBLICOS.get(i)));
            		sqlServicioPublico.adicionarServicioPublico(pm, idServicioPublico, 0, idTipoServicioPublico, idAlojamiento);
            	}
            }
            
            
            log.info("right before commit");
            tx.commit();

            log.trace ("Inserción de Alojamiento del OP: " + iD_OPERADOR + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Alojamiento (idAlojamiento, 
            		Long.parseLong(nUMHABITACIONES), uBICACION,
            		Long.parseLong(pRECIO), Long.parseLong(cambiarSiNoPorNumero(aMOBLADO)),
            		Long.parseLong(cAPACIDAD), Long.parseLong(cambiarSiNoPorNumero(cOMPARTIDO)),
            		Long.parseLong(iNDICEOCUPACION), Long.parseLong(cambiarTipoAlojamientoPorNumero(tIPO_ALOJAMIENTO)),
            		idHorario, idSeguro, Long.parseLong(iD_OPERADOR), 1);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	// persistencia 
	/*
	public List<TipoBebida> darTiposBebida ()
	{
		return sqlTipoBebida.darTiposBebida (pmf.getPersistenceManager());
	}
	*/
	public List<Operador> darOperadores()
	{
		return sqlOperador.darOperadores(pmf.getPersistenceManager());
	}
	
	public List<Alojamiento> darAlojamientos()
	{
		return sqlAlojamiento.darAlojamientos(pmf.getPersistenceManager());
	}
	
	
	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Parranderos
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
	 * TIPOBEBIDA, BEBEDOR y BAR, respectivamente
	 */
	public long [] limpiarParranderos ()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long [] resp = sqlUtil2.limpiarParranderos (pm);
            tx.commit ();
            log.info ("Borrada la base de datos");
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new long[] {-1, -1, -1, -1, -1, -1, -1};
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
		
	}
	
	
	/* ****************************************************************
	 * 			CONSULTAS
	 *****************************************************************/


	public List<Object []> darTop20Ofertas ()
	{
		List<Object []> respuesta = new LinkedList <Object []> ();
		
		List<Object> tuplas = sqlReserva.darTop20Ofertas(pmf.getPersistenceManager());
		// List<Object> tuplas = sqlBebedor.darBebedoresYNumVisitasRealizadas (pmf.getPersistenceManager());
        
		for ( Object tupla : tuplas)
        {
			
			
			Object [] datos = (Object []) tupla;
			
			if (datos != null && datos.length > 0 && datos[0] != null) {
				long idAlojamiento = ((BigDecimal) datos[0]).longValue();
				
				List<Alojamiento> aloj = sqlAlojamiento.darAlojamientoPorId(pmf.getPersistenceManager(), idAlojamiento);
				for (Alojamiento a: aloj) {
					int numReservas = ((BigDecimal) datos [1]).intValue ();
					
					Object [] resp = new Object [2];
					
					resp [0] = a;
					resp [1] = numReservas;	
					
				
				respuesta.add(resp);}
				
			}
        }

		return respuesta;
	}
	
	
	public List<Alojamiento> listarOfertaPocaDemanda ()
	{
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try {
        	tx.begin();
        	
        	List<Alojamiento> voTipos = new LinkedList<Alojamiento> ();
        	LocalDate fechaActual = LocalDateTime.now().toLocalDate();
            
            for (Alojamiento alojamiento : sqlAlojamiento.darAlojamientos (pm))
            {
            	List<Reserva> reservasAloj = sqlReserva.darReservasPorAlojamiento(pm, alojamiento.getID());
            	
            	Timestamp ultimaFecha = sqlReserva.darUltimaFechaReservasPorAlojamiento(pm, alojamiento.getID());
            	if (ultimaFecha == null) {
            		voTipos.add (alojamiento);
            	} else {
            		LocalDate uFecha = ultimaFecha.toLocalDateTime().toLocalDate();

	            	long dias = Math.abs(ChronoUnit.DAYS.between(uFecha, fechaActual));
	            	
	            	if (dias>30) {voTipos.add (alojamiento);}
	            	
	            	else {
	            		boolean treinta = false;
	            		
	            		for (int i = 0; i < reservasAloj.size() - 1; i++) {
	            			
	            		    LocalDate fechaFinal1 = reservasAloj.get(i).getFECHAFINAL().toLocalDateTime().toLocalDate();
	            		    LocalDate fechaInicio2 = reservasAloj.get(i+1).getFECHAINICIO().toLocalDateTime().toLocalDate();
	            		    long diasEntreReservas = ChronoUnit.DAYS.between(fechaInicio2,fechaFinal1);
	            		    if (diasEntreReservas <0) {
	            		    	diasEntreReservas = Math.abs(diasEntreReservas);
	            		    	if (diasEntreReservas > 30) {
		            		    	treinta = true;
		            		        voTipos.add (alojamiento);
		            		        break;
		            		    }
	            		    }
	            		}
	
	            	}
	            	
	            }
            }
			tx.commit();
			return voTipos;
        }
        catch (Exception e)
	    {
	    	e.printStackTrace();
	    	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
	        return null;
	    }
	    finally
	    {
	        if (tx.isActive())
	        {
	            tx.rollback();
	        }
	        pm.close();
	    }
        
	}
	
		

	public List<Alojamiento> darAlojamientoPorFechaYServicios(String fECHAINICIO, String fECHAFINAL, ArrayList<String> iNFO_SERVICIOS) {
		
		ArrayList<String> serviciosNumeros = new ArrayList<String>();
		
		for (String servicio : iNFO_SERVICIOS) {
			 serviciosNumeros.add( cambiarTipoServicioPorNumero(servicio) );
		}
		
		List<Object> tuplas = sqlAlojamiento.darAlojamientoPorFechaYServicios(pmf.getPersistenceManager(), fECHAINICIO, fECHAFINAL, serviciosNumeros);
		
		List<Alojamiento> respuesta = new LinkedList <Alojamiento>();
		
		for ( Object tupla : tuplas) {
			
			
			long idAlojamiento = ((BigDecimal) tupla).longValue ();
			
			Alojamiento aloj = sqlAlojamiento.darAlojamientoPorId2(pmf.getPersistenceManager(), idAlojamiento);
			
			respuesta.add(aloj);
        }
		
		return respuesta;
	}
	
	public List<Object []> darUsoUsuario (String iDCLIENTE)
	{
		List<Object []> respuesta = new LinkedList <Object []> ();
		
		List<Object> tuplas = sqlReserva.darUsoUsuario(pmf.getPersistenceManager(),Long.parseLong(iDCLIENTE));
		// List<Object> tuplas = sqlBebedor.darBebedoresYNumVisitasRealizadas (pmf.getPersistenceManager());
        
		for ( Object tupla : tuplas)
        {
			
			
			Object [] datos = (Object []) tupla;
			
			long idAlojamiento = ((BigDecimal) datos [0]).longValue ();
			String ubicacion = (String) datos[1];
			int amoblado = ((BigDecimal) datos [2]).intValue ();
			int precio = ((BigDecimal) datos [3]).intValue ();
			int cantidadPersonas = ((BigDecimal) datos [4]).intValue ();
			Timestamp fechaInicio = (Timestamp) datos[5];
			Timestamp fechaFinal = (Timestamp) datos[6];
			int totaldias = ((BigDecimal) datos [7]).intValue ();
			
			Object [] resp = new Object [8];
			
			resp [0] = idAlojamiento;
			resp [1] = ubicacion;	
			resp [2] = amoblado;	
			resp [3] = precio;	
			resp [4] = cantidadPersonas;	
			resp [5] = fechaInicio;	
			resp [6] = fechaFinal;	
			resp [7] = totaldias;	
			
			respuesta.add(resp);
        }

		return respuesta;
	}
	
	
	public List<Object []> darConsultarFuncionamiento (int aNIO)
	{
		List<Object []> respuesta = new LinkedList <Object []> ();
		
		
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate date = LocalDate.of(aNIO, 1, 1).with(weekFields.dayOfWeek(), 1).with(weekFields.weekOfWeekBasedYear(), 1);
        LocalDate endDate = LocalDate.of(aNIO, 12, 31);
        
        while (date.isBefore(endDate) || date.equals(endDate)) {
        	
        	LocalDate startOfWeek = date;
            LocalDate endOfWeek = date.plusDays(6);
        	//System.out.println(startOfWeek+" "+endOfWeek);
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fECHAINICIO = startOfWeek.format(formatter);
            String fECHAFINAL = endOfWeek.format(formatter);
            
            
			List<Object> tuplas = sqlAlojamiento.darConsultarFuncionamiento(pmf.getPersistenceManager(),fECHAINICIO,fECHAFINAL);
			
			for ( Object tupla : tuplas)
	        {
				
				
				Object [] datos = (Object []) tupla;
				
				long idAlojamiento = ((BigDecimal) datos [0]).longValue ();
				int numhabitaciones = ((BigDecimal) datos [4]).intValue ();
				String ubicacion = (String) datos[2];
				int precio = ((BigDecimal) datos [3]).intValue ();
				int amoblado = ((BigDecimal) datos [4]).intValue ();
				int cantidadPersonas = ((BigDecimal) datos [5]).intValue ();
				int indiceocupacion = ((BigDecimal) datos [7]).intValue ();
				int operador = ((BigDecimal) datos [11]).intValue ();
				
				
				Object [] resp = new Object [11];
				
				resp [0] = idAlojamiento;
				resp [1] = ubicacion;	
				resp [2] = amoblado;	
				resp [3] = precio;	
				resp [4] = cantidadPersonas;	
				resp [5] = numhabitaciones;	
				resp [6] = indiceocupacion;	
				resp [7] = date.get(weekFields.weekOfWeekBasedYear());
				resp [8] = fECHAINICIO;
				resp [9] = fECHAFINAL;
				resp [10] = operador;
				respuesta.add(resp);
	        }
			date = date.plusWeeks(1);
		}

		return respuesta;
	}
	
	
	public List<Object []> darConsultarFuncionamientoMenos (int aNIO)
	{
		List<Object []> respuesta = new LinkedList <Object []> ();
		
		
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate date = LocalDate.of(aNIO, 1, 1).with(weekFields.dayOfWeek(), 1).with(weekFields.weekOfWeekBasedYear(), 1);
        LocalDate endDate = LocalDate.of(aNIO, 12, 31);
        
        while (date.isBefore(endDate) || date.equals(endDate)) {
        	
        	LocalDate startOfWeek = date;
            LocalDate endOfWeek = date.plusDays(6);
        	//System.out.println(startOfWeek+" "+endOfWeek);
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fECHAINICIO = startOfWeek.format(formatter);
            String fECHAFINAL = endOfWeek.format(formatter);
            
            
			List<Object> tuplas = sqlAlojamiento.darConsultarFuncionamientoMenos(pmf.getPersistenceManager(),fECHAINICIO,fECHAFINAL);
			
			for ( Object tupla : tuplas)
	        {
				
				
				Object [] datos = (Object []) tupla;
				
				long idAlojamiento = ((BigDecimal) datos [0]).longValue ();
				int numhabitaciones = ((BigDecimal) datos [4]).intValue ();
				String ubicacion = (String) datos[2];
				int precio = ((BigDecimal) datos [3]).intValue ();
				int amoblado = ((BigDecimal) datos [4]).intValue ();
				int cantidadPersonas = ((BigDecimal) datos [5]).intValue ();
				int indiceocupacion = ((BigDecimal) datos [7]).intValue ();
				int operador = ((BigDecimal) datos [11]).intValue ();
				
				
				Object [] resp = new Object [11];
				
				resp [0] = idAlojamiento;
				resp [1] = ubicacion;	
				resp [2] = amoblado;	
				resp [3] = precio;	
				resp [4] = cantidadPersonas;	
				resp [5] = numhabitaciones;	
				resp [6] = indiceocupacion;	
				resp [7] = date.get(weekFields.weekOfWeekBasedYear());
				resp [8] = fECHAINICIO;
				resp [9] = fECHAFINAL;
				resp [10] = operador;
				
				respuesta.add(resp);
	        }
			date = date.plusWeeks(1);
		}

		return respuesta;
	}
	
	public List<Object []> darOperadorMasSolicitado (int aNIO)
	{
		List<Object []> respuesta = new LinkedList <Object []> ();
		
		
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate date = LocalDate.of(aNIO, 1, 1).with(weekFields.dayOfWeek(), 1).with(weekFields.weekOfWeekBasedYear(), 1);
        LocalDate endDate = LocalDate.of(aNIO, 12, 31);
        
        while (date.isBefore(endDate) || date.equals(endDate)) {
        	
        	LocalDate startOfWeek = date;
            LocalDate endOfWeek = date.plusDays(6);
        	//System.out.println(startOfWeek+" "+endOfWeek);
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fECHAINICIO = startOfWeek.format(formatter);
            String fECHAFINAL = endOfWeek.format(formatter);
            
            
			List<Object> tuplas = sqlOperador.darOperadorMasSolicitado(pmf.getPersistenceManager(),fECHAINICIO,fECHAFINAL);
			
			for ( Object tupla : tuplas)
	        {
				
				
				Object [] datos = (Object []) tupla;
				
				long idOperador = ((BigDecimal) datos [0]).longValue ();
				int ingresoActual = datos[1] != null ? ((BigDecimal) datos[1]).intValue() : 0;
				int ingresoCorrido = datos[2] != null ? ((BigDecimal) datos[2]).intValue() : 0;
				String nombre = (String) datos[3];
				String correo = (String) datos[5];
				
				Object [] resp = new Object [8];
				
				resp [0] = idOperador;
				resp [1] = ingresoActual;	
				resp [2] = ingresoCorrido;	
				resp [3] = nombre;	
				resp [4] = correo;	
				resp [5] = date.get(weekFields.weekOfWeekBasedYear());
				resp [6] = fECHAINICIO;
				resp [7] = fECHAFINAL;

				respuesta.add(resp);
	        }
			date = date.plusWeeks(1);
		}

		return respuesta;
	}
	
	
	public List<Object []> darOperadorMenosSolicitado (int aNIO)
	{
		List<Object []> respuesta = new LinkedList <Object []> ();
		
		
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate date = LocalDate.of(aNIO, 1, 1).with(weekFields.dayOfWeek(), 1).with(weekFields.weekOfWeekBasedYear(), 1);
        LocalDate endDate = LocalDate.of(aNIO, 12, 31);
        
        while (date.isBefore(endDate) || date.equals(endDate)) {
        	
        	LocalDate startOfWeek = date;
            LocalDate endOfWeek = date.plusDays(6);
        	//System.out.println(startOfWeek+" "+endOfWeek);
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fECHAINICIO = startOfWeek.format(formatter);
            String fECHAFINAL = endOfWeek.format(formatter);
            
            
			List<Object> tuplas = sqlOperador.darOperadorMenosSolicitado(pmf.getPersistenceManager(),fECHAINICIO,fECHAFINAL);
			
			for ( Object tupla : tuplas)
	        {
				
				Object [] datos = (Object []) tupla;
				
				long idOperador = ((BigDecimal) datos [0]).longValue ();
				int ingresoActual = datos[1] != null ? ((BigDecimal) datos[1]).intValue() : 0;
				int ingresoCorrido = datos[2] != null ? ((BigDecimal) datos[2]).intValue() : 0;
				String nombre = (String) datos[3];
				String correo = (String) datos[5];
				
				Object [] resp = new Object [8];
				
				resp [0] = idOperador;
				resp [1] = ingresoActual;	
				resp [2] = ingresoCorrido;	
				resp [3] = nombre;	
				resp [4] = correo;	
				resp [5] = date.get(weekFields.weekOfWeekBasedYear());
				resp [6] = fECHAINICIO;
				resp [7] = fECHAFINAL;

				
				respuesta.add(resp);
	        }
			date = date.plusWeeks(1);
		}

		return respuesta;
	}
	
	
	
	public List<Object []> darConsumoUsuario (String iDCLIENTE,String fECHAINICIO,String fECHAFINAL,String cRITERIO)
	{
		List<Object []> respuesta = new LinkedList <Object []> ();
		
		List<Object> tuplas = sqlReserva.darConsumoUsuario(pmf.getPersistenceManager(),Long.parseLong(iDCLIENTE),fECHAINICIO, fECHAFINAL,cRITERIO);
		// List<Object> tuplas = sqlBebedor.darBebedoresYNumVisitasRealizadas (pmf.getPersistenceManager());
        
		for ( Object tupla : tuplas)
        {
			
			
			Object [] datos = (Object []) tupla;
			
			long idAlojamiento = ((BigDecimal) datos [0]).longValue ();
			String ubicacion = (String) datos[1];
			int amoblado = ((BigDecimal) datos [2]).intValue ();
			int precio = ((BigDecimal) datos [3]).intValue ();
			int cantidadPersonas = ((BigDecimal) datos [4]).intValue ();
			Timestamp fechaInicio = (Timestamp) datos[5];
			Timestamp fechaFinal = (Timestamp) datos[6];
			int totaldias = ((BigDecimal) datos [7]).intValue ();
			
			Object [] resp = new Object [8];
			
			resp [0] = idAlojamiento;
			resp [1] = ubicacion;	
			resp [2] = amoblado;	
			resp [3] = precio;	
			resp [4] = cantidadPersonas;	
			resp [5] = fechaInicio;	
			resp [6] = fechaFinal;	
			resp [7] = totaldias;	
			
			respuesta.add(resp);
        }

		return respuesta;
	}

	public List<Object []> darConsumoUsuarios (String fECHAINICIO,String fECHAFINAL,String cRITERIO, String aLOJAMIENTO)
	{
		List<Object []> respuesta = new LinkedList <Object []> ();
		
		List<Object> tuplas = sqlReserva.darConsumoUsuarios(pmf.getPersistenceManager(),fECHAINICIO, fECHAFINAL, cRITERIO, Long.valueOf(aLOJAMIENTO));
		// List<Object> tuplas = sqlBebedor.darBebedoresYNumVisitasRealizadas (pmf.getPersistenceManager());
        
		for ( Object tupla : tuplas)
        {
			
			Object [] datos = (Object []) tupla;
			
			long idCliente = ((BigDecimal) datos [0]).longValue ();
			int numReservas = ((BigDecimal) datos [1]).intValue ();
			String nombre = (String) datos[2];
			int vinculacion = ((BigDecimal) datos [3]).intValue ();
			String correo = (String) datos[4];
			
			Object [] resp = new Object [5];
			
			resp [0] = idCliente;
			resp [1] = numReservas;	
			resp [2] = nombre;	
			resp [3] = vinculacion;	
			resp [4] = correo;	

			respuesta.add(resp);
        }

		return respuesta;
	}
	
	public List<Object []> darConsumoUsuarios2 (String fECHAINICIO,String fECHAFINAL,String cRITERIO, String aLOJAMIENTO)
	{
		List<Object []> respuesta = new LinkedList <Object []> ();
		
		List<Object> tuplas = sqlReserva.darConsumoUsuarios2(pmf.getPersistenceManager(),fECHAINICIO, fECHAFINAL, cRITERIO, Long.valueOf(aLOJAMIENTO));
		// List<Object> tuplas = sqlBebedor.darBebedoresYNumVisitasRealizadas (pmf.getPersistenceManager());
        
		for ( Object tupla : tuplas)
        {
			
			Object [] datos = (Object []) tupla;
			
			long idCliente = ((BigDecimal) datos [0]).longValue ();
			int numReservas = ((BigDecimal) datos [1]).intValue ();
			String nombre = (String) datos[2];
			int vinculacion = ((BigDecimal) datos [3]).intValue ();
			String correo = (String) datos[4];
			
			Object [] resp = new Object [5];
			
			resp [0] = idCliente;
			resp [1] = numReservas;	
			resp [2] = nombre;	
			resp [3] = vinculacion;	
			resp [4] = correo;	

			respuesta.add(resp);
        }

		return respuesta;
	}

	public long eliminarAlojamientoPorId (long idAlojamiento) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            
            
            sqlServicioPublico.eliminarServicioPublicoPorAlojamientoSimple(pm, idAlojamiento);
            sqlServicio.eliminarServicioPorAlojamientoSimple(pm, idAlojamiento);
            
            long resp = sqlAlojamiento.eliminarAlojamientoPorIdSimple(pm, idAlojamiento);
            
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	public long eliminarReservaPorId (long idReserva, boolean penalizacion) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();

            LocalDateTime fechaActual = LocalDateTime.now();
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechaActualStr = fechaActual.format(formatter);
            log.info("fechaActualStr: "+fechaActualStr);
            
            // CRITICO
            long resp = sqlReserva.cancelarReservaPorId(pm, idReserva, fechaActualStr);
            log.info("RESSSSSSPPPPPPP: "+ resp);

            long idAlojamiento = ((BigDecimal) sqlReserva.darAlojamiento(pm, idReserva)).longValue();
            log.info("idAlojamiento: "+idAlojamiento);

            
            if (penalizacion) {

                LocalDate fechaCancelacion = fechaActual.toLocalDate();
                LocalDate fechaInicio = sqlReserva.darFechaInicio(pm, idReserva).toLocalDateTime().toLocalDate();
                LocalDate fechaFinal = sqlReserva.darFechaFinal(pm, idReserva).toLocalDateTime().toLocalDate();
                log.info("fechaCancelacion: "+fechaCancelacion + " fechaInicio: " + fechaInicio + " fechaFinal: " + fechaFinal);
                if (fechaCancelacion.isAfter(fechaFinal)) {
                	return -2;
                }
            	
                String tipoPago = (String) sqlReserva.darTipoPago(pm, idAlojamiento);
                log.info("tipoPago: "+tipoPago);
                double porcentaje = calcularPorcentajeCancelacion(fechaInicio, fechaCancelacion, tipoPago);
                log.info("porcentaje: "+porcentaje);
                sqlReserva.actualizarPrecio(pm, porcentaje, idReserva);
            }
            
            else {
            	sqlReserva.actualizarPrecio(pm, 0, idReserva);
            }

            
            long idOperador = ((BigDecimal) sqlAlojamiento.darOperador(pm, idAlojamiento)).longValue();
            log.info("idOperador: "+idOperador);
            actualizarIngresosOperador(idOperador);
            
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	
	
	public long eliminarReservaColectivaPorId (long idReservaColectiva) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();

            LocalDateTime fechaActual = LocalDateTime.now();
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechaActualStr = fechaActual.format(formatter);
            log.info("fechaActualStr: "+fechaActualStr);
            
            long resp = sqlReserva.cancelarReservasPorIdReservaColectiva(pm, idReservaColectiva, fechaActualStr);
        	List<Reserva> listaSubReservas = sqlReserva.darReservasPorReservaColectiva(pmf.getPersistenceManager(), idReservaColectiva);
        	
            //log.info("RESSSSSSPPPPPPP: "+ resp);
           
            
            for (Reserva subReserva: listaSubReservas) {
            	long idReserva = subReserva.getID();

            	
	            long idAlojamiento = ((BigDecimal) sqlReserva.darAlojamiento(pm, idReserva)).longValue();
	            log.info("idAlojamiento: "+idAlojamiento);
	            
	            LocalDate fechaCancelacion = fechaActual.toLocalDate();
	            LocalDate fechaInicio = sqlReserva.darFechaInicio(pm, idReserva).toLocalDateTime().toLocalDate();
	            LocalDate fechaFinal = sqlReserva.darFechaFinal(pm, idReserva).toLocalDateTime().toLocalDate();
	            log.info("fechaCancelacion: "+fechaCancelacion + " fechaInicio: " + fechaInicio + " fechaFinal: " + fechaFinal);
	            if (fechaCancelacion.isAfter(fechaFinal)) {
	            	return -2;
	            }
	            String tipoPago = (String) sqlReserva.darTipoPago(pm, idAlojamiento);
	            log.info("tipoPago: "+tipoPago);
	            double porcentaje = calcularPorcentajeCancelacion(fechaInicio, fechaCancelacion, tipoPago);
	            log.info("porcentaje: "+porcentaje);
	            sqlReserva.actualizarPrecio(pm, porcentaje, idReserva);
	            
	            long idOperador = ((BigDecimal) sqlAlojamiento.darOperador(pm, idAlojamiento)).longValue();
	            log.info("idOperador: "+idOperador);
	            actualizarIngresosOperador(idOperador);
        	}
	        
	        
            tx.commit();
            
            actualizarPrecioReservaColectiva(idReservaColectiva);
        	
            return resp;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	

	
	

	private double calcularPorcentajeCancelacion(LocalDate fechaInicio, LocalDate fechaCancelacion, String tipoPago) throws Exception {
		
		double pagoTotal = 1; 
		
		Period periodo = Period.between(fechaCancelacion, fechaInicio);
		
		
		int diasLimite = 3;
		if (tipoPago.equals("mes")) diasLimite = 7;
		
		
		if (periodo.isNegative() || periodo.isZero()) { // fechaCancelacion despues de fechaInicio
			pagoTotal = (double) 0.5;
		}
		
		else if (periodo.getYears() >= 1 || periodo.getMonths() >= 1 || periodo.getDays() >= diasLimite) {
			pagoTotal = (double) 0.1;
		}
		
		else pagoTotal = (double) 0.3;

		return pagoTotal;
	}

	public List<Alojamiento> darAlojamientoPorFechaServiciosYTipoAlojamiento(String fECHAINICIO, String fECHAFINAL,
		
		ArrayList<String> iNFO_SERVICIOS, String tipoA) {
		
		ArrayList<String> serviciosNumeros = new ArrayList<String>();
		
		for (String servicio : iNFO_SERVICIOS) {
			 serviciosNumeros.add( cambiarTipoServicioPorNumero(servicio) );
		}
		
		List<Object> tuplas = sqlAlojamiento.darAlojamientoPorFechaServiciosYTipoAlojamiento(pmf.getPersistenceManager(), fECHAINICIO, fECHAFINAL, serviciosNumeros,Long.parseLong(cambiarTipoAlojamientoPorNumero(tipoA)));
		
		List<Alojamiento> respuesta = new LinkedList <Alojamiento>();
		
		for ( Object tupla : tuplas) {
			
			
			long idAlojamiento = ((BigDecimal) tupla).longValue ();
			
			Alojamiento aloj = sqlAlojamiento.darAlojamientoPorId2(pmf.getPersistenceManager(), idAlojamiento);
			
			respuesta.add(aloj);
        }
		
		return respuesta;
	}

	/**
	 * @param tipoAlojamientoStr
	 * @return
	 */
	public String analizarMayoresIngresos(String tipoAlojamientoStr) {

		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            
    		Long tipoAlojamiento = Long.parseLong(cambiarTipoAlojamientoPorNumero(tipoAlojamientoStr));
    		log.info("TIPO_ALOJAMIENTO:"+tipoAlojamiento);
    		List<Reserva> reservasIndividualesYColectivas = sqlReserva.darReservasIndividualesYColectivasPorTipoAlojamiento(pm, tipoAlojamiento);
            
    		log.info(reservasIndividualesYColectivas);
    		log.info(reservasIndividualesYColectivas.size());
    		
    		HashMap<String, Long> fechasIngresos = new HashMap<String, Long>();
    		
            log.info("FECHAS INGRESOS ANTES DEL LOOP");
    		
            // carga del mapa 
    		for (Reserva reserva : reservasIndividualesYColectivas) {
    			log.info("dentro de Reserva: "+reserva.toString());
    			LocalDate fechaReserva = reserva.getFECHAINICIO().toLocalDateTime().toLocalDate();
    			long precioReserva = reserva.getPRECIO();
    			
    			int fechaMes = fechaReserva.getMonthValue();
    			int fechaAnio = fechaReserva.getYear();
    			String fechaStr = String.valueOf(fechaMes) + "-" + String.valueOf(fechaAnio);
    					
    					
    			if (fechasIngresos.containsKey(fechaStr)) {
    				long precioActual = fechasIngresos.get(fechaStr);
    				fechasIngresos.put(fechaStr, precioActual + precioReserva);
    			}
    			else {
    				fechasIngresos.put(fechaStr, precioReserva);
    			}
    		}
    		
    		log.info("DESPUES DE LOOP");
    		
    		// encontrar el mayor
            String fechaMayorIngreso = null;
            Long mayorIngreso = null;

            for (Map.Entry<String, Long> entry : fechasIngresos.entrySet()) {
                if (mayorIngreso == null || entry.getValue() > mayorIngreso) {
                    fechaMayorIngreso = entry.getKey();
                    mayorIngreso = entry.getValue();
                }
            }
    		
	        tx.commit();
	    	
	        return fechaMayorIngreso;
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
	        return null;
	    }
	    finally
	    {
	        if (tx.isActive())
	        {
	            tx.rollback();
	        }
	        pm.close();
	    }
	}	
	

	public String[] analizarOcupaciones(String tipoAlojamientoStr) {

		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            
    		Long tipoAlojamiento = Long.parseLong(cambiarTipoAlojamientoPorNumero(tipoAlojamientoStr));
    		List<Reserva> reservasIndividualesYSubreservas = sqlReserva.darReservasIndividualesYSubreservasPorTipoAlojamiento(pm, tipoAlojamiento);
    		System.out.println(reservasIndividualesYSubreservas.size());
    		HashMap<String, HashSet<Integer>> fechasAlojamientos = new HashMap<String, HashSet<Integer>>();
    		
            // carga del mapa 
    		for (Reserva reserva : reservasIndividualesYSubreservas) {
    			
    			Timestamp fechaCancelacion = reserva.getFECHACANCELACION();
    			Timestamp fechaInicio = reserva.getFECHAINICIO();
    			Timestamp fechaFinal = reserva.getFECHAFINAL();
    			
    			if (fechaCancelacion != null && fechaCancelacion.before(fechaInicio)) {
    				break;
    			}

    			int idAlojamiento = ((Long) reserva.getALOJAMIENTO()).intValue();
    			
    			// encuentro todos los meses entre el inicio y el fin
    			
    	        LocalDate inicio = fechaInicio.toLocalDateTime().toLocalDate();
    	        LocalDate fin = fechaFinal.toLocalDateTime().toLocalDate();
    	        LocalDate actual = inicio;

    	        while (!actual.isAfter(fin)) {
    	        	String fechaStr = actual.getMonthValue() + "-" + actual.getYear();
    	        	
        			if (fechasAlojamientos.containsKey(fechaStr)) {
        				HashSet<Integer> alojamientos = fechasAlojamientos.get(fechaStr);
        				alojamientos.add(idAlojamiento);
        			}
        			else {
        			    HashSet<Integer> nuevoAlojamientoSet = new HashSet<>();
        			    nuevoAlojamientoSet.add(idAlojamiento);
        			    fechasAlojamientos.put(fechaStr, nuevoAlojamientoSet);
        			}
    	        	
    	            actual = actual.plus(1, ChronoUnit.MONTHS);
    	        }
    	        
    		}
    		
	        
	        // encontrar maximo y minimo
	        
	        String fechaMasAlojamientos = null;
	        int maxAlojamientos = 0;
	        
	        String fechaMenosAlojamientos = null;
	        int minAlojamientos = Integer.MAX_VALUE;

	        for (Map.Entry<String, HashSet<Integer>> entry : fechasAlojamientos.entrySet()) {
	            int alojamientosSize = entry.getValue().size();
	            
	            if (alojamientosSize > maxAlojamientos) {
	                fechaMasAlojamientos = entry.getKey();
	                maxAlojamientos = alojamientosSize;
	            }
	            
	            if (alojamientosSize < minAlojamientos) {
	                fechaMenosAlojamientos = entry.getKey();
	                minAlojamientos = alojamientosSize;
	            }
	        }
	        
	        String[] fechas = {fechaMasAlojamientos, fechaMenosAlojamientos};

	        tx.commit();
	    	
	        return fechas;
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
	        return null;
	    }
	    finally
	    {
	        if (tx.isActive())
	        {
	            tx.rollback();
	        }
	        pm.close();
	    }
	}

	public String[] deshabilitarAlojamientoPorId(long idAloj) 
	{
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            
            sqlAlojamiento.deshabilitarAlojamientoPorId(pm, idAloj);
            
            Alojamiento aloj = sqlAlojamiento.darAlojamientoPorId2(pm, idAloj);
            log.info(aloj.toString());
            
            
            LocalDateTime fechaActual = LocalDateTime.now();
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechaActualStr = fechaActual.format(formatter);
            log.info("fechaActualStr: ["+fechaActualStr+"]");
            log.info("idAloj"+idAloj);
            
            // desagregacion
            sqlReserva.cancelarReservasColectivasPorAlojamiento(pm, idAloj, fechaActualStr);
            sqlReserva.desagregarSubreservasPorAlojamiento(pm, idAloj, fechaActualStr);
            
            List<Reserva> reservasVigentes = sqlReserva.darReservasVigentesPorAlojamiento(pm, idAloj, fechaActualStr);
            List<Reserva> reservasProximas = sqlReserva.darReservasProximasPorAlojamiento(pm, idAloj, fechaActualStr);
            log.info("size reservasVigentes: "+reservasVigentes.size());
            log.info("size reservasProximas: "+reservasProximas.size());
            
            // reubicar con prioridad
            List<Reserva> reservasReubicar = new ArrayList<Reserva>(reservasVigentes);
            reservasReubicar.addAll(reservasProximas);
            
            
            String[] mensajeLog = new String[reservasReubicar.size()];
            log.info("size reservasReubicar: "+reservasReubicar.size());
            
            long tipoAloj = ((BigDecimal)sqlAlojamiento.darTipoAlojamientoPorId(pm, idAloj)).longValue();
            List<Alojamiento> alojamientosCandidatos = sqlAlojamiento.darAlojamientosActivosPorTipoAlojamiento(pm, tipoAloj);
            
            log.info("antes de loop reservasReubicar");
            int i = 0;
            for (Reserva reserva : reservasReubicar) {
            	log.info("Inside loop i="+i);
            	boolean encontro = false;
            	for (Alojamiento alojamiento : alojamientosCandidatos) {
            		
            		// RF4
            		Reserva reservaNueva = adicionarReserva1(cambiarTimestampAString(reserva.getFECHAINICIO()), 
            				cambiarTimestampAString(reserva.getFECHAFINAL()), String.valueOf(alojamiento.getID()),
            				String.valueOf(reserva.getCLIENTE()), new ArrayList<String>(), String.valueOf(reserva.getCANTIDADPERSONAS()), null);
            		
            		if (reservaNueva != null) {
            			mensajeLog[i] = "La reserva " + reserva.toString() + 
            					" se reubico y ahora es "+ reservaNueva.toString();
            			log.info(mensajeLog[i]);
            			encontro = true;
            			break;
            		}
            	}
            	
            	if (!encontro) {
            		mensajeLog[i] = "La reserva " + reserva.toString() + " no se pudo reubicar";
            		log.info(mensajeLog[i]);
            	}
            	
            	

            	
            	i++;
            }
            

            tx.commit();
            
            for (Reserva reserva : reservasReubicar) {
            	// RF5
            	eliminarReservaPorId(reserva.getID(), false);
            }
            
            return mensajeLog;
        }	
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            // manejar el error (agregar al string)
        	// return?
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
            
        }
        return new String[1];
	
	}
	

	public long rehabilitarAlojamientoPorId(long idAloj) 
	{
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            
            sqlAlojamiento.rehabilitarAlojamientoPorId(pm, idAloj);
                       
            long activo = ((BigDecimal)sqlAlojamiento.darActivoPorId(pm, idAloj)).longValue();

            tx.commit();

            return activo;
        }	
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            // manejar el error (agregar al string)
        	// return?
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
            
        }
        return 0;
	
	}
	
	
	public String cambiarTimestampAString(Timestamp fecha) {
		
        LocalDateTime fechaActual = fecha.toLocalDateTime();
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fechaActual.format(formatter);
	}
	
	public Reserva adicionarReserva1(String fECHAINICIO, String fECHAFINAL, String aLOJAMIENTO,
			String cLIENTE, ArrayList<String> iNFO_CONTRATO, String cANTIDADPERSONAS, String reservaMadre) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            
            log.info(tx.getIsolationLevel());
            //tx.setIsolationLevel("READ UNCOMMITTED");
            
            long idReserva = nextval ();
            
            long activo = ((BigDecimal)sqlAlojamiento.darActivoPorId(pm, Long.parseLong(aLOJAMIENTO))).longValue();
            log.info("activo: "+activo);
            
            if (activo == 0) {
            	return null;
            }

            if (!verificarCupoAlojamiento(aLOJAMIENTO, fECHAINICIO, fECHAFINAL, cANTIDADPERSONAS)) return null;
            
            
            Long idContrato = null;
            if (!iNFO_CONTRATO.isEmpty()) {
            	// crear contrato
            	idContrato= nextval();
            	sqlContrato.adicionarContrato(pm, idContrato, Long.parseLong(cambiarSiNoPorNumero(iNFO_CONTRATO.get(0))), Long.parseLong(cambiarSiNoPorNumero(iNFO_CONTRATO.get(1))), Long.parseLong(cambiarSiNoPorNumero(iNFO_CONTRATO.get(2))), Long.parseLong(cambiarSiNoPorNumero(iNFO_CONTRATO.get(3))) );
            }
            
            long tuplasInsertadas;
            long precio = 0;
            
            // caso de reserva original o subreserva
            if (aLOJAMIENTO != null) {
            	
                Object[] pRECIO = sqlReserva.consultarPrecioFinal(pm, Long.parseLong(aLOJAMIENTO));
                

    			precio = ((BigDecimal) pRECIO [0]).longValue ();
    			String pagofecha = ((String) pRECIO [1]);
    			
    			precio = obtenerPrecio(fECHAINICIO,fECHAFINAL,pagofecha,precio);
    			
    			// reserva original 
    			if (reservaMadre == null) {
                    tuplasInsertadas = sqlReserva.adicionarReserva(pm, idReserva,
                    		fECHAINICIO, fECHAFINAL, 
                    		Long.parseLong(aLOJAMIENTO), Long.parseLong(cLIENTE), 
                    		idContrato, precio, Long.parseLong(cANTIDADPERSONAS), null);
    			}
    			// subreserva
    			else {
                    tuplasInsertadas = sqlReserva.adicionarReserva(pm, idReserva,
                    		fECHAINICIO, fECHAFINAL, 
                    		Long.parseLong(aLOJAMIENTO), Long.parseLong(cLIENTE), 
                    		idContrato, precio, Long.parseLong(cANTIDADPERSONAS), 
                    		Long.parseLong(reservaMadre));
    			}
                
                long idOperador = ((BigDecimal) sqlAlojamiento.darOperador(pm, Long.parseLong(aLOJAMIENTO))).longValue();
                
                
                log.info("idOperador: " + idOperador);
                
              //after.date(1 year ago) 2022/04/03
                actualizarIngresosOperador(idOperador);
            }
            
            // caso reserva colectica
            else {
                tuplasInsertadas = sqlReserva.adicionarReserva(pm, idReserva,
                		fECHAINICIO, fECHAFINAL, null, Long.parseLong(cLIENTE), 
                		idContrato, precio, Long.parseLong(cANTIDADPERSONAS), idReserva);
            }


            

            log.trace ("Inserción de Reserva para el cliente con ID: " + cLIENTE + ": " + tuplasInsertadas + " tuplas insertadas");
            
            Reserva reserva;
            
            if (aLOJAMIENTO != null ) {
            	
    			// reserva original 
    			if (reservaMadre == null) {
	                reserva = new Reserva (idReserva, null, fECHAINICIO, fECHAFINAL, 
	                		Long.parseLong(aLOJAMIENTO),Long.parseLong(cLIENTE), 
	                		idContrato, precio,Long.parseLong(cANTIDADPERSONAS),null);
    			}
    			else {
	                reserva = new Reserva (idReserva, null, fECHAINICIO, fECHAFINAL, 
	                		Long.parseLong(aLOJAMIENTO),Long.parseLong(cLIENTE), 
	                		idContrato, precio,Long.parseLong(cANTIDADPERSONAS),Long.parseLong(reservaMadre));
    			}
            }
            
            else {	
        	   reserva = new Reserva (idReserva, null, fECHAINICIO, fECHAFINAL, 
               		null,Long.parseLong(cLIENTE), 
               		idContrato, precio,Long.parseLong(cANTIDADPERSONAS),idReserva);
           }
            
            tx.commit();
            
            return reserva;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


 }
