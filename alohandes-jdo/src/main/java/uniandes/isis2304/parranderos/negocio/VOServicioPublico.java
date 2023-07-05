package uniandes.isis2304.parranderos.negocio;

public interface VOServicioPublico {
	
	public long getID();

	public long getCOSTO();

	public long getTIPO();

	public long getALOJAMIENTO();

	@Override
	public String toString();
	
}
