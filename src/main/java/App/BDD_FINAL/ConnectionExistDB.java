package App.BDD_FINAL;


import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.xmldb.api.*;
import org.exist.xmldb.EXistResource;
import org.exist.xmldb.XmldbURI;

public class ConnectionExistDB {

	private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
	private String driver = "org.exist.xmldb.DatabaseImpl";
	private Collection col;

	public ConnectionExistDB(){
		// initialize database driver
		try{
			@SuppressWarnings("rawtypes")
			Class cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			database.setProperty("create-database", "true");
			DatabaseManager.registerDatabase(database);
			col = null;
		}catch(ClassNotFoundException e){

		} catch (InstantiationException i){

		} catch(IllegalAccessException y){

		} catch(XMLDBException t){

		}
	}

	public String MakeQuery(String search, String category){
		if(category.equals("All")){
			String result= MakeQueryAll(search);
			return result;
		}else{
			String res_final = "";
			try { 
				/*String myquery = "for $x in doc(\"/db/bdb_opti_xml.xml\")//table_data[@name = \"gene\"]/row" +
			"where ($x/@name = \"stable_id\" and $x=\"algo\")or ($x/field/@name = \"description\" and contains($x,\"R3H\"))"+
			"return ($x/field[@name=\"stable_id\"],$x/field[@name=\"description\"])";
				 */
				String myquery ="";
				if(category.equals("Gene")){
					myquery = "for $x in doc(\"/db/bdb_opti_xml.xml\")//table_data[@name = \"gene\"]/row where ($x/field/@name = \"stable_id\" and contains($x,\""+search+"\"))or ($x/field/@name = \"description\" and contains($x,\""+search+"ENSAMEG00000003002\")) return ($x/field[@name=\"stable_id\"],$x/field[@name=\"description\"])";
				}
				else if(category.equalsIgnoreCase("Transcript")){
					myquery = "for $x in doc(\"/db/bdb_opti_xml.xml\")//table_data[@name = \"transcript\"]/row where ($x/field/@name = \"stable_id\" and contains($x,\""+search+"\"))or ($x/field/@name = \"description\" and contains($x,\""+search+"ENSAMEG00000003002\")) return ($x/field[@name=\"stable_id\"],$x/field[@name=\"description\"])";
				}else if(category.equalsIgnoreCase("Translation")){
					myquery = "for $x in doc(\"/db/bdb_opti_xml.xml\")//table_data[@name = \"translation\"]/row where ($x/field/@name = \"stable_id\" and contains($x,\""+search+"\"))return ($x/field[@name=\"stable_id\"],$x/field[@name=\"transcript_id\"])";
				}

				col = DatabaseManager.getCollection(URI + XmldbURI.ROOT_COLLECTION,"admin","alfaomega");
				XPathQueryService xpqs = (XPathQueryService)col.getService("XPathQueryService", "1.0");
				xpqs.setProperty("indent", "yes");

				ResourceSet result = xpqs.query(myquery);
				ResourceIterator i = result.getIterator();
				Resource res = null;

				while(i.hasMoreResources()) {
					try {
						res = i.nextResource();
						//System.out.println(res.getContent());
						res_final = res_final + res.getContent() + "\n";
					} finally {
						//dont forget to cleanup resources

						try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
					}
				}
			} catch (XMLDBException e){
				System.out.print("No es posible realizar la consulta");
			} finally {
				//dont forget to cleanup

				if(col != null) {
					try { col.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
				}
			}
			return res_final;
		}

	}
	public String MakeQueryAll(String search){
		String result = MakeQuery(search,"Gene")+MakeQuery(search,"Transcript")+MakeQuery(search,"Translation");
		return result;
	}
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
		ConnectionExistDB conection = new ConnectionExistDB();
		String hola =conection.MakeQuery("R3H", "Gene");
		System.out.println(hola);
	}


}
