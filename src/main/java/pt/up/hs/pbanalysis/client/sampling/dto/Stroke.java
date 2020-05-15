package pt.up.hs.pbanalysis.client.sampling.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Stroke.
 *
 * @author José Carlos Paiva
 */
public class Stroke implements Serializable {

    private Long startTime;
    private Long endTime;

    private List<Dot> dots = new ArrayList<>();

    public Stroke() {
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public List<Dot> getDots() {
        return dots;
    }

    public void setDots(List<Dot> dots) {
        this.dots = dots;
    }

}
