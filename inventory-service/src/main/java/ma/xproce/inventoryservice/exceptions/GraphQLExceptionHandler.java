package ma.xproce.inventoryservice.exceptions;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        return new GraphQLError() {

            @Override
            public String getMessage() {
                return "Error: " + ex.getMessage(); // Message détaillé de l'exception
            }

            @Override
            public List<SourceLocation> getLocations() {
                // Retourne l'emplacement dans la requête GraphQL où l'erreur s'est produite
                return env.getField().getSourceLocation() != null
                        ? List.of(env.getField().getSourceLocation())
                        : null;
            }

            @Override
            public ErrorClassification getErrorType() {
                // Retourne un type d'erreur personnalisé ou par défaut
                return ErrorType.DataFetchingException;
            }
        };
    }

    private enum ErrorType implements ErrorClassification {
        DataFetchingException, // Type d'erreur par défaut
        CustomError; // Vous pouvez définir des types personnalisés
    }
}
