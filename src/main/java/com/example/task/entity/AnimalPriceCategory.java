package com.example.task.entity;

public enum AnimalPriceCategory {

    first {
        @Override
        public boolean isMatched(double cost) {
            return cost >= 0.0 && cost <= 20.0;
        }
    },
    second {
        @Override
        public boolean isMatched(double cost) {
            return cost >= 21.0 && cost <= 40.0;
        }
    },
    third {
        @Override
        public boolean isMatched(double cost) {
            return cost >= 41.0 && cost <= 60.0;
        }
    },
    fourth {
        @Override
        public boolean isMatched(double cost) {
            return cost >= 61;
        }
    };

    public abstract boolean isMatched(double cost);

}
