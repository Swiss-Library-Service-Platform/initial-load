package ch.slsp.InitialLoadUserStaff.rest.nestedClasses.user_note;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class User_Note {

    private Note_type note_type;

    private String note_text;

    private String created_by;

    private String created_date;
}
