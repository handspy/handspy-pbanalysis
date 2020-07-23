package pt.up.hs.pbanalysis.client.sampling.dto;

/**
 * Envelope wrapping handwritten data collected using a smartpen for analysis
 * (part of the sample).
 */
public class Protocol {

    private Long id;

    /**
     * ID of the project (from Projects microservice).
     */
    private Long projectId;

    /**
     * Task (from the Project Microservice) to which this protocol was written.
     */
    private Long taskId;

    /**
     * Participant (from the Project Microservice) who wrote this protocol.
     */
    private Long participantId;

    /**
     * Number of the page (if the protocol contains multiple pages)
     */
    private Integer pageNumber;

    /**
     * Language of the protocol.
     */
    private String language;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
