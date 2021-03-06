/*-
 *
 *  * Copyright 2017 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */
package org.deeplearning4j.rl4j.network.ac;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import org.nd4j.linalg.learning.config.RmsProp;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author saudet
 */
public class ActorCriticTest {

    public static ActorCriticFactorySeparateStdDense.Configuration NET_CONF =
            new ActorCriticFactorySeparateStdDense.Configuration(
                    4,         //number of layers
                    32,        //number of hidden nodes
                    0.0005,    //learning rate
                    0.001,     //l2 regularization
                    new RmsProp(), null, false
            );

    public static ActorCriticFactoryCompGraphStdDense.Configuration NET_CONF_CG =
            new ActorCriticFactoryCompGraphStdDense.Configuration(
                    2,         //number of layers
                    128,       //number of hidden nodes
                    0.005,     //learning rate
                    0.00001,   //l2 regularization
                    new RmsProp(), null, true
            );

    @Test
    public void testModelLoadSave() throws IOException {
        ActorCriticSeparate acs = new ActorCriticFactorySeparateStdDense(NET_CONF).buildActorCritic(new int[] {7}, 5);

        File fileValue = File.createTempFile("rl4j-value-", ".model");
        File filePolicy = File.createTempFile("rl4j-policy-", ".model");
        acs.save(fileValue.getAbsolutePath(), filePolicy.getAbsolutePath());

        ActorCriticSeparate acs2 = ActorCriticSeparate.load(fileValue.getAbsolutePath(), filePolicy.getAbsolutePath());

        assertEquals(acs.valueNet, acs2.valueNet);
        assertEquals(acs.policyNet, acs2.policyNet);

        ActorCriticCompGraph accg = new ActorCriticFactoryCompGraphStdDense(NET_CONF_CG).buildActorCritic(new int[] {37}, 43);

        File file = File.createTempFile("rl4j-cg-", ".model");
        accg.save(file.getAbsolutePath());

        ActorCriticCompGraph accg2 = ActorCriticCompGraph.load(file.getAbsolutePath());

        assertEquals(accg.cg, accg2.cg);
    }
}
