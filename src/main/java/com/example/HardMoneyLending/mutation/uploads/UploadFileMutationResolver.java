package com.example.HardMoneyLending.mutation.uploads;

import graphql.kickstart.tools.GraphQLMutationResolver;
import com.example.HardMoneyLending.services.investor.IInvestorService;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.Part;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class UploadFileMutationResolver implements GraphQLMutationResolver {

    private final IInvestorService investorService;

    @Autowired
    public UploadFileMutationResolver(final IInvestorService investorService) {
        this.investorService = investorService;
    }

    public UUID testMultiFilesUpload(List<Part> parts, DataFetchingEnvironment environment) {
        investorService.uploadProfile(parts, environment);
        return UUID.randomUUID();
    }
}
