package ch.slsp.InitialLoadUserStaff.rest.updateCatalogerLvl;

import ch.slsp.InitialLoadUserStaff.rest.Command;
import ch.slsp.InitialLoadUserStaff.util.LoggerFactory;
import com.google.gson.JsonArray;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Profile("updateCataLvl")
public class UpdateCataLvlCommand implements Command {

    private static final Logger LOG = LoggerFactory.getLogger();

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss.SSSS");
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

    @Value("${rest.api.fixCataLvl.input}")
    private String inputFolder;

    @Override
    public void execute(String... args) throws Exception {
        String endURL = "";
        Object[] files = Files.walk(Paths.get(String.valueOf(inputFolder)))
                .filter(Files::isRegularFile)
                .toArray();
        for (int i = 0; i < files.length; i++) {
            Path csvInputFile = (Path) files[i];
            try (CSVParser csvReader = new CSVParser(Files.newBufferedReader(csvInputFile), csvInputFormatConfiguration())) {
                for (CSVRecord csvRecord : csvReader) {
                    final String primary_id = csvRecord.get("primary_id");
                    String iz = csvRecord.get("IZ");
                    final String cataloger_level = csvRecord.get("cataloger_level");
                    final String profile = csvRecord.get("profile");
                    String query = primary_id + "?user_id_type=all_unique&apikey=";
                    try {
                        if (iz.contains("NETWORK")) {
                            query += apikeyNETWORK;
                        } else if (iz.contains("UGE")) {
                            query += apikeyUGE;
                        } else if (iz.contains("ETH")) {
                            query += apikeyETH;
                        } else if (iz.contains("UBS")) {
                            query += apikeyUBS;
                        } else if (iz.contains("RZS")) {
                            query += apikeyRZS;
                        } else if (iz.contains("HSG")) {
                            query += apikeyHSG;
                        } else if (iz.contains("USI")) {
                            query += apikeyUSI;
                        } else if (iz.contains("UZB")) {
                            query += apikeyUZB;
                        } else if (iz.contains("BCU")) {
                            query += apikeyBCUFR;
                        } else if (iz.contains("BCUFR")) {
                            query += apikeyBCUFR;
                        } else if (iz.contains("ZHAW")) {
                            iz = "ZAW";
                            query += apikeyZAW;
                        } else if (iz.contains("ZAW")) {
                            query += apikeyZAW;
                        } else if (iz.contains("UBE")) {
                            query += apikeyUBE;
                        } else if (iz.contains("ZHK")) {
                            query += apikeyZHK;
                        } else if (iz.contains("BFH")) {
                            query += apikeyBFH;
                        } else if (iz.contains("HES")) {
                            query += apikeyHES;
                        } else if (iz.contains("FHO")) {
                            query += apikeyFHO;
                        } else if (iz.contains("EPF")) {
                            query += apikeyEPF;
                        } else if (iz.contains("UNE")) {
                            query += apikeyUNE;
                        } else if (iz.contains("FNW")) {
                            query += apikeyFNW;
                        } else if (iz.contains("SUP")) {
                            query += apikeySUPSI;
                        } else  if (iz.contains("SUPSI")) {
                            query += apikeySUPSI;
                        } else if (iz.contains("HPH")) {
                            query += apikeyHEPH;
                        } else if (iz.contains("PHZ")) {
                            query += apikeyPHZ;
                        } else if (iz.contains("LIB")) {
                            query += apikeyLIB;
                        } else if (iz.contains("IID")) {
                            query += apikeyIID;
                        } else if (iz.contains("VGE")) {
                            query += apikeyVGE;
                        } else if (iz.contains("RRO")) {
                            query += apikeyRRO;
                        } else if (iz.contains("RZH")) {
                            query += apikeyRZH;
                        } else if (iz.contains("RBE")) {
                            query += apikeyRBE;
                        } else if (iz.contains("ZBS")) {
                            query += apikeyZBS;
                        } else if (iz.contains("SBK")) {
                            query += apikeySBK;
                        } else if (iz.contains("TRI")) {
                            query += apikeyTRI;
                        }
                        if (cataloger_level.isEmpty()) {
                            if (profile.contains("slsp-cat") || profile.contains("slsp-cat-plus") || profile.contains("slsp-cat-admin")) {
                                String endpointURL = baseURL + "/almaws/v1/users/" + query;
                                endURL = endpointURL;
                                JSONObject user = restTemplate.getForObject(endpointURL, JSONObject.class);
                                LOG.info("OLD CATALOGER LVL: " + user.get("cataloger_level"));
                                if (user.get("cataloger_level").toString().contains("00")) {
                                    JSONObject newCat = new JSONObject();
                                    newCat.put("value", "20");
                                    user.put("cataloger_level", newCat);
                                    restTemplate.put(endpointURL, user);
                                    JSONObject newUser= restTemplate.getForObject(endpointURL, JSONObject.class);
                                    LOG.info("NEW CATALOGER LVL: " + newUser.get("cataloger_level"));
                                    LOG.info("Fixed cataloger_level for: " + primary_id + "\n");
                                }
                            }
                        }

                    } catch (HttpClientErrorException | HttpServerErrorException e) {
                        LOG.error(e);
                        LOG.error("ENDPOINT-URL:" + endURL);
                    }
                }
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOG.error(e);
                LOG.error("ENDPOINT-URL:" + endURL);
            }
        }
    }

    private CSVFormat csvInputFormatConfiguration() {
        return CSVFormat.DEFAULT
                .withHeader("primary_id", "HTTP_MESSAGE", "TIME")
                .withFirstRecordAsHeader()
                .withDelimiter(';')
                .withTrim();
    }

    private CSVFormat csvOutputFormatConfiguration() {
        return CSVFormat.DEFAULT
                .withHeader("primary_id", "HTTP_MESSAGE", "TIME")
                .withDelimiter(';')
                .withRecordSeparator("\n")
                .withTrim();
    }

    private void writeToFiles(final CSVPrinter csvWriter, String primary_id, String response) throws IOException {
        csvWriter.printRecord(primary_id,response,sdf.format(new Date()));
    }
}