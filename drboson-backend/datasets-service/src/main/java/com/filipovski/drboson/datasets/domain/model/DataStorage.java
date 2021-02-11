package com.filipovski.drboson.datasets.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.ValueObject;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
public class DataStorage implements ValueObject {

    @Enumerated(EnumType.STRING)
    private DataStorageType type;

    @Enumerated(EnumType.STRING)
    private StorageStatus status;

    @AttributeOverrides({
            @AttributeOverride(name = "MIMEType", column = @Column(name = "source_mimetype")),
            @AttributeOverride(name = "location", column = @Column(name = "source_location")),
            @AttributeOverride(name = "type", column = @Column(name = "source_type"))
    })
    @Embedded
    private DataSource source;

    protected DataStorage() { }

    private DataStorage(DataStorageType type, DataSource source) {
        this.type = type;
        this.source = source;
        this.status = StorageStatus.COMPLETED;
    }

    private DataStorage(DataStorageType type, StorageStatus status) {
        this.type = type;
        this.status = status;
    }

    public static DataStorage createStorage(DataStorageType type, DataSource source) {
        return DataStorage.from(type, source);
    }

    public static DataStorage createStorate(DataStorageType type) {
        return DataStorage.from(type, StorageStatus.IDLE);
    }

    public static DataStorage initiateStorage(DataStorageType type) {
        return DataStorage.from(type, StorageStatus.RUNNING);
    }

    public DataStorage updateStorageStatus(StorageStatus status) {
        DataStorage storage = DataStorage.from(type, status);
        storage.source = source;

        return storage;
    }

    public DataSource source() {
        return source;
    }

    public StorageStatus status() {
        return status;
    }

    public boolean isAvailable() {
        return status != StorageStatus.COMPLETED;
    }

    public boolean isPending() {
        return status == StorageStatus.RUNNING;
    }

    private static DataStorage from(DataStorageType type, DataSource source) {
        Objects.requireNonNull(type, "Storage type must not be null");
        Objects.requireNonNull(source, "Data source must not be null");

        return new DataStorage(type, source);
    }

    private static DataStorage from(DataStorageType type, StorageStatus status) {
        Objects.requireNonNull(type, "Storage type must not be null");

        return new DataStorage(type, status);
    }

}
