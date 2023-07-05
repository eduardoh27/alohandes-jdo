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
public class TipoOperadorNatural implements VOTipoOperadorNatural
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador del tipo de tipoOperadorNatural
	 */
	private long ID;

	/**
	 * El nombre del tipo de tipoOperadorNatural
	 */
	private String NOMBRE;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public TipoOperadorNatural() 
	{
		this.ID = 0;
		this.NOMBRE = "Default";
	}

	/**
	 * Constructor con valores
	 * @param id - El identificador del tipo de tipoOperadorNatural
	 * @param nombre - El nombre del tipo de tipoOperadorNatural
	 */
	public TipoOperadorNatural(long id, String nombre) 
	{
		this.ID = id;
		this.NOMBRE = nombre;
	}

	/**
	 * @return El id del tipo de tipoOperadorNatural
	 */
	public long getId() 
	{
		return ID;
	}

	/**
	 * @param id - El nuevo id del tipo de tipoOperadorNatural
	 */
	public void setId(long id) 
	{
		this.ID = id;
	}

	/**
	 * @return El nombre del tipo de tipoOperadorNatural
	 */
	public String getNombre() 
	{
		return NOMBRE;
	}

	/**
	 * @param nombre - El nuevo nombre del tipo de tipoOperadorNatural
	 */
	public void setNombre(String nombre) 
	{
		this.NOMBRE = nombre;
	}

	
	@Override
	public String toString() {
		return "TipoOperadorNatural [ID=" + ID + ", NOMBRE=" + NOMBRE + "]";
	}

	/**
	 * @param tipo - El TipoBebida a comparar
	 * @return True si tienen el mismo nombre
	 */
	public boolean equals(Object tipo) 
	{
		TipoOperadorNatural tb = (TipoOperadorNatural) tipo;
		return ID == tb.ID && NOMBRE.equalsIgnoreCase (tb.NOMBRE);
	}

}
