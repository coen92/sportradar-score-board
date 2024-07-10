package com.coen92.job.sportradarscoreboard.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class ValueObject<T> {
    protected final T value;

    protected ValueObject(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s{value=%s}", this.getClass().getSimpleName(), value);
    }
}
