package com.filipovski.drboson.sharedkernel.domain.base;

import java.io.Serializable;

public interface IdentifiableDomainObject<ID extends Serializable> extends DomainObject {

    ID id();
}
