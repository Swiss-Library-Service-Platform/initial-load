package ch.slsp.InitialLoadUserStaff.rest.createFine;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"primary_id", "message"})
public class CreateFineResponse {

    @JsonProperty("primary_id")
    private String primary_id;

    @JsonProperty("message")
    private String message;

}
