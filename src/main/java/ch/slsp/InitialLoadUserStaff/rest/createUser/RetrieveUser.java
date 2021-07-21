package ch.slsp.InitialLoadUserStaff.rest.createUser;

import ch.slsp.InitialLoadUserStaff.rest.Command;
import ch.slsp.InitialLoadUserStaff.util.LoggerFactory;
import org.apache.http.HttpStatus;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import static org.springframework.boot.Banner.Mode.LOG;

@Component
@Profile("RetrieveUser")
public class RetrieveUser implements Command {

    private static final Logger LOG = LoggerFactory.getLogger();

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss.SSSS");
    @Autowired
    private RestTemplate restTemplate;

    @Value("${rest.api.baseURL}")
    private String baseURL;

    @Value("${rest.api.createUser.auth.key.isr}")
    private String apikey;


    @Override
    public void execute(String... args) throws Exception {
        String endURL = "";
        try {
            String query= "?limit=10&offset=0&order_by=last_name%2C%20first_name%2C%20primary_id&apikey=" + apikey;
            String endpointURL = baseURL + "/almaws/v1/users" + query;
            endURL = endpointURL;
            ResponseEntity<String> getUser = restTemplate.getForEntity(endpointURL, String.class);
            LOG.info(getUser);
            LOG.info("sucess");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOG.error("Unknown Error" + e);
        }

        LOG.info("eoc\n");
        LOG.info(endURL);

    }


}
