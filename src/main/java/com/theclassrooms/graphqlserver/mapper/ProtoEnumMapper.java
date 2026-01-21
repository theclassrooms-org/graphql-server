package com.theclassrooms.graphqlserver.mapper;

import java.util.Arrays;

public final class ProtoEnumMapper {

    public static <P extends Enum<P>, A extends Enum<A>> A mapEnum(
            P protoEnum, Class<A> apiEnum
    ) {
        if (protoEnum == null) return null;

        return Arrays.stream(apiEnum.getEnumConstants())
                .filter(a -> a.name().equals(protoEnum.name()))
                .findFirst()
                .orElse(null);
    }
}
