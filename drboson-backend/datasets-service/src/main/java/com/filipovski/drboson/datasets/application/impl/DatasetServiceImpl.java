package com.filipovski.drboson.datasets.application.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.filipovski.drboson.datasets.application.DatasetService;
import com.filipovski.drboson.datasets.application.dtos.CreateDatasetRequest;
import com.filipovski.drboson.datasets.application.dtos.DatasetDto;
import com.filipovski.drboson.datasets.application.dtos.DownloadDatasetResponse;
import com.filipovski.drboson.datasets.config.AmazonS3Config;
import com.filipovski.drboson.datasets.domain.model.*;
import com.filipovski.drboson.datasets.domain.repository.DatasetRepository;
import com.filipovski.drboson.datasets.infrastructure.store.FileStore;
import com.filipovski.drboson.datasets.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DatasetServiceImpl implements DatasetService {

    public DatasetRepository datasetRepository;
    public AmazonS3Config amazonS3Config;
    public FileStore fileStore;

    public DatasetServiceImpl(DatasetRepository datasetRepository,
                              AmazonS3Config amazonS3Config,
                              FileStore fileStore) {
        this.datasetRepository = datasetRepository;
        this.amazonS3Config = amazonS3Config;
        this.fileStore = fileStore;
    }

    @Override
    public DatasetDto createDataset(String ownerId, CreateDatasetRequest request) throws Exception {
        MultipartFile file = request.getFile();
        Objects.requireNonNull(file, "No file uploaded");

        if(file.isEmpty())
            throw new IllegalArgumentException("File must not be empty");

        String fileMIMEType = FileUtils.getContentType(file, request.getName());
        DataSource dataSource = DataSource.from(DataSource.generateLocation(request.getName()), fileMIMEType);

        try {
            fileStore.save(amazonS3Config.getDatasetBucketName(), dataSource.location(),
                    file.getInputStream(),
                    file.getSize(),
                    Optional.empty());

            Dataset dataset = Dataset.builder()
                    .ownerId(ownerId)
                    .ownerProjectId(request.getProjectId())
                    .name(request.getName())
                    .description(request.getName())
                    .dataSource(dataSource)
                    .build();

//          Event published after persisting the object
            dataset = datasetRepository.save(dataset);

            return DatasetDto.from(dataset);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<DatasetDto> getProjectDatasets(String ownerId, String projectId) {
        return datasetRepository.findProjectDatasets(OwnerId.from(ownerId), OwnerProjectId.from(projectId)).stream()
                .map(DatasetDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public DatasetDto getDataset(String ownerId, String datasetId) throws Exception {
        Dataset dataset = datasetRepository.findOwnerDataset(OwnerId.from(ownerId), DatasetId.from(datasetId))
                .orElseThrow(() -> new Exception("Dataset not found"));

        return DatasetDto.from(dataset);
    }

    @Override
    public DownloadDatasetResponse downloadDatasetContent(String ownerId, String datasetId)
            throws Exception {
        Dataset dataset = datasetRepository.findOwnerDataset(OwnerId.from(ownerId), DatasetId.from(datasetId))
                .orElseThrow(() -> new Exception("Dataset not found"));

        S3Object object = fileStore.download(amazonS3Config.getDatasetBucketName(), dataset.getDataSource().location());
        S3ObjectInputStream dataStream = object.getObjectContent();

        StreamingResponseBody streamingResponse = outputStream -> {
            int noBytesRead;
            byte[] data = new byte[1024];

            while ((noBytesRead = dataStream.read(data)) != -1)
                outputStream.write(data, 0, noBytesRead);

            object.close();
        };

        return DownloadDatasetResponse.from(dataset, streamingResponse);
    }


    @Override
    public void deleteDataset(String ownerId, String datasetId) throws Exception {
        Dataset dataset = datasetRepository.findOwnerDataset(OwnerId.from(ownerId), DatasetId.from(datasetId))
                .orElseThrow(() -> new Exception("Dataset not found"));

        // No events
        fileStore.delete(amazonS3Config.getDatasetBucketName(), dataset.getDataSource().location());
        datasetRepository.deleteOwnerDataset(OwnerId.from(ownerId), dataset.id());
    }
}
