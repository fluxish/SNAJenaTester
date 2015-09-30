package corese.query;

import java.util.ArrayList;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import corese.tester.MapResult;

public class GetEdges extends Query implements JenaQuery {
	
	public static final String QUERY_NAME = "getEdges";
	private String propertyPath;
	
	public GetEdges(String propertyPath){
		this.query = "prefix foaf:<http://xmlns.com/foaf/0.1/>"
			+ "select distinct ?x ?y "
			+ "where { "
			+ "?x param[rel] ?y "			
			+ "}";
		this.propertyPath = propertyPath;
		this.preparedQuery = this.query.replace("param[rel]", propertyPath);
		this.results = new ArrayList<MapResult>(1);
	}
	
	@Override
	public void execute(Model model) {	
		QueryExecution qe = QueryExecutionFactory.create(preparedQuery, model);
		ResultSet rs = qe.execSelect();
		
		QuerySolution qs;
		MapResult result;
		String x, y;
		if(rs.hasNext()) {
			qs = rs.next();
			x = qs.getLiteral("x").getString();
			y = qs.getLiteral("y").getString();
			result = new MapResult();
			result.put("x", x);
			result.put("y", y);
			this.results.add(result);
		}
		qe.close();
	}

	public String getPropertyPath() {
		return propertyPath;
	}
}
