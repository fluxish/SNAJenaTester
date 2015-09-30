package corese.tester;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.SizeFileComparator;

import corese.query.CalculateDiameter;
import corese.query.CalculateInDegree;
import corese.query.CalculateOutDegree;
import corese.query.CountEdges;
import corese.query.GetEdges;
import corese.query.Query;

public class CoreseTester {
	
	private String path;
	private String[] queryNames;
	private int numExecutions;
	private Map<String, String> parameters;
	
	public CoreseTester(String path, String[] queryNames, int numExecutions, Map<String, String> parameters) {
		this.path = path;
		this.queryNames = queryNames;
		this.numExecutions = numExecutions;
		this.parameters = parameters;
	}
	
	public void execute(){
		File file = new File(path);
		if(file.isDirectory()){
			executeOnDirectory(file);			
		} else {
			executeOnFile(file);
		}
	}
	
	private void executeOnFile(File rdfFile){
		List<Execution> executions = new ArrayList<Execution>(0);
		ResultsFormatter formatter;
		String resultOutputPrefix;
		String filePath = rdfFile.getAbsolutePath();
		for(String queryName : queryNames){
			System.out.printf("Esecuzione: %s su rete %s\n", queryName, filePath);
			
			try{
				executions = executeQuery(queryName, filePath, numExecutions);
				
			} catch (Exception e){
				e.printStackTrace();
			} finally {
				try {
					resultOutputPrefix = FilenameUtils.removeExtension(filePath);
					PrintStream ps = new PrintStream(new File(resultOutputPrefix + "_" + queryName + OUTPUT_FILE_POSTFIX));
					formatter = new ResultsFormatter(executions);
					formatter.output(ps);
					ps.flush();
					ps.close();
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println("Fatto!");
		}
	}
	
	private void executeOnDirectory(File dir){
		File[] rdfFiles = getRDFFiles(dir);
		List<Execution> executions;
		ResultsFormatter formatter;
		String[] nameParams;
		String resultOutputPrefix;
		String filePath;
		
		Arrays.sort(rdfFiles, SizeFileComparator.SIZE_COMPARATOR);
		
		for(File rdfFile : rdfFiles){
			for(String queryName : queryNames){
				filePath = rdfFile.getAbsolutePath();
				
				System.out.printf("Esecuzione: %s su rete %s\n", queryName, filePath);
				
				nameParams = FilenameUtils.removeExtension(rdfFile.getName()).split("_");
				parameters.put("diameter", nameParams[2]);
				executions = executeQuery(queryName, rdfFile.getAbsolutePath(), numExecutions);
				resultOutputPrefix = FilenameUtils.removeExtension(filePath);
				try {
					PrintStream ps = new PrintStream(new File(resultOutputPrefix + "_" + queryName + OUTPUT_FILE_POSTFIX));
					formatter = new ResultsFormatter(executions);
					formatter.output(ps);
					ps.flush();
					ps.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				System.out.println("Fatto!");
			}
		}
	}
	
	private List<Execution> executeQuery(String queryName, String rdfFileName, int numExecutions){
		CoreseQueryExecuter queryExecuter = new CoreseQueryExecuter(rdfFileName);
		
		Query query = null;
		if(queryName.equals(GetEdges.QUERY_NAME)){
			query = new GetEdges(parameters.get("propertyPath"));
		}
		if(queryName.equals(CalculateDiameter.QUERY_NAME)){
			query = new CalculateDiameter(parameters.get("propertyPath"), Integer.parseInt(parameters.get("numNodes")));
		}
		else if(queryName.equals(CountEdges.QUERY_NAME)){
			query = new CountEdges(parameters.get("propertyPath"));
		}
		else if(queryName.equals(CalculateInDegree.QUERY_NAME)){
			query = new CalculateInDegree(parameters.get("propertyPath"), parameters.get("propertyName"), Integer.parseInt(parameters.get("numNodes")));
		}
		else if(queryName.equals(CalculateOutDegree.QUERY_NAME)){
			query = new CalculateOutDegree(parameters.get("propertyPath"), parameters.get("propertyName"), Integer.parseInt(parameters.get("numNodes")));
		}
		
		return queryExecuter.execute(query, numExecutions);
	}
	
	public static File[] getRDFFiles(File dir) {
	    return dir.listFiles(new FilenameFilter() {
	        @Override
	        public boolean accept(File dir, String name) {
	            return name.toLowerCase().endsWith(".rdf");
	        }
	    });
	}
	
	private static final String OUTPUT_FILE_POSTFIX = "_results.txt";
}
