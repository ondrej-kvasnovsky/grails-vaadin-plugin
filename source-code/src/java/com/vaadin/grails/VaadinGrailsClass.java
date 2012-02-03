/*
 * Copyright 2010 Les Hazlewood
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
package com.vaadin.grails;

import org.codehaus.groovy.grails.commons.InjectableGrailsClass;

/**
 * Represents a Grails class that is to be configured in Spring and as such is
 * injectable.
 * 
 * @author Les Hazlewood
 * @since 1.2
 */
public interface VaadinGrailsClass extends InjectableGrailsClass {
}
