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
public class Seguro implements VOSeguro
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO de los bares
	 */
	private long ID;
	
	private String NOMBREEMPRESA;
	
	// puede ser 'presencial' o 'digital'
	private String MODALIDAD;
	
	private long CODEUDOR;
	
	private long VALORARRIENDO;
	
	private long DURACION;
	


	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Seguro()
	{
		ID = 0;
		NOMBREEMPRESA = "";
		MODALIDAD = "";
		CODEUDOR = 0;
		VALORARRIENDO = 0;
		DURACION = 0;
	}
	
	public Seguro(long iD, String nOMBREEMPRESA, String mODALIDAD, long cODEUDOR, long vALORARRIENDO, long dURACION) {
		ID = iD;
		NOMBREEMPRESA = nOMBREEMPRESA;
		MODALIDAD = mODALIDAD;
		CODEUDOR = cODEUDOR;
		VALORARRIENDO = vALORARRIENDO;
		DURACION = dURACION;
	}


	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public String getNOMBREEMPRESA() {
		return NOMBREEMPRESA;
	}

	public void setNOMBREEMPRESA(String nOMBREEMPRESA) {
		NOMBREEMPRESA = nOMBREEMPRESA;
	}

	public String getMODALIDAD() {
		return MODALIDAD;
	}

	public void setMODALIDAD(String mODALIDAD) {
		MODALIDAD = mODALIDAD;
	}

	public long getCODEUDOR() {
		return CODEUDOR;
	}

	public void setCODEUDOR(long cODEUDOR) {
		CODEUDOR = cODEUDOR;
	}

	public long getVALORARRIENDO() {
		return VALORARRIENDO;
	}

	public void setVALORARRIENDO(long vALORARRIENDO) {
		VALORARRIENDO = vALORARRIENDO;
	}

	public long getDURACION() {
		return DURACION;
	}

	public void setDURACION(long dURACION) {
		DURACION = dURACION;
	}

	@Override
	public String toString() {
		return "Seguro [ID=" + ID + ", NOMBREEMPRESA=" + NOMBREEMPRESA + ", MODALIDAD=" + MODALIDAD + ", CODEUDOR="
				+ CODEUDOR + ", VALORARRIENDO=" + VALORARRIENDO + ", DURACION=" + DURACION + "]";
	}
	
		

}
