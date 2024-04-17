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

@Service
public class PdfService implements IPdfService{
    @Autowired
    private DataService dataTableService;
    @Override
    public byte[] generateDataTablePdf(Long tableId) {
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
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}

