#!/bin/bash
#
# Deploy a jar, source jar, and javadoc jar to Sonatype's snapshot repo.
#
./gradlew uploadArchives -PisRelease=true -PsonatypeUsername="${SONATYPE_USERNAME}" -PsonatypePassword="${SONATYPE_PASSWORD}" -Psigning.keyId="${GPG_CLIENT_ID}" -Psigning.password="${GPG_PASSWORD}" -Psigning.secretKeyRingFile="/home/travis/build/testfairy/testfairy-uploader/scripts/secring.gpg" --info
RETVAL=$?
if [ $RETVAL -eq 0 ]; then
  echo 'Completed publish!'
else
  echo 'Publish failed.'
  return $RETVAL
fi
