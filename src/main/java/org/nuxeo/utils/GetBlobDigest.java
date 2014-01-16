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
@Operation(id=GetBlobDigest.ID, category=Constants.CAT_BLOB, label="Get Blob Digest", description="Calculate the digestKink digest (default is md5) of the file, and put the result in the contextVarName String variable.")
public class GetBlobDigest {

    public static final String ID = "GetBlobDigest";

    @Context
    protected OperationContext ctx;

    @Param(name = "digestKind", required = false)
    protected String digestKind;

    @Param(name = "contextVarName", required = true)
    protected String contextVarName;

    @OperationMethod(collector=BlobCollector.class)
    public Blob run(Blob input) throws IOException, Exception {

        digestKind = digestKind.toLowerCase().trim();
        if(digestKind == "") {
            digestKind = "md5";
        }

        switch(digestKind) {
        case "md5":
            ctx.put(contextVarName, DigestUtils.md5Hex(input.getByteArray()));
            break;

        case "sha":
            ctx.put(contextVarName, DigestUtils.shaHex(input.getByteArray()));
            break;

        case "sha256":
            ctx.put(contextVarName, DigestUtils.sha256Hex(input.getByteArray()));
            break;

        case "sha384":
            ctx.put(contextVarName, DigestUtils.sha384Hex(input.getByteArray()));
            break;

        case "sha512":
            ctx.put(contextVarName, DigestUtils.sha512Hex(input.getByteArray()));
            break;

        default:
            throw( new Exception("Unhandled digest kind: " + digestKind) );
            //break;
        }

        return input;
    }

}
