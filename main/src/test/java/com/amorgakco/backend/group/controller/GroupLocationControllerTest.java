package com.amorgakco.backend.group.controller;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.service.GroupLocationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GroupLocationController.class)
class GroupLocationControllerTest extends RestDocsTest {

    @MockBean
    GroupLocationService groupLocationService;

    @Test
    @DisplayName("그룹들의 위치 정보를 응답 받을 수 있다.")
    void getNearByGroups() throws Exception {
        // when
        final GroupSearchResponse response = TestGroupFactory.groupSearchResponse();
        final GroupSearchRequest request = TestGroupFactory.groupSearchRequest();
        given(groupLocationService.findGroups(request)).willReturn(response);
        final ResultActions actions =
                mockMvc.perform(
                        get("/groups/locations")
                                .queryParam("southWestLat", "37.50242")
                                .queryParam("southWestLon", "127.0167")
                                .queryParam("northEastLat", "37.52911")
                                .queryParam("northEastLon", "127.0439")
                                .queryParam("centerLat", "37.5162149")
                                .queryParam("centerLon", "127.0195806"));

        // then
        actions.andExpect(status().isOk());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "group-get-nearby",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                queryParameters(
                                        parameterWithName("southWestLat").description("지도 남서쪽 위도"),
                                        parameterWithName("southWestLon").description("지도 남서쪽 경도"),
                                        parameterWithName("northEastLat").description("지도 북동쪽 위도"),
                                        parameterWithName("northEastLon").description("지도 북동쪽 경도"),
                                        parameterWithName("centerLat").description("지도 중앙 위도"),
                                        parameterWithName("centerLon").description("지도 중앙 경도"))));
    }
}
