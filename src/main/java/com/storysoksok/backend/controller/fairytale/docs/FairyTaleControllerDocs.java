package com.storysoksok.backend.controller.fairytale.docs;

import com.storysoksok.backend.dto.fairytale.request.FairyTaleCreateRequest;
import com.storysoksok.backend.dto.fairytale.request.SecondHalfFairyTaleRequest;
import com.storysoksok.backend.dto.fairytale.response.FairyTaleImageResponse;
import com.storysoksok.backend.dto.fairytale.response.FairyTaleResponse;
import com.storysoksok.backend.dto.fairytale.response.FirstFairyTaleResponse;
import com.storysoksok.backend.dto.fairytale.response.SecondHalfFairyTaleResponse;
import com.storysoksok.backend.dto.oauth.request.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

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
                    - 사진은 1장 만들어집니다. (첫페이지 삽화)
                    - 중반부까지 RDB가 아닌 RedisHash에 저장됩니다.
                    """
    )
    ResponseEntity<FirstFairyTaleResponse> firstFairyTale(CustomOAuth2User customOAuth2User, FairyTaleCreateRequest request);

    @Operation(
            summary = "페이지별 이미지 생성",
            description = """
                    
                    이 API는 인증이 필요합니다.

                    ### 요청 파라미터
                    - **fairyTaleId** (String): 동화 ID [필수]
                    - **pageNum** (Integer): 요청 페이지 번호 [필수]

                    ### 유의사항
                    - 여기서의 fairyTaleId 파라미터는 서버에서 임시로 저장해두고 있는 데이터입니다(RDB가 아닌 RedisHash) 추후 모든 동화책 제작이 끝날 시 RDB의 PK(ID)값을 넘겨줄 예정입니다.
                    - 첫 동화 생성시 반환되는 동화ID 및 이미지를 만들어주고 싶은 페이지 번호를 받아 해당 페이지의 이미지를 만들어줍니다.
                    - 요청된 페이지 기준으로 이전 페이지까지의 줄거리를 가져와 프롬프트에 추가하고 요청된 페이지의 줄거리에 대한 이미지를 생성해줍니다.
                    - 이미지 제작까지 평균 30~45초 정도 소요됩니다.
                    """
    )
    ResponseEntity<FairyTaleImageResponse> createFairyTaleImage(CustomOAuth2User customOAuth2User, UUID fairyTaleId, Integer pageNum);

    @Operation(
            summary = "후반부 동화 생성",
            description = """
                    
                    이 API는 인증이 필요합니다.

                    ### 요청 파라미터
                    - **midPartFairyTale** (String): 중반부까지의 동화책 ID [필수]
                    - **SecondHalfRecommendStory** (String): 동화 후반부 이야기 선택지 [필수]
                    - **otherRecommendStory** (String): 기타 후반부 내용 [필수X]                    
                    
                    ### SecondHalfRecommendStory
                        FIRST_HALF_RECOMMEND_STORY("첫 선택지 선택")
                        SECOND_HALF_RECOMMEND_STORY("두번째 선택지 선택")
                        THIRD_HALF_RECOMMEND_STORY("세번째 선택지 선택")
                        ETC("직접 사용자가 입력한 내용")
                        
                    ### 반환값
                    - **memberId** (String): 현재 로그인된 회원 PK값
                    - **secondHalfFairyTaleId** (String): 완성된 동화책 ID ‼️중반부 동화책 Id와 별개의 데이터입니다.
                    - **memberName** (String): 현재 로그인된 회원 이름
                    - **pageNumber** (int): 현재 동화책의 페이지 [해당 API에서는 반드시 5페이지]
                    - **secondHalfFairyTaleStory** (List<String>): 결말까지의 동화책 후반부 내용 
                    - **imageUrl** (String): 현재 페이지 동화책 삽화
 
                    
                    ### 사용방법
                    - SecondHalfRecommendStory에 첫번째, 두번째, 세번째 선택지 혹은 기타 선택지를 입력합니다.
                    - 만약 다른 정보를 넣고 싶을 시 **ETC** 변수를 넣습니다.
                    - 참고로 선택지 정보는 첫 동화 생성 API의 반환값에 List 형식으로 반환됩니다.
                    - ❗만약 ETC 파라미터를 넣어줬을 시에는 기타 이야기를 입력할 변수(otherRecommendStory)파라미터에 반드시 기타 정보를 넣어줘야합니다.
                    - 예시: 만약 SecondHalfRecommendStory에 에 ETC를 넣어줬을 시 otherRecommendStory 변수에 원하는 기타 내용을 넣어줘야 함.
                    
                    
                    ### 유의사항
                    - 생성하기 원하는 동화 내용을 받아와 동화를 결말까지 만들어줍니다.
                    - 사진은 1장 만들어집니다. (후반부 페이지 시작부 삽화) [5페이지]
                    - 해당 부분부터 RDB에 저장됩니다.
                    """
    )
    ResponseEntity<SecondHalfFairyTaleResponse> secondHalfRecommendStory(CustomOAuth2User customOAuth2User, SecondHalfFairyTaleRequest request);

    @Operation(
            summary = "테스트용 동화 제작",
            description = """
                    
                    이 API는 인증이 필요 없습니다.

                    ### 요청 파라미터
                    - **page-num** (int): 페이지번호 [필수]

                    ### 유의사항
                    - 미리 만들어둔 동화책을 1~8페이지까지 반환합니다.
                    - 서버측에서 4초 대기 후 반환하도록 설계하였습니다.
                    """
    )
    ResponseEntity<FairyTaleResponse> getFairyTaleTest(Integer pageNum);
}

