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
public class ServicioPublico implements VOServicioPublico
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/

	private long ID;
	private long COSTO;
	private long TIPO;
	private long ALOJAMIENTO;


	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public ServicioPublico() 
    {
		ID = 0;
		COSTO = 0;
		TIPO = 0;
		ALOJAMIENTO = 0;
	}


	public ServicioPublico(long iD, long cOSTO, long tIPO, long aLOJAMIENTO) {
		ID = iD;
		COSTO = cOSTO;
		TIPO = tIPO;
		ALOJAMIENTO = aLOJAMIENTO;
	}


	

	public long getID() {
		return ID;
	}


	public void setID(long iD) {
		ID = iD;
	}


	public long getCOSTO() {
		return COSTO;
	}


	public void setCOSTO(long cOSTO) {
		COSTO = cOSTO;
	}


	public long getTIPO() {
		return TIPO;
	}


	public void setTIPO(long tIPO) {
		TIPO = tIPO;
	}


	public long getALOJAMIENTO() {
		return ALOJAMIENTO;
	}


	public void setALOJAMIENTO(long aLOJAMIENTO) {
		ALOJAMIENTO = aLOJAMIENTO;
	}


	@Override
	public String toString() {
		return "ServicioPublico [ID=" + ID + ", COSTO=" + COSTO + ", TIPO=" + TIPO + ", ALOJAMIENTO=" + ALOJAMIENTO
				+ "]";
	}
	
	
	
	
	
	

}
