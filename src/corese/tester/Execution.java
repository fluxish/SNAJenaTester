package corese.tester;

import java.util.ArrayList;
import java.util.List;

public class Execution {
	private String title;
	private String description;
	private long executionTime;
	private long usedMemory;
	private List<MapResult> queryResults = new ArrayList<MapResult>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public List<MapResult> getQueryResults() {
		return queryResults;
	}

	public void setQueryResults(List<MapResult> queryResults) {
		this.queryResults = queryResults;
	}
}
