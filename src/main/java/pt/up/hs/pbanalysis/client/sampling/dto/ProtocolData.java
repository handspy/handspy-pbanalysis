package pt.up.hs.pbanalysis.client.sampling.dto;

import java.io.Serializable;
import java.util.List;

/**
 * The Protocol's data.
 *
 * @author José Carlos Paiva
 */
public class ProtocolData implements Serializable {

    private Double width;
    private Double height;

    private List<Stroke> strokes;

    public ProtocolData() {
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public List<Stroke> getStrokes() {
        return strokes;
    }

    public void setStrokes(List<Stroke> strokes) {
        this.strokes = strokes;
    }
}
