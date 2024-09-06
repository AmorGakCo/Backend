package com.amorgakco.backend.group.service.search;

import static org.assertj.core.api.Assertions.*;

import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;

import org.junit.jupiter.api.Test;

import java.util.List;

class S2CellSearchTest {

    S2CellSearch s2CellSearch =
            new S2CellSearch(
                    List.of(
                            new DongLevelSearchStrategy(),
                            new GuLevelSearchStrategy(),
                            new CityLevelSearchStrategy()));

    @Test
    void 구레벨_검색은_전체_토큰의_절반을_반환한다() {
        // given
        final GroupSearchRequest groupSearchRequest =
                new GroupSearchRequest(
                        37.504004613572235,
                        126.90304310147118,
                        37.55796032104298,
                        127.00112045387941,
                        37.56826582236329,
                        126.97879899797212);
        final GuLevelSearchStrategy guLevelSearchStrategy = new GuLevelSearchStrategy();
        final List<String> allOfCellToken =
                guLevelSearchStrategy.getCoveringCells(groupSearchRequest);
        // when
        final List<String> halfOfCellToken = s2CellSearch.getCellTokens(groupSearchRequest);
        // then
        assertThat(halfOfCellToken.size()).isEqualTo(allOfCellToken.size() / 2);
    }

    @Test
    void 동레벨_검색은_전체_토큰을_반환한다() {
        // given
        final GroupSearchRequest groupSearchRequest =
                new GroupSearchRequest(
                        37.56914134233172,
                        126.95299050188105,
                        37.57671468451652,
                        126.96786094808684,
                        37.56826582236329,
                        126.97879899797212);
        final DongLevelSearchStrategy dongLevelSearchStrategy = new DongLevelSearchStrategy();
        final List<String> allOfToken =
                dongLevelSearchStrategy.getCoveringCells(groupSearchRequest);
        // when
        final List<String> result = s2CellSearch.getCellTokens(groupSearchRequest);
        // then
        assertThat(result.size()).isEqualTo(allOfToken.size());
    }

    @Test
    void 도시레벨_검색요청은_검색규모를_축소한다() {
        // given
        final S2LatLngRect requestRectangle =
                S2LatLngRect.fromPointPair(
                        S2LatLng.fromDegrees(37.48695173821273, 126.86109360313715),
                        S2LatLng.fromDegrees(37.60407395278887, 127.06381467289712));
        final CityLevelSearchStrategy cityLevelSearchStrategy = new CityLevelSearchStrategy();
        final GroupSearchRequest groupSearchRequest =
                new GroupSearchRequest(
                        37.48695173821273,
                        126.86109360313715,
                        37.60407395278887,
                        127.06381467289712,
                        37.554498367410076,
                        126.97758064203596);
        // when
        final S2LatLngRect rectangle = cityLevelSearchStrategy.createRectangle(groupSearchRequest);
        final boolean contain = requestRectangle.contains(rectangle);
        final boolean notContain = !rectangle.contains(requestRectangle);
        // then
        assertThat(contain).isTrue();
        assertThat(notContain).isTrue();
    }
}
