/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package todoapp.dom;

import java.util.List;

import org.apache.isis.applib.Identifier;

public final class ToDoAppDomainModule {

    private ToDoAppDomainModule(){}

    public abstract static class ActionDomainEvent<S> extends org.apache.isis.applib.services.eventbus.ActionDomainEvent<S> {
        public ActionDomainEvent(final S source, final Identifier identifier) {
            super(source, identifier);
        }

        public ActionDomainEvent(final S source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }

        public ActionDomainEvent(final S source, final Identifier identifier, final List<Object> arguments) {
            super(source, identifier, arguments);
        }
    }

    public abstract static class CollectionDomainEvent<S,T> extends org.apache.isis.applib.services.eventbus.CollectionDomainEvent<S,T> {
        public CollectionDomainEvent(final S source, final Identifier identifier, final Of of) {
            super(source, identifier, of);
        }

        public CollectionDomainEvent(final S source, final Identifier identifier, final Of of, final T value) {
            super(source, identifier, of, value);
        }
    }

    public abstract static class PropertyDomainEvent<S,T> extends org.apache.isis.applib.services.eventbus.PropertyDomainEvent<S,T> {
        public PropertyDomainEvent(final S source, final Identifier identifier) {
            super(source, identifier);
        }

        public PropertyDomainEvent(final S source, final Identifier identifier, final T oldValue, final T newValue) {
            super(source, identifier, oldValue, newValue);
        }
    }

}
