package com.filipovski.drboson.runs.application.impl;

import com.filipovski.drboson.runs.application.dtos.DownloadRunFileResponse;
import com.filipovski.drboson.runs.application.dtos.RunFileDto;

import java.util.List;

public interface RunFilesService {
    List<RunFileDto> getRunFiles(String ownerId, String runId) throws Exception;

    DownloadRunFileResponse downloadRunFile(String ownerId, String runId, String fileId) throws Exception;
}
