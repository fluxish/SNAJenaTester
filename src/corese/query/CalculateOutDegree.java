package corese.query;

import java.util.ArrayList;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import corese.tester.MapResult;

public class CalculateOutDegree extends Query implements JenaQuery {
	
	public static final String QUERY_NAME = "outDegree";
	private String propertyPath;
	private String propertyName;
	private int numNodes;
	
	public CalculateOutDegree(String propertyPath, String propertyName, int numNodes){
		this.preparedQuery = this.query = "prefix foaf:<http://xmlns.com/foaf/0.1/>"
				+ "select ?xNick ?outdegree where{"
				+ "select ?xNick (count(?y)/param[norm] as ?outdegree) "
				+ "where { "
				+ "?x param[propName] ?xNick . "
				+ "?x param[rel] ?y . "
				+ "}group by ?xNick"
				+ "}order by desc(?outdegree)";
		
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
		String xNick, outdegree;
		while(rs.hasNext()) {
			qs = rs.next();
			xNick = qs.getLiteral("xNick").getString();
			outdegree = qs.getLiteral("outdegree").getString();
			result = new MapResult();
			result.put("xNick", xNick);
			result.put("outdegree", outdegree);
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
