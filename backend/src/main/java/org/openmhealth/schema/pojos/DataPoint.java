/*
 * Copyright 2014 Open mHealth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openmhealth.schema.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Common code for data points, primarily related
 * to metadata.
 *
 * @author Danilo Bonilla
 */
public class DataPoint {

    public static final String NAMESPACE = "omh:normalized";

    @JsonProperty(value = "owner")
    protected String owner;

    @JsonProperty(value = "schema_id")
    protected String schemaId;

    @JsonProperty(value = "schema_version")
    protected Long version = 1l;

    @JsonProperty(value = "metadata")
    protected Metadata metadata;

    @JsonProperty(value = "data")
    protected DataPointBody body;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public DataPointBody getBody() {
        return body;
    }

    public void setBody(DataPointBody body) {
        this.body = body;
        this.schemaId = NAMESPACE + ":" + body.getSchemaName();
    }
}
