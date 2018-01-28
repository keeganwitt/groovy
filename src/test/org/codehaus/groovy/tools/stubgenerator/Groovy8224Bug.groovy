/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.codehaus.groovy.tools.stubgenerator

/**
 * Test that traits do not mess up with stub generation.
 *
 * @author Keegan Witt
 */
class Groovy8224Bug extends StringSourcesStubTestCase {

    Map<String, String> provideSources() {
        [
                'Groovy8224Trait.groovy': '''
                    trait Groovy8224Trait {
                        int foo
                        void bar() { }
                    }
                ''',
                'Groovy8224Impl.groovy': '''
                    class Groovy8224Impl implements Groovy8224Trait { }
                '''
        ]
    }

    void verifyStubs() {
        stubDir.listFiles().each {
            println it.text
        }
        def stubSource = stubJavaSourceFor('Groovy8224Trait')
        assert stubSource.contains('interface Groovy8224Trait')
        assert stubSource.contains('int getFoo()')
        assert stubSource.contains('void setFoo(int value)')

        stubSource = stubJavaSourceFor('Groovy8224Impl')
        assert stubSource.contains('class Groovy8224Impl')
        assert stubSource.contains('int getFoo()')
        assert stubSource.contains('void setFoo(int value)')
    }
}
