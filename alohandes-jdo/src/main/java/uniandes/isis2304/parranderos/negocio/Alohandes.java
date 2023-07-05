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

package uniandes.isis2304.parranderos.negocio;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import uniandes.isis2304.parranderos.persistencia.PersistenciaAlohandes;

/**
 * Clase principal del negocio
 * Sarisface todos los requerimientos funcionales del negocio
 *
 * @author Germán Bravo
 */
public class Alohandes 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(Alohandes.class.getName());
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaAlohandes pp;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public Alohandes ()
	{
		pp = PersistenciaAlohandes.getInstance ();
	}
	
	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public Alohandes (JsonObject tableConfig)
	{
		pp = PersistenciaAlohandes.getInstance (tableConfig);
	}
	
	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia ()
	{
		pp.cerrarUnidadPersistencia ();
	}
	
	
	
	public boolean verificarOperador(String iD_OPERADOR, String cLAVE_OPERADOR) {
        log.info ("Verificando Operador: " + iD_OPERADOR);
        boolean verificacion = pp.verificarOperador(iD_OPERADOR, cLAVE_OPERADOR);		
        log.info ("Fin verificacion Operador: " + iD_OPERADOR);
        return verificacion;
	}
	
	public boolean verificarCliente(String iD_CLIENTE, String cLAVE_CLIENTE) {
        log.info ("Verificando Cliente: " + iD_CLIENTE);
        boolean verificacion = pp.verificarCliente(iD_CLIENTE, cLAVE_CLIENTE);		
        log.info ("Fin verificacion Cliente: " + iD_CLIENTE);
        return verificacion;
	}
	
	public boolean verificarAlojamiento(String iD) {
        log.info ("Verificando Alojamiento: " + iD);
        boolean verificacion = pp.verificarAlojamiento(iD);		
        log.info ("Fin verificacion Alojamiento: " + iD);
        return verificacion;
	}
	
	public boolean verificarCupoAlojamiento(String iD, String FECHAINICIO, String FECHAFINAL, String CANTIDADPERSONAS) {
        log.info ("Verificando cupo Alojamiento: " + iD);
        boolean verificacion = pp.verificarCupoAlojamiento(iD, FECHAINICIO, FECHAFINAL, CANTIDADPERSONAS);		
        log.info ("Fin verificacion cupo Alojamiento: " + iD);
        return verificacion;
	}
	

	public boolean verificarAlojamientoOperador(String iD_OPERADOR, long idAloj) {
        log.info ("Verificando Alojamiento: " + idAloj + " de Operador " + iD_OPERADOR);
        boolean verificacion = pp.verificarAlojamientoOperador(iD_OPERADOR, idAloj);		
        log.info ("Fin verificacion Alojamiento: " + idAloj);
        return verificacion;
	}
	
	public boolean verificarReservaCliente(String iD_CLIENTE, long idReserva) {
        log.info ("Verificando Reserva: " + idReserva + " del CLIENTE " + iD_CLIENTE);
        boolean verificacion = pp.verificarReservaCliente(iD_CLIENTE, idReserva);		
        log.info ("Fin verificacion Reserva: " + idReserva);
        return verificacion;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los TIPOS DE Alojamiento
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un tipo de Alojamiento 
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del tipo de Alojamiento
	 * @return El objeto TipoAlojamiento adicionado. null si ocurre alguna Excepción
	 */
	public TipoAlojamiento adicionarTipoAlojamiento (String nombre)
	{
        log.info ("Adicionando Tipo de Alojamiento: " + nombre);
        TipoAlojamiento tipoAlojamiento = pp.adicionarTipoAlojamiento (nombre);		
        log.info ("Adicionando Tipo de Alojamiento: " + tipoAlojamiento);
        return tipoAlojamiento;
	}
	
	public Cliente adicionarCliente(long cEDULA, String cORREO, String nOMBRE, long cELULAR, String vINCULACION,
			String cLAVE) {
		log.info ("Adicionando cliente: " +  nOMBRE +  "-- correo electronico:"+ cORREO + "-- rol:CLIENTE");
        Cliente cliente = pp.adicionarCliente (cEDULA, cORREO,nOMBRE, cELULAR, vINCULACION,
    			cLAVE);       
        log.info ("Adicionando cliente: " + cliente);
        return cliente;
	}
	
	public Operador adicionarOperador(String ID, String iNGRESOPORANIOACTUAL, String iNGRESOPORANIOCORRIDO, String nOMBRE,
			String cLAVE,String cORREO) {
		log.info ("Adicionando OPERADOR: " + nOMBRE);
		Operador operador = pp.adicionarOperador(ID, iNGRESOPORANIOACTUAL,iNGRESOPORANIOCORRIDO, nOMBRE,
			cLAVE,cORREO);
		log.info ("Adicionando operador: " + operador);
		return operador;
	}
	
	public Operador adicionarOperadorEmpresa(String ID, String iNGRESOPORANIOACTUAL, String iNGRESOPORANIOCORRIDO, String nOMBRE,
			String cLAVE,String cORREO, String uBICACION, String rECEPCION, String rEGISTROTURISMO, String rEGISTROCAMARA, String nUMEROCONTACTO, String tIPOEMPRESA) {
		log.info ("Adicionando OPERADOR: " + nOMBRE);
		Operador operador = pp.adicionarOperadorEmpresa(ID, iNGRESOPORANIOACTUAL,iNGRESOPORANIOCORRIDO, nOMBRE, cLAVE,cORREO,
				uBICACION, rECEPCION, rEGISTROTURISMO, rEGISTROCAMARA, nUMEROCONTACTO, tIPOEMPRESA);
		log.info ("Adicionando operador: " + operador);
		return operador;
	}
	
	public Operador adicionarOperadorNatural(String ID, String iNGRESOPORANIOACTUAL, String iNGRESOPORANIOCORRIDO, String nOMBRE,
			String cLAVE, String cORREO, String cELULAR, String tIPOPERSONANATURAL) {
		log.info ("Adicionando OPERADOR: " + nOMBRE);
		Operador operador = pp.adicionarOperadorNatural(ID, iNGRESOPORANIOACTUAL,iNGRESOPORANIOCORRIDO, nOMBRE, cLAVE,
				cORREO, cELULAR, tIPOPERSONANATURAL);
		log.info ("Adicionando operador: " + operador);
		return operador;
	}
	
	public Reserva adicionarReserva(String fECHAINICIO, String fECHAFINAL,
			String aLOJAMIENTO, String cLIENTE, ArrayList<String> iNFO_CONTRATO, 
			String cANTIDADPERSONAS, String reservaMadre) {
		log.info ("Adicionando RESERVA para el cliente con ID: " + cLIENTE);
		Reserva reserva = pp.adicionarReserva(fECHAINICIO, fECHAFINAL, aLOJAMIENTO, cLIENTE, iNFO_CONTRATO,cANTIDADPERSONAS, reservaMadre);
		log.info ("Adicionando operador: " + reserva);
		return reserva;
	}

	
	public VOAlojamiento adicionarAlojamiento(String nUMHABITACIONES, String uBICACION, String pRECIO, String aMOBLADO,
			String cAPACIDAD, String cOMPARTIDO, String iNDICEOCUPACION, String tIPO_ALOJAMIENTO,
			ArrayList<String> iNFO_HORARIO, ArrayList<String> iNFO_SEGURO, String iD_OPERADOR,
			ArrayList<String> iNFO_SERVICIOS, ArrayList<String> iNFO_SERVICIOS_PUBLICOS) {
		
		log.info ("Adicionando ALOJAMIENTO de OP: " + iD_OPERADOR);
		Alojamiento alojamiento = pp.adicionarAlojamiento(nUMHABITACIONES, uBICACION, pRECIO, 
				aMOBLADO, cAPACIDAD,cOMPARTIDO, iNDICEOCUPACION, tIPO_ALOJAMIENTO, iNFO_HORARIO,
				iNFO_SEGURO, iD_OPERADOR, iNFO_SERVICIOS, iNFO_SERVICIOS_PUBLICOS);
		log.info ("Adicionando alojamiento: " + alojamiento);
		return alojamiento;
	}
	
	
	public Reserva adicionarReservaColectiva(String fECHAINICIO, String fECHAFINAL,
			String aLOJAMIENTO, String cLIENTE, ArrayList<String> iNFO_CONTRATO,String cANTIDADPERSONAS) {
		log.info ("Adicionando RESERVA para el cliente con ID: " + cLIENTE);
		Reserva reserva = pp.adicionarReserva(fECHAINICIO, fECHAFINAL, aLOJAMIENTO, cLIENTE, iNFO_CONTRATO, cANTIDADPERSONAS, null);
		log.info ("Adicionando operador: " + reserva);
		return reserva;
	}
	
	
	/* ****************************************************************
	 * 			Métodos para RFC2: TOP 20 ALOJAMIENTOS
	 *****************************************************************/
	
	/*
	public List<Object[]> darTop20Ofertas() {
		log.info ("Generando los VO de Tipos de Alojamiento");        
		List<Object[]> voTipos = new LinkedList<Object[]> ();
        for (Object[] tb : pp.darTop20Ofertas ())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Tipos de Alojamiento: " + voTipos.size() + " existentes");
        return voTipos;
	}
	*/
	
	public List<Object[]> darTop20Ofertas() {
		log.info ("Generando los VO de Tipos de Alojamiento");        
        List <Object[]> tuplas = pp.darTop20Ofertas ();
        log.info ("Generando los VO de Tipos de Alojamiento: existentes");
        return tuplas;
	}
	
	
	public List<VOAlojamiento> listarOfertaPocaDemanda ()
	{
		log.info ("Generando los VO de Tipos de Alojamiento");        
        List<VOAlojamiento> voTipos = new LinkedList<VOAlojamiento> ();
        for (Alojamiento tb : pp.listarOfertaPocaDemanda())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Tipos de Alojamiento: " + voTipos.size() + " existentes");
        return voTipos;
	}

	
	/* ****************************************************************
	 * Métodos para RFC4: Filtrar en Rango fechas y Por servicios
	 *****************************************************************/
	
	public List<VOAlojamiento> darAlojamientoPorFechaYServicios(String fECHAINICIO,String fECHAFINAL,ArrayList<String> iNFO_SERVICIOS) {
		log.info ("Generando los VO de Alojamientos");        
        List<VOAlojamiento> voTipos = new LinkedList<VOAlojamiento> ();
        for (Alojamiento tb : pp.darAlojamientoPorFechaYServicios (fECHAINICIO,fECHAFINAL,iNFO_SERVICIOS))
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Tipos de Alojamiento: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	public List<VOAlojamiento> darAlojamientoPorFechaServiciosYTipoAlojamiento(String fECHAINICIO,String fECHAFINAL,ArrayList<String> iNFO_SERVICIOS,String tipoA) {
		log.info ("Generando los VO de Alojamientos");        
        List<VOAlojamiento> voTipos = new LinkedList<VOAlojamiento> ();
        for (Alojamiento tb : pp.darAlojamientoPorFechaServiciosYTipoAlojamiento (fECHAINICIO,fECHAFINAL,iNFO_SERVICIOS,tipoA))
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Tipos de Alojamiento: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	/* ****************************************************************
	 * Métodos para RFC6: Dar actividad de uso por usuario
	 *****************************************************************/
	public List<Object[]> darUsoUsuario(String iDCLIENTE) {
		log.info ("Generando las RESERVAS para usuario con id: "+ iDCLIENTE);        
        List <Object[]> tuplas = pp.darUsoUsuario (iDCLIENTE);
        log.info ("Generando las reservas existente para un usuario");
        return tuplas;
	}
	
	public List<Object[]> darConsultarFuncionamiento(String aNIO) {
		log.info ("Generando las RESERVAS para usuario con id: "+ aNIO);        
        List <Object[]> tuplas = pp.darConsultarFuncionamiento (Integer.parseInt(aNIO));
        log.info ("Generando las reservas existente para un usuario");
        return tuplas;
	}
	
	public List<Object[]> darConsultarFuncionamientoMenos(String aNIO) {
		log.info ("Generando las RESERVAS para usuario con id: "+ aNIO);        
        List <Object[]> tuplas = pp.darConsultarFuncionamientoMenos (Integer.parseInt(aNIO));
        log.info ("Generando las reservas existente para un usuario");
        return tuplas;
	}
	
	public List<Object[]> darOperadorMasSolicitado(String aNIO) {
		log.info ("Generando las RESERVAS para usuario con id: "+ aNIO);        
        List <Object[]> tuplas = pp.darOperadorMasSolicitado (Integer.parseInt(aNIO));
        log.info ("Generando las reservas existente para un usuario");
        return tuplas;
	}
	
	public List<Object[]> darOperadorMenosSolicitado(String aNIO) {
		log.info ("Generando las RESERVAS para usuario con id: "+ aNIO);        
        List <Object[]> tuplas = pp.darOperadorMenosSolicitado (Integer.parseInt(aNIO));
        log.info ("Generando las reservas existente para un usuario");
        return tuplas;
	}
	
	
	public List<Object[]> darConsumoUsuarios(String fECHAINICIO,String fECHAFINAL,String cRITERIO, String aLOJAMIENTO) {
		log.info ("Generando el consumo para TODOS los usuarios con el criterio de búsqueda:"+ cRITERIO);        
        List <Object[]> tuplas = pp.darConsumoUsuarios (fECHAINICIO,fECHAFINAL,cRITERIO,aLOJAMIENTO);
        log.info ("Generando las reservas existente para un usuario");
        return tuplas;
	}
	
	public List<Object[]> darConsumoUsuarios2(String fECHAINICIO,String fECHAFINAL,String cRITERIO, String aLOJAMIENTO) {
		log.info ("Generando el consumo para TODOS los usuarios con el criterio de búsqueda:"+ cRITERIO);        
        List <Object[]> tuplas = pp.darConsumoUsuarios2 (fECHAINICIO,fECHAFINAL,cRITERIO,aLOJAMIENTO);
        log.info ("Generando las reservas existente para un usuario");
        return tuplas;
	}
	
	
	
	/**
	 * Elimina un tipo de Alojamiento por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del tipo de Alojamiento a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarTipoAlojamientoPorNombre (String nombre)
	{
		log.info ("Eliminando Tipo de Alojamiento por nombre: " + nombre);
        long resp = pp.eliminarTipoAlojamientoPorNombre (nombre);		
        log.info ("Eliminando Tipo de Alojamiento por nombre: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Elimina un tipo de Alojamiento por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idTipoAlojamiento - El id del tipo de Alojamiento a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarTipoAlojamientoPorId (long idTipoAlojamiento)
	{
		log.info ("Eliminando Tipo de Alojamiento por id: " + idTipoAlojamiento);
        long resp = pp.eliminarTipoAlojamientoPorId (idTipoAlojamiento);		
        log.info ("Eliminando Tipo de Alojamiento por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos los tipos de Alojamiento en Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos TipoAlojamiento con todos los tipos de Alojamiento que conoce la aplicación, llenos con su información básica
	 */
	public List<TipoAlojamiento> darTiposAlojamiento ()
	{
		log.info ("Consultando Tipos de Alojamiento");
        List<TipoAlojamiento> tiposAlojamiento = pp.darTiposAlojamiento ();	
        log.info ("Consultando Tipos de Alojamiento: " + tiposAlojamiento.size() + " existentes");
        return tiposAlojamiento;
	}

	/**
	 * Encuentra todos los tipos de Alojamiento en Parranderos y los devuelve como una lista de VOTipoAlojamiento
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOTipoAlojamiento con todos los tipos de Alojamiento que conoce la aplicación, llenos con su información básica
	 */
	public List<VOTipoAlojamiento> darVOTiposAlojamiento ()
	{
		log.info ("Generando los VO de Tipos de Alojamiento");        
        List<VOTipoAlojamiento> voTipos = new LinkedList<VOTipoAlojamiento> ();
        for (TipoAlojamiento tb : pp.darTiposAlojamiento ())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Tipos de Alojamiento: " + voTipos.size() + " existentes");
        return voTipos;
	}
	public List<VOReserva> darVO20 ()
	{
		log.info ("Generando los VO de Tipos de Alojamiento");        
        List<VOReserva> voTipos = new LinkedList<VOReserva> ();
        for (Reserva tb : pp.listarReservas ())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Tipos de Alojamiento: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	public List<VOReserva> listarReservasTipoMiembroComunidad (String vINCULACION)
	{
		log.info ("Generando los VO de Tipos de Alojamiento");        
        List<VOReserva> voTipos = new LinkedList<VOReserva> ();
        for (Reserva tb : pp.listarReservasTipoMiembroComunidad (vINCULACION))
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Tipos de Alojamiento: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	public List<VOReserva> darConsumoUsuario (String FECHAINICIO,String FECHAFINAL,String CRITERIO,String ALOJAMIENTO,String ID_CLIENTE)
	{
		log.info ("Generando los VO de Tipos de Alojamiento");        
        List<VOReserva> voTipos = new LinkedList<VOReserva> ();
        for (Reserva tb : pp.listarConsumoUsuario (FECHAINICIO,FECHAFINAL,CRITERIO,ALOJAMIENTO,ID_CLIENTE))
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Tipos de Alojamiento: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	public List<VOCliente> darConsumoUsuario2 (String FECHAINICIO,String FECHAFINAL,String CRITERIO,String ALOJAMIENTO,String ID_CLIENTE)
	{
		log.info ("Generando los VO de Tipos de Alojamiento");        
        List<VOCliente> voTipos = new LinkedList<VOCliente> ();
        for (Cliente tb : pp.listarConsumoUsuario2 (FECHAINICIO,FECHAFINAL,CRITERIO,ALOJAMIENTO,ID_CLIENTE))
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Tipos de Alojamiento: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	public List<VOCliente> darBuenosClientes1 ()
	{
		log.info ("Generando los VO de Cliente");        
        List<VOCliente> voTipos = new LinkedList<VOCliente> ();
        for (Cliente tb : pp.listarBuenosClientes1())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Cliente: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	public List<VOCliente> darBuenosClientes2 ()
	{
		log.info ("Generando los VO de Cliente");        
        List<VOCliente> voTipos = new LinkedList<VOCliente> ();
        for (Cliente tb : pp.listarBuenosClientes2())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Cliente: " + voTipos.size() + " existentes");
        return voTipos;
	}
	public List<VOCliente> darBuenosClientes3 ()
	{
		log.info ("Generando los VO de Cliente");        
        List<VOCliente> voTipos = new LinkedList<VOCliente> ();
        for (Cliente tb : pp.listarBuenosClientes3())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Cliente: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	public List<VOCliente> listarClienteFrecuente (String aLOJAMIENTO)
	{
		log.info ("Generando los VO de Tipos de Alojamiento");        
        List<VOCliente> voTipos = new LinkedList<VOCliente> ();
        for (Cliente tb : pp.listarClienteFrecuente (aLOJAMIENTO))
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Tipos de Alojamiento: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	/**
	 * Encuentra el tipos de Alojamiento en Parranderos con el nombre solicitado
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre de la Alojamiento
	 * @return Un objeto TipoAlojamiento con el tipos de Alojamiento de ese nombre que conoce la aplicación, 
	 * lleno con su información básica
	 */
	public TipoAlojamiento darTipoAlojamientoPorNombre (String nombre)
	{
		log.info ("Buscando Tipo de Alojamiento por nombre: " + nombre);
		List<TipoAlojamiento> tb = pp.darTipoAlojamientoPorNombre (nombre);
		return !tb.isEmpty () ? tb.get (0) : null;
	}
	
	
    //alohandes
	/*
	public List<VOTipoBebida> darVOTiposBebida ()
	{
		log.info ("Generando los VO de Tipos de bebida");        
        List<VOTipoBebida> voTipos = new LinkedList<VOTipoBebida> ();
        for (TipoBebida tb : pp.darTiposBebida ())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Tipos de bebida: " + voTipos.size() + " existentes");
        return voTipos;
	}
	*/
	
	public List<VOOperador> darVOOperadores ()
	{
		log.info ("Generando los VO de Operadores");        
        List<VOOperador> voOperadores = new LinkedList<VOOperador> ();
        for (Operador tb : pp.darOperadores())
        {
        	voOperadores.add (tb);
        }
        log.info ("Generando los VO de Operadores: " + voOperadores.size() + " existentes");
        return voOperadores;
	}
	
	public List<VOAlojamiento> darVOAlojamientos ()
	{
		log.info ("Generando los VO de Alojamientos");        
        List<VOAlojamiento> voAlojamientos = new LinkedList<VOAlojamiento> ();
        for (Alojamiento tb : pp.darAlojamientos())
        {
        	voAlojamientos.add (tb);
        }
        log.info ("Generando los VO de Alojamientos: " + voAlojamientos.size() + " existentes");
        return voAlojamientos;
	}
	
	
	public long eliminarAlojamientoPorId (long idAlojamiento)
	{
		log.info ("Eliminando Alojamiento por id: " + idAlojamiento);
        long resp = pp.eliminarAlojamientoPorId (idAlojamiento);		
        log.info ("Eliminando de Alojamiento por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	public long eliminarReservaPorId (long idReserva)
	{
		log.info ("Eliminando Reserva por id: " + idReserva);
        long resp = pp.eliminarReservaPorId (idReserva, true);		
        log.info ("Eliminando Reserva por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	public long eliminarReservaColectivaPorId (long idReservaColectiva)
	{
		log.info ("Eliminando Reserva Colectiva por id: " + idReservaColectiva);
        long resp = pp.eliminarReservaColectivaPorId (idReservaColectiva);		
        log.info ("Eliminando Reserva Colectiva por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	

	
	/* ****************************************************************
	 * 			Métodos para administración
	 *****************************************************************/

	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Parranderos
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
	 * TIPOBEBIDA, BEBEDOR y BAR, respectivamente
	 */
	public long [] limpiarParranderos ()
	{
        log.info ("Limpiando la BD de Parranderos");
        long [] borrrados = pp.limpiarParranderos();	
        log.info ("Limpiando la BD de Parranderos: Listo!");
        return borrrados;
	}

	public void actualizarPrecioReservaColectiva(long idReservaColectiva) {
		
        log.info ("Actualizando precio de Reserva Colectiva: " + idReservaColectiva);
        pp.actualizarPrecioReservaColectiva(idReservaColectiva);
        log.info ("Fin actualización precio de Reserva Colectiva: " + idReservaColectiva);
	}
	
	public String analizarMayoresIngresos(String tipoAlojamiento) {
		
        log.info ("Analizando fechas de mayor ingreso del tipo de aloj: " + tipoAlojamiento);
        String fecha = pp.analizarMayoresIngresos(tipoAlojamiento);
        log.info ("Fin analisis fechas de mayor ingreso del tipo de aloj: " + tipoAlojamiento);
        return fecha;
	}
	
	public String[] analizarOcupaciones(String tipoAlojamiento) {
		
        log.info ("Analizando fechas de mayor y menor ocupacion del tipo de aloj: " + tipoAlojamiento);
        String[] fechas = pp.analizarOcupaciones(tipoAlojamiento);
        log.info ("Fin analisis fechas de mayor ingreso del tipo de aloj: " + tipoAlojamiento);
        return fechas;
	}

	public String[] deshabilitarAlojamientoPorId(long idAloj) {
        log.info ("Deshabilitando alojamiento: " + idAloj);
        String[] mensaje = pp.deshabilitarAlojamientoPorId(idAloj);
        log.info ("Fin deshabilitando alojamiento: " + idAloj);
        return mensaje;
	}
	
	public long rehabilitarAlojamientoPorId(long idAloj) {
        log.info ("Habilitando alojamiento: " + idAloj);
        long activo = pp.rehabilitarAlojamientoPorId(idAloj);
        log.info ("Fin rehabilitando alojamiento: " + idAloj);
        return activo;
	}
	

	
	




}
