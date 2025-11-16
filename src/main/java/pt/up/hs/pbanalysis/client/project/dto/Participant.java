package pt.up.hs.pbanalysis.client.project.dto;

public class Participant {
    private Long id;

    /**
     * Name of the participant
     */
    private String name;

    /**
     * A participant belongs to a project.
     */
    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
