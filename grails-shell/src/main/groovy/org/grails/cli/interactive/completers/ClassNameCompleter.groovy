/*
 * Copyright 2014 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.cli.interactive.completers

import groovy.transform.CompileStatic
import org.grails.io.support.PathMatchingResourcePatternResolver
import org.grails.io.support.Resource


/**
 * A completer that completes class names
 *
 * @author Graeme Rocher
 * @since 3.0
 */
@CompileStatic
class ClassNameCompleter extends StringsCompleter {

        static Map<String, SortedSet<String>> RESOURCE_SCAN_CACHE = [:]
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver()

        ClassNameCompleter(File baseDir) {
                initialize(baseDir)
        }

        ClassNameCompleter(File... baseDirs) {
                initialize(baseDirs)
        }

        private void initialize(File... baseDirs) {
                if(!baseDirs) return
                SortedSet<String> allStrings = new TreeSet<>()
                for(File baseDir in baseDirs) {
                        def pattern = "file:${baseDir}/**/*.groovy".toString()
                        SortedSet<String> strings = RESOURCE_SCAN_CACHE[pattern]
                        if(strings == null) {
                                strings = new TreeSet<>()
                                RESOURCE_SCAN_CACHE[pattern] = strings
                                def resources = resourcePatternResolver.getResources(pattern)
                                for (res in resources) {
                                        if(isValidResource(res)) {
                                                def path = res.file.canonicalPath
                                                def basePath = baseDir.canonicalPath
                                                path = (path - basePath)[1..-8]
                                                path = path.replace(File.separatorChar, '.' as char)
                                                strings << path
                                        }
                                }
                        }
                        allStrings.addAll(strings)
                }
                setStrings(allStrings)
        }

        boolean isValidResource(Resource resource) {
                true
        }
}
