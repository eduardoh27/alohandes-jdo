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
 * Interfaz para los métodos get de TIPOOPERADORNATURAL.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Germán Bravo
 */
public interface VOTipoOperadorNatural 
{
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * @return El id del tipo de TIPOOPERADORNATURAL
	 */
	public long getId();

	/**
	 * @return El nombre del tipo de TIPOOPERADORNATURAL
	 */
	public String getNombre();

	/**
	 * @return Una cadena de caracteres con la información del tipo de TIPOOPERADORNATURAL
	 */
	@Override
	public String toString(); 

	/**
	 * Define la igualdad dos Tipos de TIPOOPERADORNATURAL
	 * @param tb - El tipo de TIPOOPERADORNATURAL a comparar
	 * @return true si tienen el mismo identificador y el mismo nombre
	 */
	@Override
	public boolean equals (Object tb); 
}
