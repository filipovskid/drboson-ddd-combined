package com.filipovski.drboson.datasets.domain.repository;

import com.filipovski.drboson.datasets.domain.model.Dataset;
import com.filipovski.drboson.datasets.domain.model.DatasetId;
import com.filipovski.drboson.datasets.domain.model.OwnerId;
import com.filipovski.drboson.datasets.domain.model.OwnerProjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DatasetRepository extends JpaRepository<Dataset, DatasetId> {

    @Query("select dataset from Dataset dataset where dataset.ownerId = :ownerId and dataset.ownerProjectId = :projectId")
    List<Dataset> findProjectDatasets(@Param("ownerId")OwnerId ownerId,
                                      @Param("projectId") OwnerProjectId ownerProjectId);

    @Query("select dataset from Dataset dataset where dataset.ownerId = :ownerId and dataset.id = :datasetId")
    Optional<Dataset> findOwnerDataset(@Param("ownerId") OwnerId ownerId,
                                       @Param("datasetId") DatasetId datasetId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Dataset dataset WHERE dataset.ownerId = :ownerId and dataset.id = :datasetId")
    void deleteOwnerDataset(@Param("ownerId") OwnerId ownerId,
                            @Param("datasetId") DatasetId projectId);
}
