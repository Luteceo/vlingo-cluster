// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute.message;

import io.vlingo.cluster.model.attribute.AttributeSet;
import io.vlingo.cluster.model.attribute.TrackedAttribute;
import io.vlingo.wire.node.Node;

public class ReplaceAttribute extends AttributeMessage {
  public static ReplaceAttribute from(final Node node, final AttributeSet set, final TrackedAttribute tracked) {
    return new ReplaceAttribute(node, set, tracked);
  }
  
  public ReplaceAttribute(final Node node, final AttributeSet set, final TrackedAttribute tracked) {
    super(node, set, tracked, ApplicationMessageType.ReplaceAttribute);
  }
}
