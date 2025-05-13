package me.kisoft.pdf.merger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfMergerService {

    private static final Logger log = LoggerFactory.getLogger(PdfMergerService.class);

    public InputStream mergeFiles(Collection<InputStream> streams) throws IOException {

        String operationId = RandomStringUtils.secureStrong().randomAlphabetic(16);
        List<InputStream> streamList = new ArrayList<>(streams);
        Path mergeDirectory = Files.createTempDirectory("merge-" + operationId);
        log.info("Begin merge operation {}", operationId);
        List<File> filesToMerge = new ArrayList<>();
        for (int i = 0; i < streamList.size(); i++) {
            final Path tempFile = Files.createTempFile(mergeDirectory, RandomStringUtils.randomAlphabetic(16), ".tmp");

            try (FileOutputStream out = new FileOutputStream(tempFile.toFile())) {
                IOUtils.copy(streamList.get(i), out);
            }

            File file = tempFile.toFile();
            try (PDDocument document = Loader.loadPDF(file)) {
                filesToMerge.add(file);
            } catch (IOException ex) {
                log.warn("A Non-PDF file was sent. excluding it from merge operation {}", operationId);
            }
        }

        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        pdfMergerUtility.setDestinationFileName(operationId + "-merged.pdf");
        for (int i = 0; i < filesToMerge.size(); i++) {
            pdfMergerUtility.addSource(filesToMerge.get(i));
        }

        try (ByteArrayOutputStream mergedPDFOutputStream = new ByteArrayOutputStream()) {
            pdfMergerUtility.setDestinationStream(mergedPDFOutputStream);
            pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupTempFileOnly().streamCache);
            log.info("Successfuly Performed merge operation {}", operationId);
            return new ByteArrayInputStream(mergedPDFOutputStream.toByteArray());
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            FileUtils.deleteDirectory(mergeDirectory.toFile());
            streams.forEach(IOUtils::closeQuietly);
            log.info("Complete merge operation {}", operationId);
        }

    }
}
