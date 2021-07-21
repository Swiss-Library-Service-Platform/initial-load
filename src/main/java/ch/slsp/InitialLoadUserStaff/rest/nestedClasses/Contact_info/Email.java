package ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Contact_info;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter @Setter @ToString
public class Email {

    private String preferred;

    private String segment_type;

    private String email_address;

    private String description;

    private List<EmailType> email_type;
}
