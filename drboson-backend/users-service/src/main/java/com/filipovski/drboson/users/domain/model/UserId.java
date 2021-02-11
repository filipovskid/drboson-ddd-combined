package com.filipovski.drboson.users.domain.model;

import com.filipovski.drboson.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;

@Embeddable
public class UserId extends DomainObjectId {

    public  UserId() {
        super("");
    }

    public UserId(String id) {
        super(id);
    }
}
