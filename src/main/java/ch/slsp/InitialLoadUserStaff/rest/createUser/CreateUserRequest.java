package ch.slsp.InitialLoadUserStaff.rest.createUser;

import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.*;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Contact_info.Contact_Info;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.user_role.User_role;
import com.google.gson.JsonArray;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.simple.JSONArray;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

@Getter @Setter @ToString
public class CreateUserRequest {
    private String link;
    private Record_type record_type;
    private String primary_id;
    private String first_name;
    private String middle_name;
    private String last_name;
    private Job_category job_category;
    private Gender gender;
    private User_group user_group;
    private Campus_Code campus_code;
    private Cataloger_level cataloger_level;
    private Preferred_language preferred_language;
    private Account_Type account_type;
    private String password;
    private String force_password_change;
    private Status status;
    private Contact_Info contact_info ;
    private JSONArray user_role;
    private JSONArray user_note;

}


