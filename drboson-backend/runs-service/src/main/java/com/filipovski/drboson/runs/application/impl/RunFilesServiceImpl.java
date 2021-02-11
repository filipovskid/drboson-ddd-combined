package com.filipovski.drboson.runs.application.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.filipovski.drboson.runs.application.dtos.DownloadRunFileResponse;
import com.filipovski.drboson.runs.application.dtos.RunFileDto;
import com.filipovski.drboson.runs.config.AmazonS3Config;
import com.filipovski.drboson.runs.domain.model.OwnerId;
import com.filipovski.drboson.runs.domain.model.Run;
import com.filipovski.drboson.runs.domain.model.RunId;
import com.filipovski.drboson.runs.domain.model.files.RunFile;
import com.filipovski.drboson.runs.domain.repository.RunRepository;
import com.filipovski.drboson.runs.infrastructure.store.FileStore;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RunFilesServiceImpl implements RunFilesService {

    private final RunRepository runRepository;
    private final FileStore fileStore;
    private final AmazonS3Config amazonS3Config;

    public RunFilesServiceImpl(RunRepository runRepository, FileStore fileStore, AmazonS3Config amazonS3Config) {
        this.runRepository = runRepository;
        this.fileStore = fileStore;
        this.amazonS3Config = amazonS3Config;
    }

    @Override
    public List<RunFileDto> getRunFiles(String ownerId, String runId) throws Exception {
        Run run = runRepository.findOwnerRun(OwnerId.from(ownerId), RunId.from(runId))
                .orElseThrow(() -> new Exception("Run not found"));

        return run.getFiles().stream().map(RunFileDto::from).collect(Collectors.toList());
    }

    @Override
    public DownloadRunFileResponse downloadRunFile(String ownerId, String runId, String fileId) throws Exception {
        Run run = runRepository.findOwnerRun(OwnerId.from(ownerId), RunId.from(runId))
                .orElseThrow(() -> new Exception("Run not found"));
        RunFile file = run.getFiles().stream().filter(f -> f.id().getId().equals(fileId)).findFirst()
                .orElseThrow(() -> new Exception("File not found"));

        S3Object object = fileStore.download(amazonS3Config.getRunFilesBucketName(), file.getDataSource().location());
        S3ObjectInputStream dataStream = object.getObjectContent();

        StreamingResponseBody streamingResponse = outputStream -> {
            int noBytesRead;
            byte[] data = new byte[1024];

            while ((noBytesRead = dataStream.read(data)) != -1)
                outputStream.write(data, 0, noBytesRead);

            object.close();
        };

        return DownloadRunFileResponse.from(file, streamingResponse);
    }
}
