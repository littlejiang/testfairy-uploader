#!/bin/bash
#
# Deploy a jar, source jar, and javadoc jar to Sonatype's snapshot repo.
#
# Adapted from https://coderwall.com/p/9b_lfq and
# http://benlimmer.com/2013/12/26/automatically-publish-javadoc-to-gh-pages-with-travis-ci/

# SLUG="testfairy/testfairy-uploader"
# MASTER_BRANCH="master"

# set -e

# if [ "$TRAVIS_REPO_SLUG" != "$SLUG" ]; then
#   echo "Skipping snapshot deployment: wrong repository. Expected '$SLUG' but was '$TRAVIS_REPO_SLUG'."
# elif [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
#   echo "Skipping snapshot deployment: was pull request."
# elif [ "$TRAVIS_BRANCH" != "$MASTER_BRANCH" ]; then
#   echo "Deploying to snapshot..."
#   ./gradlew uploadArchives -PisRelease=false -PsonatypeUsername="${SONATYPE_USERNAME}" -PsonatypePassword="${SONATYPE_PASSWORD}"
#   RETVAL=$?
#   if [ $RETVAL -eq 0 ]; then
#     echo 'Completed publish!'
#   else
#     echo 'Publish failed.'
#     return $RETVAL
#   fi  
# else
#   echo "Deploying to staging..."
  ./gradlew uploadArchives -PisRelease=true -PsonatypeUsername="${SONATYPE_USERNAME}" -PsonatypePassword="${SONATYPE_PASSWORD}" -Psigning.keyId="${GPG_CLIENT_ID}" -Psigning.password="${GPG_PASSWORD}" -Psigning.secretKeyRingFile="/home/travis/build/testfairy/testfairy-uploader/scripts/secring.gpg"
  RETVAL=$?
  if [ $RETVAL -eq 0 ]; then
    echo 'Completed publish!'
  else
    echo 'Publish failed.'
    return $RETVAL
  fi
# fi
