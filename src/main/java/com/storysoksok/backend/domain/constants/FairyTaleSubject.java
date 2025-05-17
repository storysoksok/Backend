package com.storysoksok.backend.domain.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FairyTaleSubject {

    HANGING_OUT_WITH_FRIEND("친구와 놀러 가는 이야기"),
    ANIMAL_ADVENTURE("동물이 모험하는 이야기"),
    SEARCHING_FOR_PRESENT("선물을 찾는 이야기"),
    USING_MAGIC("마법을 쓰는 이야기"),
    BIRTHDAY_PARTY("생일 파티 이야기"),
    MEETING_SCARY_MONSTER("무서운 괴물과 만나는 이야기"),
    LOST_AND_FOUND_WAY("길을 잃고 돌아오는 이야기"),
    RAINY_DAY("비 오는 날 이야기"),
    FINDING_HIDDEN_TREASURE("숨은 보물을 찾는 이야기"),
    FIGHT_AND_MAKE_UP_WITH_FRIEND("친구랑 싸우고 화해하는 이야기"),
    FINDING_LOST_TOY("잃어버린 장난감을 찾는 이야기"),
    DREAM_WORLD("꿈속 세상 이야기"),
    ANIMAL_HOSPITAL("동물 병원 이야기"),
    AT_SCHOOL("학교에서 있었던 이야기"),
    DISCOVERING_STRANGE_OBJECT("신기한 물건을 발견한 이야기"),
    SLIDE_ADVENTURE("미끄럼틀에서 생긴 이야기"),
    TRIP_TO_SUPERMARKET("마트에 간 이야기"),
    MAKING_PIZZA("피자를 만들던 이야기"),
    CARING_BABY_ANIMAL("아기 동물을 돌보는 이야기"),
    GOING_ON_FIELD_TRIP("소풍 가는 이야기"),
    ETC("기타");

    private final String description;
}
