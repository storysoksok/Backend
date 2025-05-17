package com.storysoksok.backend.controller.fairytale.docs;

import com.storysoksok.backend.dto.fairytale.request.FairyTaleCreateRequest;
import com.storysoksok.backend.dto.oauth.request.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

public interface FairyTaleControllerDocs {

    @Operation(
            summary = "첫 동화 생성",
            description = """
                    
                    이 API는 인증이 필요합니다.

                    ### 요청 파라미터
                    - **fairyTaleCharacter** (String): 동화속 출현 캐릭터 [필수]
                    - **fairyTaleSubject** (String): 동화 주제 [필수]
                    - **fairyTaleLocation** (String): 동화 장소 [필수]
                    - **otherCharacter** (String): 기타 동화속 출현 캐릭터 [필수X]
                    - **otherSubject** (String): 기타 동화 주제 [필수X]
                    - **otherLocation** (String): 기타 동화 장소 [필수X]                    
                    
                    ### 사용방법
                    
                    ### FairyTaleCharacter
                    
                        RABBIT("토끼")
                        DOG("강아지")
                        CAT("고양이")
                        FRIEND("친구")
                        TEACHER("선생님")
                        MOM("엄마")
                        BABY_BEAR("아기 곰")
                        PRINCESS("공주님")
                        ROBOT("로봇")
                        MONSTER("괴물")
                        WIZARD("마법사")
                        CUCUMBER("오이")
                        CHEF("요리사")
                        WHALE("고래")
                        TURTLE("거북이")
                        SUN("해님")
                        CLOUD_FAIRY("구름 요정")
                        CAR("자동차")
                        DOKKAEBI("도깨비")
                        GHOST_FRIEND("유령 친구")
                        ETC("기타")
                        
                    ### FairyTaleLocation
                    
                        HOME("우리 집")
                        SCHOOL("학교")
                        PLAYGROUND("놀이터")
                        HOSPITAL("병원")
                        KINDERGARTEN_CLASSROOM("유치원 교실")
                        BEACH("바닷가")
                        FOREST("숲속")
                        SKY("하늘 위")
                        ZOO("동물원")
                        TOY_STORE("장난감 가게")
                        SUPERMARKET("마트")
                        DREAMLAND("꿈나라")
                        CASTLE("성 안")
                        CAVE("동굴")
                        DINOSAUR_LAND("공룡나라")
                        BAKERY("빵집")
                        SWIMMING_POOL("수영장")
                        AMUSEMENT_PARK("놀이공원")
                        MAGIC_LAND("마법의 나라")
                        INSIDE_BUS("버스 안")
                        ETC("기타")           
                    
                    ### FairyTaleSubject
                    
                        HANGING_OUT_WITH_FRIEND("친구와 놀러 가는 이야기")
                        ANIMAL_ADVENTURE("동물이 모험하는 이야기")
                        SEARCHING_FOR_PRESENT("선물을 찾는 이야기")
                        USING_MAGIC("마법을 쓰는 이야기")
                        BIRTHDAY_PARTY("생일 파티 이야기")
                        MEETING_SCARY_MONSTER("무서운 괴물과 만나는 이야기")
                        LOST_AND_FOUND_WAY("길을 잃고 돌아오는 이야기")
                        RAINY_DAY("비 오는 날 이야기")
                        FINDING_HIDDEN_TREASURE("숨은 보물을 찾는 이야기")
                        FIGHT_AND_MAKE_UP_WITH_FRIEND("친구랑 싸우고 화해하는 이야기")
                        FINDING_LOST_TOY("잃어버린 장난감을 찾는 이야기")
                        DREAM_WORLD("꿈속 세상 이야기")
                        ANIMAL_HOSPITAL("동물 병원 이야기")
                        AT_SCHOOL("학교에서 있었던 이야기")
                        DISCOVERING_STRANGE_OBJECT("신기한 물건을 발견한 이야기")
                        SLIDE_ADVENTURE("미끄럼틀에서 생긴 이야기")
                        TRIP_TO_SUPERMARKET("마트에 간 이야기")
                        MAKING_PIZZA("피자를 만들던 이야기")
                        CARING_BABY_ANIMAL("아기 동물을 돌보는 이야기")
                        GOING_ON_FIELD_TRIP("소풍 가는 이야기")
                        ETC("기타")
                    
                    ### 사용방법
                    - FairyTaleCharacter, FairyTaleLocation, FairyTaleSubject 변수에 원하는 이야기 정보를 넣습니다.
                    - 만약 다른 정보를 넣고 싶을 시 **ETC** 변수를 넣습니다.
                    - ❗만약 ETC 파라미터를 넣어줬을 시에는 기타 이야기를 원하는 선택된 변수(otherCharacter, otherSubject, otherLocation)파라미터에 반드시 기타 정보를 넣어줘야합니다.
                    - 예시: 만약 FairyTaleSubject 에 ETC를 넣어줬을 시 otherSubject 변수에 원하는 기타 주제를 넣어줘야 함.
                    
                    
                    ### 유의사항
                    - 생성하기 원하는 동화 내용을 받아와 동화를 중반부까지 만들어줍니다.
                    - 사진은 4장 만들어집니다.
                    - 중반부까지 RDB가 아닌 RedisHash에 저장됩니다.
                    - ‼️해당 API는 한번 호출할때마다 챗봇1회, 이미지생성4회의 총 5번의 외부 API를 호출합니다. 비용이 많이 나가니 최소한으로 테스트해주세요.
                    """
    )
    ResponseEntity<Object> firstFairyTale(CustomOAuth2User customOAuth2User, FairyTaleCreateRequest request);
}

