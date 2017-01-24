general:
  artifacts:
    -/home/ubuntu/**repo_name**/build/outputs/reports/**testFolderName**/connected

machine:
  environment:
    ANDROID_HOME: /home/ubuntu/android
  java:
    version: oraclejdk6

dependencies:
  cache_directories:
    - ~/.android
    - ~/android
  override:
    - (echo "Downloading Android SDK v19 now!")
    - (source scripts/environmentSetup.sh && getAndroidSDK)

#test:
 # pre:
 #   - $ANDROID_HOME/tools/emulator -avd testAVD -no-skin -no-audio -no-window:
#      background: true
 #   - (./gradlew assembleDebug):
  #    timeout: 1200
   # - (./gradlew assembleDebugTest):
   #   timeout: 1200
   # - (source scripts/environmentSetup.sh && waitForAVD)
 # override:
  #  - (echo "Running JUnit tests!")
   # - (./gradlew connectedAndroidTest)
