package com.filipovski.drboson.datasets.domain.model;

public enum JobType {
    STORAGE, // Storing a dataset and making it available for processing
    RDF_READY_STORAGE, // Storing RDF dataset in a triple store for easy manipulation
    RDF_TRANSFORM_STORAGE, // Transforming and storing RDF dataset in a format suitable for processing
    PROCESSING, // Performing operations over a dataset
}
