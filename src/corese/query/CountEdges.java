package corese.query;

import java.util.ArrayList;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import corese.tester.MapResult;

public class CountEdges extends Query implements JenaQuery {
	
	public static final String QUERY_NAME = "countEdges";
	private String propertyPath;
	
	public CountEdges(String propertyPath){
		this.query = "prefix foaf:<http://xmlns.com/foaf/0.1/>"
			+ "select distinct ?x ?y (count(?x) as ?count) "
			+ "where { ?x param[rel] ?y }";
		this.propertyPath = propertyPath;
		this.preparedQuery = this.query.replace("param[rel]", propertyPath);
		this.results = new ArrayList<MapResult>();
	}
	
	@Override
	public void execute(Model model) {
		QueryExecution qe = QueryExecutionFactory.create(preparedQuery, model);
		ResultSet rs = qe.execSelect();
		QuerySolution qs;
		MapResult result;
		if(rs.hasNext()) {
			qs = rs.next();
			String count = qs.getLiteral("count").getString();
			result = new MapResult();
			result.put("distance", count);
			this.results.add(result);
		}
		qe.close();
	}

	public String getPropertyPath() {
		return propertyPath;
	}
}
