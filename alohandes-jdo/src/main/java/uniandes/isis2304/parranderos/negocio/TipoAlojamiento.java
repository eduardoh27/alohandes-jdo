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
public class TipoAlojamiento implements VOTipoAlojamiento
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador del tipo de bebida
	 */
	private long ID;

	/**
	 * El nombre del tipo de bebida
	 */
	private String NOMBRE;
	
	private String TIPOPAGO;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public TipoAlojamiento() 
	{
		this.ID = 0;
		this.NOMBRE = "Default";
		this.TIPOPAGO = "";
	}

	/**
	 * Constructor con valores
	 * @param id - El identificador del tipo de bebida
	 * @param nombre - El nombre del tipo de bebida
	 */
	public TipoAlojamiento(long id, String nombre, String tipopago) 
	{
		this.ID = id;
		this.NOMBRE = nombre;
		this.TIPOPAGO = tipopago;
	}

	/**
	 * @return El id del tipo de bebida
	 */
	public long getID() 
	{
		return ID;
	}

	/**
	 * @param id - El nuevo id del tipo de bebida
	 */
	public void setID(long id) 
	{
		this.ID = id;
	}

	/**
	 * @return El nombre del tipo de bebida
	 */
	public String getNOMBRE() 
	{
		return NOMBRE;
	}

	/**
	 * @param nombre - El nuevo nombre del tipo de bebida
	 */
	public void setTIPOPAGO(String tipopago) 
	{
		this.TIPOPAGO = tipopago;
	}
	
	public void setNOMBRE(String nombre) 
	{
		this.NOMBRE = nombre;
	}
	
	public String getTIPOPAGO() 
	{
		return TIPOPAGO;
	}

	/**
	 * @return Una cadena de caracteres con la información del tipo de bebida
	 */
	@Override
	public String toString() 
	{
		return "TipoAlojamiento [ID=" + ID + ", NOMBRE=" + NOMBRE + "TIPOPAGO"+TIPOPAGO +"]";
	}

	/**
	 * @param tipo - El TipoBebida a comparar
	 * @return True si tienen el mismo nombre
	 */
	public boolean equals(Object tipo) 
	{
		TipoAlojamiento tb = (TipoAlojamiento) tipo;
		return ID == tb.ID && NOMBRE.equalsIgnoreCase (tb.NOMBRE);
	}

}
