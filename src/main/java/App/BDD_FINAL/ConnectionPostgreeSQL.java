package App.BDD_FINAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;





public class ConnectionPostgreeSQL extends ConnectionAbstract {

	private Connection conn;
	private Statement stmt;
	private ResultSet rs;

	//parameters of the connection
	private String url = "";
	private String user = "postgres";
	private String password = "alfaomega";

	public ConnectionPostgreeSQL(String opt){
		super(opt);
		conn=null;
		stmt=null;
		rs=null;



		try {
			//we set our url according the boolean opt

			if(opt == "Optimizado") {
				url = "jdbc:postgresql://localhost/bdb_si_opti";
			}else {
				url = "jdbc:postgresql://localhost/bdb_no_opti";
			}
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to the PostgreSQL server successfully."); 
		}catch(SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}




	}

	public void MakeQuery(String search, String category) {

		Search mysearch = new Search(search, category);
		String myquery = mysearch.SimpleSearch();
		/*
		 * Procedimiento típico para realizar consultas. Se crea un Statement a partir de la conexión. Se ejecuta una consulta
		 * en el Statement mediante el método .executeQuery(...). La consulta la habremos elaborado previamente a través de la clase Search.
		 * Si la consulta se ejecuta correctamente, guardamos el resultado en rs que es del tipo ResultSet. Tanto stmt con rs están declarados
		 * como atributos privados de la clase.		
		 */
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(myquery);

			if (stmt.execute(myquery)) {
				rs = stmt.getResultSet();
			}

		}catch(SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public boolean isTheQueryMade() {
		return rs != null;
	}

	public String readQueryResult() {
		String result = "";
		if(!isTheQueryMade()) {
			throw new RuntimeException("No se ha realizado ninguna consulta");
		}
		try {
			while ( rs.next() ) {
				int numColumns = rs.getMetaData().getColumnCount();
				for ( int i = 1 ; i <= numColumns ; i++ ) {
					result += rs.getObject(i) + "\t";
				}
				result += "\n";
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}	
		return result;
	}


}