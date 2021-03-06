 /*
 * Copyright 2014 Open Networking Laboratory
 *
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

package org.onosproject.net.resource;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Representation of allocated MPLS label resource.
 */
public class MplsLabelResourceAllocation extends MplsLabelResourceRequest
        implements ResourceAllocation {
    private final MplsLabel mplsLabel;

    @Override
    public ResourceType type() {
        return ResourceType.MPLS_LABEL;
    }

    /**
     * Creates a new {@link MplsLabelResourceAllocation} with {@link MplsLabel}
     * object.
     *
     * @param mplsLabel allocated MPLS Label
     */
    public MplsLabelResourceAllocation(MplsLabel mplsLabel) {
        this.mplsLabel = mplsLabel;
    }

    /**
     * Returns the MPLS label resource.
     *
     * @return the MPLS label resource
     */
    public MplsLabel mplsLabel() {
        return mplsLabel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mplsLabel);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MplsLabelResourceAllocation other = (MplsLabelResourceAllocation) obj;
        return Objects.equals(this.mplsLabel, other.mplsLabel);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mplsLabel", mplsLabel)
                .toString();
    }
}
