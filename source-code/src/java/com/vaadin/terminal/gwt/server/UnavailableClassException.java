/*
 * Copyright 2008 Les Hazlewood
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
package com.vaadin.terminal.gwt.server;

/**
 * RuntimeException equivalent to a ClassNotFoundException. This only exists
 * because Vaadin's {@link ApplicationServlet ApplicationServlet}'s overridden
 * method does not retain the 'throws ClassNotFoundException' clause, which
 * means the {@link GrailsAwareApplicationServlet GrailsAwareApplicationServlet}
 * 's overriding implementation is not allowed to throw them. This exception is
 * thrown if necessary.
 * <p/>
 * This will be removed once <a
 * href="http://dev.vaadin.com/ticket/5225">http://dev
 * .vaadin.com/ticket/5225</a> is fixed.
 * 
 * @author Les Hazlewood
 * @since 1.2
 */
public class UnavailableClassException extends RuntimeException {

    public UnavailableClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnavailableClassException(Throwable cause) {
        super(cause);
    }
}
