package com.filipovski.drboson.runs.infrastructure.store;

import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface FileStore {
    void save(String bucketName, String key, InputStream input, long contentLength,
              Optional<Map<String, String>> metadata);

    S3Object download(String bucketName, String key);

    void delete(String bucketName, String key);
}
