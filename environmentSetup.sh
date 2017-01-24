#!/bin/bash

# Fix the CircleCI path
function getAndroidSDK(){
  export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"

  DEPS="$ANDROID_HOME/installed-dependencies"


function waitForAVD {
    (
    local bootanim=""
    export PATH=$(dirname $(dirname $(which android)))/platform-tools:$PATH
    until [[ "$bootanim" =~ "stopped" ]]; do
      sleep 5
      bootanim=$(adb -e shell getprop init.svc.bootanim 2>&1)
      echo "emulator status=$bootanim"
    done
    )
}
