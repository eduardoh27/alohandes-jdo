package uniandes.isis2304.parranderos.negocio;

public interface VOTipoAlojamiento {
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	public long getID();

	public String getNOMBRE();
	
	public String getTIPOPAGO();

	@Override
	public String toString(); 

	public boolean equals (Object tb); 
}
