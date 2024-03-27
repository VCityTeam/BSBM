#!/bin/bash

if [ $# == 0 ]; then
    echo "Usage: $0 [generate|testdriver|qualification] ?[args]"
    ./generate -help
    ./testdriver -help
    ./qualification -help
    exit 1
fi

command=$1

# create /data folder if it doesn't exist
mkdir -p /data
chmod 777 /data

if [ "$command" = "generate" ]; then
    ./$@

    # get the -fn parameter value, if it exists else set it to "dataset"
    fn=$(echo $@ | grep -oP '(?<=-fn )[^ ]+' || echo "dataset")
    # get the -s parameter value, if it exists else set it to "nt"
    s=$(echo $@ | grep -oP '(?<=-s )[^ ]+' || echo "nt")
    mv $fn*.$s /data

    # check if the -ud parameter exists
    if [[ $@ == *"-ud"* ]]; then
        udf=$(echo $@ | grep -oP '(?<=-udf )[^ ]+' || echo "dataset_update")
        mv $udf*.nt /data
    fi
elif [ "$command" = "testdriver" ]; then
    ./$@

    # get the -o parameter value, if it exists else set it to "dataset"
    o=$(echo $@ | grep -oP '(?<=-o )[^ ]+' || echo "benchmark_result.xml")
    mv $o /data
elif [ "$command" = "qualification" ]; then
    ./$@

    # get the -ql parameter value, if it exists else set it to "dataset"
    ql=$(echo $@ | grep -oP '(?<=-ql )[^ ]+' || echo "qual.log")
    mv $ql /data
else
    echo "Invalid command: $command"
    exit 1
fi