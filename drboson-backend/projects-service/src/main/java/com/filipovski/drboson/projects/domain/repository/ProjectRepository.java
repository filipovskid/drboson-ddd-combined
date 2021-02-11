package com.filipovski.drboson.projects.domain.repository;

import com.filipovski.drboson.projects.domain.model.OwnerId;
import com.filipovski.drboson.projects.domain.model.Project;
import com.filipovski.drboson.projects.domain.model.ProjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, ProjectId> {

    @Query("select p from Project p where p.ownerId = :ownerId")
    List<Project> findOwnerProjects(@Param("ownerId") OwnerId ownerId);

    @Query("select p from Project p where p.ownerId = :ownerId and p.id = :projectId")
    Optional<Project> findOwnerProject(@Param("ownerId") OwnerId ownerId,
                                        @Param("projectId") ProjectId projectId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Project p WHERE p.ownerId = :ownerId and p.id = :projectId")
    void deleteOwnerProject(@Param("ownerId") OwnerId ownerId,
                           @Param("projectId") ProjectId projectId);
}
