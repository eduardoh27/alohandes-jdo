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
public class Operador implements VOOperador
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO de los operadores
	 */
	private long ID;
	
	/**
	 * El ingreso por anio actual de los operadores
	 */
	private Long INGRESOPORANIOACTUAL;

	/**
	 * El ingreso por anio corrido de los operadores
	 */
	private Long INGRESOPORANIOCORRIDO;
	
	/**
	 * El nombre de los operadores
	 */
	private String NOMBRE;
	
	/**
	 * La clave del operador
	 */
	private String CLAVE;
	
	private String CORREO;

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Operador() 
    {
    	this.ID = 0;
    	this.INGRESOPORANIOACTUAL = (long) 0;
    	this.INGRESOPORANIOCORRIDO = (long) 0;
		this.NOMBRE = "";
		this.CLAVE = "";
		this.CORREO ="";
	}

	
	/**
	 * El constructor con valores
	 * @param iD
	 * @param iNGRESOPORANIOACTUAL
	 * @param iNGRESOPORANIOCORRIDO
	 * @param nOMBRE
	 * @param cLAVE
	 */
    public Operador(long iD, long iNGRESOPORANIOACTUAL, long iNGRESOPORANIOCORRIDO, String nOMBRE, String cLAVE,String cORREO) {
		ID = iD;
		INGRESOPORANIOACTUAL = iNGRESOPORANIOACTUAL;
		INGRESOPORANIOCORRIDO = iNGRESOPORANIOCORRIDO;
		NOMBRE = nOMBRE;
		CLAVE = cLAVE;
		CORREO = cORREO;
	}

    
	
	public long getID() {
		return ID;
	}


	public void setID(long iD) {
		ID = iD;
	}


	public Long getINGRESOPORANIOACTUAL() {
		return INGRESOPORANIOACTUAL;
	}


	public void setINGRESOPORANIOACTUAL(Long iNGRESOPORANIOACTUAL) {
		INGRESOPORANIOACTUAL = iNGRESOPORANIOACTUAL;
	}


	public Long getINGRESOPORANIOCORRIDO() {
		return INGRESOPORANIOCORRIDO;
	}


	public void setINGRESOPORANIOCORRIDO(Long iNGRESOPORANIOCORRIDO) {
		INGRESOPORANIOCORRIDO = iNGRESOPORANIOCORRIDO;
	}


	public String getNOMBRE() {
		return NOMBRE;
	}


	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}


	public String getCLAVE() {
		return CLAVE;
	}


	public void setCLAVE(String cLAVE) {
		CLAVE = cLAVE;
	}
	
	

	public String getCORREO() {
		return CORREO;
	}


	public void setCORREO(String cORREO) {
		CORREO = cORREO;
	}


	@Override
	public String toString() {
		return "Operador [ID=" + ID + ", INGRESOPORANIOACTUAL=" + INGRESOPORANIOACTUAL + ", INGRESOPORANIOCORRIDO="
				+ INGRESOPORANIOCORRIDO + ", NOMBRE=" + NOMBRE + ", CLAVE=" + CLAVE +", CORREO=" + CORREO + "]";
	}

	public String toStringDinero() {
		return "Operador [ID=" + ID + ", NOMBRE=" + NOMBRE + 
				", INGRESOPORANIOACTUAL=" + INGRESOPORANIOACTUAL + 
				", INGRESOPORANIOCORRIDO="+ INGRESOPORANIOCORRIDO + "]";
	}

}
