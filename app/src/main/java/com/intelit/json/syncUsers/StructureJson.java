
package com.intelit.json.syncUsers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class StructureJson {

    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private List<SyncUser> results = new ArrayList<SyncUser>();

    @JsonProperty("msj")
    private String msj;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SyncUser> getResults() {
        return results;
    }

    public void setResults(List<SyncUser> results) {
        this.results = results;
    }

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }
}
