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

/**
 * Clase para modelar el concepto BAR del negocio de los Parranderos
 *
 * @author Germán Bravo
 */
public class Alojamiento implements VOAlojamiento 
{


	public void setHORARIO(Long hORARIO) {
		HORARIO = hORARIO;
	}

	public void setSEGURO(Long sEGURO) {
		SEGURO = sEGURO;
	}


	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO de los bares
	 */
	private long ID;
	
	private long NUMHABITACIONES;
	
	private String UBICACION;
	
	private long PRECIO;
	
	private long AMOBLADO;
	
	private long CAPACIDAD;
	
	private long COMPARTIDO;
	
	private long INDICEOCUPACION;
	
	private long TIPO;
	
	private Long HORARIO;
	
	private Long SEGURO;
	
	private long OPERADOR;
	
	private long ACTIVO;
	
	

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */

	
	public Alojamiento() {
		super();
		ID = 0;
		NUMHABITACIONES = 0;
		UBICACION = "";
		PRECIO = 0;
		AMOBLADO = 0;
		CAPACIDAD = 0;
		COMPARTIDO = 0;
		INDICEOCUPACION = 0;
		TIPO = 0;
		HORARIO = (long) 0;
		SEGURO = (long) 0;
		OPERADOR = 0;
		ACTIVO = 1;
	}

	/**
	 * Constructor con valores
	 * @param id - El id del bart
	 * @param nombre - El nombre del bar
	 * @param ciudad - La ciudad del bar
	 * @param presupuesto - El presupuesto del bar (ALTO, MEDIO, BAJO)
	 * @param cantSedes - Las sedes del bar (Mayor que 0)
	 */
    
	public Alojamiento(long iD, long nUMHABITACIONES, String uBICACION, long pRECIO, 
			long aMOBLADO, long cAPACIDAD, long cOMPARTIDO, long iNDICEOCUPACION, 
			long tIPO, long hORARIO, long sEGURO, long oPERADOR, long aCTIVO) {
		ID = iD;
		NUMHABITACIONES = nUMHABITACIONES;
		UBICACION = uBICACION;
		PRECIO = pRECIO;
		AMOBLADO = aMOBLADO;
		CAPACIDAD = cAPACIDAD;
		COMPARTIDO = cOMPARTIDO;
		INDICEOCUPACION = iNDICEOCUPACION;
		TIPO = tIPO;
		HORARIO = hORARIO;
		SEGURO = sEGURO;
		OPERADOR = oPERADOR;
		ACTIVO = aCTIVO;
	}

	
	
	
	
	
	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public long getNUMHABITACIONES() {
		return NUMHABITACIONES;
	}

	public void setNUMHABITACIONES(long nUMHABITACIONES) {
		NUMHABITACIONES = nUMHABITACIONES;
	}

	public String getUBICACION() {
		return UBICACION;
	}

	public void setUBICACION(String uBICACION) {
		UBICACION = uBICACION;
	}

	public long getPRECIO() {
		return PRECIO;
	}

	public void setPRECIO(long pRECIO) {
		PRECIO = pRECIO;
	}

	public long getAMOBLADO() {
		return AMOBLADO;
	}

	public void setAMOBLADO(long aMOBLADO) {
		AMOBLADO = aMOBLADO;
	}

	public long getCAPACIDAD() {
		return CAPACIDAD;
	}

	public void setCAPACIDAD(long cAPACIDAD) {
		CAPACIDAD = cAPACIDAD;
	}

	public long getCOMPARTIDO() {
		return COMPARTIDO;
	}

	public void setCOMPARTIDO(long cOMPARTIDO) {
		COMPARTIDO = cOMPARTIDO;
	}

	public long getINDICEOCUPACION() {
		return INDICEOCUPACION;
	}

	public void setINDICEOCUPACION(long iNDICEOCUPACION) {
		INDICEOCUPACION = iNDICEOCUPACION;
	}

	public long getTIPO() {
		return TIPO;
	}

	public void setTIPO(long tIPO) {
		TIPO = tIPO;
	}

	public long getHORARIO() {
		return HORARIO;
	}

	public void setHORARIO(long hORARIO) {
		HORARIO = hORARIO;
	}

	public long getSEGURO() {
		return SEGURO;
	}

	public void setSEGURO(long sEGURO) {
		SEGURO = sEGURO;
	}
	
	public long getOPERADOR() {
		return OPERADOR;
	}

	public void setOPERADOR(long oPERADOR) {
		OPERADOR = oPERADOR;
	}

	public void setACTIVO(long aCTIVO) {
		ACTIVO = aCTIVO;
	}

	public long getACTIVO() {
		return ACTIVO;
	}
	
	
	@Override
	public String toString() {
		return "Alojamiento [ID=" + ID + ", NUMHABITACIONES=" + NUMHABITACIONES + ", UBICACION=" + UBICACION
				+ ", PRECIO=" + PRECIO + ", AMOBLADO=" + AMOBLADO + ", CAPACIDAD=" + CAPACIDAD + ", COMPARTIDO="
				+ COMPARTIDO + ", INDICEOCUPACION=" + INDICEOCUPACION + ", TIPO=" + TIPO + ", HORARIO=" + HORARIO
				+ ", SEGURO=" + SEGURO + ", OPERADOR=" + OPERADOR + ", ACTIVO=" + ACTIVO + "]";
	}
	

	public String toStringIndice() {
		return "Alojamiento [ID=" + ID + 
				", INDICEOCUPACION=" + INDICEOCUPACION + 
				", OPERADOR=" + OPERADOR + "]";
	}
	

	public String toStringTopOfertas() {
		return "Alojamiento [ID=" + ID + ", NUMHABITACIONES=" + NUMHABITACIONES 
				+ ", UBICACION=" + UBICACION + "]";
	}


}
