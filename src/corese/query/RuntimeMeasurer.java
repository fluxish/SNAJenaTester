package corese.query;

import com.hp.hpl.jena.rdf.model.Model;

public class RuntimeMeasurer implements QueryMeasurer {
	
	private JenaQuery query;
	private long executionTime;
	
	public RuntimeMeasurer(JenaQuery query) {
		this.query = query;
	}

	@Override
	public void execute(Model model) {
		long time = System.currentTimeMillis();
		query.execute(model);
		executionTime = System.currentTimeMillis() - time;
	}
	
	public long getExecutionTime(){
		return executionTime;
	}
}
