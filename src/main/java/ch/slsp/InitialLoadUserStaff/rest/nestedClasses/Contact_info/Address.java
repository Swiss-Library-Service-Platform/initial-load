package ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Contact_info;

import com.google.gson.JsonArray;
import lombok.Getter;
import lombok.Setter;

/***
 * not finished
 */

@Getter @Setter
public class Address {

    private Boolean preferred;
    private String segment_type;
    private String line1;
    private String line2;
    private String line3;
    private String line5;
    private String city;
    private String state_province;
    private String postal_code;
    private JsonArray address_type;


    private class country {

        private String value;
    }

}