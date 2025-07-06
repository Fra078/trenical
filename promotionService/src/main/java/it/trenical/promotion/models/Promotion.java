package it.trenical.promotion.models;

import java.util.ArrayList;
import java.util.List;

public class Promotion {
    private final String id;
    private final String name;
    private final String description;
    private final Effect effect;
    private final List<Condition> conditions;

    private Promotion(String id, String name, String description, Effect effect, List<Condition> conditions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.effect = effect;
        this.conditions = conditions;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Effect getEffect() {
        return effect;
    }
    public List<Condition> getConditions() {
        return conditions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Promotion promotion) {
        return new Builder(promotion);
    }

    public static class Builder {
        private String id;
        private String name;
        private String description;
        private Effect effect;
        private List<Condition> conditions = new ArrayList<>();
        private Builder() {}

        private Builder(Promotion promotion) {
            this.id = promotion.getId();
            this.name = promotion.getName();
            this.description = promotion.getDescription();
            this.effect = promotion.getEffect();
            this.conditions = promotion.getConditions();
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }
        public Builder setEffect(Effect effect) {
            this.effect = effect;
            return this;
        }

        public Builder addConditions(Condition condition) {
            this.conditions.add(condition);
            return this;
        }

        public Builder addAllConditions(List<Condition> conditions) {
            this.conditions.addAll(conditions);
            return this;
        }

        public Promotion build() {
            return new Promotion(id, name, description, effect, List.copyOf(conditions));
        }

    }

}
