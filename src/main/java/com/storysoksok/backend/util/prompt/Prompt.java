package com.storysoksok.backend.util.prompt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
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
            4 … \s
            
            ### RECOMMEND ###
            1 꼬마 거북이 토토가 친구와 함께 집에 돌아가요\s
            2 꼬마 거북이 토토가 친구와 함께 더 깊숙한 숲속으로 들어가요\s
            3 꼬마 거북이 토토가...\s
                  
            # 추가 요구 사항 – 후반부 선택지 \s
            • 위의 중반부 4 페이지를 **모두 출력한 직후**, 반드시 `### RECOMMEND ###` 라는 구분자 한 줄을 넣어라.  \s
              - 구분자는 **대문자 그리고 공백 없이** 정확히 `### RECOMMEND ###` 여야 하며, 본문 어디에도 다시 등장하면 안 된다. \s
            • 구분자 다음 줄부터 **후반부 진행 방향 선택지 3개**를 출력한다. \s                        
                                   
            ### 반드시 지켜야 할 규칙
            1. 너가 만들어낸 중반부 이야기와 자연스럽게 이어질 것. \s
            2. **선택지는 반드시 3개**여야 하며, `[숫자 공백 내용]` 형식을 지킨다(번호 1 2 3). \s
            """;
    private static final String FAIRY_TALE_IMAGE_PROMPT = """
            너는 자폐아를 위해 동화책에 들어갈 이미지를 제작해주는 사람이야. 이제 너에게 동화책에 들어갈 삽화를 만들기 위헤서 동화책의 내용을 제시해줄거야.
            해당 정보를 보고 동화책의 삽화를 제작하는데 규칙이 존재해.
            1. 자폐아를 위한 동화이니 되도록 귀여운 삽화를 만들어.
            2. 동화책의 페이지 기준으로 삽화를 제작해줘.
            2.1 삽화 제작시 너에게 주는 동화책의 페이지당 줄거리가 있을거야 페이지 줄거리의 내용을 보고 삽화를 만들어
            2.4 만약 이야기상 인물이 두명 이상이라면 여러명 등장해도 괜찮아.
            3. 이전 페이지의 이미지와 동일한 외형과 색감을 유지해
            3.1 이전 동화 내용의 스토리를 참고해서 이미지를 완성해.
            4. 부정(하면 안되는거): 왜곡 얼굴, 추가 팔다리, 콜라주, 텍스트 금지
            """;

    private static final String SECOND_HALF_FAIRY_TALE_STORY_PROMPT = """
            너는 자폐아동을 위한 8쪽 그림책을 만드는 작가야.
            입력으로 주어지는 “주제, 주인공, 장소, 이전까지의 이야기, 추천받은 후반 이야기”를 활용해 **전체의 50 % (5 ~ 8쪽)** 에 해당하는
            후반부 및 결말까지의 줄거리를 작성해 줘.
                        
            ### 반드시 지켜야 할 규칙
            1. **페이지 구분**: `[숫자 공백 내용]` 형식. 페이지 번호는 5부터 8까지.
            2. **분량**: 각 페이지 **150~300글자(띄어쓰기 포함)**.
            3. **문체**: 모두 `~어요` 형의 존댓말. 의성어나 감탄사는 OK.
            4. **어휘 난이도**: 초등학교 1~2학년이 이해할 수준.
            5. **이상한 문구 금지**: 8쪽 이후 내용, “어떤 일이 벌어질까요?” 같은 예고 문구 금지.
            6. **대사는 쓰지 않음**: 등장인물이 직접 말을 하지 말 것.
            7. **제4의 벽 금지**: 독자에게 직접 질문·호응 금지.
            8. **결말 작성**: 이전 이야기와 추천받은 이야기를 통해 납득될만한 결말 출력.
            8.1 **결말 부분은 반드시 8페이지(마지막 페이지)에 넣어**
                        
                        
            ### 절대 쓰면 안 되는 표현 예시
            > “앞으로 무슨 일이 일어날까요?” \s
            > “~했답니다” \s
            > “(사람에게) ~라고 말했어요” \s
                        
            ### 출력 예시 (형식만 보고 내용은 무시)
            5 파란 숲 속 꼬마 거북이 토토는 … \s
            6 토토는 반짝이는 연못에서 … \s
            7 … \s
            8 … \s
                        
            ### TITLE ###
            꼬마 거북이 토토의 신비한 모험\s

            # 추가 요구 사항 – 동화책의 제목 \s
             • 위의 후반부(결말포함) 5~8 페이지를 **모두 출력한 직후**, 반드시 `### TITLE ###` 라는 구분자 한 줄을 넣어라.  \s
               - 구분자는 **대문자 그리고 공백 없이** 정확히 `### TITLE ###` 여야 하며, 본문 어디에도 다시 등장하면 안 된다. \s
             • 구분자 다음 줄에는 **동화책의 제목**을 출력한다. \s                        
                                    
            ### 반드시 지켜야 할 규칙
             1. 너가 만들어낸 동화책의 이야기 토대로 만들어낼 것. \s
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
                .append("위 규칙과 예시를 참고하여 4쪽 분량의 동화책의 중반부와 후반부의 선택지를 완성해 줘.\n");

        return sb.toString();
    }

    public String fairyTaleImageFormat(String pageStory, Integer pageNum) {

        StringBuilder sb = new StringBuilder();
        sb.append(FAIRY_TALE_IMAGE_PROMPT).append("\n\n")
                .append("아래는 동화책 삽화 제작에 필요한 동화체 페이지 내용이야.\n")
                .append("현재 페이지 번호 :").append(pageNum).append('\n')
                .append("페이지 줄거리: ").append(pageStory).append('\n')
                .append("\n")
                .append("이 정보를 참고해서 반드시 위 규칙을 따른 이미지를 만들어줘.");

        return sb.toString();
    }

    public String fairyTaleImageFormat(List<String> pageStory, Integer pageNum, String presentPageStory) {

        StringBuilder sb = new StringBuilder();
        sb.append(FAIRY_TALE_IMAGE_PROMPT).append("\n\n")
                .append("아래는 이전 동화책 내용의 스토리야.\n")
                .append("이전 페이지들 이야기 :").append(pageStory)
                .append("아래는 동화책 삽화 제작에 필요한 동화첵 페이지 내용이야.\n")
                .append("현재 페이지 번호 :").append(pageNum).append('\n')
                .append("현재 페이지 줄거리: ").append(presentPageStory).append('\n')
                .append("\n")
                .append("이 정보를 참고해서 반드시 위 규칙을 따른 이미지를 만들어줘.");

        return sb.toString();
    }

    /**
     *
     * @param pageStory(중반부까지 내용)
     * @param secondHalfRecommendStory(후반부 추천 이야기)
     */
    public String secondHalfFairyTaleStory(List<String> pageStory, String secondHalfRecommendStory) {

        StringBuilder sb = new StringBuilder();
        sb.append(SECOND_HALF_FAIRY_TALE_STORY_PROMPT).append("\n\n")
                .append("아래는 이전 동화책 내용의 4페이지 까지의 스토리야.\n")
                .append("이전 페이지들 이야기 :").append(pageStory)
                .append("아래는 독자가 선택한 후반부의 이야기야 이렇게 이야기가 진행됐으면 좋겠데.\n")
                .append("추천받은 후반 이야기 :").append(secondHalfRecommendStory).append('\n')
                .append("\n")
                .append("위 규칙과 예시를 참고하여 동화책 5~8 페이지의 이야기와 결말 그리고 동화책 제목을 만들어.\n");

        return sb.toString();
    }
}
