package ch.slsp.InitialLoadUserStaff.rest.addRole;

import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.simple.JSONArray;

@Getter @Setter
public class addRoleRequest {
    private String primary_id;
    private String iz;
    private String role;
}
