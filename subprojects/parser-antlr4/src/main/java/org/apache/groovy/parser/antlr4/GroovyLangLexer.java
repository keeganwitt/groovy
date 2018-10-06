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
package org.apache.groovy.parser.antlr4;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.apache.groovy.parser.antlr4.internal.atnmanager.AtnManager;
import org.apache.groovy.parser.antlr4.internal.atnmanager.LexerAtnManager;

/**
 * The lexer for Groovy programming language, which is based on the lexer generated by Antlr4
 *
 * @author <a href="mailto:realbluesun@hotmail.com">Daniel.Sun</a>
 * Created on 2016/08/14
 */
public class GroovyLangLexer extends GroovyLexer {
    public GroovyLangLexer(CharStream input) {
        super(input);

        this.setInterpreter(new PositionAdjustingLexerATNSimulator(this, LexerAtnManager.INSTANCE.getATN()));
    }

    @Override
    public void recover(LexerNoViableAltException e) {
        throw e; // if some lexical error occurred, stop parsing!
    }

    @Override
    protected void rollbackOneChar() {
        ((PositionAdjustingLexerATNSimulator) getInterpreter()).resetAcceptPosition(getInputStream(), _tokenStartCharIndex - 1, _tokenStartLine, _tokenStartCharPositionInLine - 1);
    }

    private static class PositionAdjustingLexerATNSimulator extends LexerATNSimulator {
        public PositionAdjustingLexerATNSimulator(Lexer recog, ATN atn) {
            super(recog, atn);
        }

        protected void resetAcceptPosition(CharStream input, int index, int line, int charPositionInLine) {
            input.seek(index);
            this.line = line;
            this.charPositionInLine = charPositionInLine;
            this.consume(input);
        }
    }
}