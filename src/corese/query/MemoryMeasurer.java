package corese.query;

import com.hp.hpl.jena.rdf.model.Model;

public class MemoryMeasurer implements QueryMeasurer {
	
	private JenaQuery query;
	private long usedMemory;
	
	public MemoryMeasurer(JenaQuery query) {
		this.query = query;
	}

	@Override
	public void execute(Model model) {
		Runtime runtime = Runtime.getRuntime();
	    runtime.gc();
	    long preMemory = runtime.totalMemory() - runtime.freeMemory();
		query.execute(model);
		long postMemory = runtime.totalMemory() - runtime.freeMemory();
	    usedMemory = (postMemory - preMemory);
	}
	
	public long getUsedMemory(){
		return usedMemory;
	}
}
