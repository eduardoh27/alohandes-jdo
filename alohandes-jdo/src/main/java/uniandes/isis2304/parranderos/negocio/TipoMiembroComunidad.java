package uniandes.isis2304.parranderos.negocio;

public class TipoMiembroComunidad {
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

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public TipoMiembroComunidad() 
	{
		this.ID = 0;
		this.NOMBRE = "Default";
	}

	/**
	 * Constructor con valores
	 * @param id - El identificador del tipo de bebida
	 * @param nombre - El nombre del tipo de bebida
	 */

	public TipoMiembroComunidad(long iD, String nOMBRE) {
		super();
		ID = iD;
		NOMBRE = nOMBRE;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public String getNOMBRE() {
		return NOMBRE;
	}

	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}

	@Override
	public String toString() {
		return "TipoMiembroComunidad [ID=" + ID + ", NOMBRE=" + NOMBRE + "]";
	}

	/**
	 * @param tipo - El TipoMiembroComunidad a comparar
	 * @return True si tienen el mismo nombre
	 */
	public boolean equals(Object tipo) 
	{
		TipoMiembroComunidad tb = (TipoMiembroComunidad) tipo;
		return ID == tb.ID && NOMBRE.equalsIgnoreCase (tb.NOMBRE);
	}
}
