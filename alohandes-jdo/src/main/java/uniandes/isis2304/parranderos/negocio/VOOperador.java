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
 * Interfaz para los métodos get de OPERADOR.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Germán Bravo
 */
public interface VOOperador 
{
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
	
	public long getID();

	public Long getINGRESOPORANIOACTUAL();

	public Long getINGRESOPORANIOCORRIDO();

	public String getNOMBRE();


	public String getCLAVE();
	
	public String getCORREO();


	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del operador
	 */
	public String toString();
	
	public String toStringDinero();

}
