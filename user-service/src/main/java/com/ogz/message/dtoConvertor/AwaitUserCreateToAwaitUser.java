package com.ogz.message.dtoConvertor;

import org.ogz.dto.AwaitUserCreate;
import org.ogz.model.AwaitUser;

public class AwaitUserCreateToAwaitUser {

    public static AwaitUser convert(AwaitUserCreate create){
        return new AwaitUser(create.getId(),create.getIds());
    }

}
