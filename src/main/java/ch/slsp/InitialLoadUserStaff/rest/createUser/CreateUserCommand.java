package ch.slsp.InitialLoadUserStaff.rest.createUser;

import ch.slsp.InitialLoadUserStaff.rest.Command;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.*;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Contact_info.Contact_Info;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Contact_info.Email;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Contact_info.EmailType;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.user_note.Note_type;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.user_note.User_Note;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.user_role.*;
import ch.slsp.InitialLoadUserStaff.util.LoggerFactory;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import sun.rmi.runtime.Log;

@Component
@Profile("createUser")
public class CreateUserCommand implements Command {

    private static final Logger LOG = LoggerFactory.getLogger();

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss.SSSS");
    private final SimpleDateFormat sdfF = new SimpleDateFormat("dd.MM.yyyy-HH.mm");
    @Autowired
    private RestTemplate restTemplate;

    @Value("${rest.api.baseURL}")
    private String baseURL;

    @Value("${rest.api.createUser.auth.key.network}")
    private String apikeyNETWORK;
    @Value("${rest.api.createUser.auth.key.uge}")
    private String apikeyUGE;
    @Value("${rest.api.createUser.auth.key.eth}")
    private String apikeyETH;
    @Value("${rest.api.createUser.auth.key.ubs}")
    private String apikeyUBS;
    @Value("${rest.api.createUser.auth.key.rzs}")
    private String apikeyRZS;
    @Value("${rest.api.createUser.auth.key.hsg}")
    private String apikeyHSG;
    @Value("${rest.api.createUser.auth.key.usi}")
    private String apikeyUSI;
    @Value("${rest.api.createUser.auth.key.uzb}")
    private String apikeyUZB;
    @Value("${rest.api.createUser.auth.key.bcufr}")
    private String apikeyBCUFR;
    @Value("${rest.api.createUser.auth.key.zhaw}")
    private String apikeyZAW;
    @Value("${rest.api.createUser.auth.key.ube}")
    private String apikeyUBE;
    @Value("${rest.api.createUser.auth.key.zhdk}")
    private String apikeyZHK;
    @Value("${rest.api.createUser.auth.key.bfh}")
    private String apikeyBFH;
    @Value("${rest.api.createUser.auth.key.hes}")
    private String apikeyHES;
    @Value("${rest.api.createUser.auth.key.fho}")
    private String apikeyFHO;
    @Value("${rest.api.createUser.auth.key.epf}")
    private String apikeyEPF;
    @Value("${rest.api.createUser.auth.key.une}")
    private String apikeyUNE;
    @Value("${rest.api.createUser.auth.key.fhnw}")
    private String apikeyFNW;
    @Value("${rest.api.createUser.auth.key.supsi}")
    private String apikeySUPSI;
    @Value("${rest.api.createUser.auth.key.heph}")
    private String apikeyHEPH;
    @Value("${rest.api.createUser.auth.key.phz}")
    private String apikeyPHZ;
    @Value("${rest.api.createUser.auth.key.lib4ri}")
    private String apikeyLIB;
    @Value("${rest.api.createUser.auth.key.iheid}")
    private String apikeyIID;
    @Value("${rest.api.createUser.auth.key.vge}")
    private String apikeyVGE;
    @Value("${rest.api.createUser.auth.key.rro}")
    private String apikeyRRO;
    @Value("${rest.api.createUser.auth.key.rzh}")
    private String apikeyRZH;
    @Value("${rest.api.createUser.auth.key.rbe}")
    private String apikeyRBE;
    @Value("${rest.api.createUser.auth.key.zbs}")
    private String apikeyZBS;
    @Value("${rest.api.createUser.auth.key.sbk}")
    private String apikeySBK;
    @Value("${rest.api.createUser.auth.key.tri}")
    private String apikeyTRI;
    @Value("${rest.api.createUser.auth.key.isr}")
    private String apikeyISR;
    @Value("${rest.api.createUser.auth.key.sand_ubs}")
    private String apikeySandUBS;
    @Value("${rest.api.createUser.auth.key.sand_hph}")
    private String apikeySandHPH;
    @Value("${rest.api.createUser.auth.key.sand_network}")
    private String apikeySandNETWORK;



    @Value("${rest.api.createUser.sleeptimer}")
    private Integer sleep;

    @Value("${rest.api.createUser.input}")
    private String inputFolder;

