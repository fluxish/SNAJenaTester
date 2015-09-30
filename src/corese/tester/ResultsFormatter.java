package corese.tester;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultsFormatter {
	
	private List<Execution> executions;
	
	public ResultsFormatter(List<Execution> executions){
		this.executions = executions;
	}
	
	public void output(PrintStream out){
		outputPerformances(executions, out);
		if(!executions.isEmpty()){
			outputQueryResults(executions.get(0).getQueryResults(), out);
		}
	}
	
	private void outputQueryResults(List<MapResult> results, PrintStream out){
		if(!results.isEmpty()){
			Set<String> fields = results.get(0).keySet();
			for(String field : fields){
				out.printf("%s\t", field);
			}
			out.print("\n");
		}
		
		for(Map<String, String> result : results){
			for(String value : result.values()){
				out.printf("%s\t", value);
			}
			out.print("\n");
		}
		out.print("\n");
	}
	
	private void outputPerformances(List<Execution> executions, PrintStream out){
		int i, n = executions.size();
		Execution execution;
		out.println("Performances");
		out.printf("#Executions\t%d\n\n", executions.size());
		out.println("#\tTime(ms)\tMem(b)");
		
		for(i=0; i<n; i++){
			execution = executions.get(i);
			out.printf("#%d\t%s\t%s\n", i+1, execution.getExecutionTime(), execution.getUsedMemory());
		}
	}
}
