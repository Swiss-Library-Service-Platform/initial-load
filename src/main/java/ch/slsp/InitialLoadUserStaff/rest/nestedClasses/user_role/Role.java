package ch.slsp.InitialLoadUserStaff.rest.nestedClasses.user_role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class Role {

//    private String status;
    private Role_Status status;

//    private String scope;
    private Role_Scope scope;

//    private String role_type;
    private Role_Role_type role_type;

//    private String expiry_date;
    private String expiry_date;

    List<Parameter> parameter;

}
