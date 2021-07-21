package ch.slsp.InitialLoadUserStaff.rest.addRole;

import ch.slsp.InitialLoadUserStaff.rest.Command;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.user_note.Note_type;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.user_note.User_Note;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.user_role.*;
import ch.slsp.InitialLoadUserStaff.util.LoggerFactory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.*;

@Component
@Profile("addRole")

public class addRole implements Command {

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

    @Value("${rest.api.addRole.input}")
    private String inputFolder;

    @Override
    public void execute(String... args) throws Exception {
        Object[] files = Files.walk(Paths.get(String.valueOf(inputFolder)))
                .filter(Files::isRegularFile)
                .toArray();
        for (Object file : files) {
            LOG.info(file);
            Path csvInputFile = (Path) file;
            try (CSVParser csvReader = new CSVParser(Files.newBufferedReader(csvInputFile), csvInputFormatConfiguration())) {
                LOG.info("Input-File: " + csvInputFile);
                for (CSVRecord csvRecord : csvReader) {

                    //get properties from the csv-file
                    final String primary_id = csvRecord.get("primary_id");
                    String iz = csvRecord.get("IZ");
                    final String role = csvRecord.get("role");

                    try {
                        LOG.info("User: " + primary_id);
                        StringBuilder query = new StringBuilder(primary_id + "?user_id_type=all_unique&apikey=");
                        if (iz.contains("NETWORK")) {
                            query.append(apikeyNETWORK);
                        } else if (iz.contains("UGE")) {
                            query.append(apikeyUGE);
                        } else if (iz.contains("ETH")) {
                            query.append(apikeyETH);
                        } else if (iz.contains("UBS")) {
                            query.append(apikeyUBS);
                        } else if (iz.contains("RZS")) {
                            query.append(apikeyRZS);
                        } else if (iz.contains("HSG")) {
                            query.append(apikeyHSG);
                        } else if (iz.contains("USI")) {
                            query.append(apikeyUSI);
                        } else if (iz.contains("UZB")) {
                            query.append(apikeyUZB);
                        } else if (iz.contains("BCU")) {
                            query.append(apikeyBCUFR);
                        } else if (iz.contains("BCUFR")) {
                            query.append(apikeyBCUFR);
                        } else if (iz.contains("ZHAW")) {
                            iz = "ZAW";
                            query.append(apikeyZAW);
                        } else if (iz.contains("ZAW")) {
                            query.append(apikeyZAW);
                        } else if (iz.contains("UBE")) {
                            query.append(apikeyUBE);
                        } else if (iz.contains("ZHK")) {
                            query.append(apikeyZHK);
                        } else if (iz.contains("BFH")) {
                            query.append(apikeyBFH);
                        } else if (iz.contains("HES")) {
                            query.append(apikeyHES);
                        } else if (iz.contains("FHO")) {
                            query.append(apikeyFHO);
                        } else if (iz.contains("EPF")) {
                            query.append(apikeyEPF);
                        } else if (iz.contains("UNE")) {
                            query.append(apikeyUNE);
                        } else if (iz.contains("FNW")) {
                            query.append(apikeyFNW);
                        } else if (iz.contains("SUP")) {
                            iz = "SUP";
                            query.append(apikeySUPSI);
                        } else if (iz.contains("HPH")) {
                            query.append(apikeyHEPH);
                        } else if (iz.contains("PHZ")) {
                            query.append(apikeyPHZ);
                        } else if (iz.contains("LIB")) {
                            query.append(apikeyLIB);
                        } else if (iz.contains("IID")) {
                            query.append(apikeyIID);
                        } else if (iz.contains("VGE")) {
                            query.append(apikeyVGE);
                        } else if (iz.contains("RRO")) {
                            query.append(apikeyRRO);
                        } else if (iz.contains("RZH")) {
                            query.append(apikeyRZH);
                        } else if (iz.contains("RBE")) {
                            query.append(apikeyRBE);
                        } else if (iz.contains("ZBS")) {
                            query.append(apikeyZBS);
                        } else if (iz.contains("SBK")) {
                            query.append(apikeySBK);
                        } else if (iz.contains("TRI")) {
                            query.append(apikeyTRI);
                        } else if (iz.equals("ISR")) {
                            query.append(apikeyISR);
                        } else if (iz.equals("Sandbox_UBS")) {
                            query.append(apikeySandUBS);
                        } else if (iz.equals("Sandbox_HPH")) {
                            query.append(apikeySandHPH);
                        } else if (iz.equals("Sandbox_NETWORK")) {
                            query.append(apikeySandNETWORK);
                        }

                        String endpointURL = baseURL + "/almaws/v1/users/" + query;
                        JsonObject user = restTemplate.getForObject(endpointURL, JsonObject.class);
                        assert user != null;

                        //User Roles
                        String defaultScope = "41SLSP_" + iz;
                        Role tmpRole = createRole(role, defaultScope, null);
                        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                        user.getAsJsonArray("user_role").add(gson.toJsonTree(tmpRole));

                        //User notes
                        User_Note user_note = new User_Note();
                        Note_type note_type = new Note_type();
                        note_type.setValue("OTHER");
                        user_note.setNote_type(note_type);
                        String text = "IZ rights for role: " + role;
                        user_note.setNote_text(text);

                        user.getAsJsonArray("user_note").add(gson.toJsonTree(user_note));
                        restTemplate.put(endpointURL, user);
                        LOG.info("Role Update Successful : " + user);

                        TimeUnit.MILLISECONDS.sleep(sleep);
                    } catch (HttpClientErrorException | HttpServerErrorException e) {
                        LOG.error(e.getMessage());
                        LOG.error("Update User: " + primary_id + " failed\n");
                    }
                }
            }
        }
    }

    private CSVFormat csvInputFormatConfiguration() {
        return CSVFormat.DEFAULT
                .withHeader("primary_id", "IZ", "role")
                .withFirstRecordAsHeader()
                .withDelimiter(';')
                .withTrim();
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

}
