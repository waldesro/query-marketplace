package com.junglesoftware.marketplace.controller.query;


import com.junglesoftware.marketplace.api.ListAllQueriesApi;
import com.junglesoftware.marketplace.api.dto.QueryDetailsDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class QueryService implements ListAllQueriesApi {
    @Override
    public ResponseEntity<QueryDetailsDTO> listAllQueries(UUID softwareId) throws Exception {
        return ListAllQueriesApi.super.listAllQueries(softwareId);
    }
}
