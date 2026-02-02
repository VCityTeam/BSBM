# Dockerized BSBM

This is the Dockerized version of the Berlin SPARQL Benchmark.

## Links
- Original work : http://wbsg.informatik.uni-mannheim.de/bizer/berlinsparqlbenchmark/
- Sources : https://github.com/VCityTeam/BSBM
- Images published on [Docker hub](https://hub.docker.com/r/vcity/bsbm).

## Usage
```bash
docker run -v "$PWD:/app/data" -e "DATA_DESTINATION=<folder>" vcity/bsbm generate [args]
docker run -v "$PWD:/app/data" -e "DATA_DESTINATION=<folder>" vcity/bsbm generate-n [args]
docker run -v "$PWD:/app/data" -e "DATA_DESTINATION=<folder>" vcity/bsbm qualification [args]
docker run -v "$PWD:/app/data" -e "DATA_DESTINATION=<folder>" vcity/bsbm testdriver [args]
```

### generate-n options
The `generate-n` command accepts the following arguments:
- `--versions` or `-v` (required): Number of dataset versions to generate
- `--products` or `-p` (default: 100): Initial product count
- `--step` or `-s` (default: 1000): Product increment per version
- `--format` or `-f` (default: ttl): Output format (nt, ttl, trig, xml, sql, virt, monetdb)
- `--var` (default: 0): Variability percentage (0-100). Controls the percentage of products that change between versions. When set to a value greater than 0, each version generates an update dataset containing the specified percentage of products as changes.

### Diff output files
For each version >= 2, `generate-n` automatically computes the RDF diff between consecutive versions and outputs:
- `dataset-X_additions.nt`: triples present in version X but not in version X-1
- `dataset-X_deletions.nt`: triples present in version X-1 but not in version X

These files are always generated in N-Triples format regardless of the `--format` option, since they are computed by comparing sorted N-Triples representations of each version.

If you want more information about the different arguments, please refer to the original documentation.

```bash
docker run vcity/bsbm generate -help
docker run vcity/bsbm generate-n -help
docker run vcity/bsbm qualification -help
docker run vcity/bsbm testdriver -help
```

$PWD is the directory where the data will be stored.
You can change it to any directory you want.

## Modifications from source:
**Dockerfile:**
- Added new authors
- Dockerized the benchmark

**entrypoint.sh**
- Added a new entrypoint script to run the benchmark