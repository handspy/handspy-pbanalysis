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

    private Long id;
    private Long protocolId;
    private Long startTime;
    private Long endTime;

    private List<Dot> dots = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
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
