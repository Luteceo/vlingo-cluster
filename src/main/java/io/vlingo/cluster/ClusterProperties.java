// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Properties that are predefined for 1-node and 3-node clusters. These
 * are useful for test, but provided in {@code java/main} for access by tests
 * outside of {@code vlingo-cluster}.
 */
public class ClusterProperties {
  private static final Random random = new Random();
  private static AtomicInteger PORT_TO_USE = new AtomicInteger(10_000 + random.nextInt(50_000));

  public static io.vlingo.cluster.model.Properties allNodes() {
    return allNodes(PORT_TO_USE);
  }

  public static io.vlingo.cluster.model.Properties allNodes(final AtomicInteger portSeed) {
    java.util.Properties properties = new java.util.Properties();

    properties = common(allOf(properties, portSeed), 3);

    final io.vlingo.cluster.model.Properties clusterProperties =
            io.vlingo.cluster.model.Properties.openForTest(properties);

    return clusterProperties;
  }

  public static io.vlingo.cluster.model.Properties oneNode() {
    return oneNode(PORT_TO_USE);
  }

  public static io.vlingo.cluster.model.Properties oneNode(final AtomicInteger portSeed) {
    java.util.Properties properties = new java.util.Properties();

    properties = common(oneOnly(properties, portSeed), 1);

    final io.vlingo.cluster.model.Properties clusterProperties =
            io.vlingo.cluster.model.Properties.openForTest(properties);

    return clusterProperties;
  }

  private static java.util.Properties oneOnly(final java.util.Properties properties, final AtomicInteger portSeed) {
    properties.setProperty("node.node1.id", "1");
    properties.setProperty("node.node1.name", "node1");
    properties.setProperty("node.node1.host", "localhost");
    properties.setProperty("node.node1.op.port", nextPortToUseString(portSeed));
    properties.setProperty("node.node1.app.port", nextPortToUseString(portSeed));

    return properties;
  }

  private static java.util.Properties allOf(final java.util.Properties properties, final AtomicInteger portSeed) {
    oneOnly(properties, portSeed);

    properties.setProperty("node.node2.id", "2");
    properties.setProperty("node.node2.name", "node2");
    properties.setProperty("node.node2.host", "localhost");
    properties.setProperty("node.node2.op.port", nextPortToUseString(portSeed));
    properties.setProperty("node.node2.app.port", nextPortToUseString(portSeed));

    properties.setProperty("node.node3.id", "3");
    properties.setProperty("node.node3.name", "node3");
    properties.setProperty("node.node3.host", "localhost");
    properties.setProperty("node.node3.op.port", nextPortToUseString(portSeed));
    properties.setProperty("node.node3.app.port", nextPortToUseString(portSeed));

    return properties;
  }

  private static java.util.Properties common(final java.util.Properties properties, final int totalNodes) {
    properties.setProperty("cluster.ssl", "false");

    properties.setProperty("cluster.op.buffer.size", "4096");
    properties.setProperty("cluster.app.buffer.size", "10240");
    properties.setProperty("cluster.op.outgoing.pooled.buffers", "20");
    properties.setProperty("cluster.app.outgoing.pooled.buffers", "50");

    properties.setProperty("cluster.msg.charset", "UTF-8");

    properties.setProperty("cluster.app.class", "io.vlingo.cluster.model.application.FakeClusterApplicationActor");
    properties.setProperty("cluster.app.stage", "fake.app.stage");

    properties.setProperty("cluster.health.check.interval", "2000");
    properties.setProperty("cluster.live.node.timeout", "20000");
    properties.setProperty("cluster.heartbeat.interval", "7000");
    properties.setProperty("cluster.quorum.timeout", "60000");

    if (totalNodes == 1) {
      properties.setProperty("cluster.seedNodes", "node1");
    } else if (totalNodes == 3) {
      properties.setProperty("cluster.seedNodes", "node1,node2,node3");
    } else {
      throw new IllegalArgumentException("The totalNodes must be 1 or 3.");
    }

    return properties;
  }

  private static int nextPortToUse(final AtomicInteger portSeed) {
    return portSeed.incrementAndGet();
  }

  private static String nextPortToUseString(final AtomicInteger portSeed) {
    return "" + nextPortToUse(portSeed);
  }
}
