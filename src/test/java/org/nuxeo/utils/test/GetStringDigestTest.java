/*
 * (C) Copyright ${year} Nuxeo SA (http://nuxeo.com/) and others.
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
 *     thibaud
 */

package org.nuxeo.utils.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.utils.*;

import com.google.inject.Inject;

/**
 * @author Thibaud Arguillere
 */

@RunWith(FeaturesRunner.class)
@Features({PlatformFeature.class, CoreFeature.class})
@Deploy({   "org.nuxeo.ecm.automation.core",
            "org.nuxeo.utils.digest"
	})
public class GetStringDigestTest {

    private final String kSTRING = "Hi there, all is good?";
    private final String [] kHASH_KINDS = {"md5", "sha", "sha256", "sha384", "sha512"};
    private final String [] kHASH_RESULTS = {"f512211d0461eda15a4089cb6230561f", // md5
                                                "093a81906d880025e846ae91f584b2986144e961", //sha1
                                                "1e02e6fa4007e571d29a77db256ba967ff6903b4aa3485921ad8eb7a83b8e047", //sha256
                                                "e326e3083cf7d15e7499f0fd85bdb7c8cc6d2d39551c9b14be63d8fcca4b9a0c26392fd0eec282b50db079eb2ab1a4d4", // sha384
                                                "d494a0260de6e162999d11e8b522b1a24fe247fd19beafc26b6d59f03f289e55c3a5fd05f34c1168dab987ce6225dba55813593cadc43ea72562f704fe5ea790" }; // sha512};

    @Inject
    CoreSession session;

    @Inject
    AutomationService service;

    protected OperationContext _ctx;

    @Before
    public void init() throws Exception {
        _ctx = new OperationContext(session);
    }

    @After
    public void cleanup() throws Exception {

    }

    protected String testOneHash(String hashKind) throws Exception {
        OperationChain chain = new OperationChain("testChain_" + hashKind);
        chain.add(GetStringDigest.ID).set("digestKind", hashKind)
                                     .set("stringToHash", kSTRING)
                                     .set("charset", "UTF-8")
                                     .set("contextVarName", "theVar");

        service.run(_ctx, chain);

        return (String) _ctx.get("theVar");
    }

    @Test
    public void testDigest() throws Exception {

        String hash;

        int max = kHASH_KINDS.length;
        for(int i = 0; i < max; i++) {
            hash = testOneHash( kHASH_KINDS[i] );
            assertEquals("Testing " + kHASH_KINDS[i], kHASH_RESULTS[i].toLowerCase(), hash.toLowerCase());
        }
    }

    @Test
    public void testShouldDefaultMD5() throws Exception {
        String hash = testOneHash( "" );
        assertEquals("Testing " + "md5", kHASH_RESULTS[0].toLowerCase(), hash.toLowerCase());
    }

    @Test
    public void testFailOnUnhandledAlgo() throws Exception {
        String hash;
        String unhandledKind = "abcdef123456";
        try {
           hash = testOneHash(unhandledKind);
        } catch (Exception e) {
            assertTrue( e.getMessage().indexOf("Unhandled digest kind: " + unhandledKind) > -1 );
        }
    }
}


//--EOF--