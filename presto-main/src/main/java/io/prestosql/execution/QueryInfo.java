/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.prestosql.execution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.prestosql.SessionRepresentation;
import io.prestosql.spi.ErrorCode;
import io.prestosql.spi.ErrorType;
import io.prestosql.spi.PrestoWarning;
import io.prestosql.spi.QueryId;
import io.prestosql.spi.memory.MemoryPoolId;
import io.prestosql.spi.resourcegroups.ResourceGroupId;
import io.prestosql.spi.security.SelectedRole;
import io.prestosql.transaction.TransactionId;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static io.prestosql.execution.StageInfo.getAllStages;
import static java.util.Objects.requireNonNull;

@Immutable
public class QueryInfo
{
    private final QueryId queryId;
    private final SessionRepresentation session;
    private final QueryState state;
    private final MemoryPoolId memoryPool;
    private final boolean scheduled;
    private final URI self;
    private final List<String> fieldNames;
    private final String query;
    private final QueryStats queryStats;
    private final Optional<String> setCatalog;
    private final Optional<String> setSchema;
    private final Optional<String> setPath;
    private final Map<String, String> setSessionProperties;
    private final Set<String> resetSessionProperties;
    private final Map<String, SelectedRole> setRoles;
    private final Map<String, String> addedPreparedStatements;
    private final Set<String> deallocatedPreparedStatements;
    private final Optional<TransactionId> startedTransactionId;
    private final boolean clearTransactionId;
    private final String updateType;
    private final Optional<StageInfo> outputStage;
    private final ExecutionFailureInfo failureInfo;
    private final ErrorType errorType;
    private final ErrorCode errorCode;
    private final List<PrestoWarning> warnings;
    private final Set<Input> inputs;
    private final Optional<Output> output;
    private final boolean completeInfo;
    private final Optional<ResourceGroupId> resourceGroupId;

    @JsonCreator
    public QueryInfo(
            @JsonProperty("queryId") QueryId queryId,
            @JsonProperty("session") SessionRepresentation session,
            @JsonProperty("state") QueryState state,
            @JsonProperty("memoryPool") MemoryPoolId memoryPool,
            @JsonProperty("scheduled") boolean scheduled,
            @JsonProperty("self") URI self,
            @JsonProperty("fieldNames") List<String> fieldNames,
            @JsonProperty("query") String query,
            @JsonProperty("queryStats") QueryStats queryStats,
            @JsonProperty("setCatalog") Optional<String> setCatalog,
            @JsonProperty("setSchema") Optional<String> setSchema,
            @JsonProperty("setPath") Optional<String> setPath,
            @JsonProperty("setSessionProperties") Map<String, String> setSessionProperties,
            @JsonProperty("resetSessionProperties") Set<String> resetSessionProperties,
            @JsonProperty("setRoles") Map<String, SelectedRole> setRoles,
            @JsonProperty("addedPreparedStatements") Map<String, String> addedPreparedStatements,
            @JsonProperty("deallocatedPreparedStatements") Set<String> deallocatedPreparedStatements,
            @JsonProperty("startedTransactionId") Optional<TransactionId> startedTransactionId,
            @JsonProperty("clearTransactionId") boolean clearTransactionId,
            @JsonProperty("updateType") String updateType,
            @JsonProperty("outputStage") Optional<StageInfo> outputStage,
            @JsonProperty("failureInfo") ExecutionFailureInfo failureInfo,
            @JsonProperty("errorCode") ErrorCode errorCode,
            @JsonProperty("warnings") List<PrestoWarning> warnings,
            @JsonProperty("inputs") Set<Input> inputs,
            @JsonProperty("output") Optional<Output> output,
            @JsonProperty("completeInfo") boolean completeInfo,
            @JsonProperty("resourceGroupId") Optional<ResourceGroupId> resourceGroupId)
    {
        requireNonNull(queryId, "queryId is null");
        requireNonNull(session, "session is null");
        requireNonNull(state, "state is null");
        requireNonNull(self, "self is null");
        requireNonNull(fieldNames, "fieldNames is null");
        requireNonNull(queryStats, "queryStats is null");
        requireNonNull(setCatalog, "setCatalog is null");
        requireNonNull(setSchema, "setSchema is null");
        requireNonNull(setPath, "setPath is null");
        requireNonNull(setSessionProperties, "setSessionProperties is null");
        requireNonNull(resetSessionProperties, "resetSessionProperties is null");
        requireNonNull(addedPreparedStatements, "addedPreparedStatemetns is null");
        requireNonNull(deallocatedPreparedStatements, "deallocatedPreparedStatements is null");
        requireNonNull(startedTransactionId, "startedTransactionId is null");
        requireNonNull(query, "query is null");
        requireNonNull(outputStage, "outputStage is null");
        requireNonNull(inputs, "inputs is null");
        requireNonNull(output, "output is null");
        requireNonNull(resourceGroupId, "resourceGroupId is null");
        requireNonNull(warnings, "warnings is null");

        this.queryId = queryId;
        this.session = session;
        this.state = state;
        this.memoryPool = requireNonNull(memoryPool, "memoryPool is null");
        this.scheduled = scheduled;
        this.self = self;
        this.fieldNames = ImmutableList.copyOf(fieldNames);
        this.query = query;
        this.queryStats = queryStats;
        this.setCatalog = setCatalog;
        this.setSchema = setSchema;
        this.setPath = setPath;
        this.setSessionProperties = ImmutableMap.copyOf(setSessionProperties);
        this.resetSessionProperties = ImmutableSet.copyOf(resetSessionProperties);
        this.setRoles = ImmutableMap.copyOf(setRoles);
        this.addedPreparedStatements = ImmutableMap.copyOf(addedPreparedStatements);
        this.deallocatedPreparedStatements = ImmutableSet.copyOf(deallocatedPreparedStatements);
        this.startedTransactionId = startedTransactionId;
        this.clearTransactionId = clearTransactionId;
        this.updateType = updateType;
        this.outputStage = outputStage;
        this.failureInfo = failureInfo;
        this.errorType = errorCode == null ? null : errorCode.getType();
        this.errorCode = errorCode;
        this.warnings = ImmutableList.copyOf(warnings);
        this.inputs = ImmutableSet.copyOf(inputs);
        this.output = output;
        this.completeInfo = completeInfo;
        this.resourceGroupId = resourceGroupId;
    }

