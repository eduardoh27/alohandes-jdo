package uniandes.isis2304.parranderos.negocio;

public interface VOTipoServicioPublico {
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	public long getId();

	public String getNombre();

	@Override
	public String toString(); 

	public boolean equals (Object tb); 
}
