#!/bin/bash

if [ $# == 0 ]; then
    echo "Usage: $0 [generate|generate-n|testdriver|qualification] ?[args]"
    ./generate -help
    ./generate-n -help
    ./testdriver -help
    ./qualification -help
    exit 1
fi

command=$1
data_destination=$DATA_DESTINATION

# create data destination folder if it doesn't exist
mkdir -p $data_destination
chmod 777 $data_destination

if [ "$command" = "generate-n" ]; then
    ./$@
    mv dataset*.ttl $data_destination
elif [ "$command" = "generate" ]; then
    ./$@

    # get the -fn parameter value, if it exists else set it to "dataset"
    fn=$(echo $@ | grep -oP '(?<=-fn )[^ ]+' || echo "dataset")
    # get the -s parameter value, if it exists else set it to "nt"
    s=$(echo $@ | grep -oP '(?<=-s )[^ ]+' || echo "nt")
    mv $fn*.$s $data_destination

    # check if the -ud parameter exists
    if [[ $@ == *"-ud"* ]]; then
        udf=$(echo $@ | grep -oP '(?<=-udf )[^ ]+' || echo "dataset_update")
        mv $udf*.nt $data_destination
    fi
elif [ "$command" = "testdriver" ]; then
    ./$@

    # get the -o parameter value, if it exists else set it to "dataset"
    o=$(echo $@ | grep -oP '(?<=-o )[^ ]+' || echo "benchmark_result.xml")
    mv $o $data_destination
elif [ "$command" = "qualification" ]; then
    ./$@

    # get the -ql parameter value, if it exists else set it to "dataset"
    ql=$(echo $@ | grep -oP '(?<=-ql )[^ ]+' || echo "qual.log")
    mv $ql $data_destination
else
    echo "Invalid command: $command"
    exit 1
fi