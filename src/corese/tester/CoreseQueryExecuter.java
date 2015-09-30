package corese.tester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import corese.query.MemoryMeasurer;
import corese.query.Query;
import corese.query.RuntimeMeasurer;

public class CoreseQueryExecuter {

	private Model model;
	
	public CoreseQueryExecuter(String rdfPath) {
		model = ModelFactory.createDefaultModel();
		try {
			model.read(new FileInputStream(new File(rdfPath)), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public List<Execution> execute(Query query, int numExecutions){
		List<Execution> executions = new ArrayList<Execution>(numExecutions);
		Execution execution;
		int i;
		
		for(i=0; i<numExecutions; i++){
			System.out.printf("Esecuzione %d\n", (i+1));
			RuntimeMeasurer runtimeMeasurer = new RuntimeMeasurer(query);
			MemoryMeasurer memoryMeasurer = new MemoryMeasurer(runtimeMeasurer);
			memoryMeasurer.execute(model);
			
			execution = new Execution();
			execution.setExecutionTime(runtimeMeasurer.getExecutionTime());
			execution.setUsedMemory(memoryMeasurer.getUsedMemory());
			execution.setQueryResults(query.getResults());
			executions.add(execution);
		}
		
		return executions;
	}
}
