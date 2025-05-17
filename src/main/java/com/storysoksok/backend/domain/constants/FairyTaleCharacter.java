package com.storysoksok.backend.domain.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FairyTaleCharacter {

    RABBIT("토끼"),
    DOG("강아지"),
    CAT("고양이"),
    FRIEND("친구"),
    TEACHER("선생님"),
    MOM("엄마"),
    BABY_BEAR("아기 곰"),
    PRINCESS("공주님"),
    ROBOT("로봇"),
    MONSTER("괴물"),
    WIZARD("마법사"),
    CUCUMBER("오이"),
    CHEF("요리사"),
    WHALE("고래"),
    TURTLE("거북이"),
    SUN("해님"),
    CLOUD_FAIRY("구름 요정"),
    CAR("자동차"),
    DOKKAEBI("도깨비"),
    GHOST_FRIEND("유령 친구"),
    ETC("기타");

    private final String description;
}
