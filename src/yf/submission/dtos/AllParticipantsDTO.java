package yf.submission.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AllParticipantsDTO implements Serializable {

    private List<SubmissionParticipantDTO> hairStylists;
    private List<SubmissionParticipantDTO> mds;
    private List<SubmissionParticipantDTO> muas;
    private List<SubmissionParticipantDTO> phs;
    private List<SubmissionParticipantDTO> setDesigner;
    private List<SubmissionParticipantDTO> wardrobeStylists;

    public AllParticipantsDTO() {
        hairStylists = new ArrayList<>();
        mds = new ArrayList<>();
        muas = new ArrayList<>();
        phs = new ArrayList<>();
        setDesigner = new ArrayList<>();
        wardrobeStylists = new ArrayList<>();
    }

    public List<SubmissionParticipantDTO> getHairStylists() {
        return hairStylists;
    }

    public void setHairStylists(List<SubmissionParticipantDTO> hairStylists) {
        this.hairStylists = hairStylists;
    }

    public List<SubmissionParticipantDTO> getMds() {
        return mds;
    }

    public void setMds(List<SubmissionParticipantDTO> mds) {
        this.mds = mds;
    }

    public List<SubmissionParticipantDTO> getMuas() {
        return muas;
    }

    public void setMuas(List<SubmissionParticipantDTO> muas) {
        this.muas = muas;
    }

    public List<SubmissionParticipantDTO> getPhs() {
        return phs;
    }

    public void setPhs(List<SubmissionParticipantDTO> phs) {
        this.phs = phs;
    }

    public List<SubmissionParticipantDTO> getSetDesigner() {
        return setDesigner;
    }

    public void setSetDesigner(List<SubmissionParticipantDTO> setDesigner) {
        this.setDesigner = setDesigner;
    }

    public List<SubmissionParticipantDTO> getWardrobeStylists() {
        return wardrobeStylists;
    }

    public void setWardrobeStylists(List<SubmissionParticipantDTO> wardrobeStylists) {
        this.wardrobeStylists = wardrobeStylists;
    }
}
