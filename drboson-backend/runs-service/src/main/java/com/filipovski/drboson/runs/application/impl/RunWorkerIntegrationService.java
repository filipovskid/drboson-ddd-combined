package com.filipovski.drboson.runs.application.impl;

import com.filipovski.drboson.runs.domain.event.MetricLogCreatedEvent;
import com.filipovski.drboson.runs.domain.event.RunFileGeneratedEvent;
import com.filipovski.drboson.runs.domain.event.RunStatusChangedEvent;
import com.filipovski.drboson.runs.domain.model.Run;
import com.filipovski.drboson.runs.domain.model.files.RunFile;
import com.filipovski.drboson.runs.domain.model.metrics.MetricLog;
import com.filipovski.drboson.runs.domain.repository.RunRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class RunWorkerIntegrationService {

    private final RunRepository runRepository;

    public RunWorkerIntegrationService(RunRepository runRepository) {
        this.runRepository = runRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onMetricLogCreatedEvent(@NonNull MetricLogCreatedEvent event) {
        Run run = runRepository.findById(event.getRunId()).orElseThrow(() -> new RuntimeException("Run not found"));
        MetricLog log = MetricLog.from(run, event.getLog());
        run.addMetricLog(log);

        runRepository.save(run);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onRunFileGeneratedEvent(@NonNull RunFileGeneratedEvent event) {
        Run run = runRepository.findById(event.getRunId()).orElseThrow(() -> new RuntimeException("Run not found"));
        RunFile file = RunFile.from(run, event.getFileName(), event.getDataSource());
        run.addRunFile(file);

        runRepository.save(run);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onRunStatusChangedEvent(@NonNull RunStatusChangedEvent event) {
        Run run = runRepository.findById(event.getRunId()).orElseThrow(() -> new RuntimeException("Run not found"));
        run.updateRunStatus(event.getStatus());

        runRepository.save(run);
    }

}
