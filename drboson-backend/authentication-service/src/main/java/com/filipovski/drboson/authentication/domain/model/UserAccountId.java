package com.filipovski.drboson.authentication.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;

public class UserAccountId extends DomainObjectId {

    public UserAccountId() {
        super("");
    }

    public UserAccountId(String id) {
        super(id);
    }
}
