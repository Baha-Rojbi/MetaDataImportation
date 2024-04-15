package com.example.metadataimportation.Services;

import com.example.metadataimportation.Entities.DataTable;
import com.example.metadataimportation.Entities.Schema;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.Set;

@Service
public class PdfService {
    @Autowired
    private DataService dataTableService;
    public byte[] generateDataTablePdf(Long tableId) {
        // Fetch the DataTable and Schemas from the database
        // Assume dataTableService is an injected service that allows fetching DataTables
        DataTable dataTable = dataTableService.getDataTableById(tableId).orElseThrow();
        Set<Schema> schemas = dataTable.getSchemas();

        // Create a PDF document
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            // Add content to the PDF here using document.add(...)
            document.add(new Paragraph("DataTable Details: " + dataTable.getName()));
            // Add Schema details
            for (Schema schema : schemas) {
                document.add(new Paragraph("Schema: " + schema.getName()));
                // Include other details and tags as needed
            }
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}

