package com.filipovski.drboson.runs.domain.repository;

import com.filipovski.drboson.runs.domain.model.OwnerId;
import com.filipovski.drboson.runs.domain.model.ProjectId;
import com.filipovski.drboson.runs.domain.model.Run;
import com.filipovski.drboson.runs.domain.model.RunId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RunRepository extends JpaRepository<Run, RunId> {

    @Query("select run from Run run where run.ownerId = :ownerId and run.projectId = :projectId")
    List<Run> findProjectRuns(@Param("ownerId") OwnerId ownerId,
                              @Param("projectId") ProjectId projectId);

    @Query("select run from Run run where run.ownerId  = :ownerId and run.id = :runId")
    Optional<Run> findOwnerRun(@Param("ownerId") OwnerId ownerId,
                               @Param("runId") RunId runId);

    @Modifying
    @Transactional
    @Query("delete from Run run where run.ownerId = :ownerId and run.id = :runId")
    void deleteOwnerRun(@Param("ownerId") OwnerId ownerId,
                        @Param("runId") RunId runId);
}
