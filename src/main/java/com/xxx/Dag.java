package com.xxx;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class Dag {
  private Map<String, Node> nodes = new HashMap<String, Node>();

  public static class Node {
    private String id;
    private int numPartitions;
    private Set<String> parentIds;

    public Node(String id, int numPartitions, Set<String> parentIds) {
      this.setId(id);
      this.setNumPartitions(numPartitions);
      this.setParentIds(parentIds);
    }

    public Node(String id, int numPartitions, String parentIdsStr) {
      this.setId(id);
      this.setNumPartitions(numPartitions);
      if (parentIdsStr != null && !parentIdsStr.trim().isEmpty()) {
        String tmp[] = parentIdsStr.split(",");
        parentIds = new HashSet<String>();
        parentIds.addAll(Arrays.asList(tmp));
      }
      this.setParentIds(parentIds);
    }

    public Node() {
      setId("");
      setNumPartitions(0);
      setParentIds(new HashSet<String>());
    }

    public String getId() {
      return id;
    }

    public int getNumPartitions() {
      return numPartitions;
    }

    public Set<String> getParentIds() {
      return parentIds;
    }

    public static Node fromJson(String json) throws Exception {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(json, Node.class);
    }

    public String toJson() throws Exception {
      ObjectMapper mapper = new ObjectMapper();

      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    public void setId(String id) {
      this.id = id;
    }

    public void setNumPartitions(int numPartitions) {
      this.numPartitions = numPartitions;
    }

    public void setParentIds(Set<String> parentIds) {
      this.parentIds = parentIds;
    }
  }

  public void addNode(Node node) {
    getNodes().put(node.getId(), node);
  }

  public Node getNode(String id) {
    return getNodes().get(id);
  }

  public Set<String> getNodeIds() {
    return getNodes().keySet();
  }

  public static Dag fromJson(String json) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(json, Dag.class);
  }

  public String toJson() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
  }

  public Map<String, Node> getNodes() {
    return nodes;
  }

  public void setNodes(Map<String, Node> nodes) {
    this.nodes = nodes;
  }
}