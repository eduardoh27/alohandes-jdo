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
public class Cliente implements VOCliente
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO del cliente
	 */
	private long CEDULA;
	
	/**
	 * El correo del cliente
	 */
	private String CORREO;

	/**
	 * El nombre del cliente
	 */
	private String NOMBRE;
	
	/**
	 * El celular del cliente
	 */
	private long CELULAR;
	
	/**
	 * La vinculación del cliente que es de tipoMiembroComunidad
	 */
	private long VINCULACION;
	
	/**
	 * La clave del cliente
	 */
	private String CLAVE;

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    
	/**
     * Constructor por defecto
     */
	public Cliente() {
		CEDULA = 0;
		CORREO = "";
		NOMBRE = "";
		CELULAR = 0;
		VINCULACION = 0;
		CLAVE = "";
	}
	
	/**
	 * Constructor con Valores para cliente
	 * @param cEDULA
	 * @param cORREO
	 * @param nOMBRE
	 * @param cELULAR
	 * @param vINCULACION
	 * @param cLAVE
	 */
	public Cliente(long cEDULA, String cORREO, String nOMBRE, long cELULAR, long vINCULACION, String cLAVE) {
		CEDULA = cEDULA;
		CORREO = cORREO;
		NOMBRE = nOMBRE;
		CELULAR = cELULAR;
		VINCULACION = vINCULACION;
		CLAVE = cLAVE;
	}

    
	
	public long getCEDULA() {
		return CEDULA;
	}

	public void setCEDULA(long cEDULA) {
		CEDULA = cEDULA;
	}

	public String getCORREO() {
		return CORREO;
	}

	public void setCORREO(String cORREO) {
		CORREO = cORREO;
	}

	public String getNOMBRE() {
		return NOMBRE;
	}

	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}

	public long getCELULAR() {
		return CELULAR;
	}

	public void setCELULAR(long cELULAR) {
		CELULAR = cELULAR;
	}

	public long getVINCULACION() {
		return VINCULACION;
	}

	public void setVINCULACION(long vINCULACION) {
		VINCULACION = vINCULACION;
	}

	public String getCLAVE() {
		return CLAVE;
	}

	public void setCLAVE(String cLAVE) {
		CLAVE = cLAVE;
	}

	@Override
	public String toString() {
		return "Cliente [CEDULA=" + CEDULA + ", CORREO=" + CORREO + ", NOMBRE=" + NOMBRE + ", CELULAR=" + CELULAR
				+ ", VINCULACION=" + VINCULACION + ", CLAVE=" + CLAVE + "]";
	}
	
	public String toStringClienteFrecuente() {
		return "Cliente [CEDULA=" + CEDULA + ", CORREO=" + CORREO + ", NOMBRE=" + NOMBRE + ", CELULAR=" + CELULAR
				+ ", VINCULACION=" + VINCULACION + "]";
	}

}
