package pt.up.hs.pbanalysis.client.sampling.dto;

import java.io.Serializable;

/**
 * A Dot.
 *
 * @author José Carlos Paiva
 */
public class Dot implements Serializable {

    private Long timestamp;
    private Double x;
    private Double y;
    private DotType type;
    private Double pressure;

    public Dot() {
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public DotType getType() {
        return type;
    }

    public void setType(DotType type) {
        this.type = type;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }
}
