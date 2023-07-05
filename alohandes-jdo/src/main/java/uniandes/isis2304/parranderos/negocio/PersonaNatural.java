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
public class PersonaNatural implements VOPersonaNatural
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

	
	/**
	 * El celular del cliente
	 */
	private long CELULAR;
	
	/**
	 * La vinculación del cliente que es de tipoMiembroComunidad
	 */
	private long TIPO;
	

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    
	/**
     * Constructor por defecto
     */
	public PersonaNatural() {
		CEDULA = 0;
		CELULAR = 0;
		TIPO = 0;
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
	public PersonaNatural(long cEDULA,String nOMBRE, long cELULAR, long tIPO) {
		CEDULA = cEDULA;
		CELULAR = cELULAR;
		TIPO = tIPO;
	}

    
	
	public long getCEDULA() {
		return CEDULA;
	}

	public void setCEDULA(long cEDULA) {
		CEDULA = cEDULA;
	}

	public long getCELULAR() {
		return CELULAR;
	}

	public void setCELULAR(long cELULAR) {
		CELULAR = cELULAR;
	}


	public long getTIPO() {
		return TIPO;
	}

	public void setTIPO(long tIPO) {
		TIPO = tIPO;
	}

	@Override
	public String toString() {
		return "PersonaNatural [CEDULA=" + CEDULA + ", CELULAR=" + CELULAR
				+ ", TIPO=" + TIPO + "]";
	}

	
}
