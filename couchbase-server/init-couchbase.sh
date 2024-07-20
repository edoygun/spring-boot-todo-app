#!/bin/bash

/entrypoint.sh couchbase-server &

echo "Initializing Couchbase..."
# track if setup is complete so we don't try to setup again
FILE=/opt/couchbase/init/setupComplete.txt

if ! [ -f "$FILE" ]; then
  # used to automatically create the cluster based on environment variables
  # https://docs.couchbase.com/server/current/cli/cbcli/couchbase-cli-cluster-init.html

  echo $COUCHBASE_ADMINISTRATOR_USERNAME ":"  $COUCHBASE_ADMINISTRATOR_PASSWORD

echo "cluster-init..."

  sleep 30s
    /opt/couchbase/bin/couchbase-cli cluster-init -c couchbase-server \
    --cluster-username $COUCHBASE_ADMINISTRATOR_USERNAME \
    --cluster-password $COUCHBASE_ADMINISTRATOR_PASSWORD \
    --services data,index,query,eventing \
    --cluster-ramsize $COUCHBASE_RAM_SIZE \
    --cluster-index-ramsize $COUCHBASE_INDEX_RAM_SIZE \
    --cluster-eventing-ramsize $COUCHBASE_EVENTING_RAM_SIZE \
    --index-storage-setting default

  sleep 2s

  # used to auto create the bucket based on environment variables
  # https://docs.couchbase.com/server/current/cli/cbcli/couchbase-cli-bucket-create.html

echo "bucket-create..."

  /opt/couchbase/bin/couchbase-cli bucket-create -c localhost:8091 \
  --username $COUCHBASE_ADMINISTRATOR_USERNAME \
  --password $COUCHBASE_ADMINISTRATOR_PASSWORD \
  --bucket $COUCHBASE_BUCKET \
  --bucket-ramsize $COUCHBASE_BUCKET_RAMSIZE \
  --bucket-type couchbase

  sleep 2s

echo "user-manage..."

/opt/couchbase/bin/couchbase-cli user-manage \
  --cluster http://127.0.0.1 \
  --username $COUCHBASE_ADMINISTRATOR_USERNAME \
  --password $COUCHBASE_ADMINISTRATOR_PASSWORD \
  --set \
  --rbac-username $COUCHBASE_RBAC_USERNAME \
  --rbac-password $COUCHBASE_RBAC_PASSWORD \
  --auth-domain local

  sleep 2s

  # create indexes using the QUERY REST API
  # TODO add indexes

  # create file so we know that the cluster is setup and don't run the setup again
  touch $FILE
fi
  # docker compose will stop the container from running unless we do this
  # known issue and workaround
  tail -f /dev/null