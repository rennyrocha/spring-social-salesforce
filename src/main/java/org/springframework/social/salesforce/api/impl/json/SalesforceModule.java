package org.springframework.social.salesforce.api.impl.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import org.springframework.social.salesforce.api.*;


/**
 * Jackson module for api version v23.0.
 *
 * @author Umut Utkan
 */
public class SalesforceModule extends Module {


    @Override
    public void setupModule(SetupContext context) {

        context.setMixInAnnotations(ApiVersion.class, ApiVersionMixin.class);
        context.setMixInAnnotations(SalesforceProfile.class, SalesforceProfileMixin.class);
        context.setMixInAnnotations(Photo.class, PhotoMixin.class);
        context.setMixInAnnotations(Status.class, StatusMixin.class);
        context.setMixInAnnotations(SObjectSummary.class, SObjectSummaryMixin.class);
        context.setMixInAnnotations(RecordTypeInfo.class, RecordTypeInfoMixin.class);
        context.setMixInAnnotations(Relationship.class, RelationshipMixin.class);
        context.setMixInAnnotations(PickListEntry.class, PickListEntryMixin.class);
        context.setMixInAnnotations(Field.class, FieldMixin.class);
        context.setMixInAnnotations(SObjectDetail.class, SObjectDetailMixin.class);
        context.setMixInAnnotations(QueryResult.class, QueryResultMixin.class);
        context.setMixInAnnotations(ResultItem.class, ResultItemMixin.class);
    }


    @Override
    public String getModuleName() {
        return "SalesforceModule";
    }

    @Override
    public Version version() {
        return new Version(23, 0, 0, null);
    }
}
