package gr.bc.api.util;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public class ExtractionBundle {
    
    private String attributes;
    private List<Object> values;

    public ExtractionBundle(String attributes, List<Object> values) {
        this.attributes = attributes;
        this.values = values;
    }
    
    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
    
}
