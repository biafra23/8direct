package com.jaeckel.direct.testrunner;

/**
 * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
 * and res directory by default. Use the {@link org.robolectric.annotation.Config} annotation to configure.
 *
 * @param testClass the test class to be run
 * @throws org.junit.runners.model.InitializationError
 *          if junit says so
 */

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.res.Fs;
import org.robolectric.res.FsFile;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class FilteredRobolectricRunner extends RobolectricTestRunner {

    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link org.robolectric.annotation.Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws org.junit.runners.model.InitializationError
     *          if junit says so
     */
    public FilteredRobolectricRunner(Class<?> testClass) throws InitializationError {
        super(testClass);

        setLogger();
    }

    private void setLogger(){
        System.setProperty("robolectric.logging", "stdout");
    }

    @Override
    protected AndroidManifest createAppManifest(FsFile manifestFile) {
        if(!manifestFile.exists()){
            System.err.println("Filtered manifest was not found at: " + manifestFile.getParent().getName());
            return null;
        }

        //Manifest is being loaded from inside the filtered-manifest directory
        //But the res and assets directory still exists inside the main project dir
        FsFile projectDir = Fs.currentDirectory();
        return new MavenAndroidManifest(manifestFile, projectDir.join("res"), projectDir.join("assets"));
    }

    public static class MavenAndroidManifest extends AndroidManifest {
        public MavenAndroidManifest(FsFile baseDir, FsFile resDir, FsFile assetsDir){
            super(baseDir, resDir, assetsDir);
        }

        @Override
        protected List<FsFile> findLibraries() {
            // Try unpack folder from maven.
            FsFile unpack = getBaseDir().join("target/unpack/apklibs");
            if (unpack.exists()) {
                FsFile[] libs = unpack.listFiles();
                if (libs != null) {
                    return asList(libs);
                }
            }
            return emptyList();
        }

        @Override protected AndroidManifest createLibraryAndroidManifest(FsFile libraryBaseDir) {
            return new MavenAndroidManifest(libraryBaseDir.join("AndroidManifest.xml"), libraryBaseDir.join("res"), libraryBaseDir.join("assets"));
        }
    }
}