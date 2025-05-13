package me.kisoft.pdf.merger;

import io.javalin.Javalin;

public class PdfMerger {

    public static void main(String[] args) {

        Javalin.create()
                .post("/merge-multipart", (ctx) -> new PdfMergerController().mergeMultipart(ctx))
                .post("/merge-base64", (ctx) -> new PdfMergerController().mergeBase64(ctx))
                .start(7000);
    }
}