    @JsonProperty
    public QueryId getQueryId()
    {
        return queryId;
    }

    @JsonProperty
    public SessionRepresentation getSession()
    {
        return session;
    }

    @JsonProperty
    public QueryState getState()
    {
        return state;
    }

    @JsonProperty
    public MemoryPoolId getMemoryPool()
    {
        return memoryPool;
    }

    @JsonProperty
    public boolean isScheduled()
    {
        return scheduled;
    }

    @JsonProperty
    public URI getSelf()
    {
        return self;
    }

    @JsonProperty
    public List<String> getFieldNames()
    {
        return fieldNames;
    }

    @JsonProperty
    public String getQuery()
    {
        return query;
    }

    @JsonProperty
    public QueryStats getQueryStats()
    {
        return queryStats;
    }

    @JsonProperty
    public Optional<String> getSetCatalog()
    {
        return setCatalog;
    }

    @JsonProperty
    public Optional<String> getSetSchema()
    {
        return setSchema;
    }

    @JsonProperty
    public Optional<String> getSetPath()
    {
        return setPath;
    }

    @JsonProperty
    public Map<String, String> getSetSessionProperties()
    {
        return setSessionProperties;
    }

    @JsonProperty
    public Set<String> getResetSessionProperties()
    {
        return resetSessionProperties;
    }

    @JsonProperty
    public Map<String, SelectedRole> getSetRoles()
    {
        return setRoles;
    }

    @JsonProperty
    public Map<String, String> getAddedPreparedStatements()
    {
        return addedPreparedStatements;
    }

    @JsonProperty
    public Set<String> getDeallocatedPreparedStatements()
    {
        return deallocatedPreparedStatements;
    }

    @JsonProperty
    public Optional<TransactionId> getStartedTransactionId()
    {
        return startedTransactionId;
    }

    @JsonProperty
    public boolean isClearTransactionId()
    {
        return clearTransactionId;
    }

    @Nullable
    @JsonProperty
    public String getUpdateType()
    {
        return updateType;
    }

    @JsonProperty
    public Optional<StageInfo> getOutputStage()
    {
        return outputStage;
    }

    @Nullable
    @JsonProperty
    public ExecutionFailureInfo getFailureInfo()
    {
        return failureInfo;
    }

    @Nullable
    @JsonProperty
    public ErrorType getErrorType()
    {
        return errorType;
    }

    @Nullable
    @JsonProperty
    public ErrorCode getErrorCode()
    {
        return errorCode;
    }

    @JsonProperty
    public List<PrestoWarning> getWarnings()
    {
        return warnings;
    }

    @JsonProperty
    public boolean isFinalQueryInfo()
    {
        return state.isDone() && getAllStages(outputStage).stream().allMatch(StageInfo::isFinalStageInfo);
    }

    @JsonProperty
    public Set<Input> getInputs()
    {
        return inputs;
    }

    @JsonProperty
    public Optional<Output> getOutput()
    {
        return output;
    }

    @JsonProperty
    public Optional<ResourceGroupId> getResourceGroupId()
    {
        return resourceGroupId;
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("queryId", queryId)
                .add("state", state)
                .add("fieldNames", fieldNames)
                .toString();
    }

    public boolean isCompleteInfo()
    {
        return completeInfo;
    }
}
