package ch.slsp.InitialLoadUserStaff.rest.createUser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"primary_id", "message"})
public class CreateUserResponse {

    /**
     *
     * Response Body in JSON Format
     * Lookup:
     */

    @JsonProperty("primary_id")
    private String primary_id;

    @JsonProperty("message")
    private String message;

}
