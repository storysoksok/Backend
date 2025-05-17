package com.storysoksok.backend.domain.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FairyTaleLocation {
    HOME("우리 집"),
    SCHOOL("학교"),
    PLAYGROUND("놀이터"),
    HOSPITAL("병원"),
    KINDERGARTEN_CLASSROOM("유치원 교실"),
    BEACH("바닷가"),
    FOREST("숲속"),
    SKY("하늘 위"),
    ZOO("동물원"),
    TOY_STORE("장난감 가게"),
    SUPERMARKET("마트"),
    DREAMLAND("꿈나라"),
    CASTLE("성 안"),
    CAVE("동굴"),
    DINOSAUR_LAND("공룡나라"),
    BAKERY("빵집"),
    SWIMMING_POOL("수영장"),
    AMUSEMENT_PARK("놀이공원"),
    MAGIC_LAND("마법의 나라"),
    INSIDE_BUS("버스 안"),
    ETC("기타");   // 기타 항목은 맨 마지막에 두었습니다.

    private final String description;
}
