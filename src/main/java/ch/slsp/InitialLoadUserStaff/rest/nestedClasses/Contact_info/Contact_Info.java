package ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Contact_info;

import com.google.gson.JsonArray;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
public class Contact_Info {

    List<Address> addressList;
    List<Email> email;
    //List<Phone> phonse;
}