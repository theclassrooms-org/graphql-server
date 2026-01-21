package com.theclassrooms.graphqlserver.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.jspecify.annotations.Nullable;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
public class GraphqlExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected @Nullable GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.DataFetchingException)
                .message(ex.getMessage())
                .build();
    }
}
