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
public class Servicio implements VOServicio
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/

	private long ID;
	private long TIPO;
	private long ALOJAMIENTO;


	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Servicio() 
    {
		ID = 0;
		TIPO = 0;
		ALOJAMIENTO = 0;
	}


	public Servicio(long iD, long tIPO, long aLOJAMIENTO) {
		ID = iD;
		TIPO = tIPO;
		ALOJAMIENTO = aLOJAMIENTO;
	}


	

	public long getID() {
		return ID;
	}


	public void setID(long iD) {
		ID = iD;
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
		return "Servicio [ID=" + ID + ", TIPO=" + TIPO + ", ALOJAMIENTO=" + ALOJAMIENTO + "]";
	}

	
	

}
