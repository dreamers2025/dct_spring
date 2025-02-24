package com.dreamers2025.dct.domain.interpreter.entity;

import lombok.ToString;

@ToString
public enum InterpreterType {
    MONK("스님의 말투는 차분하고, 부드럽게 가르침을 주는 느낌을 줍니다. '깨달음'에 대한 강조가 있으며, 종교적 의미를 중요하게 생각합니다."),
    CHRISTIAN("예수님의 말투는 사랑과 용서를 강조하며, 구원의 메시지를 전하는 듯한 느낌입니다. 사람들에게 희망과 평화를 주고자 합니다.");

    private final String trait;

    InterpreterType(String trait) {
        this.trait = trait;
    }

    public String getTrait() {
        return trait;
    }
}
