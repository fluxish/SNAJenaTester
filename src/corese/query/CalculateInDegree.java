package corese.query;

import java.util.ArrayList;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import corese.tester.MapResult;

public class CalculateInDegree extends Query implements JenaQuery {
	
	public static final String QUERY_NAME = "inDegree";
	private String propertyPath;
	private String propertyName;
	private int numNodes;
	
	public CalculateInDegree(String propertyPath, String propertyName, int numNodes){
		this.preparedQuery = this.query = "prefix foaf:<http://xmlns.com/foaf/0.1/>"
				+ "select ?yNick ?indegree where{"
				+ "select ?yNick (count(?x)/param[norm] as ?indegree) "
				+ "where { "
				+ "?x param[rel] ?y . "
				+ "?y param[propName] ?yNick ."
				+ "}group by ?yNick"
				+ "}order by desc(?indegree) asc(?yNick)";
		
		this.propertyPath = propertyPath;
		this.propertyName = propertyName;
		this.numNodes = numNodes;
		this.preparedQuery = this.query
			.replace("param[rel]", propertyPath)
			.replace("param[propName]", propertyName)
			.replace("param[norm]", Integer.toString((numNodes-1)));
	}
	
	@Override
	public void execute(Model model) {
		this.results = new ArrayList<MapResult>();
		QueryExecution qe = QueryExecutionFactory.create(preparedQuery, model);
		ResultSet rs = qe.execSelect();
		
		QuerySolution qs;
		MapResult result;
		String yNick, indegree;
		while(rs.hasNext()) {
			qs = rs.next();
			yNick = qs.getLiteral("yNick").getString();
			indegree = qs.getLiteral("indegree").getString();
			result = new MapResult();
			result.put("yNick", yNick);
			result.put("inDegree", indegree);
			this.results.add(result);
		}
		qe.close();
	}
	
	public String getPropertyPath() {
		return propertyPath;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public int getNumNodes() {
		return numNodes;
	}
	
	public static final String QUERY_PARAMETER_PROPERTYPATH = "propertyPath";
	public static final String QUERY_PARAMETER_PROPERTYNAME = "propertyName";
	public static final String QUERY_PARAMETER_NUMNODES = "numNodes";
}