    @Override
    public void execute(String... args) throws Exception {

        //
        Object[] files = Files.walk(Paths.get(String.valueOf(inputFolder)))
                .filter(Files::isRegularFile)
                .toArray();

        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].toString());
            Path csvInputFile = (Path) files[i];
            String output = files[i].toString().replace("input", "output")
                    .replace(".csv", "");
            Path csvOutputAccepted = Paths.get(output + "Acc" + "-" + sdfF.format(new Date()) + ".csv");
            Path csvOutputUnaccepted = Paths.get(output + "Unacc" + "-" + sdfF.format(new Date()) + ".csv");
            Path csvOutputAll = Paths.get(output + "All" + "-" + sdfF.format(new Date()) + ".csv");
            try (CSVParser csvReader = new CSVParser(Files.newBufferedReader(csvInputFile), csvInputFormatConfiguration());
                 CSVPrinter csvWriterAll =
                         new CSVPrinter(new FileWriter(csvOutputAll.toFile()), csvOutputFormatConfiguration());
                 CSVPrinter csvWriterAccepted =
                         new CSVPrinter(new FileWriter(csvOutputAccepted.toFile()), csvOutputFormatConfiguration());
                 CSVPrinter csvWriterUnaccepted =
                         new CSVPrinter(new FileWriter(csvOutputUnaccepted.toFile()), csvOutputFormatConfiguration());
            ) {
                LOG.info("Input-File: " + csvInputFile);
                for (CSVRecord csvRecord : csvReader) {

                    //get properties from the csv-file
                    final String primary_id = csvRecord.get("primary_id");
                    final String last_name = csvRecord.get("last_name");
                    final String first_name = csvRecord.get("first_name");
                    final String email = csvRecord.get("e-mail");
                    final String preferred_language = csvRecord.get("preferred_language");
                    final String profile = csvRecord.get("profile");
                    String iz = csvRecord.get("IZ");
                    final String scope = csvRecord.get("scope");
                    final String circulation_desk = csvRecord.get("circulation_desk");
                    final String service_unit = csvRecord.get("service_unit");
                    final String cataloger_level = csvRecord.get("cataloger_level");

                    try {
                        LOG.info("User: " + primary_id);
                        String query = "?social_authentication=false&send_pin_number_letter=false&apikey=";
                        if (iz.equals("NETWORK")) {
                            query += apikeyNETWORK;
                        } else if (iz.equals("UGE")) {
                            query += apikeyUGE;
                        } else if (iz.equals("ETH")) {
                            query += apikeyETH;
                        } else if (iz.equals("UBS")) {
                            query += apikeyUBS;
                        } else if (iz.equals("RZS")) {
                            query += apikeyRZS;
                        } else if (iz.equals("HSG")) {
                            query += apikeyHSG;
                        } else if (iz.equals("USI")) {
                            query += apikeyUSI;
                        } else if (iz.equals("UZB")) {
                            query += apikeyUZB;
                        } else if (iz.equals("BCU")) {
                            query += apikeyBCUFR;
                        } else if (iz.equals("BCUFR")) {
                            query += apikeyBCUFR;
                        } else if (iz.equals("ZHAW")) {
                            iz = "ZAW";
                            query += apikeyZAW;
                        } else if (iz.equals("ZAW")) {
                            query += apikeyZAW;
                        } else if (iz.equals("UBE")) {
                            query += apikeyUBE;
                        } else if (iz.equals("ZHK")) {
                            iz = "ZHK";
                            query += apikeyZHK;
                        } else if (iz.equals("BFH")) {
                            query += apikeyBFH;
                        } else if (iz.equals("HES")) {
                            query += apikeyHES;
                        } else if (iz.equals("FHO")) {
                            query += apikeyFHO;
                        } else if (iz.equals("EPF")) {
                            query += apikeyEPF;
                        } else if (iz.equals("UNE")) {
                            query += apikeyUNE;
                        } else if (iz.equals("FNW")) {
                            query += apikeyFNW;
                        } else if (iz.equals("SUP")) {
                            iz = "SUP";
                            query += apikeySUPSI;
                        } else if (iz.equals("HPH")) {
                            iz = "HPH";
                            query += apikeyHEPH;
                        } else if (iz.equals("PHZ")) {
                            query += apikeyPHZ;
                        } else if (iz.equals("LIB")) {
                            iz = "LIB";
                            query += apikeyLIB;
                        } else if (iz.contains("IID")) {
                            iz = "IID";
                            query += apikeyIID;
                        } else if (iz.equals("VGE")) {
                            query += apikeyVGE;
                        } else if (iz.equals("RRO")) {
                            query += apikeyRRO;
                        } else if (iz.equals("RZH")) {
                            query += apikeyRZH;
                        } else if (iz.equals("RBE")) {
                            query += apikeyRBE;
                        } else if (iz.equals("ZBS")) {
                            query += apikeyZBS;
                        } else if (iz.equals("SBK")) {
                            query += apikeySBK;
                        } else if (iz.equals("TRI")) {
                            query += apikeyTRI;
                        } else if (iz.equals("ISR")) {
                            query += apikeyISR;
                        } else if (iz.equals("Sandbox_UBS")) {
                            query += apikeySandUBS;
                        } else if (iz.equals("Sandbox_HPH")) {
                            query += apikeySandHPH;
                        } else if (iz.equals("Sandbox_NETWORK")) {
                            query += apikeySandNETWORK;
                        }

                        CreateUserRequest request = createUserRequest(primary_id, last_name, first_name, email,
                                preferred_language, profile, iz, scope, circulation_desk, service_unit, cataloger_level);

                        String endpointURL = baseURL + "/almaws/v1/users" + query;

                        ResponseEntity<CreateUserResponse> createUserResponse = restTemplate.postForEntity(endpointURL,
                                request, CreateUserResponse.class);
                        if (createUserResponse.getStatusCodeValue() == 200) {
                            writeToFiles(csvWriterAccepted, primary_id, createUserResponse.getBody().toString());
                            writeToFiles(csvWriterAll, primary_id, createUserResponse.getBody().toString());
                        }

                        LOG.info("successfully created User: " + primary_id + "\n");
                        TimeUnit.MILLISECONDS.sleep(sleep);
                    } catch (HttpClientErrorException | HttpServerErrorException e) {
                        writeToFiles(csvWriterUnaccepted, primary_id, e.getLocalizedMessage());
                        writeToFiles(csvWriterAll, primary_id, e.getLocalizedMessage());
                        LOG.error(e.getMessage());
                        LOG.error("Create User: " + primary_id + " failed\n");
                    }
                }
            }

        }
    }

    private CreateUserRequest createUserRequest(String primary_id, String last_name, String first_name,
                                                String emails, String preferred_language, String profiles,
                                                String iz, String scopes, String circulation_desks,
                                                String service_units, String cataloger_level) {

        String[] email = emails.split("!");
        String[] profile = profiles.split("!");
        String[] scope = scopes.split("!");
        String[] circulation_desk = circulation_desks.split("!");
        String[] service_unit = service_units.split("!");

        CreateUserRequest request = new CreateUserRequest();
        Record_type record_type = new Record_type();
        record_type.setValue("STAFF");
        request.setRecord_type(record_type);

        request.setPrimary_id(primary_id);
        request.setFirst_name(first_name);
        request.setLast_name(last_name);

        Job_category job_category = new Job_category();
        job_category.setValue(" ");
        request.setJob_category(job_category);

        if (iz.contains("Sandbox_")) {
            iz = iz.replace("Sandbox_", "");
        }

        User_group user_group = new User_group();
        user_group.setValue(" ");
        request.setUser_group(user_group);

        Cataloger_level cata_level = new Cataloger_level();
        if (cataloger_level.isEmpty()) {
            if ((profiles.contains("slsp-acq;") || ((profiles.contains("slsp-acq!") || profiles.contains("slsp-acq-plus;")
                    || profiles.contains("slsp-acq-plus!")) && !profiles.contains("slsp-cat")))

                    || (profiles.contains("slsp-cat-admin") && !(profiles.contains("slsp-acq;") || profiles.contains("slsp-acq-plus;")
                    || profiles.contains("slsp-acq!") || profiles.contains("slsp-acq-plus!")
                    || profiles.contains("slsp-cat-plus!") || profiles.contains("slsp-cat!") || profiles.contains("slsp-cat-inventory!")
                    || profiles.contains("slsp-cat-plus;") || profiles.contains("slsp-cat;") || profiles.contains("slsp-cat-inventory;")))
            ) {
                cataloger_level = "20";
            }
        }
        cata_level.setValue(cataloger_level);
        request.setCataloger_level(cata_level);

        Preferred_language pref_language = new Preferred_language();
        pref_language.setValue(preferred_language);
        request.setPreferred_language(pref_language);

        Account_Type account_type = new Account_Type();
        account_type.setValue("INTERNAL");
        request.setAccount_type(account_type);

        //String password = "12345678a";
        String password = "";
        for (int i = 7; i >= 0; i--) {
            password += primary_id.charAt(i);
        }
        request.setPassword(password);

        request.setForce_password_change("FALSE");

        Status status = new Status();
        status.setValue("ACTIVE");
        request.setStatus(status);

        Contact_Info contact_info = new Contact_Info();
        contact_info.setEmail(createEmailJson(email));
        request.setContact_info(contact_info);

        List<Role> user_roles = createUserRoles(profile, iz, scope, circulation_desk, service_unit);

        JSONArray ArrayRoles = new JSONArray();
        ArrayRoles.addAll(user_roles);
        request.setUser_role(ArrayRoles);

        User_Note user_note = new User_Note();
        user_note.setCreated_by("INITIAL_LOAD");
        Note_type note_type = new Note_type();
        note_type.setValue("OTHER");
        user_note.setNote_type(note_type);
        String text = profiles + ";" + scopes + ";" + circulation_desks + ";" + service_units + ";" + cataloger_level;
        text = text.replace("!", ",");
        user_note.setNote_text(text);
        JSONArray arrayNotes = new JSONArray();
        arrayNotes.add(user_note);
        request.setUser_note(arrayNotes);

        return request;
    }

    private List<Email> createEmailJson(String[] emails) {
        List<Email> emailList = new ArrayList<>();

        for (int i = 0; i < emails.length; i++) {
            Email email = new Email();
            if (i == 0) {
                email.setPreferred("true");
            } else {
                email.setPreferred("false");
            }
            email.setSegment_type("Internal");
            email.setEmail_address(emails[i]);
            List<EmailType> email_type = new ArrayList<>();
            EmailType emailType = new EmailType();
            emailType.setValue("work");
            email_type.add(emailType);
            email.setEmail_type(email_type);
            emailList.add(email);

        }
        return emailList;
    }

    private List<Role> createUserRoles(String[] profiles, String iz, String[] scopes,
                                       String[] circulation_desk, String[] service_unit) {
        List<Role> user_roles = new ArrayList<>();
        if (iz.contains("41SLSP_")) {
            iz = iz.replace("41SLSP_", "");
        }
        String defaultScope = "41SLSP_" + iz;
        if (iz.contains("ISR")) {
            defaultScope = "41SLSP_RRO";
        }

        for (int i = 0; i < profiles.length; i++) {
            switch (profiles[i]) {
                case "slsp-acq":
                    LOG.info("Profile: SLSP-ACQ");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Cataloger
                        user_roles.add(createRole("204", defaultScope, null));
                        //Collection Inventory Operator
                        user_roles.add(createRole("228", scopes[j], null));
                        //Deposit Manager
                        user_roles.add(createRole("0", scopes[j], null));
                        //Deposit Operator
                        user_roles.add(createRole("1", scopes[j], null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Physical Inventory Operator
                        user_roles.add(createRole("210", scopes[j], null));
                        //Printout Queue Manager
                        user_roles.add(createRole("428", defaultScope, null));
                        //Purchase Request Manager
                        user_roles.add(createRole("247", scopes[j], null));
                        //Purchase Request Operator Extended
                        user_roles.add(createRole("248", scopes[j], null));
                        //Purchasing Operator
                        user_roles.add(createRole("54", scopes[j], null));
                        //Receiving Operator
                        if (iz.equals("BCUFR")) {
                            String tmpStr = "";
                            for (int o = 0; o < service_unit.length; o++) {
                                if (!service_unit[o].equals("Traitement")
                                        && !service_unit[o].equals("RELIURE")
                                        && !service_unit[o].equals("MigEnTraitement")) {
                                    tmpStr += service_unit[o] + "!";
                                }
                            }
                            String[] tmpArray = tmpStr.split("!");
                            user_roles.add(createRole("37", scopes[j],
                                    createParameterList("ServiceUnit", iz, scopes, circulation_desk, tmpArray)));
                        } else {
                            user_roles.add(createRole("37", scopes[j],
                                    createParameterList("ServiceUnit", iz, scopes, circulation_desk, service_unit)));
                        }
                        //Requests Operator
                        List<String> circ = createCircArray(circulation_desk, iz, scopes);
                        for (int k = 0; k < circ.size(); k++) {
                            List<Parameter> parameters2 = new ArrayList<>();
                            for (int l = 0; l < circulation_desk.length; l++) {
                                if (circulation_desk[l].contains(circ.get(k))
                                        || circulation_desk[l].equals("DEFAULT_CIRC_DESK")) {
                                    parameters2.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                }
                                String[] circsplit = circ.get(k).split("_");
                                if (circ.get(k).startsWith("BCUF_") && circulation_desk[l].contains(circsplit[1])
                                        || circ.get(k).startsWith("BFD_") && circulation_desk[l].contains(circsplit[1])) {
                                    if (circ.get(k).contains(circsplit[1])) {
                                        parameters2.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                    }
                                }
                            }
                            user_roles.add(createRole("51", circ.get(k), parameters2));
                        }
                        for (int k = 0; k < circulation_desk.length; k++) {
                            List<Parameter> parameters = new ArrayList<>();
                            if (circulation_desk[k].equals("DESK-ZHB2")) {
                                parameters.add(createParameter("CirculationDesk", scopes[j], circulation_desk[k]));
                                user_roles.add(createRole("51", scopes[j], parameters));
                            } else if (circulation_desk[k].equals("SPEIBIDESK")) {
                                parameters.add(createParameter("CirculationDesk", "LUSBI", "SPEIBIDESK"));
                                user_roles.add(createRole("51", "LUSBI", parameters));
                            } else if (circulation_desk[k].equals("CD_CMNE")) {
                                parameters.add(createParameter("CirculationDesk", "hes_nencm", "CD_CMNE"));
                                user_roles.add(createRole("51", "hes_nencm", parameters));
                            } else if (circulation_desk[k].equals("Circ Desk Code")) {
                                parameters.add(createParameter("CirculationDesk", "E27", "Circ Desk Code"));
                                user_roles.add(createRole("51", "E27", parameters));
                            } else if (circulation_desk[k].equals("READING_R")) {
                                parameters.add(createParameter("CirculationDesk", "E02", "READING_R"));
                                user_roles.add(createRole("51", "E02", parameters));
                            } else if (circulation_desk[k].equals("CD_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "CD_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("IAP_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "IAP_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ILCF_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "ILCF_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ICZ_LS")) {
                                parameters.add(createParameter("CirculationDesk", "N08", "ICZ_LS"));
                                user_roles.add(createRole("51", "N08", parameters));
                            } else if (circulation_desk[k].equals("Desk_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E23", "Desk_LS"));
                                user_roles.add(createRole("51", "E23", parameters));
                            } else if (circulation_desk[k].equals("MUG_LS")) {
                                parameters.add(createParameter("CirculationDesk", "Z13", "MUG_LS"));
                                user_roles.add(createRole("51", "Z13", parameters));
                            } else if (circulation_desk[k].equals("DESK_BE1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_BE1"));
                                user_roles.add(createRole("51", "E95", parameters));
                            } else if (circulation_desk[k].equals("DESK_ZH1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_ZH1"));
                                user_roles.add(createRole("51", "E95", parameters));
                            } else if (circulation_desk[k].equals("SIKJM _L")) {
                                parameters.add(createParameter("CirculationDesk", "E45", "SIKJM_L"));
                                user_roles.add(createRole("51", "E45", parameters));
                            } else if (circulation_desk[k].equals("SSA_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E19", "SSA_LS"));
                                user_roles.add(createRole("51", "E19", parameters));
                            } else if (circulation_desk[k].equals("SOND_SAMM")) {
                                parameters.add(createParameter("CirculationDesk", "A150", "SOND_SAMM"));
                                user_roles.add(createRole("51", "A150", parameters));
                            } else if (circulation_desk[k].equals("AKB_LESA")) {
                                parameters.add(createParameter("CirculationDesk", "AKB", "AKB_LESA"));
                                user_roles.add(createRole("51", "AKB", parameters));
                            }
                        }
                        //Usage Data Operator
                        user_roles.add(createRole("244", defaultScope, null));
                    }
                    break;
                case "slsp-acq-plus":
                    LOG.info("Profile: SLSP-ACQ-PLUS");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Cataloger
                        user_roles.add(createRole("204", defaultScope, null));
                        //Collection Inventory Operator
                        user_roles.add(createRole("228", scopes[j], null));
                        //Collection Inventory Operator Extended
                        user_roles.add(createRole("229", scopes[j], null));
                        //Deposit Manager
                        user_roles.add(createRole("0", scopes[j], null));
                        //Deposit Operator
                        user_roles.add(createRole("1", scopes[j], null));
                        //Deposit Operator Extended
                        user_roles.add(createRole("2", scopes[j], null));
                        //Invoice Operator Extended
                        user_roles.add(createRole("48", scopes[j], null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Physical Inventory Operator
                        user_roles.add(createRole("210", scopes[j], null));
                        //Physical Inventory Operator Extended
                        user_roles.add(createRole("225", scopes[j], null));
                        //Printout Queue Manager
                        user_roles.add(createRole("428", defaultScope, null));
                        //Purchase Request Manager
                        user_roles.add(createRole("247", scopes[j], null));
                        //Purchase Request Operator Extended
                        user_roles.add(createRole("248", scopes[j], null));
                        //Purchasing Manager
                        user_roles.add(createRole("55", scopes[j], null));
                        //Purchasing Operator
                        user_roles.add(createRole("54", scopes[j], null));
                        //Purchasing Operator Extended
                        user_roles.add(createRole("47", scopes[j], null));
                        //Receiving Operator
                        if (iz.equals("BCUFR")) {
                            String tmpStr = "";
                            for (int o = 0; o < service_unit.length; o++) {
                                if (!service_unit[o].equals("Traitement")
                                        && !service_unit[o].equals("RELIURE")
                                        && !service_unit[o].equals("MigEnTraitement")) {
                                    tmpStr += service_unit[o] + "!";
                                }
                            }
                            String[] tmpArray = tmpStr.split("!");
                            user_roles.add(createRole("37", scopes[j],
                                    createParameterList("ServiceUnit", iz, scopes, circulation_desk, tmpArray)));
                        } else {
                            user_roles.add(createRole("37", scopes[j],
                                    createParameterList("ServiceUnit", iz, scopes, circulation_desk, service_unit)));
                        }
                        //Requests Operator
                        List<String> circ = createCircArray(circulation_desk, iz, scopes);
                        for (int k = 0; k < circ.size(); k++) {
                            List<Parameter> parameters2 = new ArrayList<>();
                            for (int l = 0; l < circulation_desk.length; l++) {
                                if (circulation_desk[l].contains(circ.get(k))
                                        || circulation_desk[l].equals("DEFAULT_CIRC_DESK")) {
                                    parameters2.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                    user_roles.add(createRole("51", circ.get(k), parameters2));
                                }
                                //special case uge
                                String tmp = circulation_desk[l].replace("-", "_");
                                if (tmp.contains(circ.get(k))) {
                                    parameters2.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                    user_roles.add(createRole("51", circ.get(k), parameters2));
                                }
                                String[] circsplit = circ.get(k).split("_");
                                if (circ.get(k).startsWith("BCUF_") && circulation_desk[l].contains(circsplit[1])
                                        || circ.get(k).startsWith("BFD_") && circulation_desk[l].contains(circsplit[1])) {
                                    if (circ.get(k).contains(circsplit[1])) {
                                        parameters2.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                        user_roles.add(createRole("51", circ.get(k), parameters2));
                                    }
                                }
                            }
                        }
                        for (int k = 0; k < circulation_desk.length; k++) {
                            List<Parameter> parameters = new ArrayList<>();
                            if (circulation_desk[k].equals("DESK-ZHB2")) {
                                parameters.add(createParameter("CirculationDesk", scopes[j], circulation_desk[k]));
                                user_roles.add(createRole("51", scopes[j], parameters));
                            } else if (circulation_desk[k].equals("SPEIBIDESK")) {
                                parameters.add(createParameter("CirculationDesk", "LUSBI", "SPEIBIDESK"));
                                user_roles.add(createRole("51", "LUSBI", parameters));
                            } else if (circulation_desk[k].equals("CD_CMNE")) {
                                parameters.add(createParameter("CirculationDesk", "hes_nencm", "CD_CMNE"));
                                user_roles.add(createRole("51", "hes_nencm", parameters));
                            } else if (circulation_desk[k].equals("Circ Desk Code")) {
                                parameters.add(createParameter("CirculationDesk", "E27", "Circ Desk Code"));
                                user_roles.add(createRole("51", "E27", parameters));
                            } else if (circulation_desk[k].equals("READING_R")) {
                                parameters.add(createParameter("CirculationDesk", "E02", "READING_R"));
                                user_roles.add(createRole("51", "E02", parameters));
                            } else if (circulation_desk[k].equals("CD_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "CD_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("IAP_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "IAP_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ILCF_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "ILCF_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ICZ_LS")) {
                                parameters.add(createParameter("CirculationDesk", "N08", "ICZ_LS"));
                                user_roles.add(createRole("51", "N08", parameters));
                            } else if (circulation_desk[k].equals("Desk_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E23", "Desk_LS"));
                                user_roles.add(createRole("51", "E23", parameters));
                            } else if (circulation_desk[k].equals("MUG_LS")) {
                                parameters.add(createParameter("CirculationDesk", "Z13", "MUG_LS"));
                                user_roles.add(createRole("51", "Z13", parameters));
                            } else if (circulation_desk[k].equals("DESK_BE1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_BE1"));
                                user_roles.add(createRole("51", "E95", parameters));
                            } else if (circulation_desk[k].equals("DESK_ZH1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_ZH1"));
                                user_roles.add(createRole("51", "E95", parameters));
                            } else if (circulation_desk[k].equals("SIKJM _L")) {
                                parameters.add(createParameter("CirculationDesk", "E45", "SIKJM_L"));
                                user_roles.add(createRole("51", "E45", parameters));
                            } else if (circulation_desk[k].equals("SSA_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E19", "SSA_LS"));
                                user_roles.add(createRole("51", "E19", parameters));
                            } else if (circulation_desk[k].equals("SOND_SAMM")) {
                                parameters.add(createParameter("CirculationDesk", "A150", "SOND_SAMM"));
                                user_roles.add(createRole("51", "A150", parameters));
                            } else if (circulation_desk[k].equals("AKB_LESA")) {
                                parameters.add(createParameter("CirculationDesk", "AKB", "AKB_LESA"));
                                user_roles.add(createRole("51", "AKB", parameters));
                            }
                        }
                        //Usage Data Operator
                        user_roles.add(createRole("244", defaultScope, null));
                    }
                    break;
                case "slsp-acq-vendors":
                    LOG.info("Profile: SLSP-ACQ-VENDORS");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Vendor Account Manager
                        user_roles.add(createRole("250", scopes[j], null));
                        //Vendor Account Manager
                        user_roles.add(createRole("33", defaultScope, null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Printout Queue Manager
                        user_roles.add(createRole("428", defaultScope, null));
                    }
                    break;
                case "slsp-acq-invoices":
                    LOG.info("Profile: SLSP-ACQ-INVOICES");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Invoice Manager
                        user_roles.add(createRole("44", scopes[j], null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Printout Queue Manager
                        user_roles.add(createRole("428", defaultScope, null));
                    }
                    break;
                case "slsp-acq-budget":
                    LOG.info("Profile: SLSP-ACQ-BUDGET");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Fund Manager
                        user_roles.add(createRole("36", scopes[j], null));
                        //Ledge Manager
                        user_roles.add(createRole("34", scopes[j], null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Printout Queue Manager
                        user_roles.add(createRole("428", defaultScope, null));
                    }
                    break;
                case "slsp-acq-budget-viewer":
                    LOG.info("Profile: SLSP-ACQ-BUDGET-VIEWER");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Fund-Ledge Viewer
                        user_roles.add(createRole("245", scopes[j], null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Printout Queue Manager
                        user_roles.add(createRole("428", defaultScope, null));
                    }
                    break;
                case "slsp-acq-admin":
                    LOG.info("Profile: SLSP-ACQ-ADMIN");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Acquisitions Administrator
                        user_roles.add(createRole("56", defaultScope, null));
                        //Fiscal Period Manager
                        user_roles.add(createRole("45", defaultScope, null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                    }
                    break;
                case "slsp-analytics":
                    LOG.info("Profile: SLSP-ANALYTICS");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Design Analytics
                        user_roles.add(createRole("220", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                    }
                    break;
                case "slsp-analytics-admin":
                    LOG.info("Profile: SLSP-ANALYTICS-ADMINISTRATOR");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Analytics Administrator
                        user_roles.add(createRole("2200", defaultScope, null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                    }
                    break;
                case "slsp-cat":
                    LOG.info("Profile: SLSP-CAT");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Cataloger
                        user_roles.add(createRole("204", defaultScope, null));
                        //Collection Inventory Operator
                        user_roles.add(createRole("228", scopes[j], null));
                        //Deposit Manager
                        user_roles.add(createRole("0", scopes[j], null));
                        //Electronic Inventory Operator
                        user_roles.add(createRole("209", defaultScope, null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Physical Inventory Operator
                        user_roles.add(createRole("210", scopes[j], null));
                    }
                    break;
                case "slsp-cat-inventory":
                    LOG.info("Profile: SLSP-CAT-INVENTORY");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Physical Inventory Operator
                        user_roles.add(createRole("210", scopes[j], null));
                    }
                    break;
                case "slsp-cat-plus":
                    LOG.info("Profile: SLSP-CAT-INVENTORY");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Cataloger
                        user_roles.add(createRole("204", defaultScope, null));
                        //Cataloger Extended
                        user_roles.add(createRole("227", defaultScope, null));
                        //Collection Inventory Operator
                        user_roles.add(createRole("228", scopes[j], null));
                        //Collection Inventory Operator Extended
                        user_roles.add(createRole("229", scopes[j], null));
                        //Deposit Manager
                        user_roles.add(createRole("0", scopes[j], null));
                        //Digital Inventory Operator
                        user_roles.add(createRole("12", scopes[j], null));
                        //Digital Inventory Operator Extended
                        user_roles.add(createRole("226", scopes[j], null));
                        //Electronic Inventory Operator
                        user_roles.add(createRole("209", defaultScope, null));
                        //Electronic Inventory Operator Extended
                        user_roles.add(createRole("224", scopes[j], null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Physical Inventory Operator
                        user_roles.add(createRole("210", scopes[j], null));
                        //Physical Inventory Operator Extended
                        user_roles.add(createRole("225", scopes[j], null));
                        //Repository Manager
                        user_roles.add(createRole("29", scopes[j], null));
                    }
                    break;
                case "slsp-cat-admin":
                    LOG.info("Profile: SLSP-CAT-INVENTORY");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Catalog Administrator
                        user_roles.add(createRole("205", defaultScope, null));
                        //Catalog Manager
                        user_roles.add(createRole("206", defaultScope, null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Repository Administrator
                        user_roles.add(createRole("53", defaultScope, null));
                    }
                    break;
                case "slsp-circ-limited":
                    LOG.info("Profile: SLSP-CIRC-LIMITED");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Circulation Desk Operator Limited
                        List<String> circ = createCircArray(circulation_desk, iz, scopes);
                        for (int k = 0; k < circ.size(); k++) {
                            List<Parameter> parameters1 = new ArrayList<>();
                            for (int l = 0; l < circulation_desk.length; l++) {
                                if (circulation_desk[l].contains(circ.get(k))
                                        || circulation_desk[l].equals("DEFAULT_CIRC_DESK")) {
                                    parameters1.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                }
                                String[] circsplit = circ.get(k).split("_");
                                if (circ.get(k).startsWith("BCUF_") && circulation_desk[l].contains(circsplit[1])
                                        || circ.get(k).startsWith("BFD_") && circulation_desk[l].contains(circsplit[1])) {
                                    if (circ.get(k).contains(circsplit[1])) {
                                        parameters1.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                    }
                                }
                            }
                            user_roles.add(createRole("299", circ.get(k), parameters1));
                        }
                        for (int k = 0; k < circulation_desk.length; k++) {
                            List<Parameter> parameters = new ArrayList<>();
                            if (circulation_desk[k].equals("DESK-ZHB2")) {
                                parameters.add(createParameter("CirculationDesk", scopes[j], circulation_desk[k]));
                                user_roles.add(createRole("51", scopes[j], parameters));
                                user_roles.add(createRole("221", scopes[j], parameters));
                            } else if (circulation_desk[k].equals("SPEIBIDESK")) {
                                parameters.add(createParameter("CirculationDesk", "LUSBI", "SPEIBIDESK"));
                                user_roles.add(createRole("51", "LUSBI", parameters));
                                user_roles.add(createRole("221", "LUSBI", parameters));
                            } else if (circulation_desk[k].equals("CD_CMNE")) {
                                parameters.add(createParameter("CirculationDesk", "hes_nencm", "CD_CMNE"));
                                user_roles.add(createRole("51", "hes_nencm", parameters));
                                user_roles.add(createRole("221", "hes_nencm", parameters));
                            } else if (circulation_desk[k].equals("Circ Desk Code")) {
                                parameters.add(createParameter("CirculationDesk", "E27", "Circ Desk Code"));
                                user_roles.add(createRole("51", "E27", parameters));
                                user_roles.add(createRole("221", "E27", parameters));
                            } else if (circulation_desk[k].equals("READING_R")) {
                                parameters.add(createParameter("CirculationDesk", "E02", "READING_R"));
                                user_roles.add(createRole("51", "E02", parameters));
                                user_roles.add(createRole("221", "E02", parameters));
                            } else if (circulation_desk[k].equals("CD_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "CD_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                                user_roles.add(createRole("221", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("IAP_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", " une_bflsh", "IAP_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                                user_roles.add(createRole("221", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ILCF_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "ILCF_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                                user_roles.add(createRole("221", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ICZ_LS")) {
                                parameters.add(createParameter("CirculationDesk", "N08", "ICZ_LS"));
                                user_roles.add(createRole("51", "N08", parameters));
                                user_roles.add(createRole("221", "N08", parameters));
                            } else if (circulation_desk[k].equals("Desk_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E23", "Desk_LS"));
                                user_roles.add(createRole("51", "E23", parameters));
                                user_roles.add(createRole("221", "E23", parameters));
                            } else if (circulation_desk[k].equals("MUG_LS")) {
                                parameters.add(createParameter("CirculationDesk", "Z13", "MUG_LS"));
                                user_roles.add(createRole("51", "Z13", parameters));
                                user_roles.add(createRole("221", "Z13", parameters));
                            } else if (circulation_desk[k].equals("DESK_BE1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_BE1"));
                                user_roles.add(createRole("51", "E95", parameters));
                                user_roles.add(createRole("221", "E95", parameters));
                            } else if (circulation_desk[k].equals("DESK_ZH1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_ZH1"));
                                user_roles.add(createRole("51", "E95", parameters));
                                user_roles.add(createRole("221", "E95", parameters));
                            } else if (circulation_desk[k].equals("SIKJM_L")) {
                                parameters.add(createParameter("CirculationDesk", "E45", "SIKJM_L"));
                                user_roles.add(createRole("51", "E45", parameters));
                                user_roles.add(createRole("221", "E45", parameters));
                            } else if (circulation_desk[k].equals("SSA_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E19", "SSA_LS"));
                                user_roles.add(createRole("51", "E19", parameters));
                                user_roles.add(createRole("221", "E19", parameters));
                            } else if (circulation_desk[k].equals("SOND_SAMM")) {
                                parameters.add(createParameter("CirculationDesk", "A150", "SOND_SAMM"));
                                user_roles.add(createRole("51", "A150", parameters));
                                user_roles.add(createRole("221", "A150", parameters));
                            } else if (circulation_desk[k].equals("AKB_LESA")) {
                                parameters.add(createParameter("CirculationDesk", "AKB", "AKB_LESA"));
                                user_roles.add(createRole("51", "AKB", parameters));
                            }
                        }
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                    }
                    break;
                case "slsp-circ":
                    LOG.info("Profile: SLSP-CIRC");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Circulation Desk Manager + Requests Operator
                        List<String> circ = createCircArray(circulation_desk, iz, scopes);
                        for (int k = 0; k < circ.size(); k++) {
                            List<Parameter> parameters1 = new ArrayList<>();
                            for (int l = 0; l < circulation_desk.length; l++) {
                                if (circulation_desk[l].contains(circ.get(k))
                                        || circulation_desk[l].equals("DEFAULT_CIRC_DESK")) {
                                    parameters1.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                    user_roles.add(createRole("221", circ.get(k), parameters1));
                                    user_roles.add(createRole("51", circ.get(k), parameters1));
                                }
                                String[] circsplit = circ.get(k).split("_");
                                if (circ.get(k).startsWith("BCUF_") && circulation_desk[l].contains(circsplit[1])
                                        || circ.get(k).startsWith("BFD_") && circulation_desk[l].contains(circsplit[1])) {
                                    if (circ.get(k).contains(circsplit[1])) {
                                        parameters1.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                        user_roles.add(createRole("221", circ.get(k), parameters1));
                                        user_roles.add(createRole("51", circ.get(k), parameters1));
                                    }
                                }
                            }
                        }
                        for (int k = 0; k < circulation_desk.length; k++) {
                            List<Parameter> parameters = new ArrayList<>();
                            if (circulation_desk[k].equals("DESK-ZHB2")) {
                                parameters.add(createParameter("CirculationDesk", scopes[j], circulation_desk[k]));
                                user_roles.add(createRole("51", scopes[j], parameters));
                                user_roles.add(createRole("221", scopes[j], parameters));
                            } else if (circulation_desk[k].equals("SPEIBIDESK")) {
                                parameters.add(createParameter("CirculationDesk", "LUSBI", "SPEIBIDESK"));
                                user_roles.add(createRole("51", "LUSBI", parameters));
                                user_roles.add(createRole("221", "LUSBI", parameters));
                            } else if (circulation_desk[k].equals("CD_CMNE")) {
                                parameters.add(createParameter("CirculationDesk", "hes_nencm", "CD_CMNE"));
                                user_roles.add(createRole("51", "hes_nencm", parameters));
                                user_roles.add(createRole("221", "hes_nencm", parameters));
                            } else if (circulation_desk[k].equals("Circ Desk Code")) {
                                parameters.add(createParameter("CirculationDesk", "E27", "Circ Desk Code"));
                                user_roles.add(createRole("51", "E27", parameters));
                                user_roles.add(createRole("221", "E27", parameters));
                            } else if (circulation_desk[k].equals("READING_R")) {
                                parameters.add(createParameter("CirculationDesk", "E02", "READING_R"));
                                user_roles.add(createRole("51", "E02", parameters));
                                user_roles.add(createRole("221", "E02", parameters));
                            } else if (circulation_desk[k].equals("CD_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "CD_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                                user_roles.add(createRole("221", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("IAP_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "IAP_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ILCF_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "ILCF_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ICZ_LS")) {
                                parameters.add(createParameter("CirculationDesk", "N08", "ICZ_LS"));
                                user_roles.add(createRole("51", "N08", parameters));
                                user_roles.add(createRole("221", "N08", parameters));
                            } else if (circulation_desk[k].equals("Desk_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E23", "Desk_LS"));
                                user_roles.add(createRole("51", "E23", parameters));
                                user_roles.add(createRole("221", "E23", parameters));
                            } else if (circulation_desk[k].equals("MUG_LS")) {
                                parameters.add(createParameter("CirculationDesk", "Z13", "MUG_LS"));
                                user_roles.add(createRole("51", "Z13", parameters));
                                user_roles.add(createRole("221", "Z13", parameters));
                            } else if (circulation_desk[k].equals("DESK_BE1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_BE1"));
                                user_roles.add(createRole("51", "E95", parameters));
                                user_roles.add(createRole("221", "E95", parameters));
                            } else if (circulation_desk[k].equals("DESK_ZH1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_ZH1"));
                                user_roles.add(createRole("51", "E95", parameters));
                                user_roles.add(createRole("221", "E95", parameters));
                            } else if (circulation_desk[k].equals("SIKJM_L")) {
                                parameters.add(createParameter("CirculationDesk", "E45", "SIKJM_L"));
                                user_roles.add(createRole("51", "E45", parameters));
                                user_roles.add(createRole("221", "E45", parameters));
                            } else if (circulation_desk[k].equals("SSA_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E19", "SSA_LS"));
                                user_roles.add(createRole("51", "E19", parameters));
                                user_roles.add(createRole("221", "E19", parameters));
                            } else if (circulation_desk[k].equals("SOND_SAMM")) {
                                parameters.add(createParameter("CirculationDesk", "A150", "SOND_SAMM"));
                                user_roles.add(createRole("51", "A150", parameters));
                                user_roles.add(createRole("221", "A150", parameters));
                            } else if (circulation_desk[k].equals("AKB_LESA")) {
                                parameters.add(createParameter("CirculationDesk", "AKB", "AKB_LESA"));
                                user_roles.add(createRole("51", "AKB", parameters));
                                user_roles.add(createRole("221", "AKB", parameters));
                            }
                        }
                        //Fulfillment Services Manager
                        user_roles.add(createRole("215", scopes[j], null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Physical Inventory Operator
                        user_roles.add(createRole("210", scopes[j], null));
                        //Printout Queue Manager
                        user_roles.add(createRole("428", defaultScope, null));
                    }
                    break;
                case "slsp-circ-plus":
                    LOG.info("Profile: SLSP-CIRC-PLUS");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Circulation Desk Manager + Requests Operator
                        List<String> circ = createCircArray(circulation_desk, iz, scopes);
                        for (int k = 0; k < circ.size(); k++) {
                            List<Parameter> parameters1 = new ArrayList<>();
                            for (int l = 0; l < circulation_desk.length; l++) {
                                if (circulation_desk[l].contains(circ.get(k))
                                        || circulation_desk[l].equals("DEFAULT_CIRC_DESK")) {
                                    parameters1.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                    LOG.info("Circdesk: " + circulation_desk[l] + " circ: " + circ.get(k));
                                    LOG.info(parameters1.toString());
                                    user_roles.add(createRole("221", circ.get(k), parameters1));
                                    user_roles.add(createRole("51", circ.get(k), parameters1));
                                }
                                String[] circsplit = circ.get(k).split("_");
                                if (circ.get(k).startsWith("BCUF_") && circulation_desk[l].contains(circsplit[1])
                                        || circ.get(k).startsWith("BFD_") && circulation_desk[l].contains(circsplit[1])) {
                                    if (circ.get(k).contains(circsplit[1])) {
                                        parameters1.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                        user_roles.add(createRole("51", circ.get(k), parameters1));
                                        user_roles.add(createRole("221", circ.get(k), parameters1));
                                    }
                                }
                            }
                        }
                        for (int k = 0; k < circulation_desk.length; k++) {
                            List<Parameter> parameters = new ArrayList<>();
                            if (circulation_desk[k].equals("DESK-ZHB2")) {
                                parameters.add(createParameter("CirculationDesk", scopes[j], circulation_desk[k]));
                                user_roles.add(createRole("51", scopes[j], parameters));
                                user_roles.add(createRole("221", scopes[j], parameters));
                            } else if (circulation_desk[k].equals("SPEIBIDESK")) {
                                parameters.add(createParameter("CirculationDesk", "LUSBI", "SPEIBIDESK"));
                                user_roles.add(createRole("51", "LUSBI", parameters));
                                user_roles.add(createRole("221", "LUSBI", parameters));
                            } else if (circulation_desk[k].equals("CD_CMNE")) {
                                parameters.add(createParameter("CirculationDesk", "hes_nencm", "CD_CMNE"));
                                user_roles.add(createRole("51", "hes_nencm", parameters));
                                user_roles.add(createRole("221", "hes_nencm", parameters));
                            } else if (circulation_desk[k].equals("Circ Desk Code")) {
                                parameters.add(createParameter("CirculationDesk", "E27", "Circ Desk Code"));
                                user_roles.add(createRole("51", "E27", parameters));
                                user_roles.add(createRole("221", "E27", parameters));
                            } else if (circulation_desk[k].equals("READING_R")) {
                                parameters.add(createParameter("CirculationDesk", "E02", "READING_R"));
                                user_roles.add(createRole("51", "E02", parameters));
                                user_roles.add(createRole("221", "E02", parameters));
                            } else if (circulation_desk[k].equals("CD_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "CD_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                                user_roles.add(createRole("221", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("IAP_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "IAP_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                                user_roles.add(createRole("221", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ILCF_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "ILCF_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                                user_roles.add(createRole("221", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ICZ_LS")) {
                                parameters.add(createParameter("CirculationDesk", "N08", "ICZ_LS"));
                                user_roles.add(createRole("51", "N08", parameters));
                                user_roles.add(createRole("221", "N08", parameters));
                            } else if (circulation_desk[k].equals("Desk_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E23", "Desk_LS"));
                                user_roles.add(createRole("51", "E23", parameters));
                                user_roles.add(createRole("221", "E23", parameters));
                            } else if (circulation_desk[k].equals("MUG_LS")) {
                                parameters.add(createParameter("CirculationDesk", "Z13", "MUG_LS"));
                                user_roles.add(createRole("51", "Z13", parameters));
                                user_roles.add(createRole("221", "Z13", parameters));
                            } else if (circulation_desk[k].equals("DESK_BE1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_BE1"));
                                user_roles.add(createRole("51", "E95", parameters));
                                user_roles.add(createRole("221", "E95", parameters));
                            } else if (circulation_desk[k].equals("DESK_ZH1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_ZH1"));
                                user_roles.add(createRole("51", "E95", parameters));
                                user_roles.add(createRole("221", "E95", parameters));
                            } else if (circulation_desk[k].equals("SIKJM_L")) {
                                parameters.add(createParameter("CirculationDesk", "E45", "SIKJM_L"));
                                user_roles.add(createRole("51", "E45", parameters));
                                user_roles.add(createRole("221", "E45", parameters));
                            } else if (circulation_desk[k].equals("SSA_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E19", "SSA_LS"));
                                user_roles.add(createRole("51", "E19", parameters));
                                user_roles.add(createRole("221", "E19", parameters));
                            } else if (circulation_desk[k].equals("SOND_SAMM")) {
                                parameters.add(createParameter("CirculationDesk", "A150", "SOND_SAMM"));
                                user_roles.add(createRole("51", "A150", parameters));
                                user_roles.add(createRole("221", "A150", parameters));
                            } else if (circulation_desk[k].equals("AKB_LESA")) {
                                parameters.add(createParameter("CirculationDesk", "AKB", "AKB_LESA"));
                                user_roles.add(createRole("51", "AKB", parameters));
                                user_roles.add(createRole("221", "AKB", parameters));
                            }
                        }
                        //Course Reserves Manager
                        user_roles.add(createRole("231", scopes[j], null));
                        //Fulfillment Services Manager
                        user_roles.add(createRole("215", scopes[j], null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //License Viewer
                        user_roles.add(createRole("58", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Physical Inventory Operator
                        user_roles.add(createRole("210", scopes[j], null));
                        //Physical Inventory Operator Extended
                        user_roles.add(createRole("225", scopes[j], null));
                        //Printout Queue Manager
                        user_roles.add(createRole("428", defaultScope, null));
                    }
                    break;
                case "slsp-circ-admin":
                    LOG.info("SLSP-CIRC-ADMIN");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Fulfillment Administrator
                        user_roles.add(createRole("52", defaultScope, null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", scopes[j], null));
                        //Resource Sharing Partners Manager
                        user_roles.add(createRole("239", scopes[j], null));
                    }
                    break;
                case "slsp-emedia":
                    LOG.info("Profile: SLSP-EMEDIA");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Cataloger
                        user_roles.add(createRole("204", defaultScope, null));
                        //Deposit Manager
                        user_roles.add(createRole("0", scopes[j], null));
                        //Deposit Operator
                        user_roles.add(createRole("1", scopes[j], null));
                        //Digital Inventory Operator
                        user_roles.add(createRole("12", scopes[j], null));
                        //Digital Inventory Operator Extended
                        user_roles.add(createRole("226", scopes[j], null));
                        //Electronic Inventory Operator
                        user_roles.add(createRole("209", defaultScope, null));
                        //Electronic Inventory Operator Extended
                        user_roles.add(createRole("224", scopes[j], null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Physical Inventory Operator
                        user_roles.add(createRole("210", scopes[j], null));
                        //Purchase Request Manager
                        user_roles.add(createRole("247", scopes[j], null));
                        //Repository Manager
                        user_roles.add(createRole("29", scopes[j], null));
                        //Selector
                        user_roles.add(createRole("46", defaultScope, null));
                        //Selector Extended
                        //user_roles.add(createRole("243", defaultScope, null));
                    }
                    break;
                case "slsp-emedia-plus":
                    LOG.info("Profile: SLSP-EMEDIA-PLUS");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Cataloger
                        user_roles.add(createRole("204", defaultScope, null));
                        //CDI Inventory Operator
                        user_roles.add(createRole("208", defaultScope, null));
                        //Collection Inventory Operator
                        user_roles.add(createRole("228", scopes[j], null));
                        //Collection Inventory Operator Extended
                        user_roles.add(createRole("229", scopes[j], null));
                        //Deposit Manager
                        user_roles.add(createRole("0", scopes[j], null));
                        //Deposit Operator
                        user_roles.add(createRole("1", scopes[j], null));
                        //Deposit Operator Extended
                        user_roles.add(createRole("2", scopes[j], null));
                        //Digital Inventory Operator
                        user_roles.add(createRole("12", scopes[j], null));
                        //Digital Inventory Operator Extended
                        user_roles.add(createRole("226", scopes[j], null));
                        //Electronic Inventory Operator
                        user_roles.add(createRole("209", defaultScope, null));
                        //Electronic Inventory Operator Extended
                        user_roles.add(createRole("224", scopes[j], null));
                        //Invoice Operator Extended
                        user_roles.add(createRole("48", scopes[j], null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //License Manager
                        user_roles.add(createRole("41", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Physical Inventory Operator
                        user_roles.add(createRole("210", scopes[j], null));
                        //Physical Inventory Operator Extended
                        user_roles.add(createRole("225", scopes[j], null));
                        //Purchase Request Manager
                        user_roles.add(createRole("247", scopes[j], null));
                        //Purchase Request Operator Extended
                        user_roles.add(createRole("248", scopes[j], null));
                        //Purchasing Manager
                        user_roles.add(createRole("55", scopes[j], null));
                        //Purchasing Operator
                        user_roles.add(createRole("54", scopes[j], null));
                        //Purchasing Operator Extended
                        user_roles.add(createRole("47", scopes[j], null));
                        //Repository Manager
                        user_roles.add(createRole("29", scopes[j], null));
                        //Selector
                        user_roles.add(createRole("46", defaultScope, null));
                        //Selector Extended
                        //user_roles.add(createRole("243", defaultScope, null));
                        //Trial Manager
                        user_roles.add(createRole("217", defaultScope, null));
                        //Usage Data Operator
                        user_roles.add(createRole("244", defaultScope, null));
                    }
                    break;
                case "slsp-primo":
                    LOG.info("SLSP-PRIMO");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Catalog Administrator
                        user_roles.add(createRole("205", defaultScope, null));
                        //CDI Inventory Operator
                        user_roles.add(createRole("208", defaultScope, null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Discovey Administrator
                        user_roles.add(createRole("300", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                    }
                    break;
                case "slsp-sysadmin":
                    LOG.info("Profile: SLSP-SYSADMIN");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Acquisitions Administrator
                        user_roles.add(createRole("56", defaultScope, null));
                        //Analytics Administrator
                        user_roles.add(createRole("2200", defaultScope, null));
                        //Catalog Administrator
                        user_roles.add(createRole("205", defaultScope, null));
                        //Discovery Administrator
                        user_roles.add(createRole("300", defaultScope, null));
                        //Fulfillment Administrator
                        user_roles.add(createRole("52", defaultScope, null));
                        //General System Administrator
                        user_roles.add(createRole("26", defaultScope, null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Letter Administrator
                        user_roles.add(createRole("57", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Printout Queue Manager
                        user_roles.add(createRole("428", defaultScope, null));
                        //Repository Administrator
                        user_roles.add(createRole("53", defaultScope, null));
                    }
                    break;
                case "slsp-staff-manager":
                    LOG.info("Profile: SLSP-STAFF-MANAGER");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //User Manager
                        user_roles.add(createRole("21", defaultScope, null));
                        //User Administrator
                        user_roles.add(createRole("50", defaultScope, null));
                    }
                    break;
                case "slsp-workorder":
                    LOG.info("Profile: SLSP-WORKORDER");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                        //Patron
                        user_roles.add(createRole("200", defaultScope, null));
                        //Work Order Operator
                        List<Parameter> params = createWorkOrderParameterList("ServiceUnit", iz, scopes, circulation_desk, service_unit);
                        if (!params.isEmpty()) {
                            if (params.toString().contains("Traitement") ||
                                    params.toString().contains("RELIURE") ||
                                    params.toString().contains("MigEnTraitement") ||
                                    params.toString().contains("WOT")) {
                                user_roles.add(createRole("214", defaultScope, params));
                            } else {
                                user_roles.add(createRole("214", scopes[j], params));
                            }
                        }
                    }
                    break;
                case "slsp-desk":
                    LOG.info("Profile: SLSP-DESK");
                    for (int j = 0; j < scopes.length; j++) {
                        if (scopes[j].equals(iz)) {
                            scopes[j] = defaultScope;
                        } else if (scopes[j].equals(("ILL"))) {
                            scopes[j] = "RES_SHARE";
                            //Circulation Desk Manager + Requests Operator
                            List<Parameter> parameters = new ArrayList<>();
                            parameters.add(createParameter("CirculationDesk", scopes[j], "RES_DESK"));
                            user_roles.add(createRole("221", scopes[j], parameters));
                            user_roles.add(createRole("51", scopes[j], parameters));
                            //Fulfillment Services Manager
                            user_roles.add(createRole("215", scopes[j], null));
                            //Resource Sharing Partners Manager
                            user_roles.add(createRole("239", defaultScope, null));
                            continue;
                        }
                        //Circulation Desk Operator
                        List<String> circ = createCircArray(circulation_desk, iz, scopes);
                        for (int k = 0; k < circ.size(); k++) {
                            List<Parameter> parameters1 = new ArrayList<>();
                            for (int l = 0; l < circulation_desk.length; l++) {
                                if (circulation_desk[l].contains(circ.get(k))
                                        || circulation_desk[l].equals("DEFAULT_CIRC_DESK")) {
                                    parameters1.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                    user_roles.add(createRole("32", circ.get(k), parameters1));
                                    user_roles.add(createRole("51", circ.get(k), parameters1));
                                }

                                String circsplit[] = circ.get(k).split("_");
                                if (circ.get(k).startsWith("BCUF_") && circulation_desk[l].contains(circsplit[1])
                                        || circ.get(k).startsWith("BFD_") && circulation_desk[l].contains(circsplit[1])) {
                                    if (circ.get(k).contains(circsplit[1])) {
                                        parameters1.add(createParameter("CirculationDesk", circ.get(k), circulation_desk[l]));
                                        user_roles.add(createRole("32", circ.get(k), parameters1));
                                        user_roles.add(createRole("51", circ.get(k), parameters1));
                                    }
                                }
                            }
                        }
                        for (int k = 0; k < circulation_desk.length; k++) {
                            List<Parameter> parameters = new ArrayList<>();
                            if (circulation_desk[k].equals("DESK-ZHB2")) {
                                parameters.add(createParameter("CirculationDesk", scopes[j], circulation_desk[k]));
                                user_roles.add(createRole("51", scopes[j], parameters));
                                user_roles.add(createRole("32", scopes[j], parameters));
                            } else if (circulation_desk[k].equals("SPEIBIDESK")) {
                                parameters.add(createParameter("CirculationDesk", "LUSBI", "SPEIBIDESK"));
                                user_roles.add(createRole("51", "LUSBI", parameters));
                                user_roles.add(createRole("32", "LUSBI", parameters));
                            } else if (circulation_desk[k].equals("CD_CMNE")) {
                                parameters.add(createParameter("CirculationDesk", "hes_nencm", "CD_CMNE"));
                                user_roles.add(createRole("51", "hes_nencm", parameters));
                                user_roles.add(createRole("32", "hes_nencm", parameters));
                            } else if (circulation_desk[k].equals("Circ Desk Code")) {
                                parameters.add(createParameter("CirculationDesk", "E27", "Circ Desk Code"));
                                user_roles.add(createRole("51", "E27", parameters));
                                user_roles.add(createRole("32", "E27", parameters));
                            } else if (circulation_desk[k].equals("READING_R")) {
                                parameters.add(createParameter("CirculationDesk", "E02", "READING_R"));
                                user_roles.add(createRole("51", "E02", parameters));
                                user_roles.add(createRole("32", "E02", parameters));
                            } else if (circulation_desk[k].equals("CD_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "CD_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                                user_roles.add(createRole("32", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("IAP_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "IAP_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                                user_roles.add(createRole("32", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ILCF_CIRC")) {
                                parameters.add(createParameter("CirculationDesk", "une_bflsh", "ILCF_CIRC"));
                                user_roles.add(createRole("51", "une_bflsh", parameters));
                                user_roles.add(createRole("32", "une_bflsh", parameters));
                            } else if (circulation_desk[k].equals("ICZ_LS")) {
                                parameters.add(createParameter("CirculationDesk", "N08", "ICZ_LS"));
                                user_roles.add(createRole("51", "N08", parameters));
                                user_roles.add(createRole("32", "N08", parameters));
                            } else if (circulation_desk[k].equals("Desk_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E23", "Desk_LS"));
                                user_roles.add(createRole("51", "E23", parameters));
                                user_roles.add(createRole("32", "E23", parameters));
                            } else if (circulation_desk[k].equals("MUG_LS")) {
                                parameters.add(createParameter("CirculationDesk", "Z13", "MUG_LS"));
                                user_roles.add(createRole("51", "Z13", parameters));
                                user_roles.add(createRole("32", "Z13", parameters));
                            } else if (circulation_desk[k].equals("DESK_BE1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_BE1"));
                                user_roles.add(createRole("51", "E95", parameters));
                                user_roles.add(createRole("32", "E95", parameters));
                            } else if (circulation_desk[k].equals("DESK_ZH1")) {
                                parameters.add(createParameter("CirculationDesk", "E95", "DESK_ZH1"));
                                user_roles.add(createRole("51", "E95", parameters));
                                user_roles.add(createRole("32", "E95", parameters));
                            } else if (circulation_desk[k].equals("SIKJM_L")) {
                                parameters.add(createParameter("CirculationDesk", "E45", "SIKJM_L"));
                                user_roles.add(createRole("51", "E45", parameters));
                                user_roles.add(createRole("32", "E45", parameters));
                            } else if (circulation_desk[k].equals("SSA_LS")) {
                                parameters.add(createParameter("CirculationDesk", "E19", "SSA_LS"));
                                user_roles.add(createRole("51", "E19", parameters));
                                user_roles.add(createRole("32", "E19", parameters));
                            } else if (circulation_desk[k].equals("SOND_SAMM")) {
                                parameters.add(createParameter("CirculationDesk", "A150", "SOND_SAMM"));
                                user_roles.add(createRole("51", "A150", parameters));
                                user_roles.add(createRole("32", "A150", parameters));
                            } else if (circulation_desk[k].equals("AKB_LESA")) {
                                parameters.add(createParameter("CirculationDesk", "AKB", "AKB_LESA"));
                                user_roles.add(createRole("51", "AKB", parameters));
                                user_roles.add(createRole("32", "AKB", parameters));
                            }
                        }
                        //Physical Inventory Operator
                        user_roles.add(createRole("210", scopes[j], null));
                        //Known Issues Operator
                        user_roles.add(createRole("301", defaultScope, null));
                    }
                    break;
                case "test":
                    LOG.info("test");
                    user_roles.add(createRole("200", defaultScope, null));
                    user_roles.add(createRole("301", defaultScope, null));
                    break;
                default:
                    LOG.error("Unknown Profile: " + profiles[i]);

            }
        }
        return user_roles;
    }

    private List<String> createCircArray(String[] circulation_desk, String iz, String[] scopes) {
        List<String> circ = new ArrayList<>();
        switch (iz) {
            case "ETH":
                CircAddDefault(circulation_desk, scopes, circ);
                for (int i = 0; i < circulation_desk.length; i++) {
                    if (!circulation_desk[i].equals("DEFAULT_CIRC_DESK")) {
                        if (circulation_desk[i].length() > 3) {
                            String[] splitCircDesk = circulation_desk[i].split("_");
                            if (!circ.contains(splitCircDesk[1])) {
                                circ.add(splitCircDesk[1]);
                            }
                        } else {
                            if (!circ.contains(circulation_desk[i])) {
                                circ.add(circulation_desk[i]);
                            }
                        }

                    }
                }
                break;
            case "UBS":
                CircAddDefault(circulation_desk, scopes, circ);
                for (int i = 0; i < circulation_desk.length; i++) {
                    if (!circulation_desk[i].equals("DEFAULT_CIRC_DESK") && !circulation_desk[i].equals("DEF_NOC")) {
                        if (circulation_desk[i].contains("_")) {
                            String[] splitCircDesk = circulation_desk[i].split("_");
                            if (!circ.contains(splitCircDesk[0])) {
                                circ.add(splitCircDesk[0]);
                            }
                        }
                        if (circulation_desk[i].contains("-")) {
                            String[] splitCircDesk = circulation_desk[i].split("-");
                            if (!circ.contains(splitCircDesk[0])) {
                                circ.add(splitCircDesk[0]);
                            }
                        }

                    }
                }
                break;
            case "BCUFR":
                CircAddDefault(circulation_desk, scopes, circ);
                for (int i = 0; i < circulation_desk.length; i++) {
                    if (!circulation_desk[i].equals("DEFAULT_CIRC_DESK")) {
                        String[] splitCircDesk = circulation_desk[i].split("_");
                        splitCircDesk[0] = "BCUF_" + splitCircDesk[0];
                        if (!circ.contains(splitCircDesk[0])) {
                            circ.add(splitCircDesk[0]);
                        }
                    }
                }
            case "ZHK":
                CircAddDefault(circulation_desk, scopes, circ);
                for (int i = 0; i < circulation_desk.length; i++) {
                    if (!circulation_desk[i].equals("DEFAULT_CIRC_DESK")) {
                        if (circulation_desk[i].equals("ARCHIV")) {
                            circ.add("E65");
                        } else if (circulation_desk[i].equals("E65SELF")) {
                            circ.add("E65");
                        }
                    }
                }
                break;
            case "HSG":
            case "SUP":
            case "UBE":
            case "ZAW":
            case "UZB":
                CircAddDefault(circulation_desk, scopes, circ);
                for (String s : circulation_desk) {
                    if (!s.equals("DEFAULT_CIRC_DESK")) {
                        String[] splitCircDesk = s.split("_");
                        if (!circ.contains(splitCircDesk[0])) {
                            circ.add(splitCircDesk[0]);
                        }
                    }
                }
                break;
            case "VGE":
                CircAddDefault(circulation_desk, scopes, circ);
                for (String s : circulation_desk) {
                    if (!s.equals("DEFAULT_CIRC_DESK")) {
                        String tmp = s.toLowerCase();
                        if (tmp.endsWith("l")) {
                            tmp = tmp.replace("l", "");
                        }
                        if (tmp.endsWith("l_s")) {
                            tmp = tmp.replace("l_s", "");
                        }
                        if (!circ.contains(tmp)) {
                            circ.add(tmp);
                        }
                    }
                }
                break;
            case "TRI":
            case "RRO":
            case "PHZ":
            case "IID":
            case "RZH":
            case "RBE":
            case "HPH":
            case "HES":
            case "EPF":
            case "ZHAW":
            case "USI":
            case "RZS":
            case "BFH":
            case "UNE":
            case "SBK":
            case "ZBS":
            case "FHO":
            case "FNW":
            case "LIB":
            case "ISR":
            case "NETWORK":
                CircAddDefault(circulation_desk, scopes, circ);
                break;
            case "UGE":
                CircAddDefault(circulation_desk, scopes, circ);
                for (String s : circulation_desk) {
                    if (!s.equals("DEFAULT_CIRC_DESK")) {
                        if (s.contains("_")) {
                            String[] splitCircDesk = s.split("_");
                            if (splitCircDesk[1].endsWith("bb") || splitCircDesk[1].endsWith("b1") || splitCircDesk[1].endsWith("b2")) {
                                splitCircDesk[1] = "uge_" + splitCircDesk[1].substring(0, splitCircDesk[1].length() - 2);
                            }
                            if (splitCircDesk[1].endsWith("l") || splitCircDesk[1].endsWith("b")) {
                                splitCircDesk[1] = "uge_" + splitCircDesk[1].substring(0, splitCircDesk.length - 1);
                            }
                            if (!circ.contains(splitCircDesk[1])) {
                                circ.add(splitCircDesk[1]);
                            }
                        }
                        if (s.contains("-")) {
                            String[] splitCircDesk = s.split("-");
                            if (splitCircDesk[1].endsWith("bb") || splitCircDesk[1].endsWith("b1") || splitCircDesk[1].endsWith("b2")) {
                                splitCircDesk[1] = "uge_" + splitCircDesk[1].substring(0, splitCircDesk[1].length() - 2);
                            }
                            if (splitCircDesk[1].endsWith("l") || splitCircDesk[1].endsWith("b")) {
                                splitCircDesk[1] = "uge_" + splitCircDesk[1].substring(0, splitCircDesk[1].length() - 1);
                            }
                            if (!circ.contains(splitCircDesk[1])) {
                                circ.add(splitCircDesk[1]);
                            }
                        }
                    }
                }
                break;
        }
        return circ;
    }

    private void CircAddDefault(String[] circulation_desk, String[] scopes, List<String> circ) {
        for (int i = 0; i < circulation_desk.length; i++) {
            if (circulation_desk[i].equals("DEFAULT_CIRC_DESK")) {
                for (int j = 0; j < scopes.length; j++) {
                    if (!scopes[j].equals("RES_SHARE") && !scopes[j].equals("ILL")) {
                        if (!circ.contains(scopes[j])) {
                            if (!scopes[j].contains("41SLSP_")) {
                                circ.add(scopes[j]);
                            }
                        }
                        if (scopes[j].contains("41SLSP_IID")) {
                            circ.add("iid");
                        }
                        if (scopes[j].contains("41SLSP_ZHK")) {
                            circ.add("E65");
                        }
                        if (scopes[j].contains("41SLSP_RZS")) {
                            circ.add(scopes[j]);
                        }
                        if (scopes[j].contains("ISR")) {
                            circ.add("41SLSP_RRO");
                        }
                    }
                }
            }
        }
    }

    private Parameter createParameter(String type, String scope, String value) {
        Parameter parameter = new Parameter();
        Parameter_Type parameter_type = new Parameter_Type();
        Parameter_Scope parameter_scope = new Parameter_Scope();
        Parameter_Value parameter_value = new Parameter_Value();
        parameter_type.setValue(type);
        parameter_scope.setValue(scope);
        parameter_value.setValue(value);
        parameter.setType(parameter_type);
        parameter.setScope(parameter_scope);
        parameter.setValue(parameter_value);

        return parameter;
    }

    private List<Parameter> createParameterList(String type, String iz, String[] scopes,
                                                String[] circ_desk, String[] service_unit) {
        List<Parameter> parameters = new ArrayList<>();
        switch (type) {
            case ("ServiceUnit"):
                for (int k = 0; k < service_unit.length; k++) {
                    service_unit[k] = service_unit[k].replace("ACQDept", "AcqDept");
                    String service_scope = "";
                    if (!service_unit[k].isEmpty()) {
                        if (service_unit[k].contains("AcqDept")) {
                            service_scope = service_unit[k].replace("AcqDept", "");
                        }
                        if (service_unit[k].contains("BEST")) {
                            service_scope = "Z01";
                        }
                        if (service_unit[k].contains("Traitement")) {
                            continue;
                        }
                        if (service_unit[k].contains("RELIURE")) {
                            continue;
                        }
                        if (service_unit[k].contains("MigEnTraitement")) {
                            continue;
                        }
                        if (service_unit[k].contains("WOT")) {
                            continue;
                        }
                        if (service_unit[k].contains("MeBearbE60")) {
                            service_scope = "E60";
                        }
                        if (service_unit[k].contains("F&S")) {
                            service_scope = "Z01";
                        }
                        parameters.add(createParameter("ServiceUnit", service_scope, service_unit[k]));
                    }
                }
                break;
        }
        return parameters;
    }

    private List<Parameter> createWorkOrderParameterList(String type, String iz, String[] scopes,
                                                         String[] circ_desk, String[] service_unit) {
        List<Parameter> parameters = new ArrayList<>();
        switch (type) {
            case ("ServiceUnit"):
                for (int k = 0; k < service_unit.length; k++) {
                    service_unit[k] = service_unit[k].replace("ACQDept", "AcqDept");
                    String service_scope = "";
                    if (!service_unit[k].isEmpty()) {
                        if (service_unit[k].contains("AcqDept")) {
                            continue;
                        }
                        if (service_unit[k].contains("BEST")) {
                            service_scope = "Z01";
                        }
                        if (service_unit[k].contains("Traitement")) {
                            service_scope = "41SLSP_BCUFR";
                        }
                        if (service_unit[k].contains("RELIURE")) {
                            service_scope = "41SLSP_BCUFR";
                        }
                        if (service_unit[k].contains("MigEnTraitement")) {
                            service_scope = "41SLSP_BCUFR";
                        }
                        if (service_unit[k].contains("MeBearbE60")) {
                            service_scope = "E60";
                        }
                        if (service_unit[k].contains("F&S")) {
                            service_scope = "Z01";
                        }
                        if (service_unit[k].contains("WOT")) {
                            service_scope = "41SLSP_" + iz;
                        }
                        parameters.add(createParameter("ServiceUnit", service_scope, service_unit[k]));
                    }
                }
                break;
        }
        return parameters;
    }

    private Role createRole(String type, String scope, List<Parameter> parameters) {
        Role role = new Role();
        Role_Status status = new Role_Status();
        status.setValue("ACTIVE");
        Role_Scope role_scope = new Role_Scope();
        role_scope.setValue(scope);
        Role_Role_type role_type = new Role_Role_type();
        role_type.setValue(type);

        role.setStatus(status);
        role.setScope(role_scope);
        role.setRole_type(role_type);
        role.setParameter(parameters);

        return role;
    }

    private CSVFormat csvOutputFormatConfiguration() {
        return CSVFormat.DEFAULT
                .withHeader("primary_id", "HTTP_MESSAGE", "TIME")
                .withDelimiter(',')
                .withRecordSeparator("\n")
                .withTrim();
    }

    private CSVFormat csvInputFormatConfiguration() {
        return CSVFormat.DEFAULT
                .withHeader("primary_id", "last_name", "first_name", "e-mail", "preferred_language",
                        "profile", "IZ", "scope", "circulation_desk", "service_unit", "cataloger_level")
                .withFirstRecordAsHeader()
                .withDelimiter(';')
                .withTrim();
    }

    private void writeToFiles(final CSVPrinter csvWriter, String primary_id, String response) throws IOException {
        csvWriter.printRecord(primary_id, response, sdf.format(new Date()));
    }

}
