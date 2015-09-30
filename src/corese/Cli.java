package corese;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import corese.tester.CoreseTester;

public class Cli {
	private static final Logger log = Logger.getLogger(Cli.class.getName());
	private String[] args = null;
	private Options options = new Options();
	
	private String path;
	private String[] queryNames = new String[]{};
	private int numExecutions = 1;
	private Map<String, String> parameters = new HashMap<String, String>();

	public Cli(String[] args) {

		this.args = args;

		options.addOption("h", "help", false, "Mostra questa guida");
		options.addOption("f", "fileName", true, "Imposta il path di un file RDF su cui calcolare gli indici di centralità");
		options.addOption("q", "queries", true, "Lista di query da eseguire, i cui nomi devono essere separati da virgola (diameter,countEdges,inDegree,outDegree,inCloseness,outCloseness,betweenness)");
		options.addOption("p", "parameters", true, "coppie chiave=valore di parametri per le query (propertyPath=value1,propertyName=value2,numNodes=value3,diameter=velue4)");
		options.addOption("n", "numExecutions", true, "Imposta il numero di esecuzioni complessivo");
	}

	public void parse() {
		CommandLineParser parser = new BasicParser();

		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);

			if (cmd.hasOption("h")){
				help();
			}
			
			if (cmd.hasOption("n")){
				this.numExecutions = Integer.parseInt(cmd.getOptionValue("n"));
			}

			if (cmd.hasOption("f")) {
				this.path = cmd.getOptionValue("f");
			} else {
				log.log(Level.SEVERE, "Manca il parametro f");
				help();
			}
			
			if (cmd.hasOption("q")) {
				setQueryNames(cmd.getOptionValue("q"));
			} else {
				log.log(Level.SEVERE, "Manca il parametro q");
				help();
			}
			
			if (cmd.hasOption("q")) {
				setParameters(cmd.getOptionValue("p"));
			} else {
				log.log(Level.SEVERE, "Manca il parametro p");
				help();
			}
			
			CoreseTester coreseTester = new CoreseTester(
					this.path,
					this.queryNames,
					this.numExecutions,
					this.parameters
				);
			coreseTester.execute();

		} catch (ParseException e) {
			log.log(Level.SEVERE, "Failed to parse comand line properties", e);
			help();
		}
	}

	private void help() {
		// This prints out some help
		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp("Main", options);
		System.exit(0);
	}
	
	private void setQueryNames(String str){
		this.queryNames = str.split(",");
	}
	
	private void setParameters(String str){
		String[] pairs = str.split(",");
		String[] pair;
		for(String pairStr : pairs){
			pair = pairStr.split("=");
			this.parameters.put(pair[0], pair[1]);
		}
	}
	
}