package com.filipovski.drboson.runs.application.data;

import com.filipovski.drboson.runs.domain.model.Dataset;
import com.filipovski.drboson.runs.domain.model.DatasetId;
import com.filipovski.drboson.runs.domain.model.OwnerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DatasetIntegrationRepository extends JpaRepository<Dataset, DatasetId> {

    @Query("select dataset from Dataset dataset where dataset.ownerId = :ownerId and dataset.id = :datasetId")
    Optional<Dataset> findOwnerDataset(@Param("ownerId")OwnerId ownerId,
                                       @Param("datasetId") DatasetId datasetId);
}
