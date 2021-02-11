package com.filipovski.drboson.runs.application.exceptions;

public class FileStoreException extends RuntimeException {

    public FileStoreException() {
        super("A problem occured while handling the file");
    }

    public FileStoreException(String message) {
        super(message);
    }
}
