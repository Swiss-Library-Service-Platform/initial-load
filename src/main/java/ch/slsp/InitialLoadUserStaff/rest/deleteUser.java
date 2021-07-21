package ch.slsp.InitialLoadUserStaff.rest;

import ch.slsp.InitialLoadUserStaff.util.LoggerFactory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
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
@Profile("deleteUser")
public class deleteUser implements Command {

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
    @Value("${rest.api.createUser.auth.key.isr}")
    private String apikeyISR;
    @Value("${rest.api.createUser.auth.key.sand_ubs}")
    private String apikeySandUBS;
    @Value("${rest.api.createUser.auth.key.sand_hph}")
    private String apikeySandHPH;

    @Value("${rest.api.deleteUser.input}")
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
                    String query = primary_id + "?user_id_type=all_unique&apikey=";
                    try {
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
                            query += apikeySUPSI;
                        } else  if (iz.equals("SUPSI")) {
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
                        } else if (iz.equals("IID")) {
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
                        }

                        String endpointURL = baseURL + "/almaws/v1/users/" + query;
                        endURL = endpointURL;
                        restTemplate.delete(endpointURL);
                        LOG.info("Deleted User: " + primary_id);
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
