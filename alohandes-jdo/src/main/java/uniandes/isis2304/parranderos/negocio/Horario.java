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
public class Horario implements VOHorario
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO de los bares
	 */
	private long ID;

	private String HORAAPERTURA;

	private String HORACIERRE;

	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Horario() 
    {
		ID = 0;
		HORAAPERTURA = "";
		HORACIERRE = "";
	}

	public Horario(long iD, String hORAAPERTURA, String hORACIERRE) {
		ID = iD;
		HORAAPERTURA = hORAAPERTURA;
		HORACIERRE = hORACIERRE;
	}


	
	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public String getHORAAPERTURA() {
		return HORAAPERTURA;
	}

	public void setHORAAPERTURA(String hORAAPERTURA) {
		HORAAPERTURA = hORAAPERTURA;
	}

	public String getHORACIERRE() {
		return HORACIERRE;
	}

	public void setHORACIERRE(String hORACIERRE) {
		HORACIERRE = hORACIERRE;
	}

	@Override
	public String toString() {
		return "Horario [ID=" + ID + ", HORAAPERTURA=" + HORAAPERTURA + ", HORACIERRE=" + HORACIERRE + "]";
	}


}
