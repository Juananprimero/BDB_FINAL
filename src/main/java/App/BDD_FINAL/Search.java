package App.BDD_FINAL;



/*
 *  Esta clase se encarga de elaborar las consultas y devolverlas a modo de String
 */
public class Search {
	private String search;	//Corresponde al texto que introducimos nosotrosal hacer la búsqueda
	private String category;	//Relativo a la tabla en la que buscamos(All, Gene, Transcript o Translation)


	public Search(String s, String cat) {	//Constructor de la clase.
		search = s;
		category = cat;
	}
	/*
	 * Este método elabora una consulta simple.
	 */
	public String SimpleSearch() {		

		String result = null;
		if(category == "Gene") {
			result = "SELECT stable_id, description FROM gene "
					+ "WHERE (stable_id LIKE '"+search+"') OR (description LIKE"
					+ " '%"+search+"%');";
		}else if(category == "Transcript") {
			result = "SELECT stable_id, description FROM transcript WHERE "
					+"(description LIKE '%"+search+"%') OR (stable_id LIKE '"
					+ search + "');";
		}else if(category == "Translation") {
			result = "SELECT stable_id, transcript_id FROM translation WHERE "
					+"(stable_id LIKE '"+ search + "');";
		}else {
			result = "(SELECT stable_id FROM gene "
					+ "WHERE (stable_id LIKE '"+search+"') OR (description LIKE"
					+ " '%"+search+"%')) UNION "
					+ "(SELECT stable_id FROM transcript WHERE "
					+"(description LIKE '%"+search+"%') OR (stable_id LIKE '"
					+ search + "')) UNION "
					+ "(SELECT stable_id FROM translation WHERE "
					+"(stable_id LIKE '"+ search + "'));";
		}
		return result;		
	}	

	/*	public String ExtendedSearch() {
		res =
		return res; 
	}
	 */	
}