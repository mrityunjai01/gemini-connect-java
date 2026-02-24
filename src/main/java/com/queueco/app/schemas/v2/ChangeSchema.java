package com.queueco.app.schemas.v2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.queueco.app.PriceQty;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeSchema {
    public ChangeSchema() {
        changes = new ArrayList<PriceQty>();
    }

    public ChangeSchema(String message) throws Exception {
        changes = new ArrayList<>();
        ObjectMapper om = new ObjectMapper();

        JsonNode nodeTree = om.readValue(message, JsonNode.class);
        if (nodeTree.has("changes")) {
            nodeTree.get("changes").forEach(
                    node -> {
                        PriceQty pq = new PriceQty();
                        pq.setPrice(new BigDecimal(node.get(1).asString()));
                        pq.setQty(new BigDecimal(node.get(2).asString()));
                        pq.setAsk(node.get(0).asString().equals("sell"));
                        changes.add(pq);
                    }

            );

        }

    }

    public ChangeSchema(List<PriceQty> changes) {
        this.changes = changes;
    }

    private final List<PriceQty> changes;

    public List<PriceQty> getChanges() {
        return changes;
    }

    public Iterable<PriceQty> getPriceUpdates() {
        return changes;
    }
}
