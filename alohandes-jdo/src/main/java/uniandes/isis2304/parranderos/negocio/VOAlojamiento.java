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
 * Interfaz para los métodos get de BAR.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Germán Bravo
 */
public interface VOAlojamiento
{
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/

	public long getID();

	public long getNUMHABITACIONES();

	public String getUBICACION();

	public long getPRECIO();

	public long getAMOBLADO();

	public long getCAPACIDAD();

	public long getCOMPARTIDO();

	public long getINDICEOCUPACION();

	public long getTIPO();

	public long getHORARIO();

	public long getSEGURO();
	
	public long getOPERADOR();
	
	public long getACTIVO();


	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del bar
	 */
	public String toString();
	
	public String toStringIndice();

	public String toStringTopOfertas();

}
