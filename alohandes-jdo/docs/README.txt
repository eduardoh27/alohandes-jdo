Grupo A-09
Lina María Gómez Mesa, Eduardo José Herrera Alba


Archivo README con las instrucciones de ejecución de la aplicación transaccional.


1. Abra el proyecto de Java en el IDE de su preferencia. Algunas recomendaciones son Eclipse o VS Code.
2. Asegúrese de cambiar la información necesaria en PERSISTENCIA.XML
3. Ejecute los archivos de EsquemaAlohandes.sql para cargar las tablas y PoblamientoAlohandes.sql para poblar la base de datos en SQL Developer.
3. Ejecute el archivo InterfazAlohandesApp.java, que es el que tiene el método main.
4. Aparecerá la interfaz y podrá elegir ejecutar el RF de su interés. 
5. Utilice los botones en el panel superior de la aplicación para realizar las operaciones que desee.
6. Al realizar una operación puede suceder que la aplicación le solicite el id de un operador o cliente en conjunto
con su clave. Estos se encuentran en POBLAMIENTO ALOHANDES. Algunos ejemplos de ellos son: 


-> OPERADOR
"ID","INGRESOPORANIOACTUAL","INGRESOPORANIOCORRIDO","NOMBRE","CLAVE"
1,6611170,8740869,"Yule Roslen","MRtQFKEEl0CK"


-> CLIENTE
"CEDULA","CORREO","NOMBRE","CELULAR","VINCULACION","CLAVE"
1,"gaburrow0@mysql.com","Greggory Aburrow",3615164114,1,"GmPSHFF7"


En el archivo de datos de Poblamiento SQL puede encontrar cuáles son los usuarios que están registrados en la base de datos de la aplicación, junto con sus correos y contraseñas, para que utilice esas credenciales y acceda a la aplicación.
Recuerde acceder con un usuario que tenga el rol adecuado para la operación que desea efectuar.