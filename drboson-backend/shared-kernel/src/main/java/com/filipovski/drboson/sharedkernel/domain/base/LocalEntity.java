package com.filipovski.drboson.sharedkernel.domain.base;

public abstract class LocalEntity<ID extends DomainObjectId> extends AbstractEntity<ID> {

    public LocalEntity() {}

    public LocalEntity(ID id) {
        super(id);
    }
}
