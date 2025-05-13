package me.kisoft.pdf.merger;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.UploadedFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class PdfMergerController {

    private final PdfMergerService mergerService = new PdfMergerService();

    public void mergeMultipart(Context ctx) {
        List<UploadedFile> uploadedFiles = ctx.uploadedFiles();
        List<InputStream> uploadedInputStreams = uploadedFiles.stream()
                .map(UploadedFile::content)
                .collect(Collectors.toList());
        merge(ctx, uploadedInputStreams);
    }

    private void merge(Context ctx, List<InputStream> files) {
        if (files == null || files.isEmpty()) {
            throw new BadRequestResponse("No Files Uploaded");
        }
        try {
            InputStream mergedFile = mergerService.mergeFiles(files);
            ctx.result(mergedFile);
            ctx.contentType(ContentType.APPLICATION_PDF);
            ctx.header("Content-Disposition", "attachment; filename=\"merged.pdf\"");
        } catch (IOException ex) {
            throw new InternalServerErrorResponse(ex.getMessage());
        }
    }

    public void mergeBase64(Context ctx) {
        Base64MergeRequest mergeRequest = ctx.bodyAsClass(Base64MergeRequest.class);
        List<InputStream> filesToMerge = new ArrayList<>();
        for (int i = 0; i < mergeRequest.getFiles().size(); i++) {
            byte[] decode = Base64.getDecoder().decode(mergeRequest.getFiles().get(i));
            filesToMerge.add(new ByteArrayInputStream(decode));
        }
        merge(ctx, filesToMerge);
    }
}
