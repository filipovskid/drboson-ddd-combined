package com.filipovski.drboson.sharedkernel.domain.base;

import java.time.Instant;

public interface DomainEvent extends DomainObject {
    
    Instant occuredOn();
}
