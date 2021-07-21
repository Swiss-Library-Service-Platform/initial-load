package ch.slsp.InitialLoadUserStaff.rest.createFine;

import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Barcode;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Owner;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Status;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CreateFineRequest {
    private String link;
    private Type type;
    private Status status;
    private String original_amount;
    private String comment;
    private Owner owner;
    private Barcode barcode;

}
