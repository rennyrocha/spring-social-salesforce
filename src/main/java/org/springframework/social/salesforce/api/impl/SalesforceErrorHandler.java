package org.springframework.social.salesforce.api.impl;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.*;
import org.springframework.social.salesforce.api.InvalidIDException;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Umut Utkan
 */
public class SalesforceErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        Map<String, Object> errorDetails = extractErrorDetailsFromResponse(response);
        if (errorDetails == null) {
            handleUncategorizedError(response, errorDetails);
        }

        handleSalesforceError(response.getStatusCode(), errorDetails);

        // if not otherwise handled, do default handling and wrap with UncategorizedApiException
        handleUncategorizedError(response, errorDetails);
    }

    private void handleSalesforceError(HttpStatus statusCode, Map<String, Object> errorDetails) {
        if (statusCode.equals(HttpStatus.NOT_FOUND)) {
            throw new ResourceNotFoundException("salesforce",generateMessage(errorDetails));
        } else if (statusCode.equals(HttpStatus.SERVICE_UNAVAILABLE)) {
            throw new RateLimitExceededException("salesforce");
        } else if (statusCode.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            throw new InternalServerErrorException("salesforce", errorDetails == null ? "Contact Salesforce administrator." : generateMessage(errorDetails));
        } else if (statusCode.equals(HttpStatus.BAD_REQUEST)) {
            throw new InvalidIDException(generateMessage(errorDetails));
        } else if (statusCode.equals(HttpStatus.UNAUTHORIZED)) {
            throw new InvalidAuthorizationException("salesforce",generateMessage(errorDetails));
        } else if (statusCode.equals(HttpStatus.FORBIDDEN)) {
            throw new InsufficientPermissionException(generateMessage(errorDetails));
        }
    }

    private void handleUncategorizedError(ClientHttpResponse response, Map<String, Object> errorDetails) {
        try {
            super.handleError(response);
        } catch (Exception e) {
            if (errorDetails != null) {
                throw new UncategorizedApiException("salesforce", generateMessage(errorDetails), e);
            } else {
                throw new UncategorizedApiException("salesforce","No error details from Salesforce.", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractErrorDetailsFromResponse(ClientHttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        try {
            CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, Map.class);
            List<Map<String, Object>> errorList = (List<Map<String, Object>>) mapper.readValue(response.getBody(), listType);
            if (errorList.size() > 0) {
                return errorList.get(0);
            }
        } catch (JsonParseException e) {

        }

        return null;
    }

    private String generateMessage(Map<String, Object> errorDetails) {
        return (String) errorDetails.get("message");
    }

}
