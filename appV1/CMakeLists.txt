# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
# Gradle automatically packages shared libraries with your APK.
file(GLOB s_source src/main/cpp/*.cpp src/main/cpp/*.c)
set(LibDir ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

add_library( # Sets the name of the library.
             sgles
             # Sets the library as a shared library.
             SHARED
             # Provides a relative path to your source file(s).
             ${s_source}
             )

  add_library(
               libbitherjni
               SHARED
               IMPORTED)
   set_target_properties(
               libbitherjni
               PROPERTIES IMPORTED_LOCATION
                ${LibDir}/libbitherjni.so)

 add_library(
              libjpegbither
              SHARED
              IMPORTED)
  set_target_properties(
              libjpegbither
              PROPERTIES IMPORTED_LOCATION
              ${LibDir}/libjpegbither.so)





# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       # Links the target library to the log library
                       # included in the NDK.
                       sgles
                       libbitherjni
                       libjpegbither
                        )