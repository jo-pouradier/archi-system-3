package fr.dtos.common.utils;

import lombok.Getter;
import lombok.Setter;

public class PairDTO<T, U> {


    @Setter
    @Getter
    protected T first;

    @Setter
    @Getter
    protected U second;

    @Override
    public String toString() {
        return "{" + this.getClass().getSimpleName() +
                "first='" + this.first + '\'' +
                ", second=" + this.second +
                '}';
    }
}
