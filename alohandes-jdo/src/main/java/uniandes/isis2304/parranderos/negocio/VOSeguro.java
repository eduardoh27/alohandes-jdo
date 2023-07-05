package uniandes.isis2304.parranderos.negocio;

public interface VOSeguro {

	public long getID();

	public String getNOMBREEMPRESA();	

	public String getMODALIDAD();

	public long getCODEUDOR();

	public long getVALORARRIENDO();

	public long getDURACION();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del bar
	 */
	public String toString();

	
}
