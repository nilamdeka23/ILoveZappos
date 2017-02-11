package project.nilam.com.ilovezappos.model;

import java.util.ArrayList;
import java.util.List;


public class ResultSet {

    private String originalTerm;
    private int currentResultCount;
    private long totalResultCount;
    private String term;
    private List<Product> results = new ArrayList<Product>();

    public List<Product> getResults() {
        return results;
    }

    public ResultSet() {
    }

}
