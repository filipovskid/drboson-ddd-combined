package com.filipovski.drboson.datasets.infrastructure.store;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.filipovski.drboson.datasets.application.exceptions.FileStoreException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;


@Component
public class AmazonS3FileStore implements FileStore {

    private final AmazonS3 s3Client;

    public AmazonS3FileStore(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void save(String bucketName, String key, InputStream input, long contentLength,
                     Optional<Map<String, String>> metadata) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(contentLength);
            metadata.ifPresent(data -> data.forEach(objectMetadata::addUserMetadata));

            PutObjectRequest request = new PutObjectRequest(bucketName, key, input, objectMetadata);
            s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();

            // Should produce different exception which could be handled in application service.
            throw new FileStoreException("A problem occurred while uploading the dataset");
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();

            // Should produce different exception which could be handled in application service.
            throw new FileStoreException("A problem occurred while uploading the dataset");
        }
    }

    @Override
    public S3Object download(String bucketName, String key) {
        GetObjectRequest request = new GetObjectRequest(bucketName, key);

        return s3Client.getObject(request);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void delete(String bucketName, String key) {
        try {
            DeleteObjectRequest request = new DeleteObjectRequest(bucketName, key);
            s3Client.deleteObject(request);
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();

            throw new FileStoreException("Data cannot be deleted at the moment");
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();

            throw new FileStoreException("The data service is unavailable");
        }
    }
}
