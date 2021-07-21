package ch.slsp.InitialLoadUserStaff.rest.createFine;

import ch.slsp.InitialLoadUserStaff.rest.Command;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Barcode;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Owner;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Status;
import ch.slsp.InitialLoadUserStaff.rest.nestedClasses.Type;
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
import org.springframework.web.client.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Profile("createFine")
public class CreateFineCommand implements Command {

    @Value("${rest.api.baseURL}")
    private String baseURL;

    @Value("./fine")
    private String inputFolder;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${rest.api.createFine.auth.key.hph_psb}")
    private String apikeyHPH_PSB;

    @Value("${rest.api.createFine.auth.key.ubs_psb}")
    private String apikeyUBS_PSB;

    @Value("${rest.api.createUser.sleeptimer}")
    private Integer sleep;

    private static final Logger LOG = LoggerFactory.getLogger();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-HH.mm");

    public void execute(String... args) throws Exception {

        Object[] files = Files.walk(Paths.get(String.valueOf(inputFolder)))
                .filter(Files::isRegularFile)
                .toArray();

        for (Object file : files) {
            LOG.info(file.toString());
            Path csvInputFile = (Path) file;
            String output = file.toString().replace("input", "output")
                    .replace(".csv", "");
            Path csvOutputAccepted = Paths.get(output + "Acc-" + sdf.format(new Date()) + ".csv");
            Path csvOutputUnaccepted = Paths.get(output + "Unacc-" + sdf.format(new Date()) + ".csv");
            Path csvOutputAll = Paths.get(output + "All-" + sdf.format(new Date()) + ".csv");
            try (CSVParser csvReader = new CSVParser(Files.newBufferedReader(csvInputFile), csvInputFormatConfiguration());
                 CSVPrinter csvWriterAll =
                         new CSVPrinter(new FileWriter(csvOutputAll.toFile()), csvOutputFormatConfiguration());
                 CSVPrinter csvWriterAccepted =
                         new CSVPrinter(new FileWriter(csvOutputAccepted.toFile()), csvOutputFormatConfiguration());
                 CSVPrinter csvWriterUnaccepted =
                         new CSVPrinter(new FileWriter(csvOutputUnaccepted.toFile()), csvOutputFormatConfiguration())
            ) {
                for (CSVRecord csvRecord : csvReader) {
                    final String primary_id = csvRecord.get("primary_id");
                    final String iz = csvRecord.get("IZ");
                    final String fine = csvRecord.get("amount");
                    final String barcode = csvRecord.get("barcode");
                    final String owner = csvRecord.get("owner");
                    final String type = csvRecord.get("type");
                    final String comment = csvRecord.get("comment");

                    try {
                        CreateFineRequest req = createFine(fine, barcode, owner, type, comment);

                        String apikey ="";
                        if (iz.equals("HPH_PSB")) {
                            apikey = apikeyHPH_PSB;
                        } else if (iz.equals("UBS_PSB")) {
                            apikey = apikeyUBS_PSB;
                        } else {
                            LOG.error("unknown IZ");
                        }

                        String endpointURL = baseURL + primary_id + "/fees?apikey=" + apikey;
                        ResponseEntity<CreateFineResponse> resp =
                                restTemplate.postForEntity(endpointURL, req, CreateFineResponse.class);

                        if (resp.getStatusCodeValue() == 200) {
                            writeToFiles(csvWriterAccepted, primary_id, resp.getBody().toString());
                            writeToFiles(csvWriterAll, primary_id, resp.getBody().toString());
                        }

                        LOG.info("fine created successfully for User: " + primary_id + "\n");
                        TimeUnit.MILLISECONDS.sleep(sleep);

                    } catch (HttpClientErrorException | HttpServerErrorException e) {
                        writeToFiles(csvWriterUnaccepted, primary_id, e.getLocalizedMessage());
                        writeToFiles(csvWriterAll, primary_id, e.getLocalizedMessage());
                        LOG.error(e.getMessage());
                        LOG.error("Fine User: " + primary_id + "failed\n");
                    }

                }
            }
        }
    }

    private CreateFineRequest createFine(String fine, String barcode, String owner, String type, String comment) {
        CreateFineRequest req = new CreateFineRequest();

        req.setLink("");

        Type typ = new Type();
        typ.setValue(type);
        req.setType(typ);

        Status status = new Status();
        status.setValue("ACTIVE");
        req.setStatus(status);

        req.setOriginal_amount(fine);

        Owner own = new Owner();
        own.setValue(owner);
        req.setOwner(own);

        Barcode bar = new Barcode();
        bar.setValue(barcode);
        req.setBarcode(bar);

        req.setComment(comment);

        return req;
    }

    private CSVFormat csvInputFormatConfiguration() {
        return CSVFormat.DEFAULT
                .withHeader("primary_id", "IZ", "amount", "barcode", "type", "owner", "comment")
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
        csvWriter.printRecord(primary_id, response, sdf.format(new Date()));
    }
}

