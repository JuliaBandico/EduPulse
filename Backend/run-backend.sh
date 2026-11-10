#!/usr/bin/env bash
# EduPulse backend launcher (Linux/macOS). Needs internet the first time only.
# Uses an installed JDK if there is one; otherwise downloads a portable JDK 17 into .jdk/ (nothing is installed).
set -e
cd "$(dirname "$0")"
PORTABLE="$PWD/.jdk/jdk"

if [ -x "$PORTABLE/bin/javac" ]; then
  export JAVA_HOME="$PORTABLE"
elif [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/javac" ]; then
  :
elif command -v javac >/dev/null 2>&1; then
  unset JAVA_HOME
else
  case "$(uname -s)" in Darwin) OS=mac ;; *) OS=linux ;; esac
  case "$(uname -m)" in arm64|aarch64) ARCH=aarch64 ;; *) ARCH=x64 ;; esac
  echo "No JDK found. Downloading a portable JDK 17 into .jdk (one time)..."
  mkdir -p .jdk/x
  curl -fL "https://api.adoptium.net/v3/binary/latest/17/ga/$OS/$ARCH/jdk/hotspot/normal/eclipse" -o .jdk/jdk.tar.gz
  tar -xzf .jdk/jdk.tar.gz -C .jdk/x
  mv .jdk/x/* .jdk/jdk
  rm -rf .jdk/x .jdk/jdk.tar.gz
  [ -d "$PORTABLE/Contents/Home" ] && PORTABLE="$PORTABLE/Contents/Home"
  export JAVA_HOME="$PORTABLE"
fi

echo "Using JAVA_HOME=${JAVA_HOME:-<javac on PATH>}"
chmod +x mvnw
./mvnw -Dmaven.test.skip=true spring-boot:run
