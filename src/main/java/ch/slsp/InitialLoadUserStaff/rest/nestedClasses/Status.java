package ch.slsp.InitialLoadUserStaff.rest.nestedClasses;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Status {
    /**
     * Status value
     * EG. "ACTIVE"
     */

    private String value;
}
