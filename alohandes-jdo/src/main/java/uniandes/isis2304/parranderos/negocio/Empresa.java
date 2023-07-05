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
public class Empresa implements VOEmpresa
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO de las EMPRESAS
	 */
	private long NIT;
	
	/**
	 * La ubicación de las EMPRESAS
	 */
	private String UBICACION;

	/**
	 * Si la empresa tiene o no RECEPCION (1 o 0)
	 */
	private long RECEPCION;
	
	/**
	 * El REGISTROTURISMO de la EMPRESA
	 */
	private long REGISTROTURISMO;
	
	/**
	 * El REGISTROCAMARA de la EMPRESA
	 */
	private long REGISTROCAMARA;
	
	/**
	 * El REGISTROCAMARA de la EMPRESA
	 */
	private long NUMEROCONTACTO;
	
	/**
	 * El tipo de EMPRESA
	 */
	private long TIPO;

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Empresa() 
    {
    	this.NIT = 0;
		this.UBICACION = "";
		this.RECEPCION = 0;
		this.REGISTROTURISMO =0;
		this.REGISTROCAMARA = 0;
		this.NUMEROCONTACTO = 0;
		this.TIPO = 0;
	}

	
	/**
	 * Constructor con valores
	 * @param nIT
	 * @param uBICACION
	 * @param rECEPCION
	 * @param rEGISTROTURISMO
	 * @param rEGISTROCAMARA
	 * @param nUMEROCONTACTO
	 * @param tIPO
	 */
    public Empresa(long nIT, String uBICACION, long rECEPCION, long rEGISTROTURISMO, long rEGISTROCAMARA,
			long nUMEROCONTACTO, long tIPO) {
		NIT = nIT;
		UBICACION = uBICACION;
		RECEPCION = rECEPCION;
		REGISTROTURISMO = rEGISTROTURISMO;
		REGISTROCAMARA = rEGISTROCAMARA;
		NUMEROCONTACTO = nUMEROCONTACTO;
		TIPO = tIPO;
	}

	public long getNIT() {
		return NIT;
	}


	public void setNIT(long nIT) {
		NIT = nIT;
	}


	public String getUBICACION() {
		return UBICACION;
	}


	public void setUBICACION(String uBICACION) {
		UBICACION = uBICACION;
	}


	public long getRECEPCION() {
		return RECEPCION;
	}


	public void setRECEPCION(long rECEPCION) {
		RECEPCION = rECEPCION;
	}


	public long getREGISTROTURISMO() {
		return REGISTROTURISMO;
	}


	public void setREGISTROTURISMO(long rEGISTROTURISMO) {
		REGISTROTURISMO = rEGISTROTURISMO;
	}


	public long getREGISTROCAMARA() {
		return REGISTROCAMARA;
	}


	public void setREGISTROCAMARA(long rEGISTROCAMARA) {
		REGISTROCAMARA = rEGISTROCAMARA;
	}


	public long getNUMEROCONTACTO() {
		return NUMEROCONTACTO;
	}


	public void setNUMEROCONTACTO(long nUMEROCONTACTO) {
		NUMEROCONTACTO = nUMEROCONTACTO;
	}


	public long getTIPO() {
		return TIPO;
	}


	public void setTIPO(long tIPO) {
		TIPO = tIPO;
	}


	@Override
	public String toString() {
		return "Empresa [NIT=" + NIT + ", UBICACION=" + UBICACION + ", RECEPCION=" + RECEPCION + ", REGISTROTURISMO="
				+ REGISTROTURISMO + ", REGISTROCAMARA=" + REGISTROCAMARA + ", NUMEROCONTACTO=" + NUMEROCONTACTO
				+ ", TIPO=" + TIPO + "]";
	}


}
