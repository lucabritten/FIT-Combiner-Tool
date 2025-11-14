package com.fitcombiner;


import com.britten.fittools.modules.fitcombiner.io.FitDecoder;
import com.britten.fittools.modules.fitcombiner.domain.FitFile;
import com.garmin.fit.File;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.within;


public class FitDecoderTest {

    private final String PATH_TO_SAMPLE_FILE = "src/test/resources/sample.fit";

    @Test
    void decodeValidFitFile_shouldReturnFileIdMessageCorrectly()  {
        FitFile result = FitDecoder.decode(PATH_TO_SAMPLE_FILE);

        assertThat(result).isNotNull();
        assertThat(result.getFileType()).isEqualTo(File.ACTIVITY);
        assertThat(result.getManufacturer()).isEqualTo(87);
    }

    @Test
    void decodeValidFitFile_shouldReturnRecordMessageCorrectly()  {
        FitFile result = FitDecoder.decode(PATH_TO_SAMPLE_FILE);

        assertThat(result.getTimeStamps()).isNotEmpty();
        assertThat(result.getHeartRate()).isNotEmpty();
        assertThat(result.getLatPosition()).isNotEmpty();
    }

    @Test
    void decodeValidFitFile_shouldReturnSessionMessageCorrectly()  {
        FitFile result = FitDecoder.decode(PATH_TO_SAMPLE_FILE);

        assertThat(result.getStartTime()).isNotNull();
        assertThat(result.getAbsDistance()).isNotNull();
        assertThat(result.getAbsDistance()).isGreaterThan(0);
        assertThat(result.getAvgSpeed()).isNotNull();
        assertThat(result.getAvgSpeed()).isGreaterThan(0);
    }
    @Test
    void avgSpeed_shouldBeConsistentWithDistanceAndTime() {
        FitFile result = FitDecoder.decode(PATH_TO_SAMPLE_FILE);

        double expectedSpeed = result.getAbsDistance() / result.getDuration();
        double actual = result.getAvgSpeed();
        assertThat(actual).isCloseTo(expectedSpeed, within(0.1));
    }

    @Test
    void decodedData_shouldHaveConsistentSizes() {
        FitFile result = FitDecoder.decode(PATH_TO_SAMPLE_FILE);

        assertThat(result.getTimeStamps().size())
                .isEqualTo(result.getHeartRate().size());
        assertThat(result.getTimeStamps().size())
                .isEqualTo(result.getLatPosition().size());
    }

    @Test
    void decode_shouldThrowException_whenFileDoesNotExist() {
        assertThatThrownBy(() -> FitDecoder.decode("src/test/resources/missing.fit"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error opening file: src/test/resources/missing.fit");
    }
}
