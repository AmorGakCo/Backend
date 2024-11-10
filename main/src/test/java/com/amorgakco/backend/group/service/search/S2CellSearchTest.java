package com.amorgakco.backend.group.service.search;

import static org.assertj.core.api.Assertions.assertThat;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import java.util.List;
import org.junit.jupiter.api.Test;

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
        final GroupSearchRequest request = TestGroupFactory.guLevelGroupSearchRequest();
        final GuLevelSearchStrategy guLevelSearchStrategy = new GuLevelSearchStrategy();
        final List<String> allOfCellToken = guLevelSearchStrategy.getCoveringCells(request);
        // when
        final List<String> halfOfCellToken = s2CellSearch.getCellTokens(request);
        // then
        assertThat(halfOfCellToken.size()).isEqualTo(allOfCellToken.size() / 2);
    }

    @Test
    void 동레벨_검색은_전체_토큰을_반환한다() {
        // given
        final GroupSearchRequest request = TestGroupFactory.dongLevelGroupSearchRequest();
        final DongLevelSearchStrategy dongLevelSearchStrategy = new DongLevelSearchStrategy();
        final List<String> allOfToken = dongLevelSearchStrategy.getCoveringCells(request);
        // when
        final List<String> result = s2CellSearch.getCellTokens(request);
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
        final GroupSearchRequest request = TestGroupFactory.cityLevelGroupSearchRequest();
        // when
        final S2LatLngRect rectangle = cityLevelSearchStrategy.createRectangle(request);
        final boolean contain = requestRectangle.contains(rectangle);
        final boolean notContain = !rectangle.contains(requestRectangle);
        // then
        assertThat(contain).isTrue();
        assertThat(notContain).isTrue();
    }
}
