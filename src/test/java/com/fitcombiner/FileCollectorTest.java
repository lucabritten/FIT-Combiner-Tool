package com.fitcombiner;

import com.britten.fittools.tools.fitcombiner.exception.CollectingFilesException;
import com.britten.fittools.tools.fitcombiner.util.FileCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class FileCollectorTest {

    @TempDir
    Path tempDir;
    File fit1;
    File fit2;
    File fit3;

    @BeforeEach
    void setUp(TestInfo testInfo) throws IOException{
        if(testInfo.getTags().contains("TempDir")) {
            fit1 = tempDir.resolve("activity1.fit").toFile();
            fit2 = tempDir.resolve("activity2.fit").toFile();
            fit3 = tempDir.resolve("activity3.fit").toFile();
            assertThat(fit1.createNewFile()).isTrue();
            assertThat(fit2.createNewFile()).isTrue();
            assertThat(fit3.createNewFile()).isTrue();

            System.setProperty("user.dir", tempDir.toString());
        }
    }


    @Test
    @Tag("TempDir")
    void collectFiles_withValidArgs_returnsExpectedFiles(){
        List<File> foundFiles = FileCollector.collectFiles(new String[]{"activity1.fit", "activity2.fit", "activity3.fit"});

        assertThat(foundFiles.size()).isEqualTo(3);
        assertThat(foundFiles.stream().allMatch(f -> f.getName().endsWith(".fit"))).isTrue();
    }

    @Test
    @Tag("TempDir")
    void collectFiles_withoutArgs_findFitFilesInDirectory() {

        List<File> foundFiles = FileCollector.collectFiles(new String[]{});

        assertThat(foundFiles.size()).isEqualTo(3);
        assertThat(foundFiles.stream().allMatch(f -> f.getName().endsWith(".fit"))).isTrue();
    }

    @Test
    @Tag("TempDir")
    void collectFiles_withLessThanTwoFiles_throwsException() {

        assertThat(fit1.delete()).isTrue();
        assertThat(fit2.delete()).isTrue();

        assertThatThrownBy(() -> FileCollector.collectFiles(new String[]{}))
                .isInstanceOf(CollectingFilesException.class)
                .hasMessage("Add at least two .fit files!");

        assertThatThrownBy(() -> FileCollector.collectFiles(new String[]{"activity1.fit"}))
                .isInstanceOf(CollectingFilesException.class)
                .hasMessage("Add at least two .fit files!");
    }


    @Test
    @Tag("TempDir")
    void collectFilesFromParamList_withMixedFiles_returnsOnlyFitFiles() throws IOException{

        File randomFile1 = tempDir.resolve("table.csv").toFile();
        assertThat(randomFile1.createNewFile()).isTrue();

        File randomFile2 = tempDir.resolve("text.txt").toFile();
        assertThat(randomFile2.createNewFile()).isTrue();

        List<File> foundFiles = FileCollector.collectFiles(new String[]
                {"activity1.fit", "activity2.fit", "activity3.fit", "table.csv", "text.txt"});

        assertThat(foundFiles.size()).isEqualTo(3);
        assertThat(foundFiles.stream().allMatch(f -> f.getName().endsWith(".fit"))).isTrue();
    }
}
