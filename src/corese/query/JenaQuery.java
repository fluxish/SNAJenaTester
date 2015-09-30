package corese.query;

import com.hp.hpl.jena.rdf.model.Model;

public interface JenaQuery {
	public void execute(Model model);
}
