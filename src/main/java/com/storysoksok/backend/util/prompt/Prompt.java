package com.storysoksok.backend.util.prompt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

@Component
@Slf4j
public class Prompt {
    private static final String FIRST_FAIRY_TALE_STORY_PROMPT = """
            너는 자폐아동을 위한 8쪽 그림책을 만드는 작가야.
            입력으로 주어지는 “주제, 주인공, 장소”를 활용해 **전체의 50 % (1 ~ 4쪽)** 에 해당하는
            중반부까지의 줄거리를 작성해 줘.
                        
            ### 반드시 지켜야 할 규칙
            1. **페이지 구분**: `[숫자 공백 내용]` 형식. 페이지 번호는 1부터 4까지.
            2. **분량**: 각 페이지 **150~300글자(띄어쓰기 포함)**.
            3. **문체**: 모두 `~어요` 형의 존댓말. 의성어나 감탄사는 OK.
            4. **어휘 난이도**: 초등학교 1~2학년이 이해할 수준.
            5. **결말 작성 금지**: 5쪽 이후 내용, “어떤 일이 벌어질까요?” 같은 예고 문구 금지.
            6. **대사는 쓰지 않음**: 등장인물이 직접 말을 하지 말 것.
            7. **제4의 벽 금지**: 독자에게 직접 질문·호응 금지.
                        
            ### 절대 쓰면 안 되는 표현 예시
            > “앞으로 무슨 일이 일어날까요?” \s
            > “~했답니다” \s
            > “(사람에게) ~라고 말했어요” \s
                        
            ### 출력 예시 (형식만 보고 내용은 무시)
            1 파란 숲 속 꼬마 거북이 토토는 … \s
            2 토토는 반짝이는 연못에서 … \s
            3 … \s
            4 …
                       
            """;
    private static final String FIRST_FAIRY_TALE_IMAGE_PROMPT = """
            너는 자폐아를 위해 동화책에 들어갈 이미지를 제작해주는 사람이야. 이제 너에게 동화책에 들어갈 삽화를 만들기 위헤서 동화책의 내용을 제시해줄거야.
            해당 정보를 보고 동화책의 삽화를 제작하는데 규칙이 존재해.
            1. 자폐아를 위한 동화이니 되도록 귀여운 삽화를 만들어.
            2. 동화책의 페이지 기준으로 삽화를 제작해줘.
            2.1 삽화 제작시 너에게 주는 동화책의 페이지당 줄거리가 있을거야 페이지 줄거리의 내용을 보고 삽화를 만들어
            2.4 만약 이야기상 인물이 두명 이상이라면 여러명 등장해도 괜찮아.
            3. 이전 페이지의 이미지와 동일한 외형과 색감을 유지해
            4. 부정(하면 안되는거): 왜곡 얼굴, 추가 팔다리, 콜라주, 텍스트 금지
            """;

    public String firstFairyTaleFormat(Map<String, String> params) {

        String subject = defaultIfBlank(params.get("FairyTaleSubject"), "모험");
        String character = defaultIfBlank(params.get("FairyTaleCharacter"), "꼬마 토끼");
        String location = defaultIfBlank(params.get("FairyTaleLocation"), "푸른 숲");

        StringBuilder sb = new StringBuilder();
        sb.append(FIRST_FAIRY_TALE_STORY_PROMPT).append("\n\n")
                .append("아래는 동화책 제작에 필요한 정보야.\n")
                .append("주제: ").append(subject).append('\n')
                .append("주인공: ").append(character).append('\n')
                .append("장소: ").append(location).append('\n')
                .append("\n")
                .append("위 규칙과 예시를 참고하여 4쪽 분량의 동화책 중반부를 완성해 줘.\n");

        return sb.toString();
    }

    public String firstFairyTaleImageFormat(String pageStory, Integer pageNum) {

        StringBuilder sb = new StringBuilder();
        sb.append(FIRST_FAIRY_TALE_IMAGE_PROMPT).append("\n\n")
                .append("아래는 동화책 삽화 제작에 필요한 동화체 페이지당 내용이야.\n")
                .append("현재 페이지 번호 :").append(pageNum).append('\n')
                .append("페이지 줄거리: ").append(pageStory).append('\n')
                .append("\n")
                .append("이 정보를 참고해서 반드시 위 규칙을 따른 이미지를 만들어줘.");

        return sb.toString();
    }
}
