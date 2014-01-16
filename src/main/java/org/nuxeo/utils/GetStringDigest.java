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

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.DigestUtils;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.ClientException;

/**
 * @author Thibaud Arguillere
 */
@Operation(id=GetStringDigest.ID, category=Constants.CAT_SERVICES, label="Get String Digest", description="Calculate the digestKink digest (default is md5) of stringToHash, and put the result in the contextVarName String variable.")
public class GetStringDigest {

    public static final String ID = "GetStringDigest";

    @Context
    protected OperationContext ctx;

    @Param(name = "digestKind", required = false)
    protected String digestKind;

    @Param(name = "stringToHash")
    protected String stringToHash;

    @Param(name = "charset", values = {"UTF-8"})
    protected String charset;

    @Param(name = "contextVarName", required = true)
    protected String contextVarName;

    @OperationMethod
    public void run() throws ClientException, UnsupportedEncodingException {

        // Check parameter
        if(stringToHash == null) {
            stringToHash = "";
        }

        // Cleanup
        digestKind = digestKind.toLowerCase().trim();
        if(digestKind == "") {
            digestKind = "md5";
        }

        charset = charset.trim();
        if(charset == "") {
            charset = "UTF-8";
        }

        byte[] bytes = stringToHash.getBytes(charset);

        switch(digestKind) {
        case "md5":
            ctx.put(contextVarName, DigestUtils.md5Hex(bytes));
            break;

        case "sha":
            ctx.put(contextVarName, DigestUtils.shaHex(bytes));
            break;

        case "sha256":
            ctx.put(contextVarName, DigestUtils.sha256Hex(bytes));
            break;

        case "sha384":
            ctx.put(contextVarName, DigestUtils.sha384Hex(bytes));
            break;

        case "sha512":
            ctx.put(contextVarName, DigestUtils.sha512Hex(bytes));
            break;

        default:
            throw new ClientException("Unhandled digest kind: " + digestKind);
            //break;
        }
    }

}
