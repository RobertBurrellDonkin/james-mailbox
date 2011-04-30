/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mailbox;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.Flags;

/**
 * Listens to <code>Mailbox</code> events.<br>
 * Note that listeners may be removed asynchronously.<br>
 * When {@link #isClosed()} returns true, the listener may be removed from the
 * mailbox by the dispatcher.
 */
public interface MailboxListener {

    /**
     * Informs this listener about the given event.
     * 
     * @param event
     *            not null
     */
    void event(final Event event);

    /**
     * <p>
     * Is this listener closed?<br>
     * Closed listeners may be unsubscribed.
     * </p>
     * <p>
     * Be aware that if the listener is marked as close it will not get any
     * events passed anymore!
     * </p>
     * 
     * @return true when closed, false when open
     */
    boolean isClosed();

    /**
     * A mailbox event.
     */
    public class Event {
        private final MailboxSession session;
        private final MailboxPath path;

        public Event(final MailboxSession session, final MailboxPath path) {
            this.session = session;
            this.path = path;
        }

        /**
         * Gets the {@link MailboxSession} in which's context the {@link Event}
         * happened
         * 
         * @return session
         */
        public MailboxSession getSession() {
            return session;
        }

        /**
         * Return the path of the Mailbox this event belongs to.
         * 
         * @return path
         */
        public MailboxPath getMailboxPath() {
            return path;
        }
    }

    /**
     * Indicates that mailbox has been deleted.
     */
    public class MailboxDeletion extends Event {

        public MailboxDeletion(final MailboxSession session, MailboxPath path) {
            super(session, path);
        }
    }

    /**
     * Indicates that a mailbox has been Added.
     */
    public class MailboxAdded extends Event {
        public MailboxAdded(final MailboxSession session, MailboxPath path) {
            super(session, path);
        }
    }

    /**
     * Indicates that a mailbox has been renamed.
     */
    public abstract class MailboxRenamed extends Event {
        public MailboxRenamed(final MailboxSession session, MailboxPath path) {
            super(session, path);
        }

        /**
         * Gets the new name for this mailbox.
         * 
         * @return name, not null
         */
        public abstract MailboxPath getNewPath();
    }

    /**
     * A mailbox event related to a message.
     */
    public abstract class MessageEvent extends Event {

        public MessageEvent(MailboxSession session, MailboxPath path) {
            super(session, path);
        }

        /**
         * Gets the message UIDs for the subject of this event.
         * 
         * @return message uids
         */
        public abstract List<Long> getUids();
    }

    public abstract class Expunged extends MessageEvent {

        public Expunged(MailboxSession session, MailboxPath path) {
            super(session, path);
        }
    }

    /**
     * A mailbox event related to updated flags
     */
    public abstract class FlagsUpdated extends MessageEvent {

        public FlagsUpdated(MailboxSession session, MailboxPath path) {
            super(session, path);
        }

        public abstract List<UpdatedFlags> getUpdatedFlags();
    }

    /**
     * A mailbox event related to added message
     */
    public abstract class Added extends MessageEvent {

        public Added(MailboxSession session, MailboxPath path) {
            super(session, path);
        }
        
        /**
         * Return the flags which were set for the added message
         * 
         * @return flags
         */
        public abstract Map<Long, Flags> getFlags();
    }

}
