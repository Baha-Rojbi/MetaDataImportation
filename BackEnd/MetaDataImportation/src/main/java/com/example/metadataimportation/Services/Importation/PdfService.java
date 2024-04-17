package com.example.metadataimportation.Services.Importation;

import com.example.metadataimportation.Entities.Importation.DataTable;
import com.example.metadataimportation.Entities.Importation.Schema;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.Set;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
@Service
public class PdfService implements IPdfService{
    @Autowired
    private DataService dataTableService;
    private final String ACCOUNT_SID = "AC1d39955a23413357ea2300498f56f78c";
    private final String AUTH_TOKEN = "2907cab12bb6e2a9fcc603c3d8e85bf7";
    private final String FROM_PHONE_NUMBER = "+19382536454";
    @Override
    public byte[] generateDataTablePdf(Long tableId) {
        // Initialize Twilio client
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Fetch the DataTable and Schemas from the database
        DataTable dataTable = dataTableService.getDataTableById(tableId).orElseThrow();

        // Create a PDF document
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add Title
            Paragraph title = new Paragraph(dataTable.getName());
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            // Add DataTable Details
            document.add(new Paragraph("DataTable Details:"));
            document.add(new Paragraph("Name: " + dataTable.getName()));
            document.add(new Paragraph("Creator: " + dataTable.getCreator()));
            document.add(new Paragraph("Description: " + dataTable.getDescription()));
            // Add other details as needed

            // Add Schema Details
            Set<Schema> schemas = dataTable.getSchemas();
            if (!schemas.isEmpty()) {
                document.add(new Paragraph("DataTable Schemas:"));
                for (Schema schema : schemas) {
                    document.add(new Paragraph("Schema: " + schema.getName()));
                    document.add(new Paragraph("Type: " + schema.getType()));
                    document.add(new Paragraph("Tags: " + schema.getTags()));
                    // Add other schema details as needed
                }
            }

            document.close();

            // Send SMS notification
            sendSmsNotification("PDF Downloaded for DataTable: " + dataTable.getName() +" by : " +dataTable.getCreator());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    // Method to send SMS notification using Twilio
    private void sendSmsNotification(String message) {
        // Send SMS using Twilio
        Message.creator(
                new PhoneNumber("+21653802106"),
                new PhoneNumber(FROM_PHONE_NUMBER),
                message
        ).create();
    }
}

