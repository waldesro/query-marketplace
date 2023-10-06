package com.junglesoftware.marketplace.services;


import com.junglesoftware.marketplace.api.QueryApiDelegate;
import com.junglesoftware.marketplace.dto.QueryDetailDTO;
import com.junglesoftware.marketplace.dto.QueryDetailsDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QueryService implements QueryApiDelegate {

    @Override
    public QueryDetailsDTO listAllQueries(UUID softwareId) {
        QueryDetailsDTO queryDetailsDTO = new QueryDetailsDTO();
        QueryDetailDTO queryDetailDTO = new QueryDetailDTO();
        queryDetailDTO.setId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        queryDetailsDTO.addQueriesItem(queryDetailDTO);
        return queryDetailsDTO;
    }
}
