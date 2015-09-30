package corese.query;

import java.util.List;

import corese.tester.MapResult;


public abstract class Query implements JenaQuery {
	
	protected String query;
	protected String preparedQuery;
	protected List<MapResult> results;
	
	public String getQuery(){
		return query;
	}
	
	public String getPreparedQuery(){
		return preparedQuery;
	}

	public List<MapResult> getResults() {
		return results;
	}
}
