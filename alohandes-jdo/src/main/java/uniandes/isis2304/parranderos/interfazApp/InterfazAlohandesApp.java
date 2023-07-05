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

package uniandes.isis2304.parranderos.interfazApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.parranderos.negocio.Alohandes;
import uniandes.isis2304.parranderos.negocio.VOAlojamiento;
import uniandes.isis2304.parranderos.negocio.VOCliente;
import uniandes.isis2304.parranderos.negocio.VOOperador;
import uniandes.isis2304.parranderos.negocio.VOReserva;
import uniandes.isis2304.parranderos.negocio.VOTipoAlojamiento;

/**
 * Clase principal de la interfaz
 * @author Germán Bravo
 */
@SuppressWarnings("serial")

public class InterfazAlohandesApp extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazAlohandesApp.class.getName());
	
	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp2.json"; 
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_ALOHANDES.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
    /**
     * Asociación a la clase principal del negocio.
     */
    
    private Alohandes alohandes;
    
	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
    /**
     * Objeto JSON con la configuración de interfaz de la app.
     */
    private JsonObject guiConfig;
    
    /**
     * Panel de despliegue de interacción para los requerimientos
     */
    private PanelDatos panelDatos;
    
    /**
     * Menú de la aplicación
     */
    private JMenuBar menuBar;
    
	private final String[] OPCIONES_NO_SI = {"SI", "NO"};
	
	private final String[] OPCIONES_EMPRESA_PERSONANATURAL = {"EMPRESA","PERSONANATURAL"};
	
	private final String[] OPCIONES_TIPOEMPRESA = {"Hotel", "Hostal",
			"viviendaUniversitaria"};
	
	private final String[] OPCIONES_TIPOOPERADORNATURAL= {"Vecino", "PersonaNatural",
	"miembroComunidad"};
	
	private final String[] OPCIONES_VINCULACION = {"Profesor", "Empleado",
	"Egresado", "Estudiante", "padreEstudiante"};
	
	private final String[] OPCIONES_TIPOALOJAMIENTO = {"hotelSuite","hotelSemisuit","hotelEstandar", "habitacionHostal",
	"habitacionVivienda", "apartamento", "vivienda", "habitacionResidencia"};
	
	private final String[] OPCIONES_TIPOSERVICIOPUBLICO = {"agua","luz","telefono","tvCable",
			"internet"};
	
	private final String[] OPCIONES_TIPOSERVICIO = {"restaurante", 
            "piscina", "parqueadero", 
            "wifi", "recepcion",
            "comida", "accesoCocina",
            "banioPrivado", "banioCompartido",
            "habitacionIndividual", "habitacionCompartida",
            "baniera", "yacuzzi", 
            "sala", "cocineta",
            "servicioRestaurante", "salaEstudio",
            "salaEsparcimiento", "gimnasio"};

	private final String[] OPCIONES_MODALIDADSEGURO = {"presencial", "digital"};
	
	private final String[] OPCIONES_DURACIONSEGURO = {"3", "6", "12"};
	
	private final String[] ADMIN = {"ADMIN","password"};
	
	private final String[] CEO = {"CEO","password"};
	
	private final String[] USUARIO = {"CLIENTE","ADMIN"};
	
	private final String[] OPCIONESADMIN = {"CEDULA","VINCULACION","NOMBRE","NUMRESERVAS"};
	
	private final String[] OPCIONESCLIENTE = {"PRECIO","FECHAINICIO","FECHAFINAL","CANTIDADPERSONAS"};
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
    /**
     * Construye la ventana principal de la aplicación. <br>
     * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
     */
    public InterfazAlohandesApp( )
    {
        // Carga la configuración de la interfaz desde un archivo JSON
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        
        // Configura la apariencia del frame que contiene la interfaz gráfica
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        alohandes = new Alohandes (tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicació, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Alohandes App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
    
    /**
     * Método para configurar el frame principal de la aplicación
     */
    private void configurarFrame(  )
    {
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	
    	if ( guiConfig == null )
    	{
    		log.info ( "Se aplica configuración por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
    	
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );

        setTitle( titulo );
		setSize ( ancho, alto);        
    }

    /**
     * Método para crear el menú de la aplicación con base em el objeto JSON leído
     * Genera una barra de menú y los menús con sus respectivas opciones
     * @param jsonMenu - Arreglo Json con los menùs deseados
     */
    private void crearMenu(  JsonArray jsonMenu )
    {    	
    	// Creación de la barra de menús
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu)
        {
        	// Creación de cada uno de los menús
        	JsonObject jom = men.getAsJsonObject(); 

        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	
        	JMenu menu = new JMenu( menuTitle);
        	
        	for (JsonElement op : opciones)
        	{       	
        		// Creación de cada una de las opciones del menú
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }
    
	/* ****************************************************************
	 * 			CRUD de TipoAlojamiento
	 *****************************************************************/
    /**
     * Adiciona un tipo de Alojamiento con la información dada por el usuario
     * Se crea una nueva tupla de tipoAlojamiento en la base de datos, si un tipo de Alojamiento con ese nombre no existía
     */
    public void adicionarTipoAlojamiento( )
    {
    	try 
    	{
    		String nombreTipo = JOptionPane.showInputDialog (this, "Nombre del tipo de alojamiento?", "Adicionar tipo de Alojamiento", JOptionPane.QUESTION_MESSAGE);
    		
    		if (nombreTipo != null)
    		{
        		VOTipoAlojamiento tb = alohandes.adicionarTipoAlojamiento (nombreTipo);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un tipo de Alojamiento con nombre: " + nombreTipo);
        		}
        		String resultado = "En adicionarTipoAlojamiento\n\n";
        		resultado += "Tipo de Alojamiento adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los tipos de Alojamiento existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarTipoAlojamiento( )
    {
    	try 
    	{
			List <VOTipoAlojamiento> lista = alohandes.darVOTiposAlojamiento();

			String resultado = "En listarTipoAlojamiento";
			resultado +=  "\n" + listarTiposAlojamiento (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el tipo de Alojamiento con el identificador dado po el usuario
     * Cuando dicho tipo de Alojamiento no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarTipoAlojamientoPorId( )
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id del tipo de alojamiento?", "Borrar tipo de Alojamiento por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idTipoStr != null)
    		{
    			long idTipo = Long.valueOf (idTipoStr);
    			long tbEliminados = alohandes.eliminarTipoAlojamientoPorId (idTipo);

    			String resultado = "En eliminar TipoAlojamiento\n\n";
    			resultado += tbEliminados + " Tipos de Alojamiento eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Busca el tipo de Alojamiento con el nombre indicado por el usuario y lo muestra en el panel de datos
     */
    public void buscarTipoAlojamientoPorNombre( )
    {
    	try 
    	{
    		String nombreTb = JOptionPane.showInputDialog (this, "Nombre del tipo de alojamiento?", "Buscar tipo de Alojamiento por nombre", JOptionPane.QUESTION_MESSAGE);
    		if (nombreTb != null)
    		{
    			VOTipoAlojamiento tipoAlojamiento = alohandes.darTipoAlojamientoPorNombre (nombreTb);
    			String resultado = "En buscar Tipo Alojamiento por nombre\n\n";
    			if (tipoAlojamiento != null)
    			{
        			resultado += "El tipo de Alojamiento es: " + tipoAlojamiento;
    			}
    			else
    			{
        			resultado += "Un tipo de Alojamiento con nombre: " + nombreTb + " NO EXISTE\n";    				
    			}
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    

	/* ****************************************************************
	 * RF1: REGISTRAR LOS OPERADORES DE ALOJAMIENTO PARA ALOHANDES
	 *****************************************************************/
    public String cambiarSiNoPorNumero (String booleano) {
		if (booleano == "SI") return "1";
		else return "0"; // NO
    }
    
    public String cambiarTipoEmpresaPorNumero (String tipoEmpresa) {
		if (tipoEmpresa == "Hotel") {
			return "1";
		}
		else if (tipoEmpresa == "Hostal") {
			return "2";
		}
		else return "3"; // ViviendaUniversitaria
    }
    
    public String cambiarTipoPersonaNaturalPorNumero (String tipoPersonaNatural) {
		if (tipoPersonaNatural == "Vecino") {
			return "1";
		}
		else if (tipoPersonaNatural == "PersonaNatural") {
			return "2";
		}
		else return "3"; // miembroComunidad
    }
    
    public void adicionarOperador( )
    {
    	try 
    	{
    		//String INGRESOPORANIOACTUAL = JOptionPane.showInputDialog (this, "Ingrese el INGRESOPORANIOACTUAL?", "Adicionar el INGRESOPORANIOACTUAL", JOptionPane.QUESTION_MESSAGE);
    		//String INGRESOPORANIOCORRIDO = JOptionPane.showInputDialog (this, "Ingrese el INGRESOPORANIOCORRIDO?", "Adicionar tipo de INGRESOPORANIOCORRIDO", JOptionPane.QUESTION_MESSAGE);
    		String NOMBRE = JOptionPane.showInputDialog (this, "Nombre del OPERADOR?", "Adicionar Nombre del OPERADOR", JOptionPane.QUESTION_MESSAGE);
    		String CLAVE = JOptionPane.showInputDialog (this, "Ingrese CLAVE del OPERADOR?", "Adicionar CLAVE del OPERADOR", JOptionPane.QUESTION_MESSAGE);
    		// NO generar id
    		String CORREO = JOptionPane.showInputDialog (this, "Ingrese su CORREO?", "Adicionar su CORREO", JOptionPane.QUESTION_MESSAGE);
    		
            String TIPOOPERADOR = (String) JOptionPane.showInputDialog(this, "Elija el TIPO",
                                                        "Adicionar nuevo tipo", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_EMPRESA_PERSONANATURAL, OPCIONES_EMPRESA_PERSONANATURAL[0]);

            String INGRESOPORANIOACTUAL = "0";
            String INGRESOPORANIOCORRIDO = "0";

    		
    		if (TIPOOPERADOR == "EMPRESA") {
    			
        		String NIT = JOptionPane.showInputDialog (this, "Ingrese el NIT?", "Adicionar el NIT", JOptionPane.QUESTION_MESSAGE);
        		String UBICACION = JOptionPane.showInputDialog (this, "Ingrese la UBICACION?", "Adicionar la UBICACION", JOptionPane.QUESTION_MESSAGE);
        		String RECEPCION = (String) JOptionPane.showInputDialog(this, "Elija la recepcion",
                        "Adicionar recepcion", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
        		String REGISTROTURISMO = JOptionPane.showInputDialog (this, "Ingrese el REGISTROTURISMO?", "Adicionar el REGISTROTURISMO", JOptionPane.QUESTION_MESSAGE);
        		String REGISTROCAMARA = JOptionPane.showInputDialog (this, "Ingrese el REREGISTROCAMARA?", "Adicionar el REREGISTROCAMARA", JOptionPane.QUESTION_MESSAGE);
        		String NUMEROCONTACTO = JOptionPane.showInputDialog (this, "Ingrese el NUMEROCONTACTO?", "Adicionar el NUMEROCONTACTO", JOptionPane.QUESTION_MESSAGE);
                String TIPOEMPRESA = (String) JOptionPane.showInputDialog(this, "Elija el TIPOEMPRESA",
                        "Adicionar el TIPOEMPRESA", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_TIPOEMPRESA, OPCIONES_TIPOEMPRESA[0]);
                boolean algunNull = Stream.of(INGRESOPORANIOACTUAL, 
                		INGRESOPORANIOCORRIDO, NOMBRE, CLAVE, UBICACION, 
                		REGISTROTURISMO, REGISTROCAMARA, NUMEROCONTACTO, 
                		TIPOEMPRESA).anyMatch(value -> value == null);
                //log.info("algunNull: " + algunNull);
        		if (!algunNull)
        		{	

        			RECEPCION = cambiarSiNoPorNumero(RECEPCION);
        			TIPOEMPRESA = cambiarTipoEmpresaPorNumero(TIPOEMPRESA);
        			
            		VOOperador tb = alohandes.adicionarOperadorEmpresa (NIT, 
            				INGRESOPORANIOACTUAL,INGRESOPORANIOCORRIDO,NOMBRE,CLAVE,CORREO,
            				UBICACION, RECEPCION, REGISTROTURISMO, REGISTROCAMARA,
            				NUMEROCONTACTO, TIPOEMPRESA);
            		
            		if (tb == null)
            		{
            			throw new Exception ("No se pudo crear un OPERADOR EMPRESA con nombre: " + NOMBRE);
            		}
            		
            		String resultado = "En adicionarOperadorEmpresa\n\n";
            		resultado += "Operador adicionado exitosamente: " + tb;
        			resultado += "\n Operación terminada";
        			panelDatos.actualizarInterfaz(resultado);
        		}
        		else
        		{
        			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
        		}
    		}
    			
    		else {
    			
    			String CEDULA = JOptionPane.showInputDialog (this, "Ingrese la CEDULA?", "Adicionar la CEDULA", JOptionPane.QUESTION_MESSAGE);
        		String CELULAR = JOptionPane.showInputDialog (this, "Ingrese el CELULAR?", "Adicionar el CELULAR", JOptionPane.QUESTION_MESSAGE);
        		String TIPOPERSONANATURAL = (String) JOptionPane.showInputDialog(this, "Elija el TIPOEMPRESA",
                        "Adicionar el TIPOEMPRESA", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_TIPOOPERADORNATURAL, OPCIONES_TIPOOPERADORNATURAL[0]);
                
                boolean algunNull = Stream.of(INGRESOPORANIOACTUAL, 
                		INGRESOPORANIOCORRIDO, NOMBRE, CLAVE,
                		CORREO, CELULAR, TIPOPERSONANATURAL).anyMatch(value -> value == null);
                //log.info("algunNull: " + algunNull);
        		if (!algunNull)
        		{
        			TIPOPERSONANATURAL = cambiarTipoPersonaNaturalPorNumero(TIPOPERSONANATURAL);
        			
            		VOOperador tb = alohandes.adicionarOperadorNatural (CEDULA, 
            				INGRESOPORANIOACTUAL,INGRESOPORANIOCORRIDO,NOMBRE,CLAVE,
            				CORREO, CELULAR, TIPOPERSONANATURAL);
            		if (tb == null)
            		{
            			throw new Exception ("No se pudo crear un OPERADOR EMPRESA con nombre: " + NOMBRE);
            		}
            		
            		String resultado = "En adicionarOperadorEmpresa\n\n";
            		resultado += "Operador adicionado exitosamente: " + tb;
        			resultado += "\n Operación terminada";
        			panelDatos.actualizarInterfaz(resultado);
        		}
        		else
        		{
        			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
        		}
    			
    		}
    		

		} 
    	catch (Exception e) 
    	{
    		//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
	/* ****************************************************************
	 * RF2: REGISTRAR EL ALOJAMIENTO PARA ALOHANDES
	 *****************************************************************/
    
    public void adicionarAlojamiento( )
    {
    	try 
    	{
    		String ID_OPERADOR = JOptionPane.showInputDialog (this, "Ingrese el ID_OPERADOR?", "Adicionar el ID_OPERADOR", JOptionPane.QUESTION_MESSAGE);
    		String CLAVE_OPERADOR = JOptionPane.showInputDialog (this, "Ingrese la CLAVE_OPERADOR?", "Adicionar la CLAVE_OPERADOR", JOptionPane.QUESTION_MESSAGE);
    		
    		// verificar en base de datos
    		boolean verificacion = alohandes.verificarOperador(ID_OPERADOR, CLAVE_OPERADOR);
    		log.info("verificacion: "+verificacion);
    		
    		if (!verificacion){
    			throw new Exception ("El ID_OPERADOR o la CLAVE_OPERADOR son incorrectos");
    		}
    		
    		String NUMHABITACIONES = JOptionPane.showInputDialog (this, "Ingrese el  NUMERO DE HABITACIONES?", "Adicionar el NUMHABITACIONES", JOptionPane.QUESTION_MESSAGE);
    		String UBICACION = JOptionPane.showInputDialog (this, "Ingrese la UBICACION?", "Adicionar el UBICACION", JOptionPane.QUESTION_MESSAGE);
    		String PRECIO = JOptionPane.showInputDialog (this, "Ingrese el PRECIO?", "Adicionar el PRECIO", JOptionPane.QUESTION_MESSAGE);
    		String AMOBLADO = (String) JOptionPane.showInputDialog(this, "Esta amoblado?",
                    "Esta amoblado", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
    		String CAPACIDAD = JOptionPane.showInputDialog (this, "Ingrese el CAPACIDAD?", "Adicionar el CAPACIDAD", JOptionPane.QUESTION_MESSAGE);
    		String COMPARTIDO = (String) JOptionPane.showInputDialog(this, "Es compartido?",
                    "Es compartido", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
    		String INDICEOCUPACION = JOptionPane.showInputDialog (this, "Ingrese el INDICEOCUPACION?", "Adicionar el INGRESOPORANIOACTUAL", JOptionPane.QUESTION_MESSAGE);
    		String TIPO_ALOJAMIENTO= (String) JOptionPane.showInputDialog(this, "Elija el TIPOALOJAMIENTO",
                    "Elegir el TIPO_ALOJAMIENTO", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_TIPOALOJAMIENTO, OPCIONES_TIPOALOJAMIENTO[0]);
            
    		String HORARIO = (String) JOptionPane.showInputDialog(this, "Tiene horario?",
                    "Tiene horario", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
    		
            ArrayList<String> INFO_HORARIO = new ArrayList<String>();;
            
            if (HORARIO == "SI") {
            	String HORA_APERTURA = (String) JOptionPane.showInputDialog(this, "Ingrese la HORA_APERTURA?", "Adicionar la HORA_APERTURA", JOptionPane.QUESTION_MESSAGE, null, null, "hh:mm"); 
        		String HORA_CIERRE = (String) JOptionPane.showInputDialog(this, "Ingrese la HORA_CIERRE?", "Adicionar la HORA_CIERRE", JOptionPane.QUESTION_MESSAGE, null, null, "hh:mm"); 
    			// verificar si ya estan en la BD
        		INFO_HORARIO.add("2023-01-01 " + HORA_APERTURA + ":00");
        		INFO_HORARIO.add("2023-01-01 " + HORA_CIERRE + ":00");
    		}
            
            String SEGURO = (String) JOptionPane.showInputDialog(this, "Tiene seguro?",
                    "Tiene seguro", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
            
            ArrayList<String> INFO_SEGURO = new ArrayList<String>();;
            
            if (SEGURO == "SI") {
            	String NOMBRE_EMPRESA = JOptionPane.showInputDialog (this, "Informacion seguro",
            			"Adicionar el NOMBRE_EMPRESA", JOptionPane.QUESTION_MESSAGE);
        		
            	String MODALIDAD = (String) JOptionPane.showInputDialog(this, "Informacion seguro",
                        "Tiene MODALIDAD", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_MODALIDADSEGURO, OPCIONES_MODALIDADSEGURO[0]);
        		
            	String CODEUDOR = (String) JOptionPane.showInputDialog(this, "Informacion seguro",
                        "Tiene codeudor", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
        		
            	//String VALOR_ARRIENDO = JOptionPane.showInputDialog (this, "Ingrese el INFO_SEGURO?", "Adicionar el VALOR_ARRIENDO", JOptionPane.QUESTION_MESSAGE);
            	
            	String DURACION = (String) JOptionPane.showInputDialog(this, "Informacion seguro",
                        "Ingrese DURACION en meses", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_DURACIONSEGURO, OPCIONES_DURACIONSEGURO[0]);

            	INFO_SEGURO.add(NOMBRE_EMPRESA);
            	INFO_SEGURO.add(MODALIDAD);
            	INFO_SEGURO.add(CODEUDOR);
            	INFO_SEGURO.add(PRECIO);
            	INFO_SEGURO.add(DURACION);
            }
            
            
            String SERVICIOS = (String) JOptionPane.showInputDialog(this, "Tiene servicios?",
                    "Agregar servicios?", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
            
            ArrayList<String> INFO_SERVICIOS = new ArrayList<String>();
            
            if (SERVICIOS == "SI") {
            	
                JList<String> listaOpciones = new JList<>(OPCIONES_TIPOSERVICIO);
                JScrollPane scrollPane = new JScrollPane(listaOpciones);

                int opcion = JOptionPane.showOptionDialog(null, scrollPane, "Selecciona las opciones que desees", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                
                if (opcion == JOptionPane.YES_OPTION) {    
                	List<String> opciones = listaOpciones.getSelectedValuesList();
                    
                	INFO_SERVICIOS.addAll( opciones );
                }
            }
            
            String SERVICIOS_PUBLICOS = (String) JOptionPane.showInputDialog(this, "Tiene servicios publicos?",
                    "Agregar servicios publicos?", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
            
            ArrayList<String> INFO_SERVICIOS_PUBLICOS = new ArrayList<String>();
            log.info(INFO_SERVICIOS_PUBLICOS);
            
            if (SERVICIOS_PUBLICOS == "SI") {
            	
                JList<String> listaOpciones2 = new JList<>(OPCIONES_TIPOSERVICIOPUBLICO);
                JScrollPane scrollPane = new JScrollPane(listaOpciones2);

                int opcion2 = JOptionPane.showOptionDialog(null, scrollPane, "Selecciona las opciones que desees", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                
                if (opcion2 == JOptionPane.YES_OPTION) {    
                	List<String> opciones2 = listaOpciones2.getSelectedValuesList();
                	INFO_SERVICIOS_PUBLICOS.addAll( opciones2 );
                }
                log.info(INFO_SERVICIOS_PUBLICOS);
            }
            
            boolean algunNull = Stream.of(NUMHABITACIONES, 
            		UBICACION, PRECIO, AMOBLADO,
            		CAPACIDAD, COMPARTIDO, INDICEOCUPACION, 
            		TIPO_ALOJAMIENTO, ID_OPERADOR).anyMatch(value -> value == null);
            
            
    		if (!algunNull)
    		{
        		VOAlojamiento tb = alohandes.adicionarAlojamiento(NUMHABITACIONES,
        				UBICACION,PRECIO,AMOBLADO, CAPACIDAD, COMPARTIDO, INDICEOCUPACION, 
        				TIPO_ALOJAMIENTO, INFO_HORARIO, INFO_SEGURO, ID_OPERADOR, 
        				INFO_SERVICIOS, INFO_SERVICIOS_PUBLICOS);
        		
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un ALOJAMIENTO con operador: " + ID_OPERADOR);
        		}
        		String resultado = "En adicionarAlojamiento\n\n";
        		resultado += "Alojamiento adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
    		//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    
    /* ****************************************************************
	 * RF3: REGISTRAR CLIENTES PARA ALOHANDES
	 *****************************************************************/
    
    public void adicionarCliente( )
    {
    	try 
    	{
    		long CEDULA = Long.parseLong(JOptionPane.showInputDialog (this, "Ingrese su CEDULA", "Adicionar la CEDULA", JOptionPane.QUESTION_MESSAGE));
    		String CORREO = JOptionPane.showInputDialog (this, "Ingrese su CORREO", "Adicionar tipo de CORREO", JOptionPane.QUESTION_MESSAGE);
    		String NOMBRE = JOptionPane.showInputDialog (this, "Ingrese su NOMBRE", "Adicionar Nombre del CLIENTE", JOptionPane.QUESTION_MESSAGE);
    		long CELULAR = Long.parseLong(JOptionPane.showInputDialog (this, "Ingrese su CELULAR", "Adicionar CELULAR del CLIENTE", JOptionPane.QUESTION_MESSAGE));
    		
    		String VINCULACION = (String) JOptionPane.showInputDialog(this, "Elija la vinculacion",
                    "Adicionar la vinculacion", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_VINCULACION, OPCIONES_VINCULACION[0]);

    		String CLAVE = JOptionPane.showInputDialog (this, "Ingrese su CLAVE", "Adicionar CLAVE del CLIENTE", JOptionPane.QUESTION_MESSAGE);
    		
    		if ((CELULAR!= 0) && (CLAVE != null ))
    		{
        		VOCliente tb = alohandes.adicionarCliente (CEDULA,CORREO,NOMBRE,CELULAR, VINCULACION,CLAVE);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un CLIENTE con nombre: " + NOMBRE);
        		}
        		String resultado = "En adicionarCliente\n\n";
        		resultado += "Cliente adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
    		//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /* ****************************************************************
	 * RF4: REGISTRAR UNA RESERVA
	 *****************************************************************/
    
    public void adicionarReserva()
    {
    	try 
    	{
    		
    		String ID_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su cedula?", "Adicionar su cedula", JOptionPane.QUESTION_MESSAGE);
    		String CLAVE_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su clave?", "Adicionar la clave", JOptionPane.QUESTION_MESSAGE);
    		
    		boolean verificacionCliente = alohandes.verificarCliente(ID_CLIENTE, CLAVE_CLIENTE);
    		log.info("verificacion: "+verificacionCliente);
    		
    		if (!verificacionCliente){
    			throw new Exception ("El ID_CLIENTE o la CLAVE_CLIENTE son incorrectos");
    		}
    		
    		String ALOJAMIENTO = JOptionPane.showInputDialog (this, "Ingrese el ALOJAMIENTO?", "Adicionar el ALOJAMIENTO", JOptionPane.QUESTION_MESSAGE);
    		
    		boolean verificacionAlojamiento = alohandes.verificarAlojamiento(ALOJAMIENTO);
    		log.info("verificacion: "+verificacionAlojamiento);
    		
    		if (!verificacionAlojamiento){
    			throw new Exception ("El alojamiento seleccionado es incorrecto");
    		}
    		
    		String CANTIDADPERSONAS = JOptionPane.showInputDialog (this, "Ingrese la cantidad de personas?", "Adicionar la CANTIDADPERSONAS", JOptionPane.QUESTION_MESSAGE);
    		String FECHAINICIO = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAINICIO?", "Adicionar la FECHAINICIO", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
    		String FECHAFINAL = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAFINAL?", "Adicionar la FECHAFINAL", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
    		
    		boolean verificacionCupoAlojamiento = alohandes.verificarCupoAlojamiento(ALOJAMIENTO, FECHAINICIO, FECHAFINAL, CANTIDADPERSONAS);
    		log.info("verificacionCupo: "+verificacionCupoAlojamiento);
    		
    		if (!verificacionCupoAlojamiento){
    			throw new Exception ("No hay cupo en el alojamiento seleccionado");
    		}

 
            String CONTRATO = (String) JOptionPane.showInputDialog(this, "Tiene contrato?",
                    "Tiene contrato", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
    		
            ArrayList<String> INFO_CONTRATO = new ArrayList<String>();;
            
            if (CONTRATO == "SI") {
            	
            	String internet = (String) JOptionPane.showInputDialog(this, "Tiene internet?",
                        "Tiene internet", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
            	String admin = (String) JOptionPane.showInputDialog(this, "Tiene administracion?",
                        "Tiene administracion", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
            	String serviciosPublicos = (String) JOptionPane.showInputDialog(this, "Tiene servicios publicos?",
                        "Tiene servicios publicos", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
            	String tv = (String) JOptionPane.showInputDialog(this, "Tiene tv?",
                        "Tiene tv", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_NO_SI, OPCIONES_NO_SI[0]);
            	
            	// verificar si ya estan en la BD
        		
            	INFO_CONTRATO.add(internet);
            	INFO_CONTRATO.add(admin);
            	INFO_CONTRATO.add(serviciosPublicos);
            	INFO_CONTRATO.add(tv);
            	
            }
            
    		if ((FECHAINICIO!= null) && (FECHAFINAL != null )&& (ALOJAMIENTO != null) && (ID_CLIENTE != null))
    		{
    			
        		VOReserva tb = alohandes.adicionarReserva (FECHAINICIO, FECHAFINAL, ALOJAMIENTO, ID_CLIENTE, INFO_CONTRATO, CANTIDADPERSONAS, null);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear una RESERVA para el cliente con ID: " + ID_CLIENTE);
        		}
        		String resultado = "En adicionarReserva\n\n";
        		resultado += "Reserva adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
    		//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /* ****************************************************************
	 * RF5: CANCELAR UNA RESERVA
	 *****************************************************************/
    

    public void eliminarReservaPorId( )
    {
    	try 
    	{

    		String ID_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su cedula?", "Adicionar su cedula", JOptionPane.QUESTION_MESSAGE);
    		String CLAVE_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su clave?", "Adicionar la clave", JOptionPane.QUESTION_MESSAGE);
    		
    		boolean verificacionCliente = alohandes.verificarCliente(ID_CLIENTE, CLAVE_CLIENTE);
    		log.info("verificacion: "+verificacionCliente);
    		
    		if (!verificacionCliente){
    			throw new Exception ("El ID_CLIENTE o la CLAVE_CLIENTE son incorrectos");
    		}
    		
    		String idReservaStr = JOptionPane.showInputDialog (this, "Ingrese el Id de la reserva?", "Borrar tipo de Reserva por Id", JOptionPane.QUESTION_MESSAGE);
    		
    		if (idReservaStr != null)
    		{
    			long idReserva = Long.valueOf (idReservaStr);
    			
        		boolean verificacionReservaCliente = alohandes.verificarReservaCliente(ID_CLIENTE, idReserva);

        		if (!verificacionReservaCliente){
        			throw new Exception ("La reserva no es del Cliente");
        		}
    			
    			
    			long tbEliminados = alohandes.eliminarReservaPorId (idReserva);

    			String resultado = "En eliminar Reserva\n\n";
    			resultado += tbEliminados + " de Reserva eliminados\n";
    			if (tbEliminados == -2) {
    				resultado += "No se puede eliminar la reserva porque ya expiro\n";
    			}
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /* ****************************************************************
	 * RF6: RETIRAR UNA OFERTA DE ALOJAMIENTO
	 *****************************************************************/
    
    public void eliminarAlojamientoPorId( )
    {
    	try 
    	{
    		
    		
    		String ID_OPERADOR = JOptionPane.showInputDialog (this, "Ingrese el ID_OPERADOR?", "Adicionar el ID_OPERADOR", JOptionPane.QUESTION_MESSAGE);
    		String CLAVE_OPERADOR = JOptionPane.showInputDialog (this, "Ingrese la CLAVE_OPERADOR?", "Adicionar la CLAVE_OPERADOR", JOptionPane.QUESTION_MESSAGE);

    		boolean verificacionOperador = alohandes.verificarOperador(ID_OPERADOR, CLAVE_OPERADOR);

    		if (!verificacionOperador){
    			throw new Exception ("El ID_OPERADOR o la CLAVE_OPERADOR son incorrectos");
    		}
    		
    		
    		String idAlojStr= JOptionPane.showInputDialog (this, "Id del alojamiento?", "Borrar Alojamiento por Id", JOptionPane.QUESTION_MESSAGE);
    		
    		if (idAlojStr != null)
    		{
    			long idAloj = Long.valueOf (idAlojStr);
    			
        		boolean verificacionAlojamientoOperador = alohandes.verificarAlojamientoOperador(ID_OPERADOR, idAloj);

        		if (!verificacionAlojamientoOperador){
        			throw new Exception ("El Alojamiento no es del Operador");
        		}
    			
    			
    			long tbEliminados = alohandes.eliminarAlojamientoPorId (idAloj);

    			String resultado = "En eliminar Alojamiento\n\n";
    			resultado += tbEliminados + " de Alojamiento eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /* ****************************************************************
	 * RF7: REGISTRAR RESERVA COLECTIVA
	 *****************************************************************/
    public void adicionarReserva2(String[] listaAloj, String FECHAINICIO, String FECHAFINAL, String ID_CLIENTE, String ID_COLECTIVA)
    {
    	log.info("ADICIONAR RESERVA 2");
    	try 
    	{
    		log.info("DENTRO DE TRY");
			String ALOJAMIENTO = (String) JOptionPane.showInputDialog(this, "Elija el id del Alojamiento?",
	                "ID alojamiento", JOptionPane.QUESTION_MESSAGE, null, listaAloj, listaAloj[0]);
	        
    		String CANTIDADPERSONAS = JOptionPane.showInputDialog (this, "Ingrese la cantidad de personas?", "Adicionar la CANTIDADPERSONAS", JOptionPane.QUESTION_MESSAGE);
    		
    		boolean verificacionAlojamiento = alohandes.verificarAlojamiento(ALOJAMIENTO);
    		log.info("verificacion: "+verificacionAlojamiento);
    		
    		if (!verificacionAlojamiento){
    			throw new Exception ("El alojamiento seleccionado es incorrecto");
    		}
    		
    		
    		boolean verificacionCupoAlojamiento = alohandes.verificarCupoAlojamiento(ALOJAMIENTO, FECHAINICIO, FECHAFINAL, CANTIDADPERSONAS);
    		log.info("verificacionCupo: "+verificacionCupoAlojamiento);
    		
    		if (!verificacionCupoAlojamiento){
    			throw new Exception ("No hay cupo en el alojamiento seleccionado");
    		}
    		
    		
            ArrayList<String> INFO_CONTRATO = new ArrayList<String>();;
            
    		if ((FECHAINICIO!= null) && (FECHAFINAL != null )&& (ALOJAMIENTO != null) && (ID_CLIENTE != null))
    		{
    			
        		VOReserva tb = alohandes.adicionarReserva (FECHAINICIO, FECHAFINAL, ALOJAMIENTO, ID_CLIENTE, INFO_CONTRATO, CANTIDADPERSONAS, ID_COLECTIVA);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear una RESERVA para el cliente con ID: " + ID_CLIENTE);
        		}
        		String resultado = "En adicionarReserva\n\n";
        		resultado += "Reserva adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
    		//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void registrarReservaColectiva() {
    	try {
	    	String ID_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su cedula?", "Adicionar su cedula", JOptionPane.QUESTION_MESSAGE);
			String CLAVE_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su clave?", "Adicionar la clave", JOptionPane.QUESTION_MESSAGE);
			
			boolean verificacionCliente = alohandes.verificarCliente(ID_CLIENTE, CLAVE_CLIENTE);
			log.info("verificacion: "+verificacionCliente);
			
			if (!verificacionCliente){
				throw new Exception ("El ID_CLIENTE o la CLAVE_CLIENTE son incorrectos");
			}
			String cantidadReservas = JOptionPane.showInputDialog (this, "Ingrese la cantidadReservas ?", "Adicionar la cantidadReservas", JOptionPane.QUESTION_MESSAGE);
			
			String FECHAINICIO = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAINICIO?", "Adicionar la FECHAINICIO", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
			String FECHAFINAL = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAFINAL?", "Adicionar la FECHAFINAL", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
			
			String TIPO_ALOJAMIENTO = (String) JOptionPane.showInputDialog(this, "Elija el TIPOALOJAMIENTO",
	                "Elegir el TIPO_ALOJAMIENTO", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_TIPOALOJAMIENTO, OPCIONES_TIPOALOJAMIENTO[0]);
			// SERVICIOS QUE LE GUSTARÍA TENER
			ArrayList<String> INFO_SERVICIOS = new ArrayList<String>();
			JList<String> listaOpciones = new JList<>(OPCIONES_TIPOSERVICIO);
	        JScrollPane scrollPane = new JScrollPane(listaOpciones);
	
	        int opcion = JOptionPane.showOptionDialog(null, scrollPane, "Selecciona las opciones que desees", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
	        
	        if (opcion == JOptionPane.YES_OPTION) {    
	        	List<String> opciones = listaOpciones.getSelectedValuesList();
	        	INFO_SERVICIOS.addAll( opciones );
	        }
	        
	        List <VOAlojamiento> posiblesAlojamientos = alohandes.darAlojamientoPorFechaServiciosYTipoAlojamiento(FECHAINICIO, FECHAFINAL, INFO_SERVICIOS, TIPO_ALOJAMIENTO);
	        
	        log.info("POSIBLES ALOJAMIENTOS SIZE: "+posiblesAlojamientos.size());
	        
	        
	        if (posiblesAlojamientos.isEmpty()) {
	        	
				String resultado = "No hay alojamientos disponibles para esas fechas, servicios y tipo de alojamiento";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
	        }
	        
	        else {
	        	
		        String[] listaAloj = new String[posiblesAlojamientos.size()];		
		        for (int i=0; i < posiblesAlojamientos.size() ;i++) {  
		        	listaAloj[i] = String.valueOf(posiblesAlojamientos.get(i).getID());  	
		        }
		        
		        // crear colectiva
		        VOReserva reservaColectiva = alohandes.adicionarReserva (FECHAINICIO, FECHAFINAL, null, ID_CLIENTE, new ArrayList<String>(), "0", null);
		        long idReservaColectiva = reservaColectiva.getID();
		        
		        log.info("cantidad de reservas: "+cantidadReservas);
		        
		        for (int i=0; i<Integer.parseInt(cantidadReservas);i++) {
		        	adicionarReserva2(listaAloj, FECHAINICIO, FECHAFINAL, ID_CLIENTE, String.valueOf(idReservaColectiva));	
		        }
		        
		        alohandes.actualizarPrecioReservaColectiva(idReservaColectiva);
		        
				String resultado = "En listarAlojamientosenRangoFechasYconciertosTipos";
				resultado +=  "\n" + listarAlojamientosDisponibles (posiblesAlojamientos);
				resultado += "\n Operación terminada";
				//panelDatos.actualizarInterfaz(resultado);
	        }
			
    	} 
		catch (Exception e) 
		{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /* ****************************************************************
	 * RF8 - CANCELAR RESERVA COLECTIVA
	 *****************************************************************/
    
    public void cancelarReservaColectiva() {
    	try {
    		
    		String ID_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su cedula?", "Adicionar su cedula", JOptionPane.QUESTION_MESSAGE);
    		String CLAVE_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su clave?", "Adicionar la clave", JOptionPane.QUESTION_MESSAGE);
    		
    		boolean verificacionCliente = alohandes.verificarCliente(ID_CLIENTE, CLAVE_CLIENTE);
    		log.info("verificacion: "+verificacionCliente);
    		
    		if (!verificacionCliente){
    			throw new Exception ("El ID_CLIENTE o la CLAVE_CLIENTE son incorrectos");
    		}
    		
    		String idReservaColectivaStr = JOptionPane.showInputDialog (this, "Ingrese el Id de la reserva colectiva?", "Borrar Reserva colectiva por Id", JOptionPane.QUESTION_MESSAGE);
    		
    		if (idReservaColectivaStr != null)
    		{
    			long idReservaColectiva = Long.valueOf (idReservaColectivaStr);
    			
        		boolean verificacionReservaCliente = alohandes.verificarReservaCliente(ID_CLIENTE, idReservaColectiva);

        		if (!verificacionReservaCliente){
        			throw new Exception ("La reserva colectiva no es del Cliente");
        		}
    			
    			long tbEliminados = alohandes.eliminarReservaColectivaPorId (idReservaColectiva);

    			String resultado = "En eliminar Reserva\n\n";
    			resultado += tbEliminados + " de Reserva eliminados\n";
    			if (tbEliminados == -2) {
    				resultado += "No se puede eliminar la reserva porque ya expiro\n";
    			}
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
			
			
    	}
    	catch (Exception e) 
		{
    		
		}
    }
    
    /* ****************************************************************
	 * RF9 - DESHABILITAR OFERTA DE ALOJAMIENTO
	 *****************************************************************/
    public void deshabilitarAlojamientoPorId() {
    	try 
    	{
    		String ID_OPERADOR = JOptionPane.showInputDialog (this, "Ingrese el ID_OPERADOR?", "Adicionar el ID_OPERADOR", JOptionPane.QUESTION_MESSAGE);
    		String CLAVE_OPERADOR = JOptionPane.showInputDialog (this, "Ingrese la CLAVE_OPERADOR?", "Adicionar la CLAVE_OPERADOR", JOptionPane.QUESTION_MESSAGE);

    		boolean verificacionOperador = alohandes.verificarOperador(ID_OPERADOR, CLAVE_OPERADOR);

    		if (!verificacionOperador){
    			throw new Exception ("El ID_OPERADOR o la CLAVE_OPERADOR son incorrectos");
    		}
    		
    		
    		String idAlojStr= JOptionPane.showInputDialog (this, "Id del alojamiento?", "Deshabilitar Alojamiento por Id", JOptionPane.QUESTION_MESSAGE);
    		
    		if (idAlojStr != null)
    		{
    			long idAloj = Long.valueOf (idAlojStr);
    			
        		boolean verificacionAlojamientoOperador = alohandes.verificarAlojamientoOperador(ID_OPERADOR, idAloj);

        		if (!verificacionAlojamientoOperador){
        			throw new Exception ("El Alojamiento no es del Operador");
        		}
    			
    			
    			String[] mensajeLog = alohandes.deshabilitarAlojamientoPorId (idAloj);

    			String resultado = "En deshabilitar Alojamiento";
    			for (String mensaje : mensajeLog) {
    				resultado += "\n" + mensaje; 
    			}
    			//resultado += tbEliminados + " de Alojamiento eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
		{
    		
		}
    }
    
    /* ****************************************************************
	 * RF10 - REHABILITAR OFERTA DE ALOJAMIENTO
	 *****************************************************************/
    
    public void habilitarAlojamientoPorId() {
    	try 
    	{
    		String ID_OPERADOR = JOptionPane.showInputDialog (this, "Ingrese el ID_OPERADOR?", "Adicionar el ID_OPERADOR", JOptionPane.QUESTION_MESSAGE);
    		String CLAVE_OPERADOR = JOptionPane.showInputDialog (this, "Ingrese la CLAVE_OPERADOR?", "Adicionar la CLAVE_OPERADOR", JOptionPane.QUESTION_MESSAGE);

    		boolean verificacionOperador = alohandes.verificarOperador(ID_OPERADOR, CLAVE_OPERADOR);

    		if (!verificacionOperador){
    			throw new Exception ("El ID_OPERADOR o la CLAVE_OPERADOR son incorrectos");
    		}
    		
    		
    		String idAlojStr= JOptionPane.showInputDialog (this, "Id del alojamiento?", "Rehabilitar Alojamiento por Id", JOptionPane.QUESTION_MESSAGE);
    		
    		if (idAlojStr != null)
    		{
    			long idAloj = Long.valueOf (idAlojStr);
    			
        		boolean verificacionAlojamientoOperador = alohandes.verificarAlojamientoOperador(ID_OPERADOR, idAloj);

        		if (!verificacionAlojamientoOperador){
        			throw new Exception ("El Alojamiento no es del Operador");
        		}
    			
    			long activo = alohandes.rehabilitarAlojamientoPorId (idAloj);

    			String resultado = "En rehabilitar Alojamiento";
    			resultado += activo;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
		{
    		
		}
    }

	/* ****************************************************************
	 * 			REQUERIMIENTOS DE CONSULTA (RFC)
	 *****************************************************************/
	
    /* ****************************************************************
	 * RFC1: MOSTRAR EL DINERO RECIBIDO POR CADA 
	 * PROVEEDOR DE ALOJAMIENTO DURANTE EL AÑO ACTUAL 
	 * Y EL AÑO CORRIDO
	 *****************************************************************/
    
  
    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicación
     */

    public void listarOperador( )
    {
    	try 
    	{
			List <VOOperador> lista = alohandes.darVOOperadores();

			String resultado = "En listarOperador";
			resultado +=  "\n" + listarOperadores(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    private String listarOperadores(List<VOOperador> lista) 
    {
    	String resp = "Los operadores existentes son:\n";
    	int i = 1;
        for (VOOperador tb : lista)
        {
        	resp += i++ + ". " + tb.toStringDinero() + "\n";
        }
        return resp;
	}

    
    
    /* ****************************************************************
	 * 			Mostrar las 20 ofertas más populares (RFC2)
	 *****************************************************************/
    
    public void listarReservas( )
    {
    	try 
    	{
			List <VOReserva> lista = alohandes.darVO20();

			String resultado = "En listarTipoAlojamiento";
			resultado +=  "\n" + listarReservas (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    private String listarReservas(List<VOReserva> lista) 
    {
    	String resp = "Las TOP 20 ofertas en Alohandes son:\n";
    	int i = 1;
        for (VOReserva tb : lista)
        {
        	resp += i++ + "." + tb.toString() + "\n";
        }
        return resp;
	}
    

    
    public void listarTop20Oferta( )
    {
    	try 
    	{
    		List<Object[]> lista = alohandes.darTop20Ofertas();
			log.info(" Se obtuvo ya la lista de las Top 20 Ofertas");
			String resultado = "En listarTop20 Ofertas";
			resultado +=  "\n" + listarTop20Ofertas (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    private String listarTop20Ofertas(List<Object[]> lista) 
    {
    	String resp = "Las TOP 20 ofertas en Alohandes son:\n";
    	int i = 1;

        for (Object [] tupla : lista)
        {
			VOAlojamiento aloj = (VOAlojamiento) tupla [0];
			int numReservas = (int) tupla [1];
			
	        String resp1 = i++ + ". " + "[";
			resp1 += aloj.toStringTopOfertas() + ", ";
			resp1 += "numVisitas: " + numReservas;
	        resp1 += "]";
	        resp += resp1 + "\n";
        }

        return resp;
	}

    /* ****************************************************************
	 * RFC3: MOSTRAR EL ÍNDICE DE OCUPACIÓN DE CADA 
	 * UNA DE LAS OFERTAS DE ALOJAMIENTO REGISTRADAS
	 *****************************************************************/
    
  
    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicación
     */

    public void listarAlojamiento( )
    {
    	try 
    	{
			List <VOAlojamiento> lista = alohandes.darVOAlojamientos();

			String resultado = "En listarAlojamiento";
			resultado +=  "\n" + listarAlojamientos(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    private String listarAlojamientos(List<VOAlojamiento> lista) 
    {
    	String resp = "Los alojamientos existentes son:\n";
    	int i = 1;
        for (VOAlojamiento tb : lista)
        {
        	resp += i++ + ". " + tb.toStringIndice() + "\n";
        }
        return resp;
	}
    
    /* ****************************************************************
	 * Mostrar los alojamientos disponibles en un rango 
	 * de fechas, que cumplen con un conjunto de requerimientos de 
	 * dotación o servicios
       (RFC4)
	*****************************************************************/
    
    public void listarAlojamientoDisponible( )
    {
    	try 
    	{
    		// RANGO DE FECHAS: SELECCIONADAS POR EL USUARIO
    		String FECHAINICIO = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAINICIO?", "Adicionar la FECHAINICIO", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
    		String FECHAFINAL = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAFINAL?", "Adicionar la FECHAFINAL", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
    		
    		// SERVICIOS QUE LE GUSTARÍA TENER
    		ArrayList<String> INFO_SERVICIOS = new ArrayList<String>();
    		JList<String> listaOpciones = new JList<>(OPCIONES_TIPOSERVICIO);
            JScrollPane scrollPane = new JScrollPane(listaOpciones);

            int opcion = JOptionPane.showOptionDialog(null, scrollPane, "Selecciona las opciones que desees", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            
            if (opcion == JOptionPane.YES_OPTION) {    
            	List<String> opciones = listaOpciones.getSelectedValuesList();
            	INFO_SERVICIOS.addAll( opciones );
            }
    		
			List <VOAlojamiento> lista = alohandes.darAlojamientoPorFechaYServicios(FECHAINICIO,FECHAFINAL,INFO_SERVICIOS);

			String resultado = "En listarAlojamientosenRangoFechasYconciertosTipos";
			resultado +=  "\n" + listarAlojamientosDisponibles (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
    		e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    private String listarAlojamientosDisponibles(List<VOAlojamiento> lista) 
    {
    	String resp = "Los alojamientos disponibles son:\n";
    	int i = 1;
        for (VOAlojamiento tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /* ****************************************************************
	 * RFC5 - MOSTRAR EL USO DE ALOHANDES PARA CADA TIPO DE USUARIO DE LA COMUNIDAD
	*****************************************************************/
    public void mostrarUsoTodos() {
    	try {
    		String VINCULACION = (String) JOptionPane.showInputDialog(this, "Elija la vinculacion",
                    "Adicionar la vinculacion", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_VINCULACION, OPCIONES_VINCULACION[0]);
    		List <VOReserva> lista = alohandes.listarReservasTipoMiembroComunidad(VINCULACION);
    		
			String resultado = "En listarTipoAlojamiento";
			resultado +=  "\n" + listarReservasTipoMiembroComunidad (lista, VINCULACION);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
    	}
    	catch (Exception e)  {
    		e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    private String listarReservasTipoMiembroComunidad(List<VOReserva> lista, String vinculacion) 
    {
    	String resp = "El uso de ALOHANDES para "+ vinculacion +" es:\n";
    	int i = 1;
        for (VOReserva tb : lista)
        {
        	resp += i++ + "." + tb.toStringPorMiembro() + "\n";
        }
        return resp;
	}
    
    /* ****************************************************************
	 * MOSTRAR EL USO DE ALOHANDES PARA UN USUARIO DADO (NÚMERO DE NOCHES 
	 * O MESES CONTRATADOS,CARACTERÍSTICAS DEL ALOJAMIENTO UTILIZADO, 
	 * DINERO PAGADO. (RFC6)
	*****************************************************************/
    
    public void mostrarUsoUsuario() {
    	try {
    	
	    	String ID_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su cedula?", "Adicionar su cedula", JOptionPane.QUESTION_MESSAGE);
			String CLAVE_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su clave?", "Adicionar la clave", JOptionPane.QUESTION_MESSAGE);
			
			boolean verificacionCliente = alohandes.verificarCliente(ID_CLIENTE, CLAVE_CLIENTE);
			log.info("verificacion: "+verificacionCliente);
			
			if (!verificacionCliente){
				throw new Exception ("El ID_CLIENTE o la CLAVE_CLIENTE son incorrectos");
			}
			List<Object[]> lista = alohandes.darUsoUsuario(ID_CLIENTE);
	
			String resultado = "En mostrarUsoUsuario";
			resultado +=  "\n" + listarUsoUsuario (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
		
    }
    
    private String listarUsoUsuario(List<Object[]> lista) 
    {
    	String resp = "Su uso de ALOHANDES es:\n";
    	int i = 1;

        for (Object [] tupla : lista)
        {
	        String resp1 = i++ + ". " + "[";
			resp1 += "ALOJAMIENTO: " + tupla[0] +", ";
			resp1 += "UBICACION: " + tupla[1]+", ";
			resp1 += "AMOBLADO: " + tupla[2]+", ";
			resp1 += "PRECIO: " + tupla[3]+", ";
			resp1 += "CANTIDADPERSONAS: " + tupla[4]+", ";
			resp1 += "FECHAINICIO: " + tupla[5]+", ";
			resp1 += "FECHAFINAL: " + tupla[6]+", ";
			resp1 += "TOTALDIAS: " + tupla[7];
            
	        resp1 += "]";
	        resp += resp1 + "\n";
        }

        return resp;
	}
    
    /* ****************************************************************
	 * RFC7 - ANALIZAR LA OPERACIÓN DE ALOHANDES
	*****************************************************************/
    public void analizarOperacion() {
    	try {
    		String tipoAlojamiento = (String) JOptionPane.showInputDialog(this, "Elija el TIPOALOJAMIENTO",
                    "Elegir el TIPO ALOJAMIENTO", JOptionPane.QUESTION_MESSAGE, null, OPCIONES_TIPOALOJAMIENTO, OPCIONES_TIPOALOJAMIENTO[0]);
            
    		String fechaMayoresIngresos = alohandes.analizarMayoresIngresos(tipoAlojamiento);
    		String[] fechasOcupaciones = alohandes.analizarOcupaciones(tipoAlojamiento);
    		
			String resultado = "Analisis de operacion de Alohandes";
			resultado +=  "\n" + "La fecha de mayores ingresos: " + fechaMayoresIngresos;
			resultado += "\n" + "La fecha de mayor ocupacion: " + fechasOcupaciones[0];
			resultado += "\n" + "La fecha de menor ocupacion: " + fechasOcupaciones[1];
			resultado += "\nOperación terminada";
			panelDatos.actualizarInterfaz(resultado);
			
    	}
    	catch (Exception e) 
		{
    		
		}
    }
    
    /* ****************************************************************
	 * RFC8 - ENCONTRAR LOS CLIENTES FRECUENTES
	*****************************************************************/
    
    public void encontrarClienteFrecuente() {
    	try {
    		String ALOJAMIENTO = JOptionPane.showInputDialog (this, "Ingrese el ALOJAMIENTO?", "Adicionar el ALOJAMIENTO", JOptionPane.QUESTION_MESSAGE);
    		boolean verificacionAlojamiento = alohandes.verificarAlojamiento(ALOJAMIENTO);
    		log.info("verificacion: "+verificacionAlojamiento);
    		
    		if (!verificacionAlojamiento){
    			throw new Exception ("El alojamiento seleccionado es incorrecto");
    		}
    		List <VOCliente> lista = alohandes.listarClienteFrecuente(ALOJAMIENTO);
    		
			String resultado = "En listarClienteFrecuente";
			resultado +=  "\n" + listarClienteFrecuente (lista, ALOJAMIENTO);
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
    	}
    	catch (Exception e)  {
    		e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    private String listarClienteFrecuente(List<VOCliente> lista, String alojamiento) 
    {
    	String resp = "Los clientes mas frecuentes para el alojamiento "+ alojamiento +" son:\n";
    	int i = 1;
        for (VOCliente tb : lista)
        {
        	resp += i++ + "." + tb.toStringClienteFrecuente() + "\n";
        }
        return resp;
	}
    
    /* ****************************************************************
	 * RFC9 - ENCONTRAR LAS OFERTAS DE ALOJAMIENTO QUE NO TIENEN MUCHA DEMANDA
	*****************************************************************/
    public void encontrarOfertaPocaDemanda() {
    	try {
    		
    		List <VOAlojamiento> lista  = alohandes.listarOfertaPocaDemanda();
    		String resultado = "En listarOfertas Poca demanda";
			resultado +=  "\n" + listarOfertaPocaDemanda(lista);
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
    	}
    	catch (Exception e)  {
    		e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    private String listarOfertaPocaDemanda(List<VOAlojamiento> lista) 
    {
    	String resp = " Las ofertas con poca demanda son :\n";
    	int i = 1;
        for (VOAlojamiento tb : lista)
        {
        	resp += i++ + "." + tb.toString() + "\n";
        }
        return resp;
	}
    /* ****************************************************************
	 * RFC10 - Consultar Consumo - Alohandes
	*****************************************************************/
    public void consultarConsumo() {
    	try {
    		
    		String TIPOUSUARIO = (String) JOptionPane.showInputDialog(this, "Elija su tipo de usuario",
                    "Ver el tipo de usuario", JOptionPane.QUESTION_MESSAGE, null, USUARIO, USUARIO[0]);
    		
    		
    		if (TIPOUSUARIO.equals("ADMIN")) {
    			
    			String CLAVE_ADMIN = JOptionPane.showInputDialog (this, "Ingrese su clave?", "Adicionar la clave", JOptionPane.QUESTION_MESSAGE);
    			
    			boolean verificacionAdmin = CLAVE_ADMIN.equals(ADMIN[1]);
    			log.info("verificacion: "+verificacionAdmin);
    			
    			if (!verificacionAdmin){
    				throw new Exception ("La clave del ADMIN es incorrecta");
    			}
    			
    			String ALOJAMIENTO = JOptionPane.showInputDialog (this, "Ingrese el ALOJAMIENTO?", "Adicionar el ALOJAMIENTO", JOptionPane.QUESTION_MESSAGE);
        		boolean verificacionAlojamiento = alohandes.verificarAlojamiento(ALOJAMIENTO);
        		log.info("verificacion: "+verificacionAlojamiento);
        		
        		if (!verificacionAlojamiento){
        			throw new Exception ("El alojamiento seleccionado es incorrecto");
        		}
        		String FECHAINICIO = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAINICIO?", "Adicionar la FECHAINICIO", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
        		String FECHAFINAL = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAFINAL?", "Adicionar la FECHAFINAL", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
        		
        		String CRITERIO = (String) JOptionPane.showInputDialog(this, "Elija el criterio por el que desea ordenar la consulta",
                        "Ver el tipo de usuario", JOptionPane.QUESTION_MESSAGE, null, OPCIONESADMIN, OPCIONESADMIN[0]);
        		long start = System.currentTimeMillis();
        		List<Object[]> lista = alohandes.darConsumoUsuarios(FECHAINICIO,FECHAFINAL,CRITERIO,ALOJAMIENTO);

        		long finish = System.currentTimeMillis();
        		long timeElapsed = finish - start;
        		System.out.println(timeElapsed);
    			String resultado = "En consultarConsumo desde el punto de vista del ADMIN";
    			resultado +=  "\n El uso encontrado fue: " + listarConsultarConsumoUsuarios (lista);
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);

    		}
    		else {
    			
    			
    			String ID_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su cedula?", "Adicionar su cedula", JOptionPane.QUESTION_MESSAGE);
    			String CLAVE_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su clave?", "Adicionar la clave", JOptionPane.QUESTION_MESSAGE);
    			
    			boolean verificacionCliente = alohandes.verificarCliente(ID_CLIENTE, CLAVE_CLIENTE);
    			log.info("verificacion: "+verificacionCliente);
    			
    			if (!verificacionCliente){
    				throw new Exception ("El ID_CLIENTE o la CLAVE_CLIENTE son incorrectos");
    			}
    			
    			String ALOJAMIENTO = JOptionPane.showInputDialog (this, "Ingrese el ALOJAMIENTO?", "Adicionar el ALOJAMIENTO", JOptionPane.QUESTION_MESSAGE);
        		boolean verificacionAlojamiento = alohandes.verificarAlojamiento(ALOJAMIENTO);
        		log.info("verificacion: "+verificacionAlojamiento);
        		
        		if (!verificacionAlojamiento){
        			throw new Exception ("El alojamiento seleccionado es incorrecto");
        		}
        		
        		String FECHAINICIO = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAINICIO?", "Adicionar la FECHAINICIO", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
        		String FECHAFINAL = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAFINAL?", "Adicionar la FECHAFINAL", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
        		
        		String CRITERIO = (String) JOptionPane.showInputDialog(this, "Elija el criterio por el que desea ordenar la consulta",
                        "Ver el tipo de usuario", JOptionPane.QUESTION_MESSAGE, null, OPCIONESCLIENTE, OPCIONESCLIENTE[0]);
        		
        		
        		long start = System.currentTimeMillis();
        		
        		List <VOReserva> lista = alohandes.darConsumoUsuario(FECHAINICIO,FECHAFINAL,CRITERIO,ALOJAMIENTO,ID_CLIENTE);

        		long finish = System.currentTimeMillis();
        		long timeElapsed = finish - start;
        		System.out.println(timeElapsed);
        		
        		
        		
    			String resultado = "En mostrar el consumo para un cliente particular";
    			resultado +=  "\n" + listarConsultarConsumoUsuario (lista,ID_CLIENTE);
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    			
    	
    		}
    	
    	}
    	catch (Exception e)  {
    		e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    private String listarConsultarConsumoUsuario(List<VOReserva> lista, String iDCLIENTE) 
    {
    	String resp = "El uso de ALOHANDES para "+ iDCLIENTE +" es:\n";
    	int i = 1;
        for (VOReserva tb : lista)
        {
        	resp += i++ + "." + tb.toStringPorMiembro() + "\n";
        }
        return resp;
	}
    
    private String listarConsultarConsumoUsuarios(List<Object[]> lista) 
    {
    	String resp = " \n";
    	int i = 1;

        for (Object [] tupla : lista)
        {
	        String resp1 = i++ + ". " + "[";
			resp1 += "CEDULA: " + tupla[0] +", ";
			resp1 += "NUMRESERVAS: " + tupla[1]+", ";
			resp1 += "NOMBRE: " + tupla[2]+", ";
			resp1 += "VINCULACION: " + tupla[3]+", ";
			resp1 += "CORREO: " + tupla[4];
            
	        resp1 += "]";
	        resp += resp1 + "\n";
        }

        return resp;
	}
    
    /* ****************************************************************
	 * RFC11 - Consultar Consumo - Alohandes - V2
	*****************************************************************/
    
    public void consultarConsumo2() {
    	
	try {
    		
    		String TIPOUSUARIO = (String) JOptionPane.showInputDialog(this, "Elija su tipo de usuario",
                    "Ver el tipo de usuario", JOptionPane.QUESTION_MESSAGE, null, USUARIO, USUARIO[0]);
    		
    		
    		if (TIPOUSUARIO.equals("ADMIN")) {
    			
    			String CLAVE_ADMIN = JOptionPane.showInputDialog (this, "Ingrese su clave?", "Adicionar la clave", JOptionPane.QUESTION_MESSAGE);
    			
    			boolean verificacionAdmin = CLAVE_ADMIN.equals(ADMIN[1]);
    			log.info("verificacion: "+verificacionAdmin);
    			
    			if (!verificacionAdmin){
    				throw new Exception ("La clave del ADMIN es incorrecta");
    			}
    			
    			String ALOJAMIENTO = JOptionPane.showInputDialog (this, "Ingrese el ALOJAMIENTO?", "Adicionar el ALOJAMIENTO", JOptionPane.QUESTION_MESSAGE);
        		boolean verificacionAlojamiento = alohandes.verificarAlojamiento(ALOJAMIENTO);
        		log.info("verificacion: "+verificacionAlojamiento);
        		
        		if (!verificacionAlojamiento){
        			throw new Exception ("El alojamiento seleccionado es incorrecto");
        		}
        		String FECHAINICIO = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAINICIO?", "Adicionar la FECHAINICIO", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
        		String FECHAFINAL = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAFINAL?", "Adicionar la FECHAFINAL", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
        		
        		String CRITERIO = (String) JOptionPane.showInputDialog(this, "Elija el criterio por el que desea ordenar la consulta",
                        "Ver el tipo de usuario", JOptionPane.QUESTION_MESSAGE, null, OPCIONESADMIN, OPCIONESADMIN[0]);
        		long start = System.currentTimeMillis();
        		List<Object[]> lista = alohandes.darConsumoUsuarios2(FECHAINICIO,FECHAFINAL,CRITERIO,ALOJAMIENTO);

        		long finish = System.currentTimeMillis();
        		long timeElapsed = finish - start;
        		System.out.println(timeElapsed);
    			String resultado = "En consultarConsumo desde el punto de vista del ADMIN";
    			resultado +=  "\n El uso encontrado fue: " + listarConsultarConsumoUsuarios (lista);
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);

    		}
    		else {
    			
    			
    			String ID_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su cedula?", "Adicionar su cedula", JOptionPane.QUESTION_MESSAGE);
    			String CLAVE_CLIENTE = JOptionPane.showInputDialog (this, "Ingrese su clave?", "Adicionar la clave", JOptionPane.QUESTION_MESSAGE);
    			
    			boolean verificacionCliente = alohandes.verificarCliente(ID_CLIENTE, CLAVE_CLIENTE);
    			log.info("verificacion: "+verificacionCliente);
    			
    			if (!verificacionCliente){
    				throw new Exception ("El ID_CLIENTE o la CLAVE_CLIENTE son incorrectos");
    			}
    			
    			String ALOJAMIENTO = JOptionPane.showInputDialog (this, "Ingrese el ALOJAMIENTO?", "Adicionar el ALOJAMIENTO", JOptionPane.QUESTION_MESSAGE);
        		boolean verificacionAlojamiento = alohandes.verificarAlojamiento(ALOJAMIENTO);
        		log.info("verificacion: "+verificacionAlojamiento);
        		
        		if (!verificacionAlojamiento){
        			throw new Exception ("El alojamiento seleccionado es incorrecto");
        		}
        		
        		String FECHAINICIO = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAINICIO?", "Adicionar la FECHAINICIO", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
        		String FECHAFINAL = (String) JOptionPane.showInputDialog(this, "Ingrese la FECHAFINAL?", "Adicionar la FECHAFINAL", JOptionPane.QUESTION_MESSAGE, null, null, "yyyy-mm-dd"); 
        		
        		String CRITERIO = (String) JOptionPane.showInputDialog(this, "Elija el criterio por el que desea ordenar la consulta",
                        "Ver el tipo de usuario", JOptionPane.QUESTION_MESSAGE, null, OPCIONESCLIENTE, OPCIONESCLIENTE[0]);
        		
        		
        		long start = System.currentTimeMillis();
        		
        		List <VOReserva> lista = alohandes.darConsumoUsuario(FECHAINICIO,FECHAFINAL,CRITERIO,ALOJAMIENTO,ID_CLIENTE);

        		long finish = System.currentTimeMillis();
        		long timeElapsed = finish - start;
        		System.out.println(timeElapsed);
        		
    			String resultado = "En mostrar el consumo para un cliente particular";
    			if (lista.isEmpty()) {
    				resultado += "\nEl cliente con ID=" + ID_CLIENTE + " hizo ninguna reserva en el alojamiento y fechas indicadas.";
    			}
    			else {
    				resultado += "\nEl cliente con ID=" + ID_CLIENTE + " hizo al menos una reserva en el alojamiento y fechas indicadas. Ver RFC10.";
    			}
    			resultado += "\nOperación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    			
    	
    		}
    	
    	}
    	catch (Exception e)  {
    		e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    
    /* ****************************************************************
	 * RFC12 - Consultar Funcionamiento
	*****************************************************************/
    
    public void consultarFuncionamiento() {
    	try {
    	
    		String CLAVE_CEO = JOptionPane.showInputDialog (this, "Ingrese su clave: CEO ?", "Adicionar la clave del CEO", JOptionPane.QUESTION_MESSAGE);
			
			boolean verificacionCEO = CLAVE_CEO.equals(CEO[1]);
			log.info("verificacion: "+verificacionCEO);
			
			if (!verificacionCEO){
				throw new Exception ("La clave del ADMIN es incorrecta");
			}
			String ANIO = JOptionPane.showInputDialog (this, "Ingrese su anio de interes?", "Anio para hacer la consulta", JOptionPane.QUESTION_MESSAGE);
			
			
    		long start = System.currentTimeMillis();

    		List<Object[]> listaMasOcupacionAlojamiento = alohandes.darConsultarFuncionamiento(ANIO);
			List<Object[]> listaMenosOcupacionAlojamiento = alohandes.darConsultarFuncionamientoMenos(ANIO);
			List<Object[]> listaOperadorMasSolicitado = alohandes.darOperadorMasSolicitado(ANIO);
			List<Object[]> listaOperadorMenosSolicitado = alohandes.darOperadorMenosSolicitado(ANIO);
			
    		long finish = System.currentTimeMillis();
    		long timeElapsed = finish - start;
    		System.out.println(timeElapsed);
    		
			
			String resultado = "En consultar funcionamiento Alohandes";
			resultado +=  "\n Las OFERTAS de ALOJAMIENTO con MÁS OCUPACIÓN (xSEMANA) SON:" + listarFuncionamiento (listaMasOcupacionAlojamiento);
			resultado +=  "\n Las OFERTAS de ALOJAMIENTO con MENOS OCUPACIÓN (xSEMANA) SON:" + listarFuncionamiento (listaMenosOcupacionAlojamiento);
			resultado +=  "\n Los OPERADORES MÁS SOLICITADOS (xSEMANA) SON:" + listarFuncionamientoOperador (listaOperadorMasSolicitado);
			resultado +=  "\n Los OPERADORES MENOS SOLICITADOS (xSEMANA) SON:" + listarFuncionamientoOperador (listaOperadorMenosSolicitado);
			
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
		
    }
    
    private String listarFuncionamiento(List<Object[]> lista) 
    {
    	String resp = "\n";

        for (Object [] tupla : lista)
        {
	        String resp1 = "Semana " +tupla[7] + ": " + tupla[8] + " - " + tupla[9]+ ". " + "[";
			resp1 += "ALOJAMIENTO: " + tupla[0] +", ";
			resp1 += "UBICACION: " + tupla[1]+", ";
			resp1 += "AMOBLADO: " + tupla[2]+", ";
			resp1 += "PRECIO: " + tupla[3]+", ";
			resp1 += "CANTIDADPERSONAS: " + tupla[4]+", ";
			resp1 += "NUMHABITACIONES: " + tupla[5]+", ";
			resp1 += "INDICEOCUPACION: " + tupla[6]+", ";
			resp1 += "OPERADOR: " + tupla[10];
            
	        resp1 += "]";
	        resp += resp1 + "\n";
        }

        return resp;
	}
    
    private String listarFuncionamientoOperador(List<Object[]> lista) 
    {
    	String resp = "\n";

        for (Object [] tupla : lista)
        {
	        String resp1 = "Semana " +tupla[5] + ": " + tupla[6] + " - " + tupla[7]+ ". " + "[";
			resp1 += "OPERADOR: " + tupla[0] +", ";
			resp1 += "INGRESOACTUAL: " + tupla[1]+", ";
			resp1 += "INGRESOCORRIDO: " + tupla[2]+", ";
			resp1 += "NOMBRE: " + tupla[3]+", ";
			resp1 += "CORREO: " + tupla[4];
            
	        resp1 += "]";
	        resp += resp1 + "\n";
        }

        return resp;
	}
    
    /* ****************************************************************
	 * RFC13 - Consultar Buenos Clientes
	*****************************************************************/
    
    public void consultarBuenosClientes() {
    	

    	try {
    	
    		String CLAVE_CEO = JOptionPane.showInputDialog (this, "Ingrese su clave: CEO ?", "Adicionar la clave del CEO", JOptionPane.QUESTION_MESSAGE);
			
			boolean verificacionCEO = CLAVE_CEO.equals(CEO[1]);
			log.info("verificacion: "+verificacionCEO);
			
			if (!verificacionCEO){
				throw new Exception ("La clave del ADMIN es incorrecta");
			}	
			
			
    		long start = System.currentTimeMillis();

			List<VOCliente> listaBuenosClientes1 = alohandes.darBuenosClientes1();
			List<VOCliente> listaBuenosClientes2 = alohandes.darBuenosClientes2();
			List<VOCliente> listaBuenosClientes3 = alohandes.darBuenosClientes3();

    		long finish = System.currentTimeMillis();
    		long timeElapsed = finish - start;
    		System.out.println(timeElapsed);
    		
    		
			String resultado = "En consultar buenos clientes Alohandes";
			resultado +=  "\n Los buenos clientes por reservar al menos una vez al mes son: " + listarBuenosClientes(listaBuenosClientes1);
			resultado +=  "\n Los buenos clientes por siempre reservar alojamientos costosos son: " + listarBuenosClientes(listaBuenosClientes2);
			resultado +=  "\n Los buenos clientes por siempre reservar suites son: " + listarBuenosClientes(listaBuenosClientes3);
			
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
		
    }
    	
    private String listarBuenosClientes(List<VOCliente> lista) 
    {
    	String resp = "\n";
    	int i = 1;
        for (VOCliente tb : lista)
        {
        	resp += i++ + "." + tb.toString() + "\n";
        }
        return resp;
	}
    
	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Parranderos
	 */
	public void mostrarLogParranderos ()
	{
		mostrarArchivo ("parranderos.log");
	}
	
	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}
	
	/**
	 * Limpia el contenido del log de parranderos
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogParranderos ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("parranderos.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de parranderos ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia todas las tuplas de todas las tablas de la base de datos de parranderos
	 * Muestra en el panel de datos el número de tuplas eliminadas de cada tabla
	 */
	public void limpiarBD ()
	{
		try 
		{
			//TODO: COMPLETAR!!!!!!
    		// Ejecución de la demo y recolección de los resultados
			long eliminados [] = alohandes.limpiarParranderos();
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " Gustan eliminados\n";
			resultado += eliminados [1] + " Sirven eliminados\n";
			resultado += eliminados [2] + " Visitan eliminados\n";
			resultado += eliminados [3] + " Alojamientos eliminadas\n";
			resultado += eliminados [4] + " Tipos de Alojamiento eliminados\n";
			resultado += eliminados [5] + " Bebedores eliminados\n";
			resultado += eliminados [6] + " Bares eliminados\n";
			resultado += "\nLimpieza terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	/**
	 * Muestra la presentación general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}
	
	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}
	
	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}
	
	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}
	
	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	/**
	 * Muestra la documentación Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}
	
	/**
     * Muestra la información acerca del desarrollo de esta apicación
     */
    public void acercaDe ()
    {
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Parranderos Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Germán Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia Jiménez, Christian Ariza\n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
    }
    

	/* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/
    /**
     * Genera una cadena de caracteres con la lista de los tipos de Alojamiento recibida: una línea por cada tipo de Alojamiento
     * @param lista - La lista con los tipos de Alojamiento
     * @return La cadena con una líea para cada tipo de Alojamiento recibido
     */
    private String listarTiposAlojamiento(List<VOTipoAlojamiento> lista) 
    {
    	String resp = "Los tipos de alojamientos existentes son:\n";
    	int i = 1;
        for (VOTipoAlojamiento tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
     * @param e - La excepción recibida
     * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
     */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
    /**
     * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
     * Invoca al método correspondiente según el evento recibido
     * @param pEvento - El evento del usuario
     */
    @Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazAlohandesApp.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		} 
	}
    
	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
    /**
     * Este método ejecuta la aplicación, creando una nueva interfaz
     * @param args Arreglo de argumentos que se recibe por línea de comandos
     */
    public static void main( String[] args )
    {
        try
        {
        	
            // Unifica la interfaz para Mac y para Windows.
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazAlohandesApp interfaz = new InterfazAlohandesApp( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
