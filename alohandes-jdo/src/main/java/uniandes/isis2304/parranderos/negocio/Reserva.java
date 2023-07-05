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


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;


/**
 * Clase para modelar el concepto BAR del negocio de los Parranderos
 *
 * @author Germán Bravo
 */
public class Reserva implements VOReserva
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO de los bares
	 */
	private long ID;
	
	private Timestamp FECHACANCELACION;
	private Timestamp FECHAINICIO;
	private Timestamp FECHAFINAL;
	
	private Long ALOJAMIENTO;
	private long CLIENTE;
	private Long CONTRATO;
	private long PRECIO;
	private long CANTIDADPERSONAS;
	private Long RESERVAMADRE;
	private List<Object []> TOPOFERTAS;
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Reserva() 
    {
		this.ID = 0;
		this.FECHACANCELACION = new Timestamp (0);
		this.FECHAINICIO = new Timestamp (0);
		this.FECHAFINAL = new Timestamp (0);
		this.ALOJAMIENTO = null;
		this.CLIENTE = 0;
		this.CONTRATO = (long) 0;
		this.PRECIO = 0;
		this.CANTIDADPERSONAS = 0;
		this.RESERVAMADRE = null;
		TOPOFERTAS = new LinkedList<Object []> ();
	}

	
	public Reserva(long id, Timestamp fechacancelacion, 
			Timestamp fechainicio, Timestamp fechafinal, 
			Long alojamiento, long cliente, Long contrato, long precio, long cantidadPersonas, Long reservamadre) {

		this.ID = id;
		this.FECHACANCELACION = fechacancelacion;
		this.FECHAINICIO = fechainicio;
		this.FECHAFINAL = fechafinal;
		this.ALOJAMIENTO = alojamiento;
		this.CLIENTE = cliente;
		this.CONTRATO = contrato;
		this.PRECIO = precio;
		this.CANTIDADPERSONAS = cantidadPersonas;
		this.RESERVAMADRE = reservamadre;
		TOPOFERTAS = new LinkedList<Object []> ();
	}

	public Reserva(long idReserva, String fECHACANCELACION2, String fECHAINICIO2, String fECHAFINAL2, Long alojamiento,
			long cliente, Long idContrato, long precio, long cantidadPersonas, Long reservamadre) throws ParseException {
		this.ID = idReserva;
		this.FECHACANCELACION = cambiarATimestamp(fECHACANCELACION2);
		this.FECHAINICIO = cambiarATimestamp(fECHAINICIO2);
		this.FECHAFINAL = cambiarATimestamp(fECHAFINAL2);
		this.ALOJAMIENTO = alojamiento;
		this.CLIENTE = cliente;
		this.CONTRATO = idContrato;
		this.PRECIO = precio;
		this.CANTIDADPERSONAS = cantidadPersonas;
		this.RESERVAMADRE = reservamadre;
		TOPOFERTAS = new LinkedList<Object []> ();
	}


	public long getID() {
		return ID;
	}
	

	public void setID(long id) {
		this.ID = id;
	}
	
	public static Timestamp cambiarATimestamp(String timestampString) throws ParseException {
		if (timestampString != null) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = dateFormat.parse(timestampString);
        return new Timestamp(date.getTime());}
		else {
		return null;}
    }


	public long getCANTIDADPERSONAS() {
		return CANTIDADPERSONAS;
	}


	public void setCANTIDADPERSONAS(long cANTIDADPERSONAS) {
		CANTIDADPERSONAS = cANTIDADPERSONAS;
	}


	public Long getRESERVAMADRE() {
		return RESERVAMADRE;
	}


	public void setRESERVAMADRE(Long rESERVAMADRE) {
		RESERVAMADRE = rESERVAMADRE;
	}


	public Timestamp getFECHACANCELACION() {
		return FECHACANCELACION;
	}


	public void setFECHACANCELACION(Timestamp fechacancelacion) {
		this.FECHACANCELACION = fechacancelacion;
	}


	public Timestamp getFECHAINICIO() {
		return FECHAINICIO;
	}


	public void setFECHAINICIO(Timestamp fechainicio) {
		
		this.FECHAINICIO = fechainicio;
	}


	public Timestamp getFECHAFINAL() {
		return FECHAFINAL;
	}


	public void setFECHAFINAL(Timestamp fechafinal) {
		this.FECHAFINAL = fechafinal;
	}


	public Long getALOJAMIENTO() {
		return ALOJAMIENTO;
	}


	public void setALOJAMIENTO(Long alojamiento) {
		this.ALOJAMIENTO = alojamiento;
	}


	public long getCLIENTE() {
		return CLIENTE;
	}


	public void setCLIENTE(long cliente) {
		this.CLIENTE = cliente;
	}


	public Long getCONTRATO() {
		return CONTRATO;
	}


	public void setCONTRATO(Long contrato) {
		this.CONTRATO = contrato;
	}


	public long getPRECIO() {
		return PRECIO;
	}


	public void setPRECIO(long precio) {
		this.PRECIO = precio;
	}
	
	public List<Object []> getTOPOFERTAS() 
	{
		return TOPOFERTAS;
	}

	/**
	 * @param visitasRealizadas - La nueva lista de visitas del bebedor
	 */
	public void setTOPOFERTAS (List<Object []> tOPOFERTAS) 
	{
		this.TOPOFERTAS = tOPOFERTAS;
	}
	
	@Override
	public String toString() {
		return "Reserva [ID=" + ID + ", FECHACANCELACION=" + FECHACANCELACION + ", FECHAINICIO=" + FECHAINICIO
				+ ", FECHAFINAL=" + FECHAFINAL + ", ALOJAMIENTO=" + ALOJAMIENTO + ", CLIENTE=" + CLIENTE + ", CONTRATO="
				+ CONTRATO + ", PRECIO=" + PRECIO + ", CANTIDADPERSONAS=" + CANTIDADPERSONAS + ", RESERVAMADRE="
				+ RESERVAMADRE + ", TOPOFERTAS=" + TOPOFERTAS + "]";
	}

	public String toStringCompleto () 
	{
		String resp =  this.toString();
		resp += "\n --- Visitas realizadas\n";
		int i = 1;
		for (Object [] oferta : TOPOFERTAS)
		{
			Alojamiento alojamiento = (Alojamiento) oferta [0];
			Long numReservas = (Long) oferta [1];
			resp += i++ + ". " + "[" +alojamiento.toString() + ", alojamiento= " + numReservas + ", numReservas= " + numReservas.toString() +"]\n";
		}
		return resp;
	}
	
	public String toStringPorMiembro() {
		return "Reserva [ID=" + ID + ", FECHACANCELACION=" + FECHACANCELACION + ", FECHAINICIO=" + FECHAINICIO
				+ ", FECHAFINAL=" + FECHAFINAL + ", ALOJAMIENTO=" + ALOJAMIENTO + ", CLIENTE=" + CLIENTE + ", CONTRATO="
				+ CONTRATO + ", PRECIO=" + PRECIO + ", CANTIDADPERSONAS=" + CANTIDADPERSONAS+"]";
	}
}
