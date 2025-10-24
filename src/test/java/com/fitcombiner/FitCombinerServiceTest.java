package com.fitcombiner;

import com.fitcombiner.model.FitFile;
import com.fitcombiner.service.FitCombinerService;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class FitCombinerServiceTest {

    private List<File> files;

    @BeforeEach
    void setup(TestInfo testInfo){
        if(testInfo.getTags().contains("FileList")) {
            files = new ArrayList<>();
            files.add(new File("src/test/resources/splittedActivity1.fit"));
            files.add(new File("src/test/resources/splittedActivity2.fit"));
            files.add(new File("src/test/resources/splittedActivity3.fit"));
        }
    }

    @AfterEach
    void tearDown(TestInfo testInfo){
        if(testInfo.getTags().contains("FileList")) {
            File mergedFile = new File("src/test/resources/mergedActivity.fit");
            boolean isDeleted = mergedFile.delete();
            assertThat(isDeleted).isTrue();
        }
    }

    @Test
    @Tag("FileList")
    void mergeAll_withValidFiles_mergesSuccessfully(){
        FitFile mergedFile = FitCombinerService.mergeAll(files);
        assertThat(mergedFile).isNotNull();
    }

    @Test
    void mergeAll_withEmptyFiles_throwsException(){
        List<File> emptyFileList = new ArrayList<>();
        assertThatThrownBy(() -> FitCombinerService.mergeAll(emptyFileList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("At least two valid files are required!");
    }

    @Test
    void mergeAll_withNullFile_throwsException(){
        List<File> nullFileList = new ArrayList<>();
        nullFileList.add(null);

        assertThatThrownBy(() -> FitCombinerService.mergeAll(nullFileList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("At least two valid files are required!");
    }

    @Test
    void mergeAll_withOverlappingFiles_throwsIllegalArgumentException(){
        List<File> overLappingFileList = new ArrayList<>();
        overLappingFileList.add(new File("src/test/resources/FullActivity.fit"));
        overLappingFileList.add(new File("src/test/resources/splittedActivity2.fit"));

        assertThatThrownBy(() -> FitCombinerService.mergeAll(overLappingFileList))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error while merging FIT files: Both files are overlapping.");
    }

}
