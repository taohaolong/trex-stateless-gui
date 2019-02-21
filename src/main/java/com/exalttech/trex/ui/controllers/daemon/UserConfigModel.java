package com.exalttech.trex.ui.controllers.daemon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserConfigModel {

    private List<MetaField> metadata;
    private List<ConfigNode> valuedata;

    public UserConfigModel(List<MetaField> metadata) {
        this.metadata = metadata;
        initFromMetadata();
    }

    private void initFromMetadata() {
        valuedata = new ArrayList<>();

        for (MetaField metaField : this.metadata) {
            valuedata.add(new ConfigNode(metaField));
        }
    }

    public String getYAMLString() throws JsonProcessingException {
        List<String> errors = getErrors();

        if (errors.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return "### Config file generated by TRex Stateless GUI ###\n\n" + mapper.writeValueAsString(asList());
        }

        return "### errors in config:\n# "+ errors.stream().collect(Collectors.joining("\n# "));
    }

    public boolean isValid() {
        return getErrors().isEmpty();
    }

    public List<String> getErrors() {
        List<String> errors = new ArrayList<>();
        for (ConfigNode node : valuedata) {
            errors.addAll(node.getValidationErrors());
        }
        return errors;
    }

    public void fromList(List<Object> list) {
        fromMap((Map<String, Object>)list.get(0));
    }

    public List<Object> asList() {
        return Collections.singletonList(asMap());
    }

    public Map<String, Object> asMap() {
        Map<String, Object> config = new LinkedHashMap<>();
        for (ConfigNode configNode : valuedata) {
            if (configNode.getValue() != null) {
                config.put(configNode.getId(), configNode.getValue());
            }
        }
        return config;
    }

    public void fromMap(Map<String, Object> map) {
        initFromMetadata();
        for (Map.Entry<String, Object> entry: map.entrySet()) {
            valuedata.stream()
                    .filter(x -> x.getId().equals(entry.getKey()))
                    .findFirst()
                    .ifPresent(x -> x.setValue(entry.getValue()));
        }
    }

    public void fromYAMLString(String decoded) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<Object> defaultConfigModel = mapper.readValue(decoded, new TypeReference<List<Object>>() {});
        fromList(defaultConfigModel);
    }

    public List<ConfigNode> getValueFields() {
        return valuedata;
    }
}