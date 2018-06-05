package App.BDD_FINAL;

public abstract class ConnectionAbstract {
	
	//Indica si la consulta es optimizada o no	
	private String opt;					
	
	
	//Constructor de la clase
	public ConnectionAbstract(String opt){		
		this.opt = opt;						
	}
	
	//Realiza una consulta
	public abstract void MakeQuery(String search, String category);	
	
	//Combrueba si la consulta ha devuelto un resultado
	public abstract boolean isTheQueryMade();
	
	public abstract String readQueryResult();
	
}