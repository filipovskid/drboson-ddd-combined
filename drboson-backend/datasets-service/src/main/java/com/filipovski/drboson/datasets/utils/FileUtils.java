package com.filipovski.drboson.datasets.utils;

import org.apache.any23.mime.MIMEType;
import org.apache.any23.mime.TikaMIMETypeDetector;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    public static final List<String> rdfMIMETypes = Arrays.asList("application/rdf+xml", "text/n3", "text/rdf+n3",
            "application/n3", "text/x-nquads", "text/rdf+nq", "text/nq", "application/nq", "text/turtle",
            "application/x-turtle", "application/turtle", "application/trix");

    public static final List<String> commonMIMETypes = Arrays.asList("application/zip", "text/plain", "text/csv",
            "text/tsv", "drboson/data");

    public static String getContentType(MultipartFile file, String filename) {
        Tika tika = new Tika();
        TikaMIMETypeDetector rdfDetector = new TikaMIMETypeDetector();

        try {
            String contentType = tika.detect(file.getBytes());
            MIMEType rdfMIMEType = rdfDetector.guessMIMEType(filename,
                    new BufferedInputStream(file.getInputStream()), MIMEType.parse(file.getContentType()));
            contentType =  rdfMIMEType == null ? contentType : rdfMIMEType.getFullType();

            return contentType;
        } catch (IOException e) {
            throw new IllegalStateException("Can't get content type !");
        }
    }
}
