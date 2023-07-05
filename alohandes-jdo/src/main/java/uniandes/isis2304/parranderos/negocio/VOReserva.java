package uniandes.isis2304.parranderos.negocio;

import java.sql.Timestamp;

public interface VOReserva {

	public long getID();

	public 	Timestamp getFECHACANCELACION();

	public Timestamp getFECHAINICIO();

	public Timestamp getFECHAFINAL();

	public Long getALOJAMIENTO();

	public long getCLIENTE();
	
	public Long getCONTRATO();

	public long getPRECIO();

	@Override
	public String toString();

	public String toStringCompleto();
	
	public String toStringPorMiembro();
	
}
