/*
 * (C) Copyright 2004 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Thibaud Arguillere
 */

package org.nuxeo.utils;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.BlobCollector;
import org.nuxeo.ecm.core.api.Blob;

/**
 * @author Thibaud Arguillere
 */
@Operation(id=GetBlobMD5.ID, category=Constants.CAT_BLOB, label="Get Blob MD5", description="Put in the contextVarName String variable the MD5 hash of the file. If getSavedDigest is true, the operation returns the existing hash (it does not calculate a new one)")
public class GetBlobMD5 {

    public static final String ID = "GetBlobMD5";

    @Context
    protected OperationContext ctx;

    @Param(name = "contextVarName", required = true)
    protected String contextVarName;

    @Param(name = "getSavedDigest", required = false, values = {"false"})
    protected boolean getSavedDigest;

    @OperationMethod(collector=BlobCollector.class)
    public Blob run(Blob input) throws IOException {

        // We let the default exception handle a null input
        if(getSavedDigest) {
            ctx.put(contextVarName, input.getDigest());
        } else {
            ctx.put(contextVarName, DigestUtils.md5Hex(input.getByteArray()));
        }

        return input;
    }
}
