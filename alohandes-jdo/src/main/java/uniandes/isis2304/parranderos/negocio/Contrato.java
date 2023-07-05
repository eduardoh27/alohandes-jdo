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
public class Contrato implements VOContrato
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO de los bares
	 */
	private long ID;

	private long INTERNET;
	
	private long ADMINISTRACION;
	
	private long SERVICIOSPUBLICOS;
	
	private long TV;
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Contrato() 
    {
    	this.ID = 0;
		this.INTERNET = 0;
		this.ADMINISTRACION=0;
		this.SERVICIOSPUBLICOS =0;
		this.TV =0;
	}

	

    public Contrato(long iD, long iNTERNET, long aDMINISTRACION,
			long sERVICIOSPUBLICOS, long tV) {
		ID = iD;
		INTERNET = iNTERNET;
		ADMINISTRACION = aDMINISTRACION;
		SERVICIOSPUBLICOS = sERVICIOSPUBLICOS;
		TV = tV;
	}
    
    


		
	public long getID() {
		return ID;
	}



	public void setID(long iD) {
		ID = iD;
	}

	public long getINTERNET() {
		return INTERNET;
	}



	public void setINTERNET(long iNTERNET) {
		INTERNET = iNTERNET;
	}



	public long getADMINISTRACION() {
		return ADMINISTRACION;
	}



	public void setADMINISTRACION(long aDMINISTRACION) {
		ADMINISTRACION = aDMINISTRACION;
	}



	public long getSERVICIOSPUBLICOS() {
		return SERVICIOSPUBLICOS;
	}



	public void setSERVICIOSPUBLICOS(long sERVICIOSPUBLICOS) {
		SERVICIOSPUBLICOS = sERVICIOSPUBLICOS;
	}



	public long getTV() {
		return TV;
	}



	public void setTV(long tV) {
		TV = tV;
	}



	@Override
	public String toString() {
		return "Contrato [ID=" + ID + ", INTERNET=" + INTERNET + ", ADMINISTRACION=" + ADMINISTRACION
				+ ", SERVICIOSPUBLICOS=" + SERVICIOSPUBLICOS + ", TV=" + TV + "]";
	}
	

}
