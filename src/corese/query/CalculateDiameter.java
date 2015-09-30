package corese.query;

import java.util.ArrayList;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import corese.tester.MapResult;

public class CalculateDiameter extends Query implements JenaQuery {
	
	private String propertyPath;
	private int numNodes;
	
	public CalculateDiameter(String propertyPath, int numNodes){
		this.query = "prefix foaf:<http://xmlns.com/foaf/0.1/> "
			+ "select (max(?minLength) as ?max) "
			+ "where{select ?from ?to (min(pathLength($path)) as ?minLength) "
			+ "where{"
			+ "filter(?from != ?to) . "
			+ "?from s param[rel]{,param[numNodes]} :: $path ?to . "
			+ "} "
			+ "group by ?from ?to}";
		this.propertyPath = propertyPath;
		this.numNodes = numNodes;
		this.preparedQuery = this.query.replace("param[rel]", propertyPath).replace("param[numNodes]", Integer.toString(numNodes));
	}
	
	@Override
	public void execute(Model model) {
		this.results = new ArrayList<MapResult>();
		QueryExecution qe = QueryExecutionFactory.create(preparedQuery, model);
		ResultSet rs = qe.execSelect();
		QuerySolution qs;
		MapResult result;
		if(rs.hasNext()) {
			qs = rs.next();
			String max = qs.getLiteral("max").getString();
			result = new MapResult();
			result.put("distance", max);
			this.results.add(result);
		}
		qe.close();
	}
	
	public String getPropertyPath() {
		return propertyPath;
	}
	
	public int getNumNodes(){
		return numNodes;
	}
	
	public static final String QUERY_NAME = "diameter";
	public static final String QUERY_PARAMETER_PROPERTYPATH = "propertyPath";
	public static final String QUERY_PARAMETER_DIAMETER = "numNodes";
}
