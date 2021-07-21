package ch.slsp.InitialLoadUserStaff.rest.nestedClasses.user_role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Parameter {

    private Parameter_Type type;

    private Parameter_Scope scope;

    private Parameter_Value value;


}
